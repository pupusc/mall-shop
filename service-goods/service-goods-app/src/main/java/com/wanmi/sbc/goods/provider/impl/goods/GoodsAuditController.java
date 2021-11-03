package com.wanmi.sbc.goods.provider.impl.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goods.GoodsAuditProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsAuditModifyRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsAuditQueryRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifyRequest;
import com.wanmi.sbc.goods.info.request.GoodsSyncQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import com.wanmi.sbc.goods.bean.vo.GoodsSyncVO;
import com.wanmi.sbc.goods.info.model.root.GoodsSync;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdResponse;

@RestController
@Validated
@Slf4j
public class GoodsAuditController implements GoodsAuditProvider {

    @Autowired
    private GoodsSyncService goodsSyncService;
    
    
    @Override
    public BaseResponse<MicroServicePage<GoodsSyncVO>> list(GoodsAuditQueryRequest request) {
        GoodsSyncQueryRequest goodsQueryRequest = KsBeanUtil.convert(request, GoodsSyncQueryRequest.class);
        return BaseResponse.success(goodsSyncService.list(goodsQueryRequest));
    }

    @Override
    public BaseResponse auditGoods(GoodsAuditQueryRequest request) {
        goodsSyncService.auditGoods(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse approveAdManual(GoodsAuditQueryRequest request) {
        goodsSyncService.approveAdManual(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse rejectAdManual(GoodsAuditQueryRequest request) {
        goodsSyncService.rejectAdManual(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse launchApprove(GoodsAuditQueryRequest request) {
        goodsSyncService.approveLaunch(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse launchReject(GoodsAuditQueryRequest request) {
        goodsSyncService.rejectLaunch(request);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse publish(GoodsAuditQueryRequest request) {
        goodsSyncService.publish(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<GoodsViewByIdResponse> detail(String goodsNo) {
        return BaseResponse.success(goodsSyncService.detail(goodsNo));
    }

    @Override
    public BaseResponse reAudit(GoodsAuditQueryRequest request) {
        goodsSyncService.reAudit(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modify(GoodsAuditModifyRequest request) {
        goodsSyncService.modify(request);
        return BaseResponse.SUCCESSFUL();
    }
}
