package com.wanmi.sbc.goods.provider.impl.goods;

import com.wanmi.ares.interfaces.GoodsService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goods.GoodsAuditProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsAuditQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsSyncQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsSyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.wanmi.sbc.goods.bean.vo.GoodsSyncVO;
import com.wanmi.sbc.goods.info.model.root.GoodsSync;
import java.util.Collections;
import java.util.List;
import com.wanmi.sbc.common.base.MicroServicePage;

@RestController
@Validated
@Slf4j
public class GoodsAuditController implements GoodsAuditProvider {

    @Autowired
    private GoodsSyncService goodsSyncService;
    
    @Override
    public BaseResponse<MicroServicePage<GoodsSyncVO>> list(GoodsAuditQueryRequest request) {
        GoodsSyncQueryRequest goodsQueryRequest = KsBeanUtil.convert(request, GoodsSyncQueryRequest.class);
        Page<GoodsSync> page = goodsSyncService.list(goodsQueryRequest);
        MicroServicePage<GoodsSyncVO> list = KsBeanUtil.convertPage(page, GoodsSyncVO.class);
        return BaseResponse.success(list);
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
}
