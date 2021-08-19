package com.wanmi.sbc.crm.customerplan.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanPageRequest;
import com.wanmi.sbc.crm.bean.enums.PlanStatus;
import com.wanmi.sbc.crm.bean.enums.SendFlag;
import com.wanmi.sbc.crm.customerplan.mapper.CustomerPlanMapper;
import com.wanmi.sbc.crm.customerplan.mapper.CustomerPlanSendMapper;
import com.wanmi.sbc.crm.customerplan.mapper.CustomerPlanTriggerSendMapper;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlan;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlanSend;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlanTriggerSend;
import com.wanmi.sbc.crm.customerplanapppush.model.root.CustomerPlanAppPushRel;
import com.wanmi.sbc.crm.customerplanapppush.service.CustomerPlanAppPushRelService;
import com.wanmi.sbc.crm.customerplansms.model.root.CustomerPlanSmsRel;
import com.wanmi.sbc.crm.customerplansms.service.CustomerPlanSmsRelService;
import com.wanmi.sbc.crm.customgroup.mapper.CustomerBaseInfoMapper;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailBatchAddRequest;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.bean.enums.PointsServiceType;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeBatchSendCouponRequest;
import com.wanmi.sbc.marketing.bean.dto.CouponActivityConfigAndCouponInfoDTO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * \* author: zgl
 * \* date: 2020-1-9
 * \* time: 18:30
 * \* description:
 * \
 */
@Service
@EnableBinding
@Slf4j
public class CustomerPlanTaskService {
    @Autowired
    private CustomerPointsDetailSaveProvider customerPointsDetailSaveProvider;
    @Autowired
    private CouponCodeProvider couponCodeProvider;
    @Autowired
    private CustomerPlanService customerPlanService;
    @Autowired
    private CustomerPlanMapper customerPlanMapper;

    @Autowired
    private CustomerPlanSmsRelService customerPlanSmsRelService;

    @Autowired
    private CustomerPlanAppPushRelService customerPlanAppPushRelService;
    @Autowired
    private CustomerPlanTriggerSendMapper customerPlanTriggerSendMapper;
    @Autowired
    private CustomerPlanSendMapper customerPlanSendMapper;
    @Autowired
    private CustomerBaseInfoMapper customerBaseInfoMapper;
    @Autowired
    private BinderAwareChannelResolver resolver;


    private final int PAGE_SIZE = 100;

    public void generator(LocalDate localDate){
        if(localDate==null){
            localDate = LocalDate.now().minusDays(1);
        }
        CustomerPlanPageRequest pageRequest = CustomerPlanPageRequest
                .builder()
                .delFlag(DeleteFlag.NO.toValue())
                .planStatus(PlanStatus.BEGIN.toValue())
                .giftPackageFull(false)
                .build();

        pageRequest.setPageSize(PAGE_SIZE);
        int pageNum=0;
        boolean flag = true;
        while(flag){
            pageRequest.setPageNum(pageNum);
            PageInfo<CustomerPlan> pageInfo = this.customerPlanService.taskPageInfo(pageRequest);

            if(pageInfo.getTotal()>0&&pageInfo.getEndRow()>0){
                for (CustomerPlan customerPlan : pageInfo.getList()) {
                    customerProcess(customerPlan,localDate);
                    sendProcess(customerPlan);
                }
            }
            if(pageInfo.isHasNextPage()){
                pageNum++;
            }else{
                flag = false;
                break;
            }
        }
    }

