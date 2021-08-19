package com.wanmi.sbc.elastic.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.coupon.EsCouponInfoQueryProvider;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoPageRequest;
import com.wanmi.sbc.elastic.api.response.coupon.EsCouponInfoPageResponse;
import com.wanmi.sbc.elastic.bean.vo.coupon.EsCouponInfoVO;
import com.wanmi.sbc.elastic.coupon.model.root.EsCouponInfo;
import com.wanmi.sbc.elastic.coupon.service.EsCouponInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class EsCouponInfoQueryController implements EsCouponInfoQueryProvider {

    @Autowired
    private EsCouponInfoService esCouponInfoService;

    @Override
    public BaseResponse<EsCouponInfoPageResponse> page(@Valid EsCouponInfoPageRequest request) {
        Page<EsCouponInfo> esCouponInfoPage = esCouponInfoService.page(request);
        MicroServicePage<EsCouponInfoVO> esCouponInfoVOS = KsBeanUtil.convertPage(esCouponInfoPage,EsCouponInfoVO.class);
        esCouponInfoService.wrapperScopeNamesAndCateNames(esCouponInfoVOS.getContent());
        return BaseResponse.success(new EsCouponInfoPageResponse(esCouponInfoVOS));
    }
}
