package com.wanmi.ms.log.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Created by aqlu on 14-3-19.
 */
public class IntfLogBean implements Serializable {
    private static Long serialVersionUID = 1L;

    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static String systemCurrentTime(){
        return df.format(LocalDateTime.now());
    }

    private String invokeTime = systemCurrentTime(); // 时间戳，日志记录的时间,精确到毫秒，采用服务器的本地时间。ex: 2012-5-4 11:13:34:234

    private String uuId; // 调用标识

    private String sessionId; // 用于标识多次交互接口的一次会话调用了多次的接口的情况

    private String methodName; // 接口服务方法名称

    private String senderName; // 发送方服务英文标识

    private String senderHost; // IP地址和端口

    private String receiverName; // 接收方服务英文标示

    private String receiverHost; // 接收方IP和端口Ò

    private String srvGroup; // 服务组，参见Dubbo的Group概念

    private String version; // 服务版本号，参加Dubbo的版本号概念

    private Object paramTypes; // 接口参数的类型

    private Object paramValues; // 接口参数的值

    private Object resultValue; // 返回结果的值

    private long costTime = 0l; // 接口调用的时间消耗

    private String exceptionMsg; // 接口异常消息，只保留message,不保留异常堆栈；

    private String pinpointTraceId; // pinpoint trace id

    public String toSimpleString() {
        return String.format(
                "{\"invokeTime\": \"%s\" , \"uuId\": \"%s\", \"sessionId\": \"%s\", \"methodName\": \"%s\","
                        + " \"senderName\": \"%s\", \"senderHost\": \"%s\", \"receiverName\": \"%s\", "
                        + "\"receiverHost\": \"%s\", \"srvGroup\": \"%s\", \"version\": \"%s\", \"paramTypes\": [],"
                        + " \"paramValues\": null, \"resultValue\": null, \"costTime\": %s, \"exceptionMsg\": \"%s\"}",
                this.invokeTime, this.uuId, this.sessionId, this.methodName, this.senderName, this.senderHost,
                this.receiverName, this.receiverHost, this.srvGroup, this.version, this.costTime, this.exceptionMsg);
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderHost() {
        return senderHost;
    }

    public void setSenderHost(String senderHost) {
        this.senderHost = senderHost;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverHost() {
        return receiverHost;
    }

    public void setReceiverHost(String receiverHost) {
        this.receiverHost = receiverHost;
    }

    public String getSrvGroup() {
        return srvGroup;
    }

    public void setSrvGroup(String srvGroup) {
        this.srvGroup = srvGroup;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Object paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object getParamValues() {
        return paramValues;
    }

    public void setParamValues(Object paramValues) {
        this.paramValues = paramValues;
    }

    public Object getResultValue() {
        return resultValue;
    }

    public void setResultValue(Object resultValue) {
        this.resultValue = resultValue;
    }

    public String getInvokeTime() {
        return invokeTime;
    }

    public void setInvokeTime(String invokeTime) {
        this.invokeTime = invokeTime;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public String getPinpointTraceId() {
        return pinpointTraceId;
    }

    public void setPinpointTraceId(String pinpointTraceId) {
        this.pinpointTraceId = pinpointTraceId;
    }
}