    private void customerProcess(CustomerPlan customerPlan,LocalDate localDate){
        String[] group = customerPlan.getReceiveValue().split("_");
        CustomerPlanSend send = CustomerPlanSend.builder()
                .planId(customerPlan.getId())
                .giftPackageTotal(customerPlan.getGiftPackageTotal()-customerPlan.getGiftPackageCount())
                .pointFlag(customerPlan.getPointFlag() ? 1 : 0)
                .couponFlag(customerPlan.getCouponFlag() ? 1 : 0)
                .smsFlag(customerPlan.getSmsFlag() ? 1 : 0)
                .appPushFlag(customerPlan.getAppPushFlag() ? 1 : 0)
                .groupType(Integer.parseInt(group[0]))
                .groupId(Long.parseLong(group[1]))
                .statDate(localDate.format(DateTimeFormatter.ofPattern(DateUtil.FMT_DATE_1)))
//                .statDate(DateUtil.yesterdayDate())
                .build();
        int sendCount = 0;
        if(customerPlan.getTriggerFlag()){
            CustomerPlanTriggerSend triggerSend = KsBeanUtil.convert(send,CustomerPlanTriggerSend.class);
            if (customerPlan.getCustomerLimitFlag()){

                triggerSend.setCustomerLimit(customerPlan.getCustomerLimit());

            }
            List<String> triggers = Arrays.asList(customerPlan.getTriggerCondition().split(","));
            triggerSend.setTriggerList(triggers.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList()));
            sendCount = this.customerPlanTriggerSendMapper.insertSelect(triggerSend);

        }else {
            sendCount = this.customerPlanSendMapper.insertSelect(send);
        }
        customerPlan.setGiftPackageCount(customerPlan.getGiftPackageCount()+sendCount);
        this.customerPlanMapper.updateByPrimaryKey(customerPlan);
    }

    private void sendProcess(CustomerPlan customerPlan){
        int pageNum = 1;
        PageInfo<CustomerPlanSend> pageInfo;
        CustomerPlanSend send = CustomerPlanSend.builder().planId(customerPlan.getId()).build();
        boolean flag= true;
        while(flag) {
            if (!customerPlan.getTriggerFlag()){
                pageInfo = PageHelper.startPage(pageNum, PAGE_SIZE).doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        customerPlanSendMapper.selectByType(send);
                    }
                });
            }else{
                pageInfo = PageHelper.startPage(pageNum, PAGE_SIZE).doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        customerPlanTriggerSendMapper.selectByType(send);
                    }
                });
            }
            if(pageInfo.getTotal()>0&&pageInfo.getEndRow()>0){
                List<CustomerPlanSend> pointList = new ArrayList<>(),
                                   couponList = new ArrayList<>(),
                                   smsList = new ArrayList<>(),
                                   appPushProcessList = new ArrayList<>();
                for(CustomerPlanSend planSend: pageInfo.getList()){
                    if(planSend.getPointFlag().equals(SendFlag.NEED_SEND.toValue())){
                        pointList.add(planSend);
                    }
                    if(planSend.getCouponFlag().equals(SendFlag.NEED_SEND.toValue())){
                        couponList.add(planSend);
                    }
                    if(planSend.getSmsFlag().equals(SendFlag.NEED_SEND.toValue())){
                        smsList.add(planSend);
                    }
                    if(planSend.getAppPushFlag().equals(SendFlag.NEED_SEND.toValue())){
                        appPushProcessList.add(planSend);
                    }
                }
                if(CollectionUtils.isNotEmpty(pointList)){
                    pointProcess(pointList,customerPlan.getTriggerFlag(),(long) customerPlan.getPoints());
                }
                if(CollectionUtils.isNotEmpty(couponList)){
                    couponProcess(couponList,customerPlan);
                }
                if(CollectionUtils.isNotEmpty(smsList)){
                    smsProcess(smsList,customerPlan);
                }
                if(CollectionUtils.isNotEmpty(appPushProcessList)){
                    appPushProcess(appPushProcessList,customerPlan);
                }
            }
            if(pageInfo.isHasNextPage()){
                pageNum++;
            }else{
                flag=false;
                break;
            }

        }
    }

    private void pointProcess(List<CustomerPlanSend> customerPlanSends,Boolean triggerFlag,Long points){
        List<String> customerList = customerPlanSends.stream().map(CustomerPlanSend::getCustomerId).collect(Collectors.toList());
        BaseResponse baseResponse = this.customerPointsDetailSaveProvider.batchAdd(CustomerPointsDetailBatchAddRequest
                .builder()
                .customerIdList(customerList)
                .points(points)
                .serviceType(PointsServiceType.RIGHTS)
                .type(OperateType.GROWTH)
                .build()
        );
        if(baseResponse.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            List<Long> idList = customerPlanSends.stream().map(CustomerPlanSend::getId).collect(Collectors.toList());
            CustomerPlanSend customerPlanSend  = CustomerPlanSend.builder().idList(idList).pointFlag(SendFlag.SEND_END.toValue()).build();
            if(triggerFlag){
                CustomerPlanTriggerSend customerPlanTriggerSend = KsBeanUtil.convert(customerPlanSend,CustomerPlanTriggerSend.class);
                customerPlanTriggerSendMapper.updateByPrimaryKeySelective(customerPlanTriggerSend);
            }else{
                customerPlanSendMapper.updateByPrimaryKeySelective(customerPlanSend);
            }
        }
    }

    private void couponProcess(List<CustomerPlanSend> customerPlanSends,CustomerPlan customerPlan){
        List<String> customerList = customerPlanSends.stream().map(CustomerPlanSend::getCustomerId).collect(Collectors.toList());
        CouponActivityConfigAndCouponInfoDTO dto = new CouponActivityConfigAndCouponInfoDTO();
        dto.setActivityId(customerPlan.getActivityId());
        BaseResponse baseResponse = this.couponCodeProvider.sendBatchCouponCodeByCustomerList(CouponCodeBatchSendCouponRequest.builder()
                .customerIds(customerList)
                .list(Arrays.asList(dto))
                .build());
        if(baseResponse.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            List<Long> idList = customerPlanSends.stream().map(CustomerPlanSend::getId).collect(Collectors.toList());
            CustomerPlanSend customerPlanSend  = CustomerPlanSend.builder().idList(idList).couponFlag(SendFlag.SEND_END.toValue()).build();
            if(customerPlan.getTriggerFlag()){
                CustomerPlanTriggerSend customerPlanTriggerSend = KsBeanUtil.convert(customerPlanSend,CustomerPlanTriggerSend.class);
                customerPlanTriggerSendMapper.updateByPrimaryKeySelective(customerPlanTriggerSend);
            }else{
                customerPlanSendMapper.updateByPrimaryKeySelective(customerPlanSend);
            }
        }
    }

    private void smsProcess(List<CustomerPlanSend> customerPlanSends,CustomerPlan customerPlan){
        List<String> customerList = customerPlanSends.stream().map(CustomerPlanSend::getCustomerId).collect(Collectors.toList());
        CustomerPlanSmsRel customerPlanSmsRel = this.customerPlanSmsRelService.findByPlanId(customerPlan.getId());
        if(!Objects.isNull(customerPlanSmsRel)){
            List<String> customerAccounts = this.customerBaseInfoMapper.selectPhoneByCustomerId(customerList);
            if(CollectionUtils.isNotEmpty(customerAccounts)) {
                Map<String, Object> map = new HashMap<>();
                map.put("phoneNumbers", String.join(",",customerAccounts));
                map.put("signId", customerPlanSmsRel.getSignId());
                map.put("templateCode", customerPlanSmsRel.getTemplateCode());
                map.put("signName", customerPlanSmsRel.getSignName());
                resolver.resolveDestination(MQConstant.Q_SMS_SEND_MESSAGE_ADD).send(new GenericMessage<>(JSONObject.toJSONString(map)));
                List<Long> idList = customerPlanSends.stream().map(CustomerPlanSend::getId).collect(Collectors.toList());
                CustomerPlanSend customerPlanSend = CustomerPlanSend.builder().idList(idList).smsFlag(SendFlag.SEND_END.toValue()).build();
                if (customerPlan.getTriggerFlag()) {
                    CustomerPlanTriggerSend customerPlanTriggerSend = KsBeanUtil.convert(customerPlanSend, CustomerPlanTriggerSend.class);
                    customerPlanTriggerSendMapper.updateByPrimaryKeySelective(customerPlanTriggerSend);
                } else {
                    customerPlanSendMapper.updateByPrimaryKeySelective(customerPlanSend);
                }
            }
        }
    }

    private void appPushProcess(List<CustomerPlanSend> customerPlanSends,CustomerPlan customerPlan){
        List<String> customerList = customerPlanSends.stream().map(CustomerPlanSend::getCustomerId).collect(Collectors.toList());
        CustomerPlanAppPushRel appPushRel = this.customerPlanAppPushRelService.findByPlanId(customerPlan.getId());
        if(!Objects.isNull(appPushRel)){
            Map<String,Object> map = new HashMap<>();
            map.put("planId",appPushRel.getPlanId());
            map.put("msgName",appPushRel.getName());
            map.put("msgTitle",appPushRel.getNoticeTitle());
            map.put("msgContext",appPushRel.getNoticeContext());
            map.put("msgRecipient",4);
            map.put("msgRecipientDetail",String.join(",",customerList));
            map.put("pushFlag",1);
            if(StringUtils.isNotEmpty(appPushRel.getCoverUrl())) {
                map.put("msgImg", appPushRel.getCoverUrl());
            }

            resolver.resolveDestination(MQConstant.Q_SMS_SERVICE_PUSH_ADD).send(new GenericMessage<>(JSONObject.toJSONString(map)));
            List<Long> idList = customerPlanSends.stream().map(CustomerPlanSend::getId).collect(Collectors.toList());
            CustomerPlanSend customerPlanSend  = CustomerPlanSend.builder().idList(idList).appPushFlag(SendFlag.SEND_END.toValue()).build();
            if(customerPlan.getTriggerFlag()){
                CustomerPlanTriggerSend customerPlanTriggerSend = KsBeanUtil.convert(customerPlanSend,CustomerPlanTriggerSend.class);
                customerPlanTriggerSendMapper.updateByPrimaryKeySelective(customerPlanTriggerSend);
            }else{
                customerPlanSendMapper.updateByPrimaryKeySelective(customerPlanSend);
            }
        }
    }
    private int upDiv(long x,long y){
       /* int r = x/y;
        if ((x ^ y) >= 0 && (r * y != x)) {
            r++;
        }
        return r;*/

        double r = Math.ceil(x/y);
        return (int)r;
    }
}
