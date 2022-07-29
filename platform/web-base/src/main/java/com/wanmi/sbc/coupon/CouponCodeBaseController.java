package com.wanmi.sbc.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.coupon.request.CouponCheckoutBaseRequest;
import com.wanmi.sbc.coupon.request.CouponFetchBaseRequest;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCheckoutRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodePageRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeSimplePageRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponFetchRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCheckoutResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCodePageResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCodeSimplePageResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 *
 * Created by CHENLI on 2018/9/21.
 */
@RestController
@RequestMapping("/coupon-code")
@Api(tags = "CouponCodeBaseController", description = "S2B web公用-我的优惠券API")
@RefreshScope
public class CouponCodeBaseController {

//    @Value("${coupon.mini.ids:[]}")
//    private List<String> couponIds;

    @Autowired
    private CouponCodeQueryProvider couponCodeQueryProvider;

    @Autowired
    private CouponCodeProvider couponCodeProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * APP / H5 查询我的优惠券
     * @param request
     * @return
     */
    @ApiOperation(value = "APP/H5查询我的优惠券")
    @RequestMapping(value = "/my-coupon", method = RequestMethod.POST)
    public BaseResponse<CouponCodePageResponse> listMyCouponList(@RequestBody CouponCodePageRequest request){
        request.setCustomerId(commonUtil.getOperatorId());
//        String terminal = HttpUtil.getRequest().getHeader("terminal");
//        if("MINIPROGRAM".equals(terminal)){
//            BaseResponse<MarketingNacosConfigResponse> miniAppConfig = couponCodeQueryProvider.getMiniAppConfig();
//            request.setCouponIds(miniAppConfig.getContext().getAppMiniCouponIdList());
//        }
        return couponCodeQueryProvider.page(request);
    }

    /**
     * 根据活动和优惠券领券
     * @param baseRequest
     * @return
     */
    @ApiOperation(value = "根据活动和优惠券领券")
    @RequestMapping(value = "/fetch-coupon", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse customerFetchCoupon(@Valid @RequestBody CouponFetchBaseRequest baseRequest){
        CouponFetchRequest request = new CouponFetchRequest();
        KsBeanUtil.copyProperties(baseRequest, request);
        request.setCustomerId(commonUtil.getOperatorId());

        couponCodeProvider.fetch(request);
        return BaseResponse.success(null);
    }

    /**
     * 使用优惠券选择时的后台处理
     * @param baseRequest
     * @return
     */
    @ApiOperation(value = "使用优惠券选择时的后台处理")
    @RequestMapping(value = "/checkout-coupons", method = RequestMethod.POST)
    public BaseResponse<CouponCheckoutResponse> checkoutCoupons(@Valid @RequestBody CouponCheckoutBaseRequest baseRequest) {
        CouponCheckoutRequest request = new CouponCheckoutRequest();
        KsBeanUtil.copyProperties(baseRequest, request);
        request.setCustomerId(commonUtil.getOperatorId());

        return couponCodeQueryProvider.checkout(request);
    }

    /**
     * APP / H5 查询我的优惠券
     * @param request
     * @return
     */
    @ApiOperation(value = "查询我的优惠券")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public BaseResponse<CouponCodeSimplePageResponse> list(@RequestBody CouponCodeSimplePageRequest request){
        Operator operator = commonUtil.getOperator();
        request.setCustomerId(operator.getUserId());
        //是否第一次登陆
        if(Boolean.TRUE.equals(request.getFirstLoginShowFlag())
                && Boolean.FALSE.equals(operator.getFirstLogin())){
            return BaseResponse.success(CouponCodeSimplePageResponse.builder().build());
        }

        return couponCodeQueryProvider.simplePage(request);
    }
}
