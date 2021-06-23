package com.wanmi.sbc.crm.customerplan;

import com.google.common.base.Splitter;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.crmgroup.CrmGroupProvider;
import com.wanmi.sbc.crm.api.provider.customerplan.CustomerPlanQueryProvider;
import com.wanmi.sbc.crm.api.provider.customerplan.CustomerPlanSaveProvider;
import com.wanmi.sbc.crm.api.request.crmgroup.CrmGroupRequest;
import com.wanmi.sbc.crm.api.request.customerplan.*;
import com.wanmi.sbc.crm.api.response.customerplan.CustomerPlanByActivityIdResponse;
import com.wanmi.sbc.crm.api.response.customerplan.CustomerPlanByIdResponse;
import com.wanmi.sbc.crm.api.response.customerplan.CustomerPlanPageResponse;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanCouponVO;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanVO;
import com.wanmi.sbc.crm.customerplan.response.CustomerPlanDetailByIdResponse;
import com.wanmi.sbc.elastic.api.provider.coupon.EsCouponActivityProvider;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityAddRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityDeleteByIdRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityPauseByIdRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityStartByIdRequest;
import com.wanmi.sbc.elastic.bean.dto.coupon.EsCouponActivityDTO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponActivityProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponInfoQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.*;
import com.wanmi.sbc.marketing.bean.constant.Constant;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.enums.TerminalType;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityVO;
import com.wanmi.sbc.message.api.constant.SmsErrorCode;
import com.wanmi.sbc.message.api.provider.smssign.SmsSignQueryProvider;
import com.wanmi.sbc.message.api.provider.smstemplate.SmsTemplateQueryProvider;
import com.wanmi.sbc.message.api.request.smssign.SmsSignByIdRequest;
import com.wanmi.sbc.message.api.request.smstemplate.SmsTemplateListRequest;
import com.wanmi.sbc.message.bean.constant.ReceiveGroupType;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.bean.vo.SmsSignVO;
import com.wanmi.sbc.message.bean.vo.SmsTemplateVO;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Api(description = " 人群运营计划管理API", tags = "CustomerPlanController")
@RestController
@RequestMapping(value = "/customerplan")
public class CustomerPlanController {

    @Autowired
    private CustomerPlanQueryProvider customerPlanQueryProvider;

    @Autowired
    private CustomerPlanSaveProvider customerPlanSaveProvider;

    @Autowired
    private CouponInfoQueryProvider couponInfoQueryProvider;

    @Autowired
    private CouponActivityProvider couponActivityProvider;

    @Autowired
    private SmsSignQueryProvider smsSignQueryProvider;

    @Autowired
    private SmsTemplateQueryProvider smsTemplateQueryProvider;

    @Autowired
    private CrmGroupProvider crmGroupProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EsCouponActivityProvider esCouponActivityProvider;

