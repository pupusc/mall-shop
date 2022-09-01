package com.wanmi.sbc.quartz.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.ResultCode;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.crm.api.provider.crmgroup.CrmGroupProvider;
import com.wanmi.sbc.crm.api.request.crmgroup.CrmGroupRequest;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailPageRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerListCustomerIdByPageableRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelQueryRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailForPageVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.quartz.enums.TaskBizType;
import com.wanmi.sbc.quartz.enums.TaskStatus;
import com.wanmi.sbc.quartz.model.entity.TaskInfo;
import com.wanmi.sbc.quartz.service.QuartzManagerService;
import com.wanmi.sbc.quartz.service.TaskJobService;
import com.wanmi.sbc.message.api.provider.appmessage.AppMessageProvider;
import com.wanmi.sbc.message.api.provider.messagesend.MessageSendQueryProvider;
import com.wanmi.sbc.message.api.request.appmessage.AppMessageAddRequest;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendByIdRequest;
import com.wanmi.sbc.message.api.response.appmessage.AppMessageAddResponse;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendByIdResponse;
import com.wanmi.sbc.message.bean.constant.ReceiveGroupType;
import com.wanmi.sbc.message.bean.enums.MessageSendType;
import com.wanmi.sbc.message.bean.enums.MessageType;
import com.wanmi.sbc.message.bean.vo.AppMessageVO;
import com.wanmi.sbc.message.bean.vo.MessageSendCustomerScopeVO;
import com.wanmi.sbc.message.bean.vo.MessageSendVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
@Component
public class MessageSendJob implements Job {

    @Autowired
    private TaskJobService taskJobService;

    @Autowired
    private QuartzManagerService quartzManagerService;

    @Autowired
    private MessageSendQueryProvider messageSendQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private AppMessageProvider appMessageProvider;

    @Autowired
    private CrmGroupProvider crmGroupProvider;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String messageId = jobDataMap.getString("bizId");
        log.info("消息发送，消息ID：{},定时任务开始运行！",messageId);
        TaskInfo taskInfo = taskJobService.findByBizId(messageId);
        if (Objects.isNull(taskInfo) || taskInfo.getBizType() != TaskBizType.MESSAGE_SEND || taskInfo.getState() == TaskStatus.END){
            log.info("根据业务ID：{},查询此任务已不存在/不是消息发送任务/已结束=====>>移除此任务！",messageId);
            quartzManagerService.delete(messageId, TaskJobService.MESSAGE_SEND);
            return;
        }

        BaseResponse<MessageSendByIdResponse> baseResponse = messageSendQueryProvider.getById(new MessageSendByIdRequest(Long.valueOf(messageId)));
        MessageSendByIdResponse response = baseResponse.getContext();
        MessageSendVO messageSendVO = response.getMessageSendVO();
        List<MessageSendCustomerScopeVO> scopeVOList = messageSendVO.getScopeVOList();
        if(Objects.isNull(messageSendVO) || MessageType.Preferential != messageSendVO.getMessageType()
                || DeleteFlag.YES == messageSendVO.getDelFlag()){
            log.info("根据消息任务ID：{},未查询到消息信息/关联接收人信息，或者此消息类型不是优惠服务/此活动已删除========>>更新任务状态&&移除此任务",messageId);
            //更新任务为已结束
            taskInfo.setEndTime(LocalDateTime.now());
            taskInfo.setState(TaskStatus.END);
            taskJobService.addTaskJob(taskInfo);
            //删除任务
            quartzManagerService.delete(messageId, TaskJobService.MESSAGE_SEND);
            return;
        }

        //封装app消息
        AppMessageVO appMessageVO = new AppMessageVO();
        appMessageVO.setMessageType(MessageType.Preferential);
        appMessageVO.setImgUrl(messageSendVO.getImgUrl());
        appMessageVO.setTitle(messageSendVO.getTitle());
        appMessageVO.setContent(messageSendVO.getContent());
        appMessageVO.setSendTime(taskInfo.getStartTime());
        appMessageVO.setJoinId(messageSendVO.getMessageId());
        appMessageVO.setRouteParam(this.replaceRoute(messageSendVO.getRouteParams()).toJSONString());
        appMessageVO.setPcRouteParam(this.replaceRoute(messageSendVO.getPcRouteParams()).toJSONString());

