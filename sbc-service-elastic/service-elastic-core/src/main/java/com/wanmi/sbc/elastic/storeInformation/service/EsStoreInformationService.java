package com.wanmi.sbc.elastic.storeInformation.service;

import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.vo.EsStoreInfoVo;
import com.wanmi.sbc.elastic.api.request.settlement.SettlementStoreInfoModifyRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.*;
import com.wanmi.sbc.elastic.settlement.service.EsSettlementService;
import com.wanmi.sbc.elastic.sku.service.EsSkuService;
import com.wanmi.sbc.elastic.spu.serivce.EsSpuService;
import com.wanmi.sbc.elastic.standard.service.EsStandardService;
import com.wanmi.sbc.elastic.storeInformation.model.root.StoreInformation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author yangzhen
 * @Description //ES公司店铺信息
 * @Date 9:44 2020/12/8
 * @Param
 * @return
 **/
@Slf4j
@Service
public class EsStoreInformationService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private EsSkuService esSkuService;

    @Autowired
    private EsSpuService esSpuService;

    @Autowired
    private EsStandardService esStandardService;

    @Autowired
    private EsSettlementService esSettlementService;


    /**
     * 初始化店铺信息到ES
     */
    public void initStoreInformation(StoreInformationRequest request) {
        if (!elasticsearchTemplate.indexExists(EsConstants.DOC_STORE_INFORMATION_TYPE)) {
            //第一次进入 没有索引时
            elasticsearchTemplate.getClient().admin().indices()
                    .prepareCreate(EsConstants.DOC_STORE_INFORMATION_TYPE).execute().actionGet();
            elasticsearchTemplate.putMapping(StoreInformation.class);
        }


        StoreInformation storeInformation = new StoreInformation();
        KsBeanUtil.copyPropertiesThird(request, storeInformation);
        storeInformation.setId(String.valueOf(request.getStoreId()));
        List<IndexQuery> storeInformations = new ArrayList<>();
        IndexQuery iq = new IndexQuery();
        iq.setObject(storeInformation);
        iq.setIndexName(EsConstants.DOC_STORE_INFORMATION_TYPE);
        iq.setType(EsConstants.DOC_STORE_INFORMATION_TYPE);
        storeInformations.add(iq);
        elasticsearchTemplate.bulkIndex(storeInformations);
    }

    /**
     * 批量刷es数据
     */
    public void initStoreInformationList(EsInitStoreInfoRequest request) {
        List<EsStoreInfoVo> esStoreInfoVOList = request.getLists();

        if (!elasticsearchTemplate.indexExists(EsConstants.DOC_STORE_INFORMATION_TYPE)) {
            //第一次进入 没有索引时
            elasticsearchTemplate.getClient().admin().indices()
                    .prepareCreate(EsConstants.DOC_STORE_INFORMATION_TYPE).execute().actionGet();
            elasticsearchTemplate.putMapping(StoreInformation.class);
        }

        if (CollectionUtils.isNotEmpty(esStoreInfoVOList)) {
            List<IndexQuery> storeInformations = new ArrayList<>();
            esStoreInfoVOList.forEach(esStoreInfoVO -> {
                IndexQuery iq = new IndexQuery();
                StoreInformation storeInformation = new StoreInformation();
                storeInformation = KsBeanUtil.convert(esStoreInfoVO, StoreInformation.class);
                storeInformation.setId(String.valueOf(esStoreInfoVO.getStoreId()));
                iq.setObject(storeInformation);
                iq.setIndexName(EsConstants.DOC_STORE_INFORMATION_TYPE);
                iq.setType(EsConstants.DOC_STORE_INFORMATION_TYPE);
                storeInformations.add(iq);
            });
            elasticsearchTemplate.bulkIndex(storeInformations);
        }
    }



    /**
     * 修改基本信息
     */
    public Boolean modifyStoreBasicInfo(StoreInfoModifyRequest request) {
        Client client = elasticsearchTemplate.getClient();
        StoreInfoQueryRequest queryRequest = new StoreInfoQueryRequest();
        queryRequest.setStoreId(request.getStoreId());
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        List<StoreInformation> esStoreInfoList = elasticsearchTemplate.queryForList(searchQuery, StoreInformation.class);

        Map<String,Object> updateMap ;
        if (esStoreInfoList != null) {
            for(StoreInformation storeInformation : esStoreInfoList){
                updateMap = new HashMap<>();
                updateMap.put("storeName",request.getStoreName());
                updateMap.put("supplierName",request.getSupplierName());
                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_STORE_INFORMATION_TYPE)
                        .setType(EsConstants.DOC_STORE_INFORMATION_TYPE)
                        .setId(storeInformation.getId()).setDoc(updateMap).execute().actionGet();

                storeInformation.setStoreName(request.getStoreName());
                storeInformation.setSupplierName(request.getSupplierName());
            }

            //更新商品
            esSpuService.updateCompanyName(esStoreInfoList);

            //更新sku商品
            esSkuService.updateCompanyName(esStoreInfoList);

            //更新商品库
            esStandardService.updateProviderName(esStoreInfoList);

            //更新结算单店铺名称
            esSettlementService.updateSettlementStoreInfo(SettlementStoreInfoModifyRequest
                    .builder()
                    .storeId(request.getStoreId())
                    .storeName(request.getStoreName()).build());
        }
        return Boolean.TRUE;
    }


    /**
     * 修改店铺审核状态/开关店/启用禁用/确认打款
     */
    public Boolean modifyStoreState(StoreInfoStateModifyRequest request) {
        Client client = elasticsearchTemplate.getClient();
        StoreInfoQueryRequest queryRequest = new StoreInfoQueryRequest();
        queryRequest.setStoreId(request.getStoreId());
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<StoreInformation> esStoreInfoList = elasticsearchTemplate.queryForList(searchQuery, StoreInformation.class);

        Map<String,Object> updateMap ;
        if (esStoreInfoList != null) {
            for(StoreInformation storeInformation : esStoreInfoList){
                updateMap = new HashMap<>();
                //审核
                if(Objects.nonNull(request.getAuditState())){
                    updateMap.put("auditState",request.getAuditState());
                    updateMap.put("auditReason",request.getAuditReason());
                }
                //开关店
                if(Objects.nonNull(request.getStoreState())){
                    updateMap.put("storeState",request.getStoreState());
                    updateMap.put("storeClosedReason",request.getStoreClosedReason());
                }
                //启用禁用
                if(Objects.nonNull(request.getAccountState())){
                    updateMap.put("accountState",request.getAccountState());
                    updateMap.put("accountDisableReason",request.getAccountDisableReason());
                }

                //是否确认打款
                if(Objects.nonNull(request.getRemitAffirm())){
                    updateMap.put("remitAffirm",request.getRemitAffirm());
                }
                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_STORE_INFORMATION_TYPE)
                        .setType(EsConstants.DOC_STORE_INFORMATION_TYPE)
                        .setId(storeInformation.getId()).setDoc(updateMap).execute().actionGet();
            }

        }
        return Boolean.TRUE;
    }



    /**
     * 店铺审核时
     */
    public Boolean modifyStoreReject(StoreInfoRejectModifyRequest request) {
        Client client = elasticsearchTemplate.getClient();
        StoreInfoQueryRequest queryRequest = new StoreInfoQueryRequest();
        queryRequest.setStoreId(request.getStoreId());
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<StoreInformation> esStoreInfoList = elasticsearchTemplate.queryForList(searchQuery, StoreInformation.class);

        Map<String,Object> updateMap ;
        if (esStoreInfoList != null) {
            for(StoreInformation storeInformation : esStoreInfoList){
                updateMap = new HashMap<>();
                updateMap.put("auditState",request.getAuditState().toValue());
                if(CheckState.CHECKED.equals(request.getAuditState())){
                    updateMap.put("companyType",request.getCompanyType().toValue());
                    updateMap.put("storeType",request.getStoreType().toValue());
                    updateMap.put("storeState",request.getStoreState().toValue());
                    updateMap.put("contractStartDate",request.getContractStartDate().format(DateTimeFormatter.ofPattern("yyyy" +
                            "-MM-dd HH:mm:ss.SSS")));
                    updateMap.put("contractEndDate",request.getContractEndDate().format(DateTimeFormatter.ofPattern("yyyy" +
                            "-MM-dd HH:mm:ss.SSS")));
                    updateMap.put("applyEnterTime",request.getApplyEnterTime().format(DateTimeFormatter.ofPattern("yyyy" +
                            "-MM-dd HH:mm:ss.SSS")));
                }else if(CheckState.NOT_PASS.equals(request.getAuditState())){
                    updateMap.put("auditState",request.getAuditReason());
                }


                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_STORE_INFORMATION_TYPE)
                        .setType(EsConstants.DOC_STORE_INFORMATION_TYPE)
                        .setId(storeInformation.getId()).setDoc(updateMap).execute().actionGet();
            }

        }
        return Boolean.TRUE;
    }


    /**
     * 修改签约信息
     */
    public Boolean modifyStoreContractInfo(StoreInfoContractRequest request) {
        Client client = elasticsearchTemplate.getClient();
        StoreInfoQueryRequest queryRequest = new StoreInfoQueryRequest();
        queryRequest.setStoreId(request.getStoreId());
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<StoreInformation> esStoreInfoList = elasticsearchTemplate.queryForList(searchQuery, StoreInformation.class);

        Map<String,Object> updateMap ;
        if (esStoreInfoList != null) {
            for(StoreInformation storeInformation : esStoreInfoList){
                updateMap = new HashMap<>();
                updateMap.put("companyType",request.getCompanyType().toValue());
                updateMap.put("contractStartDate",request.getContractStartDate().format(DateTimeFormatter.ofPattern("yyyy" +
                        "-MM-dd HH:mm:ss.SSS")));
                updateMap.put("contractEndDate",request.getContractEndDate().format(DateTimeFormatter.ofPattern("yyyy" +
                        "-MM-dd HH:mm:ss.SSS")));
                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_STORE_INFORMATION_TYPE)
                        .setType(EsConstants.DOC_STORE_INFORMATION_TYPE)
                        .setId(storeInformation.getId()).setDoc(updateMap).execute().actionGet();
            }

        }
        return Boolean.TRUE;
    }

}
