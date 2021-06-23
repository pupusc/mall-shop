package com.wanmi.sbc.elastic.customer.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerPageRequest;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerShowPhoneVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerAddRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerPageRequest;
import com.wanmi.sbc.elastic.api.response.customer.EsDistributionCustomerAddResponse;
import com.wanmi.sbc.elastic.customer.model.root.EsDistributionCustomer;
import com.wanmi.sbc.elastic.customer.repository.EsDistributionCustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: HouShuai
 * @date: 2020/12/8 17:06
 * @description: 分销员列表
 */
@Slf4j
@Service
public class EsDistributionCustomerService {

    @Autowired
    private EsDistributionCustomerRepository esDistributionCustomerRepository;

    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    /**
     * 新增分销员
     * @param request
     * @return
     */
    public BaseResponse<EsDistributionCustomerAddResponse> add(EsDistributionCustomerAddRequest request) {
        List<DistributionCustomerShowPhoneVO> distributionCustomerVOList = request.getList();
        if (CollectionUtils.isEmpty(distributionCustomerVOList)) {
            return BaseResponse.success(new EsDistributionCustomerAddResponse(Collections.emptyList()));
        }

        List<EsDistributionCustomer> distributionCustomerList = this.copyBeanList(distributionCustomerVOList);

        Iterable<EsDistributionCustomer> esDistributionCustomers = saveAll(distributionCustomerList);

        List<EsDistributionCustomer> distributionCustomers = Lists.newArrayList(esDistributionCustomers);

        List<DistributionCustomerVO> esDistributionCustomerVOList = this.copyField(distributionCustomers);

        EsDistributionCustomerAddResponse addResponse = new EsDistributionCustomerAddResponse(esDistributionCustomerVOList);

        return BaseResponse.success(addResponse);
    }

    /**
     * DistributionCustomerShowPhoneVO  转EsDistributionCustomer
     *
     * @param distributionCustomerVOList
     * @return
     */
    private List<EsDistributionCustomer> copyBeanList(List<DistributionCustomerShowPhoneVO> distributionCustomerVOList) {
        return distributionCustomerVOList.stream().map(entity -> {
            EsDistributionCustomer distributionCustomer = EsDistributionCustomer.builder().build();
            BeanUtils.copyProperties(entity, distributionCustomer);
            return distributionCustomer;
        }).collect(Collectors.toList());
    }

    /**
     * EsDistributionCustomer转EsDistributionCustomerVO
     *
     * @param distributionCustomers
     * @return
     */
    private List<DistributionCustomerVO> copyField(List<EsDistributionCustomer> distributionCustomers) {
        return distributionCustomers.stream().map(entity -> {
            DistributionCustomerVO distributionCustomerVO = new DistributionCustomerVO();
            BeanUtils.copyProperties(entity, distributionCustomerVO);
            return distributionCustomerVO;
        }).collect(Collectors.toList());
    }


    /**
     * 初始化分销员
     * @param request
     */
    public void init(EsDistributionCustomerPageRequest request) {
        Boolean flg = Boolean.TRUE;
        int pageNum = request.getPageNum();
        Integer pageSize = 2000;

        DistributionCustomerPageRequest queryRequest = KsBeanUtil.convert(request, DistributionCustomerPageRequest.class);
        try {
            while (flg) {
                queryRequest.putSort("createTime", SortType.DESC.toValue());
                queryRequest.setPageNum(pageNum);
                queryRequest.setPageSize(pageSize);
                List<DistributionCustomerShowPhoneVO> distributionCustomers = distributionCustomerQueryProvider.pageShowPhone(queryRequest).getContext()
                        .getDistributionCustomerVOPage().getContent();
                if (CollectionUtils.isEmpty(distributionCustomers)) {
                    flg = Boolean.FALSE;
                    log.info("==========ES初始化分销员列表，结束pageNum:{}==============", pageNum);
                } else {
                    List<EsDistributionCustomer> newInfos = KsBeanUtil.convert(distributionCustomers, EsDistributionCustomer.class);
                    saveAll(newInfos);
                    log.info("==========ES初始化分销员列表成功，当前pageNum:{}==============", pageNum);
                    pageNum++;
                }
            }
        } catch (Exception e) {
            log.info("==========ES初始化分销员列表异常，异常pageNum:{}==============", pageNum);
            throw new SbcRuntimeException("K-120011", new Object[]{pageNum});
        }

    }

    private Iterable<EsDistributionCustomer> saveAll(List<EsDistributionCustomer> newInfos) {
        //手动删除索引时，重新设置mapping
        if(!elasticsearchTemplate.indexExists(EsDistributionCustomer.class)){
            elasticsearchTemplate.createIndex(EsDistributionCustomer.class);
            elasticsearchTemplate.putMapping(EsDistributionCustomer.class);
        }
        return esDistributionCustomerRepository.saveAll(newInfos);
    }
}
