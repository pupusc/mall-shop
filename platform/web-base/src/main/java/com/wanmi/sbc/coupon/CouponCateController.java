package com.wanmi.sbc.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCateQueryProvider;
import com.wanmi.sbc.marketing.bean.vo.CouponCateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: songhanlin
 * @Date: Created In 10:39 AM 2018/9/14
 * @Description: 优惠券分类Controller
 */
@RestController
@RequestMapping("/coupon-cate")
@Validated
@Api(tags = "CouponCateController", description = "S2B web公用-优惠券分类API")
public class CouponCateController {

    @Resource
    private CouponCateQueryProvider couponCateQueryProvider;

    /**
     * 查询优惠券分类列表
     *
     * @return
     */
    @ApiOperation(value = "查询优惠券分类列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResponse<List<CouponCateVO>> listCouponCate() {
        return BaseResponse.success(couponCateQueryProvider.list().getContext().getList());
    }
}
