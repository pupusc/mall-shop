package com.wanmi.sbc.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodePageRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCodePageResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "CouponCodeController", description = "我的优惠券Api")
@RestController
@RequestMapping("/coupon-code")
public class CouponCodeController {

    @Autowired
    private CouponCodeQueryProvider couponCodeQueryProvider;
//    private CouponCodeService couponCodeService;

    @Autowired
    private CommonUtil commonUtil;


    /**
     * PC 查询我的优惠券
     * @param request
     * @return
     */
    @ApiOperation(value = "查询我的优惠券")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public BaseResponse<CouponCodePageResponse> listMyCouponList(@RequestBody CouponCodePageRequest request){
        request.setCustomerId(commonUtil.getOperatorId());
        return couponCodeQueryProvider.page(request);
    }
}
