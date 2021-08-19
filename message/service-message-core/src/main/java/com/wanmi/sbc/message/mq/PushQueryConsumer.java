package com.wanmi.sbc.message.mq;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.message.api.constant.PushConstants;
import com.wanmi.sbc.message.bean.constant.PushErrorCode;
import com.wanmi.sbc.message.bean.enums.MethodType;
import com.wanmi.sbc.message.bean.enums.PushPlatform;
import com.wanmi.sbc.message.bean.enums.PushStatus;
import com.wanmi.sbc.message.bean.vo.PushSendVO;
import com.wanmi.sbc.message.pushUtil.PushService;
import com.wanmi.sbc.message.pushUtil.root.QueryResultEntry;
import com.wanmi.sbc.message.pushdetail.model.root.PushDetail;
import com.wanmi.sbc.message.pushdetail.service.PushDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @program: sbc-micro-service
 * @description: 运营计划push查询推送详情
 * @create: 2020-02-04 17:22
 **/
@Slf4j
@Component
@EnableBinding(PushQuerySink.class)
public class PushQueryConsumer {

    @Autowired
    private PushService pushService;

    @Autowired
    private PushDetailService pushDetailService;

    @StreamListener(PushConstants.Q_SMS_SERVICE_PUSH_QUERY_INPUT)
    public void pushQuery(String json){
        log.info("=============== PushQueryConsumer处理start ===============");
        PushSendVO pushSendVO = JSON.parseObject(json, PushSendVO.class);
        if (StringUtils.isNotBlank(pushSendVO.getIosTaskId())){
            QueryResultEntry resultEntry = pushService.queryOrCancel(pushSendVO.getIosTaskId(), PushPlatform.IOS,
                    MethodType.QUERY);
            if ("SUCCESS".equals(resultEntry.getRet())){
                PushDetail detail = new PushDetail();
                detail.setTaskId(resultEntry.getTaskId());
                detail.setPlatform(PushPlatform.IOS);
                detail.setOpenSum(resultEntry.getOpenCount());
                detail.setSendStatus(PushStatus.fromValue(resultEntry.getStatus()));
                detail.setSendSum(resultEntry.getSentCount());
                detail.setCreateTime(LocalDateTime.now());
                detail.setPlanId(pushSendVO.getPlanId());
                pushDetailService.add(detail);
            } else {
                log.error("PushSendQueryController.detail::友盟iOS查询接口失败");
                throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL, new Object[]{"iOS查询"});
            }
        } else if (StringUtils.isNotBlank(pushSendVO.getAndroidTaskId())){
            QueryResultEntry resultEntry = pushService.queryOrCancel(pushSendVO.getAndroidTaskId(),
                    PushPlatform.ANDROID,
                    MethodType.QUERY);
            if ("SUCCESS".equals(resultEntry.getRet())){
                PushDetail detail = new PushDetail();
                detail.setTaskId(resultEntry.getTaskId());
                detail.setPlatform(PushPlatform.ANDROID);
                detail.setOpenSum(resultEntry.getOpenCount());
                detail.setSendStatus(PushStatus.fromValue(resultEntry.getStatus()));
                detail.setSendSum(resultEntry.getSentCount());
                detail.setCreateTime(LocalDateTime.now());
                detail.setPlanId(pushSendVO.getPlanId());
                pushDetailService.add(detail);
            } else {
                log.error("PushSendQueryController.detail::友盟android查询接口失败");
                throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL, new Object[]{"android查询"});
            }
        }
        log.info("=============== PushQueryConsumer处理end ===============");
    }
}