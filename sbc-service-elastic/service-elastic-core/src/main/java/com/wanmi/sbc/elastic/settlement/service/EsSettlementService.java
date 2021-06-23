package com.wanmi.sbc.elastic.settlement.service;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.request.settlement.*;
import com.wanmi.sbc.elastic.api.response.settlement.EsSettlementResponse;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsSearchInfoResponse;
import com.wanmi.sbc.elastic.bean.dto.settlement.EsSettlementDTO;
import com.wanmi.sbc.elastic.bean.vo.settlement.EsSettlementVO;
import com.wanmi.sbc.elastic.settlement.model.root.EsSettlement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName EsSettlementService
 * @Description 结算单
 * @Author yangzhen
 * @Date 2020/12/11 14:25
 * @Version 1.0
 */
@Slf4j
@Service
public class EsSettlementService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ResultsMapper resultsMapper;


    /**
     * 初始化结算单到ES
     */
    public void initSettlement(EsSettlementRequest request) {
        EsSettlement esSettlement = new EsSettlement();
        KsBeanUtil.copyPropertiesThird(request,esSettlement);
        esSettlement.setId(String.valueOf(request.getSettleId()));
        List<IndexQuery> esSettlements = new ArrayList<>();
        IndexQuery iq = new IndexQuery();
        iq.setObject(esSettlement);
        iq.setIndexName(EsConstants.DOC_SETTLEMENT);
        iq.setType(EsConstants.DOC_SETTLEMENT);
        esSettlements.add(iq);
        elasticsearchTemplate.bulkIndex(esSettlements);
    }



    /**
     * 初始化店铺信息到ES
     */
    public void initSettlementList(EsSettlementListRequest request) {
        if (!elasticsearchTemplate.indexExists(EsConstants.DOC_SETTLEMENT)) {
            //第一次进入 没有索引时
            elasticsearchTemplate.getClient().admin().indices()
                    .prepareCreate(EsConstants.DOC_SETTLEMENT).execute().actionGet();
            elasticsearchTemplate.putMapping(EsSettlement.class);
        }

        List<EsSettlementDTO>  esSettlementDTOs = request.getLists();
        if(CollectionUtils.isNotEmpty(esSettlementDTOs)){
            List<IndexQuery> settlements = new ArrayList<>();
            esSettlementDTOs.forEach(esSettlementDTO  -> {
                IndexQuery iq = new IndexQuery();
                EsSettlement esSettlement = new EsSettlement();
                esSettlement = KsBeanUtil.convert(esSettlementDTO,EsSettlement.class);
                esSettlement.setId(String.valueOf(esSettlementDTO.getSettleId()));

                esSettlement.setStartTime(DateUtil.parseDay(esSettlementDTO.getStartTime()));
                esSettlement.setEndTime(DateUtil.parseDay(esSettlementDTO.getEndTime()));

                iq.setObject(esSettlement);
                iq.setIndexName(EsConstants.DOC_SETTLEMENT);
                iq.setType(EsConstants.DOC_SETTLEMENT);
                settlements.add(iq);
            });
            elasticsearchTemplate.bulkIndex(settlements);
        }
    }



    /**
     * es分页查找结算单
     *
     * @param queryRequest
     * @return
     */
    public EsSettlementResponse querySettlementPage(EsSettlementPageRequest queryRequest) {
        EsSettlementResponse response = new EsSettlementResponse();
        EsSearchInfoResponse<EsSettlementVO> page = elasticsearchTemplate.query(queryRequest.getSearchCriteria(),
                searchResponse -> EsSearchInfoResponse.build(searchResponse, resultsMapper, EsSettlementVO.class));

        response.setEsSettlementVOPage(new MicroServicePage<>(page.getData().stream().map(settlementViewVo -> {
            settlementViewVo.setStartTime(DateUtil.getDate(DateUtil.parse(settlementViewVo.getStartTime(),DateUtil.FMT_TIME_4)));
            settlementViewVo.setEndTime(DateUtil.getDate(DateUtil.parse(settlementViewVo.getEndTime(),DateUtil.FMT_TIME_4)));
            settlementViewVo.setSettlementCode(settlementViewVo.getSettleCode());
            return settlementViewVo;
        }).collect(Collectors.toList()),
                PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()), page.getTotal()));
        return response;

    }

    /**
     * 设置结算状态
     */
    public Boolean updateSettlementStatus(SettlementQueryRequest request) {
        Client client = elasticsearchTemplate.getClient();
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(request.getWhereCriteria()).build();
        Iterable<EsSettlement> esSettlementList = elasticsearchTemplate.queryForList(searchQuery, EsSettlement.class);

        Map<String,Object> updateMap ;
        if (esSettlementList != null) {
            for(EsSettlement esSettlement : esSettlementList){
                updateMap = new HashMap<>();
                updateMap.put("settleStatus", request.getStatus());
                updateMap.put("settleTime",DateUtil.format(LocalDateTime.now(),DateUtil.FMT_TIME_4));
                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_SETTLEMENT)
                        .setType(EsConstants.DOC_SETTLEMENT)
                        .setId(esSettlement.getId()).setDoc(updateMap).execute().actionGet();
            }

        }
        return Boolean.TRUE;
    }

    /**
     * 更改店铺名称 店铺类型
     */
    public Boolean updateSettlementStoreInfo(SettlementStoreInfoModifyRequest request) {
        Client client = elasticsearchTemplate.getClient();
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(request.getWhereCriteria()).build();
        Iterable<EsSettlement> esSettlementList = elasticsearchTemplate.queryForList(searchQuery, EsSettlement.class);

        Map<String,Object> updateMap ;
        if (esSettlementList != null) {
            for(EsSettlement esSettlement : esSettlementList){
                updateMap = new HashMap<>();
                updateMap.put("storeName", request.getStoreName());
                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_SETTLEMENT)
                        .setType(EsConstants.DOC_SETTLEMENT)
                        .setId(esSettlement.getId()).setDoc(updateMap).execute().actionGet();
            }

        }
        return Boolean.TRUE;
    }

}
