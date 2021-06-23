package com.wanmi.sbc.elastic.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.coupon.EsCouponActivityQueryProvider;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityPageRequest;
import com.wanmi.sbc.elastic.api.response.coupon.EsCouponActivityPageResponse;
import com.wanmi.sbc.elastic.bean.vo.coupon.EsCouponActivityVO;
import com.wanmi.sbc.elastic.coupon.model.root.EsCouponActivity;
import com.wanmi.sbc.elastic.coupon.service.EsCouponActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class EsCouponActivityQueryController implements EsCouponActivityQueryProvider {

    @Autowired
    private EsCouponActivityService esCouponActivityService;

    @Override
    public BaseResponse<EsCouponActivityPageResponse> page(@RequestBody @Valid EsCouponActivityPageRequest request) {
        Page<EsCouponActivity> esCouponInfoPage = esCouponActivityService.page(request);
        MicroServicePage<EsCouponActivityVO> esCouponInfoVOS = KsBeanUtil.convertPage(esCouponInfoPage, EsCouponActivityVO.class);
        return BaseResponse.success(new EsCouponActivityPageResponse(esCouponInfoVOS));
    }
}