    @ApiOperation(value = "分页查询 人群运营计划")
    @PostMapping("/page")
    public BaseResponse<CustomerPlanPageResponse> getPage(@RequestBody @Valid CustomerPlanPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO.toValue());
        pageReq.putSort("id", "desc");
        return customerPlanQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "根据id查询 人群运营计划")
    @GetMapping("/{id}")
    public BaseResponse<CustomerPlanDetailByIdResponse> getById(@PathVariable Long id) {
        CustomerPlanDetailByIdResponse response = new CustomerPlanDetailByIdResponse();
        CustomerPlanByIdRequest idReq = new CustomerPlanByIdRequest();
        idReq.setId(id);
        CustomerPlanByIdResponse idResponse = customerPlanQueryProvider.getById(idReq).getContext();
        response.setCustomerPlanVO(idResponse.getCustomerPlanVO());
        response.setCustomerPlanCouponList(idResponse.getCustomerPlanCouponList());
        response.setPlanSms(idResponse.getPlanSms());
        response.setPlanAppPush(idResponse.getPlanAppPush());
        //获取优惠券
        if(CollectionUtils.isNotEmpty(idResponse.getCustomerPlanCouponList())){
            List<String> couponIds = idResponse.getCustomerPlanCouponList().stream()
                    .map(CustomerPlanCouponVO::getCouponId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(couponIds)){
                response.setCouponInfoList(couponInfoQueryProvider.queryCouponInfos(CouponInfoQueryRequest.builder().couponIds(couponIds).build()).getContext().getCouponCodeList());
            }
        }
        return BaseResponse.success(response);
    }

    @ApiOperation(value = "根据活动id查询 人群运营计划")
    @GetMapping("/activity/{activityId}")
    public BaseResponse<CustomerPlanDetailByIdResponse> getByActivityId(@PathVariable String activityId) {
        CustomerPlanDetailByIdResponse response = new CustomerPlanDetailByIdResponse();
        CustomerPlanByActivityIdRequest idReq = new CustomerPlanByActivityIdRequest();
        idReq.setActivityId(activityId);
        CustomerPlanByActivityIdResponse idResponse = customerPlanQueryProvider.getByActivityId(idReq).getContext();
        response.setCustomerPlanVO(idResponse.getCustomerPlanVO());
        response.setCustomerPlanCouponList(idResponse.getCustomerPlanCouponList());
        response.setPlanSms(idResponse.getPlanSms());
        response.setPlanAppPush(idResponse.getPlanAppPush());
        //获取优惠券
        if(CollectionUtils.isNotEmpty(idResponse.getCustomerPlanCouponList())){
            List<String> couponIds = idResponse.getCustomerPlanCouponList().stream()
                    .map(CustomerPlanCouponVO::getCouponId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(couponIds)){
                response.setCouponInfoList(couponInfoQueryProvider.queryCouponInfos(CouponInfoQueryRequest.builder().couponIds(couponIds).build()).getContext().getCouponCodeList());
            }
        }
        return BaseResponse.success(response);
    }

    @MultiSubmit
    @GlobalTransactional
    @ApiOperation(value = "新增 人群运营计划")
    @PostMapping("/add")
    public BaseResponse add(@RequestBody @Valid CustomerPlanAddRequest addReq) {
        //验证名称
        this.checkName(addReq.getPlanName(), null);
        this.checkGroupObject(addReq.getReceiveValue());
        addReq.setCreatePerson(commonUtil.getOperatorId());
        addReq.setActivityId(null);
        if(addReq.getCouponFlag()) {
            CouponActivityVO couponActivity = couponActivityProvider.add(this.buildByPlanAdd(addReq)).getContext().getCouponActivity();
            addReq.setActivityId(couponActivity.getActivityId());
            EsCouponActivityDTO esCouponActivityDTO = KsBeanUtil.convert(couponActivity, EsCouponActivityDTO.class);
            List<String> joinLevels = Splitter.on(",").trimResults().splitToList(couponActivity.getJoinLevel());
            esCouponActivityDTO.setJoinLevels(joinLevels);
            esCouponActivityProvider.add(new EsCouponActivityAddRequest(esCouponActivityDTO));

        }
        //填充签名名称
        if(Objects.nonNull(addReq.getPlanSms()) && Objects.nonNull(addReq.getPlanSms().getSignId())){
            addReq.getPlanSms().setSignName(this.getSignNameWithCheck(addReq.getPlanSms().getSignId()));
            //验证模板
            this.checkTemplate(addReq.getPlanSms().getTemplateCode());
        }
        return customerPlanSaveProvider.add(addReq);
    }

    @MultiSubmit
    @GlobalTransactional
    @ApiOperation(value = "修改 人群运营计划")
    @PutMapping("/modify")
    public BaseResponse modify(@RequestBody @Valid CustomerPlanModifyRequest modifyReq) {
        //验证名称
        this.checkName(modifyReq.getPlanName(), modifyReq.getId());
        this.checkGroupObject(modifyReq.getReceiveValue());
        CustomerPlanByIdResponse idResponse = customerPlanQueryProvider.getById(
                CustomerPlanByIdRequest.builder().id(modifyReq.getId()).build()).getContext();
        if(Objects.isNull(idResponse) || Objects.isNull(idResponse.getCustomerPlanVO())){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        modifyReq.setActivityId(idResponse.getCustomerPlanVO().getActivityId());
        //填充签名名称
        if(Objects.nonNull(modifyReq.getPlanSms()) && Objects.nonNull(modifyReq.getPlanSms().getSignId())){
            modifyReq.getPlanSms().setSignName(this.getSignNameWithCheck(modifyReq.getPlanSms().getSignId()));
            //验证模板
            this.checkTemplate(modifyReq.getPlanSms().getTemplateCode());
        }
        //原信息-已选择优惠券活动
        if(StringUtils.isNotBlank(modifyReq.getActivityId())){
            //新信息-有选择优惠券活动->调用修改优惠券接口
            if(modifyReq.getCouponFlag()){
                CouponActivityModifyRequest request = this.buildByPlanModify(modifyReq);
                request.setActivityId(modifyReq.getActivityId());
                CouponActivityVO couponActivity = couponActivityProvider.modify(request).getContext().getCouponActivity();
                EsCouponActivityDTO esCouponActivityDTO = KsBeanUtil.convert(couponActivity, EsCouponActivityDTO.class);
                List<String> joinLevels = Splitter.on(",").trimResults().splitToList(couponActivity.getJoinLevel());
                esCouponActivityDTO.setJoinLevels(joinLevels);
                esCouponActivityProvider.add(new EsCouponActivityAddRequest(esCouponActivityDTO));
            }else { //新信息-未选择优惠券活动->调用删除优惠券接口
                deleteActivity(modifyReq.getActivityId());
                //置空信息
                modifyReq.setActivityId(null);
            }
        }else if(modifyReq.getCouponFlag()){ //新信息-有选择优惠券
            CustomerPlanAddRequest addReq = KsBeanUtil.convert(modifyReq, CustomerPlanAddRequest.class);
            addReq.setCreatePerson(modifyReq.getUpdatePerson());
            CouponActivityVO couponActivity = couponActivityProvider.add(this.buildByPlanAdd(addReq)).getContext().getCouponActivity();
            modifyReq.setActivityId(couponActivity.getActivityId());
            EsCouponActivityDTO esCouponActivityDTO = KsBeanUtil.convert(couponActivity, EsCouponActivityDTO.class);
            List<String> joinLevels = Splitter.on(",").trimResults().splitToList(couponActivity.getJoinLevel());
            esCouponActivityDTO.setJoinLevels(joinLevels);
            esCouponActivityProvider.add(new EsCouponActivityAddRequest(esCouponActivityDTO));
        }
        return customerPlanSaveProvider.modify(modifyReq);
    }

    @MultiSubmit
    @GlobalTransactional
    @ApiOperation(value = "根据id删除 人群运营计划")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        CustomerPlanByIdResponse idResponse = customerPlanQueryProvider.getById(
                CustomerPlanByIdRequest.builder().id(id).build()).getContext();
        if(Objects.isNull(idResponse) || Objects.isNull(idResponse.getCustomerPlanVO())){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //如果有优惠券活动-删除
        if(StringUtils.isNotBlank(idResponse.getCustomerPlanVO().getActivityId())) {
            deleteActivity(idResponse.getCustomerPlanVO().getActivityId());
        }
        CustomerPlanDelByIdRequest delByIdReq = new CustomerPlanDelByIdRequest();
        delByIdReq.setId(id);
        return customerPlanSaveProvider.deleteById(delByIdReq);
    }

    @MultiSubmit
    @GlobalTransactional
    @ApiOperation(value = "暂停人群运营计划")
    @PutMapping("/pause/{id}")
    public BaseResponse pause(@PathVariable Long id) {
        CustomerPlanByIdResponse idResponse = customerPlanQueryProvider.getById(
                CustomerPlanByIdRequest.builder().id(id).build()).getContext();
        if(Objects.isNull(idResponse) || Objects.isNull(idResponse.getCustomerPlanVO())){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //如果有优惠券活动-暂停
        if(StringUtils.isNotBlank(idResponse.getCustomerPlanVO().getActivityId())) {
            CouponActivityPauseByIdRequest idRequest = new CouponActivityPauseByIdRequest();
            idRequest.setId(idResponse.getCustomerPlanVO().getActivityId());
            couponActivityProvider.pause(idRequest);
            esCouponActivityProvider.pause(new EsCouponActivityPauseByIdRequest(idRequest.getId()));
        }
        return customerPlanSaveProvider.modifyPauseFlag(
                CustomerPlanModifyPauseFlagRequest.builder().pauseFlag(Boolean.TRUE).id(id)
                        .updatePerson(commonUtil.getOperatorId()).build());
    }

    @MultiSubmit
    @GlobalTransactional
    @ApiOperation(value = "开启人群运营计划")
    @PutMapping("/start/{id}")
    public BaseResponse start(@PathVariable Long id) {
        CustomerPlanByIdResponse idResponse = customerPlanQueryProvider.getById(
                CustomerPlanByIdRequest.builder().id(id).build()).getContext();
        if(Objects.isNull(idResponse) || Objects.isNull(idResponse.getCustomerPlanVO())){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //如果有优惠券活动-暂停
        if(StringUtils.isNotBlank(idResponse.getCustomerPlanVO().getActivityId())) {
            CouponActivityStartByIdRequest idRequest = new CouponActivityStartByIdRequest();
            idRequest.setId(idResponse.getCustomerPlanVO().getActivityId());
            couponActivityProvider.start(idRequest);
            esCouponActivityProvider.start(new EsCouponActivityStartByIdRequest(idRequest.getId()));
        }
        return customerPlanSaveProvider.modifyPauseFlag(
                CustomerPlanModifyPauseFlagRequest.builder().pauseFlag(Boolean.FALSE).id(id)
                        .updatePerson(commonUtil.getOperatorId()).build());
    }

    /**
     * 删除优惠券活动
     * @param id
     */
    private void deleteActivity(String id){
        CouponActivityDeleteByIdAndOperatorIdRequest idRequest = new CouponActivityDeleteByIdAndOperatorIdRequest();
        idRequest.setId(id);
        idRequest.setOperatorId(commonUtil.getOperatorId());
        couponActivityProvider.deleteByIdAndOperatorId(idRequest);
        esCouponActivityProvider.deleteById(new EsCouponActivityDeleteByIdRequest(id));
    }

    /**
     * 封装优惠券活动新增请求参数
     * @param addRequest
     * @return
     */
    private CouponActivityAddRequest buildByPlanAdd(CustomerPlanAddRequest addRequest){
        return CouponActivityAddRequest.builder()
                .activityName(addRequest.getPlanName())
                .startTime(addRequest.getStartDate().atTime(0, 0, 0))
                .endTime(addRequest.getEndDate().atTime(23,59,59))
                .couponActivityType(CouponActivityType.RIGHTS_COUPON)
                .pauseFlag(DefaultFlag.NO)
                .receiveType(addRequest.getCustomerLimitFlag()?DefaultFlag.YES:DefaultFlag.NO)
                .receiveCount(addRequest.getCustomerLimitFlag()?addRequest.getCustomerLimit():null)
                .terminals(String.valueOf(TerminalType.ALL.toValue()))
                .storeId(Constant.BOSS_DEFAULT_STORE_ID)
                .platformFlag(DefaultFlag.YES)
                .joinLevel(String.valueOf(MarketingJoinLevel.SPECIFY_GROUP.toValue()))
                .joinLevelType(DefaultFlag.YES)
                .createPerson(addRequest.getCreatePerson())
                .couponActivityConfigs(addRequest.getPlanCouponList().stream()
                        .map(c -> {
                            CouponActivityConfigSaveRequest coupon = new CouponActivityConfigSaveRequest();
                            coupon.setCouponId(c.getCouponId());
                            coupon.setTotalCount(c.getGiftCount().longValue());
                            return coupon;
                        }).collect(Collectors.toList()))
                .leftGroupNum(addRequest.getGiftPackageTotal())
                .build();
    }

    /**
     * 封装优惠券活动编辑请求参数
     * @param modifyRequest
     * @return
     */
    private CouponActivityModifyRequest buildByPlanModify(CustomerPlanModifyRequest modifyRequest){
        CouponActivityModifyRequest activityReq = new CouponActivityModifyRequest();
        activityReq.setActivityName(modifyRequest.getPlanName());
        activityReq.setStartTime(modifyRequest.getStartDate().atTime(0, 0, 0));
        activityReq.setEndTime(modifyRequest.getEndDate().atTime(23,59,59));
        activityReq.setCouponActivityType(CouponActivityType.RIGHTS_COUPON);
        activityReq.setPauseFlag(DefaultFlag.NO);
        activityReq.setReceiveType(modifyRequest.getCustomerLimitFlag()?DefaultFlag.YES:DefaultFlag.NO);
        activityReq.setReceiveCount(modifyRequest.getCustomerLimitFlag()?modifyRequest.getCustomerLimit():null);
        activityReq.setTerminals(String.valueOf(TerminalType.ALL.toValue()));
        activityReq.setStoreId(Constant.BOSS_DEFAULT_STORE_ID);
        activityReq.setPlatformFlag(DefaultFlag.YES);
        activityReq.setJoinLevel(String.valueOf(MarketingJoinLevel.SPECIFY_GROUP.toValue()));
        activityReq.setUpdatePerson(modifyRequest.getUpdatePerson());
        activityReq.setLeftGroupNum(modifyRequest.getGiftPackageTotal());
        activityReq.setCouponActivityConfigs(modifyRequest.getPlanCouponList().stream()
                        .map(c -> {
                            CouponActivityConfigSaveRequest coupon = new CouponActivityConfigSaveRequest();
                            coupon.setCouponId(c.getCouponId());
                            coupon.setTotalCount(c.getGiftCount().longValue());
                            return coupon;
                        }).collect(Collectors.toList()));
        return activityReq;
    }

    /**
     * @return boolean
     * @Author lvzhenwei
     * @Description 校验计划名称是否存在
     * @Date 16:40 2019/12/9
     * @Param [signListRequest]
     **/
    private void checkName(String planName, Long id) {
        //判断计划名称是否已经存在
        List<CustomerPlanVO> customerPlanList = customerPlanQueryProvider
                .list(CustomerPlanListRequest.builder().planName(planName).delFlag(DeleteFlag.NO).build())
                .getContext().getCustomerPlanList();
        if (CollectionUtils.isNotEmpty(customerPlanList)) {
            if (Objects.nonNull(id)
                    && customerPlanList.size() == 1
                    && !customerPlanList.get(0).getId().equals(id)) {
                //抛出异常
                throw new SbcRuntimeException("K-200401");
            }else if(Objects.isNull(id)) {
                //抛出异常
                throw new SbcRuntimeException("K-200401");
            }
        }
    }

    private void checkGroupObject(String joinId){
        String[] joinIds = joinId.split(",");
        //指定人群
        List<Long> sysGroupList = new ArrayList<>();
        List<Long> customGroupList = new ArrayList<>();
        for(String str : joinIds){
            String[] arr = str.split("_");
            if(arr[0].equals(ReceiveGroupType.CUSTOM)){
                customGroupList.add(Long.valueOf(arr[1]));
            }
            if(arr[0].equals(ReceiveGroupType.SYS)){
                sysGroupList.add(Long.valueOf(arr[1]));
            }
        }

        Long count = crmGroupProvider.countByGroupIds(
                CrmGroupRequest.builder().sysGroupList(sysGroupList).customGroupList(customGroupList).build()).getContext();
        if(count.intValue() != joinIds.length){
            throw new SbcRuntimeException("K-200402");
        }
    }

    /**
     * 返回签名名称同时验证
     * @param signId
     * @return
     */
    private String getSignNameWithCheck(Long signId){
        SmsSignVO sign = smsSignQueryProvider.getById(
                SmsSignByIdRequest.builder().id(signId).build()).getContext().getSmsSignVO();
        if(sign == null || sign.getReviewStatus()!=ReviewStatus.REVIEWPASS|| sign.getDelFlag().equals(DeleteFlag.YES)){
            throw new SbcRuntimeException(SmsErrorCode.NO_SMS_SIGN);
        }
        return sign.getSmsSignName();
    }

    private void checkTemplate(String templateCode){
        List<SmsTemplateVO> voList = smsTemplateQueryProvider.list(SmsTemplateListRequest.builder().templateCode
                (templateCode).reviewStatus(ReviewStatus.REVIEWPASS).delFlag(DeleteFlag.NO).build()).getContext().getSmsTemplateVOList();
        if(CollectionUtils.isEmpty(voList)){
            throw new SbcRuntimeException(SmsErrorCode.NO_SMS_TEMPLATE);
        }
    }
}
