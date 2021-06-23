package com.wanmi.sbc.coupon;

import com.google.common.base.Splitter;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.ResultCode;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.coupon.EsCouponActivityProvider;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityAddRequest;
import com.wanmi.sbc.elastic.bean.dto.coupon.EsCouponActivityDTO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponActivityProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponActivityAddRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponActivityDisabledTimeRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponActivityModifyRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponActivityDetailResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponActivityDisabledTimeResponse;
import com.wanmi.sbc.marketing.bean.constant.Constant;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityDisabledTimeVO;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityVO;
import com.wanmi.sbc.quartz.service.TaskJobService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@RestController
@Api(tags = "CouponActivityController", description = "S2B 平台端-优惠券活动管理API")
@RequestMapping("/coupon-activity")
@Validated
public class CouponActivityController {

    @Autowired
    private CouponActivityProvider couponActivityProvider;

    @Autowired
    private CouponActivityQueryProvider couponActivityQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private TaskJobService taskJobService;

    @Autowired
    private EsCouponActivityProvider esCouponActivityProvider;

    /**
     * 新增活动
     *
     * @param couponActivityAddRequest
     * @return
     */
    @ApiOperation(value = "新增优惠券活动")
    @MultiSubmit
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResponse add(@Valid @RequestBody CouponActivityAddRequest couponActivityAddRequest) {
        couponActivityAddRequest.setPlatformFlag(DefaultFlag.YES);
        couponActivityAddRequest.setCreatePerson(commonUtil.getOperatorId());
        couponActivityAddRequest.setStoreId(Constant.BOSS_DEFAULT_STORE_ID);
        //设置是否平台等级
        couponActivityAddRequest.setJoinLevelType(DefaultFlag.YES);
        BaseResponse<CouponActivityDetailResponse> response = couponActivityProvider.add(couponActivityAddRequest);
        //记录操作日志
        if (ResultCode.SUCCESSFUL.equals(response.getCode())) {
            CouponActivityVO couponActivity = response.getContext().getCouponActivity();
            EsCouponActivityDTO esCouponActivityDTO = KsBeanUtil.convert(couponActivity, EsCouponActivityDTO.class);
            List<String> joinLevels = Splitter.on(",").trimResults().splitToList(couponActivity.getJoinLevel());
            esCouponActivityDTO.setJoinLevels(joinLevels);
            esCouponActivityProvider.add(new EsCouponActivityAddRequest(esCouponActivityDTO));
            operateLogMQUtil.convertAndSend("营销", "创建优惠券活动", "优惠券活动：" + couponActivityAddRequest.getActivityName());

            addOrModifyTaskJob(response.getContext().getCouponActivity(), null);
        }

        return response;
    }

    /**
     * 修改活动
     *
     * @param couponActivityModifyRequest
     * @return
     */
    @ApiOperation(value = "修改优惠券活动")
    @MultiSubmit
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public BaseResponse modify(@Valid @RequestBody CouponActivityModifyRequest couponActivityModifyRequest) {
        couponActivityModifyRequest.setPlatformFlag(DefaultFlag.YES);
        couponActivityModifyRequest.setUpdatePerson(commonUtil.getOperatorId());
        couponActivityModifyRequest.setStoreId(Constant.BOSS_DEFAULT_STORE_ID);
        CouponActivityVO couponActivity = couponActivityProvider.modify(couponActivityModifyRequest).getContext().getCouponActivity();
        EsCouponActivityDTO esCouponActivityDTO = KsBeanUtil.convert(couponActivity, EsCouponActivityDTO.class);
        List<String> joinLevels = Splitter.on(",").trimResults().splitToList(couponActivity.getJoinLevel());
        esCouponActivityDTO.setJoinLevels(joinLevels);
        esCouponActivityProvider.add(new EsCouponActivityAddRequest(esCouponActivityDTO));
        //记录操作日志

        operateLogMQUtil.convertAndSend("营销", "编辑优惠券活动", "优惠券活动：" + couponActivityModifyRequest.getActivityName());
        addOrModifyTaskJob(null, couponActivityModifyRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 指定赠券定时任务
     *
     * @param couponActivityVO
     */
    private void addOrModifyTaskJob(CouponActivityVO couponActivityVO, CouponActivityModifyRequest
            couponActivityModifyRequest) {
        //修改优惠券活动
        if (Objects.nonNull(couponActivityModifyRequest)) {
            couponActivityVO = new CouponActivityVO();
            couponActivityVO.setActivityId(couponActivityModifyRequest.getActivityId());
            couponActivityVO.setStartTime(couponActivityModifyRequest.getStartTime());
            couponActivityVO.setCouponActivityType(couponActivityModifyRequest.getCouponActivityType());

        }
        if (Objects.nonNull(couponActivityVO) && Objects.equals(CouponActivityType.SPECIFY_COUPON, couponActivityVO
                .getCouponActivityType())) {
            //指定赠券定时任务
            taskJobService.addOrModifyTaskJob(couponActivityVO.getActivityId(), couponActivityVO.getStartTime());
        }

    }


    /**
     * 查询活动（注册赠券活动、进店赠券活动）不可用的时间范围
     *
     * @return
     */
    @ApiOperation(value = "查询活动（注册赠券活动、进店赠券活动）不可用的时间范围")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "couponActivityType", value =
                    "优惠券活动类型，0全场赠券，1指定赠券，2进店赠券，3注册赠券， 4权益赠券",
                    required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "activityId", value = "优惠券活动Id",
                    required = true)
    })
    @RequestMapping(value = "/activity-disable-time/{couponActivityType}/{activityId}", method = RequestMethod.GET)
    public BaseResponse<List<CouponActivityDisabledTimeVO>> queryActivityEnableTime(@PathVariable @NotNull int
                                                                                                couponActivityType,
                                                                                    @PathVariable String activityId) {
        CouponActivityDisabledTimeRequest request = new CouponActivityDisabledTimeRequest();
        request.setCouponActivityType(CouponActivityType.fromValue(couponActivityType));
        if (!"-1".equals(activityId)) {
            request.setActivityId(activityId);
        }
        request.setStoreId(Constant.BOSS_DEFAULT_STORE_ID);
        CouponActivityDisabledTimeResponse response = couponActivityQueryProvider.queryActivityEnableTime(request)
                .getContext();
        return BaseResponse.success(response.getCouponActivityDisabledTimeVOList());
    }
}
