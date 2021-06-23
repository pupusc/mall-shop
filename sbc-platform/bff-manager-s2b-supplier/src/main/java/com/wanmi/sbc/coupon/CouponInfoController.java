package com.wanmi.sbc.coupon;

import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.coupon.EsCouponInfoProvider;
import com.wanmi.sbc.elastic.api.provider.coupon.EsCouponInfoQueryProvider;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoAddRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoPageRequest;
import com.wanmi.sbc.elastic.api.response.coupon.EsCouponInfoPageResponse;
import com.wanmi.sbc.elastic.bean.dto.coupon.EsCouponInfoDTO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponInfoProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponInfoQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoAddRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoAddResponse;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "CouponInfoController", description = "优惠券信息 API")
@RestController
@RequestMapping("/coupon-info")
@Validated
public class CouponInfoController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CouponInfoProvider couponInfoProvider;

    @Autowired
    private CouponInfoQueryProvider couponInfoQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EsCouponInfoProvider esCouponInfoProvider;

    @Autowired
    private EsCouponInfoQueryProvider esCouponInfoQueryProvider;

    /**
     * 获取优惠券列表
     *
     * @param couponInfoQueryRequest
     * @return
     */
    @ApiOperation(value = "获取优惠券列表")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<EsCouponInfoPageResponse> page(@RequestBody EsCouponInfoPageRequest couponInfoQueryRequest) {
        couponInfoQueryRequest.setPlatformFlag(DefaultFlag.NO);
        couponInfoQueryRequest.setDelFlag(DeleteFlag.NO);
        couponInfoQueryRequest.setStoreId(commonUtil.getStoreId());
        couponInfoQueryRequest.putSort("createTime", SortType.DESC.toValue());
        return esCouponInfoQueryProvider.page(couponInfoQueryRequest);
    }

    /**
     * 新增优惠券
     * @param request
     * @return
     */
    @ApiOperation(value = "新增优惠券")
    @MultiSubmit
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse add(@Valid @RequestBody CouponInfoAddRequest request) {
        request.setPlatformFlag(DefaultFlag.NO);
        request.setCouponType(CouponType.STORE_VOUCHERS);
        request.setStoreId(commonUtil.getStoreId());
        request.setCreatePerson(commonUtil.getOperatorId());
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "创建优惠券", "优惠券名称：" + request.getCouponName());
        BaseResponse<CouponInfoAddResponse> baseResponse = couponInfoProvider.add(request);
        CouponInfoVO couponInfoVO = baseResponse.getContext().getCouponInfoVO();
        EsCouponInfoDTO esCouponInfoDTO = KsBeanUtil.convert(couponInfoVO,EsCouponInfoDTO.class);
        esCouponInfoProvider.add(new EsCouponInfoAddRequest(esCouponInfoDTO));
        return BaseResponse.SUCCESSFUL();
    }

}
