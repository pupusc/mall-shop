package com.wanmi.sbc.goods.mini.wx.callback.handler;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.mini.enums.goods.WxGoodsEditStatus;
import com.wanmi.sbc.goods.mini.enums.goods.WxGoodsStatus;
import com.wanmi.sbc.goods.mini.enums.review.WxReviewResult;
import com.wanmi.sbc.goods.mini.model.goods.WxGoodsModel;
import com.wanmi.sbc.goods.mini.model.review.WxReviewLogModel;
import com.wanmi.sbc.goods.mini.repository.goods.WxGoodsRepository;
import com.wanmi.sbc.goods.mini.repository.review.WxReviewLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ProductAuditCallbackHandler implements CallbackHandler {

    @Autowired
    private WxGoodsRepository wxGoodsRepository;
    @Autowired
    private WxReviewLogRepository wxReviewLogRepository;

    @Override
    public boolean support(String eventType) {
        return "open_product_spu_audit".equals(eventType);
    }

    @Override
    public void handle(Map<String, Object> paramMap) {
        Map<String, String> auditResult = (Map<String, String>) paramMap.get("OpenProductSpuAudit");
        String wxStatus = auditResult.get("status");
        String goodsId = auditResult.get("out_product_id");
        if("4".equals(wxStatus)){
            //成功
            WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(goodsId, DeleteFlag.NO);
            wxGoodsModel.setStatus(WxGoodsStatus.ON_SHELF);
            wxGoodsModel.setEditStatus(WxGoodsEditStatus.CHECK_SUCCESS);
            wxGoodsRepository.save(wxGoodsModel);

            WxReviewLogModel wxReviewLogModel = new WxReviewLogModel();
            wxReviewLogModel.setRelateId(wxGoodsModel.getId());
            wxReviewLogModel.setReviewedTime(LocalDateTime.now());
            wxReviewLogModel.setReviewResult(WxReviewResult.SUCCESS);
            wxReviewLogRepository.save(wxReviewLogModel);

            /*WxGoodsStatus wxGoodsStatus = wxGoodsModel.getStatus();
            WxGoodsEditStatus editStatus = wxGoodsModel.getEditStatus();
            if(wxGoodsStatus.equals(WxGoodsStatus.ON_UPLOAD) && editStatus.equals(WxGoodsEditStatus.ON_CHECK)){
                //第一次审核成功

            }else if(wxGoodsStatus.equals(WxGoodsStatus.UPLOAD) && editStatus.equals(WxGoodsEditStatus.ON_CHECK)){

            }*/
        }else if("3".equals(wxStatus)){
            //失败
            String rejectReason = auditResult.get("reject_reason");
            WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(goodsId, DeleteFlag.NO);
            wxGoodsModel.setStatus(WxGoodsStatus.UPLOAD);
            wxGoodsModel.setEditStatus(WxGoodsEditStatus.CHECK_FAILED);
            wxGoodsRepository.save(wxGoodsModel);

            WxReviewLogModel wxReviewLogModel = new WxReviewLogModel();
            wxReviewLogModel.setRelateId(wxGoodsModel.getId());
            wxReviewLogModel.setReviewedTime(LocalDateTime.now());
            wxReviewLogModel.setReviewReason(rejectReason);
            wxReviewLogModel.setReviewResult(WxReviewResult.FAILED);
            wxReviewLogRepository.save(wxReviewLogModel);

        }

    }
}
