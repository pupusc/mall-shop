package com.wanmi.sbc.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.customerplan.CustomerPlanQueryProvider;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanListRequest;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanVO;
import com.wanmi.sbc.elastic.api.provider.coupon.EsCouponInfoProvider;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoAddRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoDeleteByIdRequest;
import com.wanmi.sbc.elastic.bean.dto.coupon.EsCouponInfoDTO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponInfoProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponInfoQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.*;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoCopyByIdResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoDetailByIdResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoModifyResponse;
import com.wanmi.sbc.marketing.bean.constant.CouponErrorCode;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Api(tags = "CouponInfoBaseController", description = "优惠券基本 Api")
@RestController
@RequestMapping("/coupon-info")
@Validated
public class CouponInfoBaseController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CouponInfoProvider couponInfoProvider;

    @Autowired
    private CouponInfoQueryProvider couponInfoQueryProvider;

    @Autowired
    private CustomerPlanQueryProvider customerPlanQueryProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EsCouponInfoProvider esCouponInfoProvider;
    /**
     * 删除优惠券
     *
     * @param couponId
     * @return
     */
    @ApiOperation(value = "删除优惠券")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "couponId", value = "优惠券Id", required = true)
    @RequestMapping(value = "/{couponId}", method = RequestMethod.DELETE)
    public BaseResponse deleteMarketingId(@PathVariable("couponId") String couponId) {
        CouponInfoVO vo = couponInfoQueryProvider.getById(CouponInfoByIdRequest.builder().couponId(couponId).build())
                .getContext();
        if (Objects.nonNull(vo)) {
            //验证是否购买CRM
            TradeConfigGetByTypeRequest typeRequest = new TradeConfigGetByTypeRequest();
            typeRequest.setConfigType(ConfigType.CRM_FLAG);
            Integer status = auditQueryProvider.getTradeConfigByType(typeRequest).getContext().getStatus();
            if(Constants.yes.equals(status)) {
                //判断计划名称是否已经存在
                List<CustomerPlanVO> customerPlanList = customerPlanQueryProvider.list(
                        CustomerPlanListRequest.builder().couponId(couponId).notEndStatus(Boolean.TRUE).delFlag(DeleteFlag.NO)
                                .build()).getContext().getCustomerPlanList();
                if (CollectionUtils.isNotEmpty(customerPlanList)) {
                    throw new SbcRuntimeException("K-200403");
                }
            }

            couponInfoProvider.deleteById(CouponInfoDeleteByIdRequest.builder().couponId(couponId)
                    .operatorId(commonUtil.getOperatorId()).build());

            esCouponInfoProvider.deleteById(new EsCouponInfoDeleteByIdRequest(couponId));
            //记录操作日志
            operateLogMQUtil.convertAndSend("营销", "删除优惠券", "优惠券名称：" + vo.getCouponName());
            return BaseResponse.SUCCESSFUL();
        } else {
            throw new SbcRuntimeException(CouponErrorCode.COUPON_INFO_NOT_EXIST);
        }
    }

    /**
     * 修改优惠券
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "修改优惠券")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse modify(@Valid @RequestBody CouponInfoModifyRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        operateLogMQUtil.convertAndSend("营销", "编辑优惠券", "优惠券名称：" + request.getCouponName());
        BaseResponse<CouponInfoModifyResponse> baseResponse = couponInfoProvider.modify(request);
        CouponInfoVO couponInfoVO = baseResponse.getContext().getCouponInfoVO();
        EsCouponInfoDTO esCouponInfoDTO = KsBeanUtil.convert(couponInfoVO,EsCouponInfoDTO.class);
        esCouponInfoProvider.add(new EsCouponInfoAddRequest(esCouponInfoDTO));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据优惠券Id获取优惠券详细信息
     *
     * @param couponId
     * @return
     */
    @ApiOperation(value = "根据优惠券Id获取优惠券详细信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "couponId", value = "优惠券Id", required = true)
    @RequestMapping(value = "/{couponId}", method = RequestMethod.GET)
    public BaseResponse<CouponInfoDetailByIdResponse> getCouponInfoById(@PathVariable("couponId") String couponId) {
        return couponInfoQueryProvider.getDetailById(CouponInfoDetailByIdRequest.builder().couponId(couponId).build());
    }

    /**
     * 复制优惠券
     *
     * @param couponId
     * @return
     */
    @ApiOperation(value = "复制优惠券")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "couponId", value = "优惠券Id", required = true)
    @RequestMapping(value = "/copy/{couponId}", method = RequestMethod.GET)
    public BaseResponse copyCouponInfo(@PathVariable("couponId") String couponId) {
        CouponInfoVO vo = couponInfoQueryProvider.getById(CouponInfoByIdRequest.builder().couponId(couponId).build())
                .getContext();
        if (Objects.nonNull(vo)) {
            //记录操作日志
            operateLogMQUtil.convertAndSend("营销", "复制优惠券", "优惠券名称：" + vo.getCouponName());
            BaseResponse<CouponInfoCopyByIdResponse> baseResponse = couponInfoProvider.copyById(CouponInfoCopyByIdRequest.builder().couponId(couponId)
                    .operatorId(commonUtil.getOperatorId()).build());
            CouponInfoVO couponInfoVO = baseResponse.getContext().getCouponInfoVO();
            EsCouponInfoDTO esCouponInfoDTO = KsBeanUtil.convert(couponInfoVO,EsCouponInfoDTO.class);
            esCouponInfoProvider.add(new EsCouponInfoAddRequest(esCouponInfoDTO));
            return BaseResponse.SUCCESSFUL();
        } else {
            throw new SbcRuntimeException(CouponErrorCode.COUPON_INFO_NOT_EXIST);
        }
    }

}
