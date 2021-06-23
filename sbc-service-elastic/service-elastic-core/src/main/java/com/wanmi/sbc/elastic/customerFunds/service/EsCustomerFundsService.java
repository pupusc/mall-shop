package com.wanmi.sbc.elastic.customerFunds.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelListByCustomerIdsRequest;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelListByCustomerIdsResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerBaseVO;
import com.wanmi.sbc.elastic.api.request.customerFunds.*;
import com.wanmi.sbc.elastic.api.response.customerFunds.EsCustomerFundsResponse;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsSearchInfoResponse;
import com.wanmi.sbc.elastic.bean.dto.customerFunds.EsCustomerFundsDTO;
import com.wanmi.sbc.elastic.bean.vo.customerFunds.EsCustomerFundsVO;
import com.wanmi.sbc.elastic.customerFunds.model.root.EsCustomerFunds;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName EsCustomerFundsService
 * @Description 结算单
 * @Author yangzhen
 * @Date 2020/12/11 14:25
 * @Version 1.0
 */
@Slf4j
@Service
public class EsCustomerFundsService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ResultsMapper resultsMapper;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;



    /**
     * 初始化会员资金到es
     */
    public Boolean initCustomerFunds(EsCustomerFundsRequest request) {
        if (!elasticsearchTemplate.indexExists(EsConstants.DOC_CUSTOMER_FUNDS)) {
            //第一次进入 没有索引时
            elasticsearchTemplate.getClient().admin().indices()
                    .prepareCreate(EsConstants.DOC_CUSTOMER_FUNDS).execute().actionGet();
            elasticsearchTemplate.putMapping(EsCustomerFunds.class);
        }

        EsCustomerFunds esCustomerFund = new EsCustomerFunds();
        KsBeanUtil.copyPropertiesThird(request, esCustomerFund);
        esCustomerFund.setId(request.getCustomerFundsId());
        List<IndexQuery> esCustomerFunds = new ArrayList<>();
        IndexQuery iq = new IndexQuery();
        iq.setObject(esCustomerFund);
        iq.setIndexName(EsConstants.DOC_CUSTOMER_FUNDS);
        iq.setType(EsConstants.DOC_CUSTOMER_FUNDS);
        esCustomerFunds.add(iq);
        elasticsearchTemplate.bulkIndex(esCustomerFunds);
        return Boolean.TRUE;
    }


    /**
     * 批量初始化会员资金到es
     */
    public Boolean initCustomerFundsList(EsCustomerFundsListRequest request) {
        if (!elasticsearchTemplate.indexExists(EsConstants.DOC_CUSTOMER_FUNDS)) {
            //第一次进入 没有索引时
            elasticsearchTemplate.getClient().admin().indices()
                    .prepareCreate(EsConstants.DOC_CUSTOMER_FUNDS).execute().actionGet();
            elasticsearchTemplate.putMapping(EsCustomerFunds.class);
        }

        List<EsCustomerFundsDTO> esCustomerFundsDTOs = request.getEsCustomerFundsDTOS();
        if (CollectionUtils.isNotEmpty(esCustomerFundsDTOs)) {
            List<IndexQuery> esCustomerFunds = new ArrayList<>();
            esCustomerFundsDTOs.forEach(esCustomerFundsDTO -> {
                IndexQuery iq = new IndexQuery();
                EsCustomerFunds po = new EsCustomerFunds();
                po = KsBeanUtil.convert(esCustomerFundsDTO, EsCustomerFunds.class);
                po.setId(esCustomerFundsDTO.getCustomerFundsId());
                iq.setObject(po);
                iq.setIndexName(EsConstants.DOC_CUSTOMER_FUNDS);
                iq.setType(EsConstants.DOC_CUSTOMER_FUNDS);
                esCustomerFunds.add(iq);
            });
            elasticsearchTemplate.bulkIndex(esCustomerFunds);
        }
        return Boolean.TRUE;
    }


    /**
     * es分页查找会员资金
     *
     * @param queryRequest
     * @return
     */
    public EsCustomerFundsResponse queryCustomerFundsPage(EsCustomerFundsPageRequest queryRequest) {
        EsCustomerFundsResponse response = new EsCustomerFundsResponse();
        if (!elasticsearchTemplate.indexExists(EsConstants.DOC_CUSTOMER_FUNDS)) {
            response.setEsCustomerFundsVOPage(new MicroServicePage<>());
            return response;
        }

        EsSearchInfoResponse<EsCustomerFundsVO> page = elasticsearchTemplate.query(queryRequest.getSearchCriteria(),
                searchResponse -> EsSearchInfoResponse.build(searchResponse, resultsMapper, EsCustomerFundsVO.class));
        //查询会员等级
        List<EsCustomerFundsVO> lists = page.getData();
        setCustomerLevelName(lists);
        response.setEsCustomerFundsVOPage(new MicroServicePage<>(lists,
                PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()), page.getTotal()));
        return response;

    }


    private void setCustomerLevelName(List<EsCustomerFundsVO> customerFundsVOList) {
        List<String> customerIdList = customerFundsVOList.stream().map(EsCustomerFundsVO::getCustomerId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(customerIdList)) {
            BaseResponse<CustomerLevelListByCustomerIdsResponse> baseResponse = customerLevelQueryProvider.listByCustomerIds(new CustomerLevelListByCustomerIdsRequest(customerIdList));
            List<CustomerBaseVO> customerLevelVOList = baseResponse.getContext().getCustomerLevelVOList();
            if (CollectionUtils.isNotEmpty(customerLevelVOList)) {
                Map<String, String> map = customerLevelVOList.stream().filter(customerBaseVO -> StringUtils.isNotBlank(customerBaseVO.getCustomerId())).collect(Collectors.toMap(CustomerBaseVO::getCustomerId,
                        (customerBaseVO) -> StringUtils.isNotBlank(customerBaseVO.getCustomerLevelName()) ? customerBaseVO.getCustomerLevelName() : StringUtils.EMPTY));
                if (MapUtils.isNotEmpty(map)) {
                    customerFundsVOList.forEach(customerFundsVO -> {
                        String levelName = map.get(customerFundsVO.getCustomerId());
                        if (StringUtils.isNotBlank(levelName)) {
                            customerFundsVO.setCustomerLevelName(levelName);
                        }
                    });
                }
            }
        }
    }

    /**
     * 修改会员资金
     */
    public Boolean updateCustomerFunds(EsCustomerFundsModifyRequest request) {
        Client client = elasticsearchTemplate.getClient();
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(request.getWhereCriteria()).build();
        Iterable<EsCustomerFunds> esCustomerFundsList = elasticsearchTemplate.queryForList(searchQuery, EsCustomerFunds.class);

        Map<String, Object> updateMap;
        if (esCustomerFundsList != null) {
            for (EsCustomerFunds esCustomerFunds : esCustomerFundsList) {
                updateMap = new HashMap<>();

                //修改会员名称
                if (StringUtils.isNotEmpty(request.getCustomerName())) {
                    updateMap.put("customerName", request.getCustomerName());
                }
                //修改会员账号
                if (StringUtils.isNotEmpty(request.getCustomerAccount())) {
                    updateMap.put("customerAccount", request.getCustomerAccount());
                }
                //账户余额
                if (Objects.nonNull(request.getAccountBalance())) {
                    updateMap.put("accountBalance", request.getAccountBalance());
                }
                //冻结余额
                if (Objects.nonNull(request.getBlockedBalance())) {
                    updateMap.put("blockedBalance", request.getBlockedBalance());
                }
                //可提现金额
                if (Objects.nonNull(request.getWithdrawAmount())) {
                    updateMap.put("withdrawAmount", request.getWithdrawAmount());
                }
                //是否分销员
                if (Objects.nonNull(request.getDistributor())) {
                    updateMap.put("distributor", request.getDistributor());
                }
                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_CUSTOMER_FUNDS)
                        .setType(EsConstants.DOC_CUSTOMER_FUNDS)
                        .setId(esCustomerFunds.getId()).setDoc(updateMap).execute().actionGet();
            }

        }
        return Boolean.TRUE;
    }


    /**
     * 增量加金额
     */
    public Boolean updateGrantAmountCustomerFunds(EsCustomerFundsGrantAmountModifyRequest request) {
        Client client = elasticsearchTemplate.getClient();
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(request.getWhereCriteria()).build();
        Iterable<EsCustomerFunds> esCustomerFundsList = elasticsearchTemplate.queryForList(searchQuery, EsCustomerFunds.class);

        Map<String, Object> updateMap;
        if (esCustomerFundsList != null) {
            for (EsCustomerFunds esCustomerFunds : esCustomerFundsList) {
                updateMap = new HashMap<>();

                //账户余额
                if (Objects.nonNull(request.getAccountBalance())) {
                    updateMap.put("accountBalance", esCustomerFunds.getAccountBalance().add(request.getAmount()));
                }
                //可提现金额
                if (Objects.nonNull(request.getWithdrawAmount())) {
                    updateMap.put("withdrawAmount", esCustomerFunds.getWithdrawAmount().add(request.getAmount()));
                }
                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_CUSTOMER_FUNDS)
                        .setType(EsConstants.DOC_CUSTOMER_FUNDS)
                        .setId(esCustomerFunds.getId()).setDoc(updateMap).execute().actionGet();
            }

        }
        return Boolean.TRUE;
    }


    /**
     * 根据会员id 查看es是否存在该数据
     */
    public Boolean queryCustomerFundsIsExist(EsCustomerFundsModifyRequest request) {
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(request.getWhereCriteria()).build();
        List<EsCustomerFunds> esCustomerFunds = elasticsearchTemplate.queryForList(searchQuery, EsCustomerFunds.class);
        return CollectionUtils.isNotEmpty(esCustomerFunds);
    }


}
