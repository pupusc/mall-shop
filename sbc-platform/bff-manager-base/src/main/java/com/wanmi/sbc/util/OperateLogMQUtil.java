package com.wanmi.sbc.util;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.setting.api.request.OperationLogAddRequest;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.nonNull;

/**
 * 操作日志发送公共类
 * Created by liguang on 2018-10-17
 */
@Slf4j
@Component
@EnableBinding
public class OperateLogMQUtil {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 操作日志记录
     *
     * @param opModule
     * @param opCode
     * @param opContext
     */
    public void convertAndSend(String opModule, String opCode, String opContext) {
        Claims claims = (Claims) HttpUtil.getRequest().getAttribute("claims");
        this.convertAndSend(opModule, opCode, opContext, claims);
    }

    /**
     * 操作日志记录  由于在jwt.excluded-urls中配置了路径之后 获取不到Claims 所以提供手动传入的方式
     *
     * @param opModule
     * @param opCode
     * @param opContext
     * @param claims
     */
    public void convertAndSend(String opModule, String opCode, String opContext, Claims claims) {
       OperationLogAddRequest operationLog = new OperationLogAddRequest();
       if (nonNull(claims)) {
           operationLog.setEmployeeId(Objects.toString(claims.get("employeeId"), StringUtils.EMPTY));
           // accountName
           operationLog.setOpAccount(Objects.toString(claims.get("EmployeeName"), StringUtils.EMPTY));
           operationLog.setStoreId(Long.valueOf(Objects.toString(claims.get("storeId"), "0")));
           operationLog.setCompanyInfoId(Long.valueOf(Objects.toString(claims.get("companyInfoId"), "0")));
           operationLog.setOpRoleName(Objects.toString(claims.get("roleName"), StringUtils.EMPTY));
           operationLog.setOpName(Objects.toString(claims.get("realEmployeeName"), StringUtils.EMPTY));
       } else {
           operationLog.setEmployeeId(StringUtils.EMPTY);
           operationLog.setOpAccount(StringUtils.EMPTY);
           operationLog.setOpName(StringUtils.EMPTY);
           operationLog.setStoreId(0L);
           operationLog.setCompanyInfoId(0L);
           operationLog.setOpRoleName(StringUtils.EMPTY);
       }
       operationLog.setOpModule(opModule);
       operationLog.setOpCode(opCode);
       operationLog.setOpContext(opContext);
       operationLog.setOpIp(HttpUtil.getIpAddr());
       operationLog.setOpTime(LocalDateTime.now());

       resolver.resolveDestination(MQConstant.OPERATE_LOG_ADD).send(new GenericMessage<>(JSONObject.toJSONString(operationLog)));
    }
}
