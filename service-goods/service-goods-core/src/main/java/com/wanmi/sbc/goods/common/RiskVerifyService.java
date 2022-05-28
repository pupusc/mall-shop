package com.wanmi.sbc.goods.common;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.utils.StringUtils;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.common.ImageAuditRequest;
import com.wanmi.sbc.goods.api.request.common.ImageVerifyRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsImageAuditResponse;
import com.wanmi.sbc.goods.common.model.root.RiskVerify;
import com.wanmi.sbc.goods.common.repository.RiskVerifyRepository;
import com.wanmi.sbc.goods.fandeng.ExternalService;
import com.wanmi.sbc.goods.info.repository.GoodsSyncRepository;
import com.wanmi.sbc.goods.info.request.GoodsStockSyncQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class RiskVerifyService {

    @Autowired
    private RiskVerifyRepository riskVerifyRepository;

    @Autowired
    private ExternalService externalService;

    @Autowired
    private GoodsSyncRepository goodsSyncRepository;

    public void verifyImage() {
//        GoodsSyncQueryRequest goodsSyncQueryRequest = GoodsSyncQueryRequest.builder().adAuditStatus(GoodsAdAuditStatus.WAITTOAUDIT.toValue()).build();
//        goodsSyncQueryRequest.setPageSize(100);
//        Page<GoodsSync> goodsList = goodsSyncRepository.findAll(goodsSyncQueryRequest.getWhereCriteria(),goodsSyncQueryRequest.getPageRequest());
//        if(goodsList == null || CollectionUtils.isEmpty(goodsList.getContent() )){
//            log.info("verifyImage,there is no wait to tongdun list");
//            return;
//        }
//        GoodsStockSyncQueryRequest request = new GoodsStockSyncQueryRequest();
//        request.setStatus(0);
//        request.setGoodsNos(goodsList.getContent().stream().map(GoodsSync::getGoodsNo).collect(Collectors.toList()));
//        List<RiskVerify> list = riskVerifyRepository.findAll(request.getVerifyWhereCriteria());
//        if(CollectionUtils.isEmpty(list)){
//            log.info("verifyImage,there id no image list");
//            return;
//        }
//        list.stream().forEach(p->{
//            try {
//                BaseResponse<GoodsImageAuditResponse> response =  externalService.aduitImage(ImageAuditRequest.builder().verifyTool("GreenVerifyTongdun").content(p.getImageUrl()).imgType("ADS").customerId(p.getId().toString()).build());
//                if(response != null && response.getContext() != null &&  !StringUtils.isEmpty(response.getContext().getRequestId())) {
//                    riskVerifyRepository.updateStatusById(1,p.getId(),response.getContext().getRequestId());
//                }
//            }catch (Exception e){
//                log.warn("push to tongdun error,request:{}", JSON.toJSONString(p),e);
//            }
//        });
//        goodsSyncRepository.batchUpdateAdStatus(goodsList.getContent().stream().map(GoodsSync::getId).collect(Collectors.toList()), GoodsAdAuditStatus.AUDITING.toValue(),GoodsAdAuditStatus.WAITTOAUDIT.toValue());
        GoodsStockSyncQueryRequest request = new GoodsStockSyncQueryRequest();
        request.setStatus(0);
        request.setPageNum(0);
        request.setPageSize(100);
        Page<RiskVerify> pageList = riskVerifyRepository.findAll(request.getVerifyWhereCriteria(), request.getPageRequest());
        if (pageList == null || CollectionUtils.isEmpty(pageList.getContent())) {
            return;
        }
        pageList.getContent().stream().forEach(p -> {
            try {
                BaseResponse<GoodsImageAuditResponse> response = externalService.aduitImage(ImageAuditRequest.builder().verifyTool("GreenVerifyTongdun").content(p.getImageUrl()).imgType("ADS").customerId(p.getId().toString()).build());
                if (response != null && response.getContext() != null && !StringUtils.isEmpty(response.getContext().getRequestId())) {
                    riskVerifyRepository.updateStatusById(1, p.getId(), response.getContext().getRequestId());
                }
            } catch (Exception e) {
                log.warn("push to tongdun error,request:{}", JSON.toJSONString(p), e);
            }
        });
    }


    @Transactional
    public void verifyImageCallBack(ImageVerifyRequest imageVerifyRequest) {
        RiskVerify riskVerify = riskVerifyRepository.getByRequestId(imageVerifyRequest.getRequestId());
        riskVerify.setRequestId(imageVerifyRequest.getRequestId());
        riskVerify.setStatus(imageVerifyRequest.getSuggestion().equals("PASS") ? 2 : imageVerifyRequest.getSuggestion().equals("BLOCK") ? 3 : 1);
        if (imageVerifyRequest.getSuggestion().equals("BLOCK")) {
            riskVerify.setErrorMsg(imageVerifyRequest.getHitReason());
            //goodsSyncRepository.updateAdStatus(riskVerify.getGoodsNo(), GoodsAdAuditStatus.FAIL.toValue(), GoodsAdAuditStatus.AUDITING.toValue());
            riskVerifyRepository.updateStatus(riskVerify.getStatus(), riskVerify.getErrorMsg(), imageVerifyRequest.getOcrStr(), riskVerify.getRequestId());
            goodsSyncRepository.updateStatus(riskVerify.getGoodsNo(),5);
            return;
        }
        riskVerifyRepository.updateStatus(riskVerify.getStatus(), riskVerify.getErrorMsg(), imageVerifyRequest.getOcrStr(), riskVerify.getRequestId());
        if (riskVerifyRepository.queryCount(riskVerify.getGoodsNo(), imageVerifyRequest.getRequestId()) == 0) {
            //全部审核通过
            //goodsSyncRepository.updateAdStatus(riskVerify.getGoodsNo(), GoodsAdAuditStatus.SUCCESS.toValue(), GoodsAdAuditStatus.AUDITING.toValue());
            goodsSyncRepository.updateStatus(riskVerify.getGoodsNo(),2);
        }
    }

}
