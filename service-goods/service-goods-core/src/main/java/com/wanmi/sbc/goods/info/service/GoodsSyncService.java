package com.wanmi.sbc.goods.info.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.request.goods.GoodsAuditQueryRequest;
import com.wanmi.sbc.goods.bean.enums.GoodsAdAuditStatus;
import com.wanmi.sbc.goods.info.request.GoodsSyncQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.wanmi.sbc.goods.info.repository.GoodsSyncRepository;
import java.util.List;
import com.wanmi.sbc.goods.info.model.root.GoodsSync;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class GoodsSyncService {

    @Autowired
    private GoodsSyncRepository goodsSyncRepository;

    public Page<GoodsSync> list(GoodsSyncQueryRequest goodsSyncQueryRequest){
        return  goodsSyncRepository.findAll(goodsSyncQueryRequest.getWhereCriteria(),goodsSyncQueryRequest.getPageRequest());
    }

    /**
     * 提交广告法审核
     * @param request
     */
    public void auditGoods(GoodsAuditQueryRequest request){
        if(request == null || CollectionUtils.isEmpty(request.getIds())){
            throw new SbcRuntimeException("");
        }
        goodsSyncRepository.batchUpdateAdStatus(request.getIds(), GoodsAdAuditStatus.WAITTOAUDIT.toValue());
    }

    /**
     * 广告法人工审核通过
     * @param request
     */
    public void approveAdManual(GoodsAuditQueryRequest request){
        if(request == null || CollectionUtils.isEmpty(request.getIds())){
            throw new SbcRuntimeException("");
        }
        goodsSyncRepository.batchApprove(request.getIds());
    }

    /**
     * 广告法人工审核拒绝
     * @param request
     */
    public void rejectAdManual(GoodsAuditQueryRequest request){
        if(request == null || CollectionUtils.isEmpty(request.getIds())){
            throw new SbcRuntimeException("");
        }
        goodsSyncRepository.batchReject(request.getIds(), request.getRejectReason());
    }

    /**
     * 上架审核通过
     * @param request
     */
    public void approveLaunch(GoodsAuditQueryRequest request){
        if(request == null || CollectionUtils.isEmpty(request.getIds())){
            throw new SbcRuntimeException("");
        }
        goodsSyncRepository.batchApproveLaunch(request.getIds());
    }

    /**
     * 上架审核拒绝
     * @param request
     */
    public void rejectLaunch(GoodsAuditQueryRequest request){
        if(request == null || CollectionUtils.isEmpty(request.getIds())){
            throw new SbcRuntimeException("");
        }
        goodsSyncRepository.batchRejectLaunch(request.getIds(), request.getRejectReason());
    }

    /**
      发布商品
     */
    public void publish(GoodsAuditQueryRequest request){
        if(request == null || CollectionUtils.isEmpty(request.getIds())){
            throw new SbcRuntimeException("");
        }
        goodsSyncRepository.batchPublish(request.getIds());
    }
}