        //处理消息发送
        handleMessageSend(messageSendVO, scopeVOList, appMessageVO, taskInfo);


    }

    /**
     * 处理消息发送
     * @param messageSendVO
     * @param scopeVOList
     * @param taskInfo
     */
    private void handleMessageSend(MessageSendVO messageSendVO, List<MessageSendCustomerScopeVO> scopeVOList, AppMessageVO appMessageVO, TaskInfo taskInfo){
        Long messageId = messageSendVO.getMessageId();

        log.info("根据消息任务ID：{},关联的接收人：{}，APP消息详情信息：{},消息发送开始！",messageId,messageSendVO.getMessageType(),appMessageVO);

        //接收人列表
        List<String> joinIds = scopeVOList.stream().map(scopeVO -> scopeVO.getJoinId()).collect(Collectors.toList());


        Integer pageNum = NumberUtils.INTEGER_ZERO;
        Integer pageSize = 1000;
        int maxTmpId = 0;
        LocalDateTime now = LocalDateTime.now();
        while(Boolean.TRUE){
            List<String> customerIds;
            //指定用户
            if(MessageSendType.CUSTOMER.equals(messageSendVO.getSendType())){
                customerIds = joinIds;
                BaseResponse<AppMessageAddResponse> baseResponse = appMessageProvider.addBatch(new AppMessageAddRequest(appMessageVO, customerIds));
                if(ResultCode.SUCCESSFUL.equals(baseResponse.getCode())){
                    log.info("根据消息任务ID：{},消息发送，指定用户，发送成功========>>更新任务状态&&移除此任务",messageId);
                    modifyTaskInfo(taskInfo,String.valueOf(messageId));
                }
                break;
            }
            //全部用户
            if(MessageSendType.ALL.equals(messageSendVO.getSendType())){

//                //获取365会员卡信息
//                Optional<FdPaidCast> fdPaidCast = fdPaidCastRepository.findByFdPayTypeAndDelFlag(userStatus, DeleteFlag.NO);
                PaidCardCustomerRelQueryRequest request = new PaidCardCustomerRelQueryRequest();
                request.setMaxTmpId(maxTmpId);
                request.setCurrentTime(now);
                request.setPaidCardIdList(Arrays.asList("47ca4b34aff245cb886effb167554804")); //此处暂时写死等后续确定好再处理 
                request.setPageSize(pageSize);
                List<PaidCardCustomerRelVO> context = paidCardCustomerRelQueryProvider.pageByMaxAutoId(request).getContext();
                if (CollectionUtils.isEmpty(context)) {
                    customerIds = null;
                } else {
                    List<String> customerIdsTmp = new ArrayList<>();
                    for (PaidCardCustomerRelVO paidCardCustomerRelVO : context) {
                        customerIdsTmp.add(paidCardCustomerRelVO.getCustomerId());
                        if (paidCardCustomerRelVO.getMaxTmpId() > maxTmpId) {
                            maxTmpId = paidCardCustomerRelVO.getMaxTmpId();
                        }
                    }
                    customerIds = customerIdsTmp;
                }
//
//                CustomerDetailPageRequest request = new CustomerDetailPageRequest();
//                request.setPageSize(pageSize);
//                request.setPageNum(pageNum);
//                List<CustomerDetailForPageVO> detailResponseList = customerQueryProvider.page(request).getContext().getDetailResponseList();
//                if(CollectionUtils.isNotEmpty(detailResponseList)){
//                    customerIds = detailResponseList.stream().map(CustomerDetailForPageVO::getCustomerId).collect(Collectors.toList());
//                }else{
//                    customerIds = null;
//                }

            }else if(MessageSendType.LEVEL.equals(messageSendVO.getSendType())){
                //指定等级
                List<Long> ids = joinIds.stream().map(id -> Long.valueOf(id)).collect(Collectors.toList());
                CustomerListCustomerIdByPageableRequest levelRequest = new CustomerListCustomerIdByPageableRequest();
                levelRequest.setPageSize(pageSize);
                levelRequest.setPageNum(pageNum);
                levelRequest.setCustomerLevelIds(ids);
                customerIds = customerQueryProvider.listCustomerIdByPageable(levelRequest).getContext().getCustomerIds();
            }else if(MessageSendType.GROUP.equals(messageSendVO.getSendType())){
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
                //查询系统人群和自定义人群的会员（去重）
                CrmGroupRequest crmGroupRequest = CrmGroupRequest.builder()
                        .sysGroupList(sysGroupList)
                        .customGroupList(customGroupList)
                        .build();
                crmGroupRequest.setPageNum(pageNum);
                crmGroupRequest.setPageSize(pageSize);
                customerIds = crmGroupProvider.queryListByGroupId(crmGroupRequest).getContext();
            }else{
                log.info("根据消息任务ID：{},消息接收类型：{}，消息类型不符合要求，请检查相关信息",messageId, messageSendVO.getSendType());
                break;
            }
            if(CollectionUtils.isEmpty(customerIds)){
                log.info("根据消息任务ID：{},消息接收类型：{}，已查询不到用户，发送成功========>>更新任务状态&&移除此任务",messageId,messageSendVO.getSendType());
                //更新任务为已结束
                modifyTaskInfo(taskInfo,String.valueOf(messageId));
                break;
            }
            log.info("根据消息任务ID：{},消息接收类型：{}，消息发送指定ID集合详情信息：{}，开始发送消息！",messageId,messageSendVO.getSendType(),customerIds);
            appMessageProvider.addBatch(new AppMessageAddRequest(appMessageVO, customerIds));
            pageNum++;
        }


    }

    /**
     * 更新任务状态
     * @param taskInfo
     * @param messageId
     */
    private void modifyTaskInfo(TaskInfo taskInfo,String messageId){
        //更新任务为已结束
        taskInfo.setEndTime(LocalDateTime.now());
        taskInfo.setState(TaskStatus.END);
        taskJobService.addTaskJob(taskInfo);
        //删除任务
        quartzManagerService.delete(messageId, TaskJobService.MESSAGE_SEND);
    }

    private JSONObject replaceRoute(String route){
        JSONObject params = new JSONObject();
        if (StringUtils.isNotBlank(route)){
            String router = route.replaceAll("'", "\"");
            JSONObject jsonObject = JSONObject.parseObject(router);
            String link = jsonObject.getString("linkKey");
            JSONObject info = jsonObject.getJSONObject("info");
            switch (link){
                case "goodsList":
                    String skuId = info.getString("skuId");
                    params.put("type", 0);
                    params.put("skuId", skuId);
                    break;
                case "categoryList":
                    JSONArray selectedKeys = info.getJSONArray("selectedKeys");
                    String pathNames = info.getString("pathName");
                    String[] names =  pathNames.split(",");
                    String cateId = selectedKeys.getString(selectedKeys.size()-1);
                    String cateName = names[names.length-1];
                    params.put("type", 3);
                    params.put("cateId", cateId);
                    params.put("cateName", cateName);
                    break;
                case "storeList":
                    String storeId = info.getString("storeId");
                    params.put("type", 4);
                    params.put("storeId", storeId);
                    break;
                case "promotionList":
                    params.put("type", 5);
                    String cateKey = info.getString("cateKey");
                    if ("groupon".equals(cateKey)){
                        String goodsInfoId = info.getString("goodsInfoId");
                        params.put("node", 0);
                        params.put("skuId", goodsInfoId);
                    } else if ("full".equals(cateKey)){
                        String marketingId = info.getString("marketingId");
                        params.put("node", 2);
                        params.put("mid", marketingId);
                    } else if ("flash".equals(cateKey)){
                        params.put("node", 1);
                        String goodsInfoId = info.getString("goodsInfoId");
                        params.put("skuId", goodsInfoId);
                    } else if ("onePrice".equals(cateKey)) {
                        String marketingId = info.getString("marketingId");
                        params.put("node", 2);
                        params.put("mid", marketingId);
                    } else if ("halfPrice".equals(cateKey)) {
                        String marketingId = info.getString("marketingId");
                        params.put("node", 2);
                        params.put("mid", marketingId);
                    } else if ("comBuy".equals(cateKey)) {
                        String goodsInfoId = info.getString("goodsInfoId");
                        params.put("node", 3);
                        params.put("skuId", goodsInfoId);
                    } else if ("preOrder".equals(cateKey)) {
                        String goodsInfoId = info.getString("goodsInfoId");
                        params.put("node", 1);
                        params.put("skuId", goodsInfoId);
                    } else if ("preSell".equals(cateKey)) {
                        String goodsInfoId = info.getString("goodsInfoId");
                        params.put("node", 1);
                        params.put("skuId", goodsInfoId);
                    }
                    break;
                case "userpageList":
                    params.put("type", 12);
                    String appPath = info.getString("appPath");
                    String wechatPath = info.getString("wechatPath");
                    String path = info.getString("path");
                    String key = info.getString("key");
                    String linkUrl = info.getString("link");
                    params.put("router", appPath);
                    params.put("wechatPath", wechatPath);
                    params.put("path", path);
                    params.put("key", key);
                    params.put("linkUrl", linkUrl);
                    break;
                case "pageList":
                    params.put("type",6);
                    String pageType = info.getString("pageType");
                    String pageCode = info.getString("pageCode");
                    params.put("pageType", pageType);
                    params.put("pageCode", pageCode);
                    break;
            }
        }
        return params;
    }

}
