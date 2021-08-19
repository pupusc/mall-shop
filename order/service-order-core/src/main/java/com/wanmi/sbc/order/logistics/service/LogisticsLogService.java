package com.wanmi.sbc.order.logistics.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.WebUtil;
import com.wanmi.sbc.order.bean.dto.KuaiDiHundredNoticeResultItemDTO;
import com.wanmi.sbc.order.bean.dto.KuaidiHundredNoticeDTO;
import com.wanmi.sbc.order.logistics.model.root.LogisticsLog;
import com.wanmi.sbc.order.logistics.model.root.LogisticsLogDetail;
import com.wanmi.sbc.order.logistics.repository.LogisticsLogRepository;
import com.wanmi.sbc.order.logistics.request.LogisticsLogQueryRequest;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.LogisticsRopResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * 物流信息服务
 * Created by dyt on 2020/4/17.
 */
@Slf4j
@Service
@Transactional(readOnly = true, timeout = 10)
public class LogisticsLogService {

    /**
     * kuaidi100 请求地址
     */
    private static final String KUAIDI_URL = "https://poll.kuaidi100.com/poll";

    @Autowired
    private LogisticsLogRepository logisticsLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;


    @Transactional
    public void add(LogisticsLog logisticsLog) {
        //获取快递100配置信息
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setConfigType(ConfigType.KUAIDI100.toValue());
        LogisticsRopResponse response = systemConfigQueryProvider.findKuaiDiConfig(request).getContext();
        if(Constants.yes.equals(response.getSubscribeStatus())){
            String callBackUrl = response.getCallBackUrl();
            if(StringUtils.isBlank(callBackUrl)){
                throw new SbcRuntimeException("K-050142");
            }
            if(!(callBackUrl.endsWith("/")|| callBackUrl.endsWith("\\"))){
                callBackUrl = callBackUrl+"/";
            }

            logisticsLog.setEndFlag(Boolean.FALSE);
            String id = this.addLogisticsLog(logisticsLog).getId();
            callBackUrl += id;
            Map<String, Object> resMap = fetchKuaidi(logisticsLog, callBackUrl, response);
            logisticsLog.setSuccessFlag(Boolean.valueOf(Objects.toString(resMap.get("result"),"false")));
            logisticsLog.setMessage(Objects.toString(resMap.get("message")));
            this.update(logisticsLog);
        }
    }


    /**
     *
     * @param noticeDTO
     */
    @Transactional
    public void modifyForKuaiDi100(KuaidiHundredNoticeDTO noticeDTO) {
        LogisticsLog log = query(LogisticsLogQueryRequest.builder().id(noticeDTO.getId()).build()).stream().findFirst().orElse(null);
        if (Objects.nonNull(log)) {
            log.setStatus(noticeDTO.getStatus());
            log.setMessage(noticeDTO.getMessage());
            if (String.valueOf(Constants.yes).equals(noticeDTO.getAutoCheck())) {
                log.setAutoCheck(noticeDTO.getAutoCheck());
            }
            if (StringUtils.isNotEmpty(noticeDTO.getComNew())) {
                log.setComNew(noticeDTO.getComNew());
            }
            if (Objects.nonNull(noticeDTO.getLastResult())) {
                log.setState(noticeDTO.getLastResult().getState());
                if(Objects.nonNull(noticeDTO.getLastResult().getIscheck())) {
                    log.setIsCheck(noticeDTO.getLastResult().getIscheck());
                }
                log.setLogisticNo(noticeDTO.getLastResult().getNu());
                if (CollectionUtils.isNotEmpty(noticeDTO.getLastResult().getData())) {
                    log.setLogisticsLogDetails(new ArrayList<>());
                    for (KuaiDiHundredNoticeResultItemDTO dto : noticeDTO.getLastResult().getData()) {
                        LogisticsLogDetail detail = new LogisticsLogDetail();
                        KsBeanUtil.copyPropertiesThird(dto, detail);
                        detail.setTime(dto.getFtime());
                        log.getLogisticsLogDetails().add(detail);
                    }

                    if(Constants.yes.equals(NumberUtils.toInt(log.getIsCheck(), 0))){
                        log.setCheckTime(DateUtil.parse(log.getLogisticsLogDetails().get(0).getTime(), DateUtil.FMT_TIME_1));
                    }
                }
            }
            logisticsLogRepository.save(log);
        }
    }

    /**
     * 结束物流信息
     * @param orderNo 订单号
     */
    @Transactional
    public void modifyEndFlagByOrderNo(String orderNo) {
        List<LogisticsLog> logs = this.query(LogisticsLogQueryRequest.builder().orderNo(orderNo).build());
        if(CollectionUtils.isNotEmpty(logs)){
            logs.forEach(l -> {
                l.setEndFlag(Boolean.TRUE);
                this.update(l);
            });
        }
    }

    /**
     * 查询物流记录
     *
     * @param request
     * @return
     */
    public List<LogisticsLog> query(LogisticsLogQueryRequest request) {
        return mongoTemplate.find(new Query(request.getCriteria()).with(Sort.by(Sort.Direction.DESC, "updateTime")).limit(1000),
                LogisticsLog.class);
    }

    /**
     * 调快递100接口
     * @param logisticsLog
     * @param callBackUrl
     * @param response
     * @return
     */
    private Map<String, Object> fetchKuaidi(LogisticsLog logisticsLog, String callBackUrl, LogisticsRopResponse response){
        String yes = Constants.yes.toString();
        JSONObject parameters = new JSONObject();
        parameters.put("callbackurl", callBackUrl);
        parameters.put("resultv2", yes);
        if(StringUtils.isBlank(logisticsLog.getComOld())) {
            parameters.put("autoCom", yes);
        }
        parameters.put("phone", logisticsLog.getPhone());


        JSONObject param = new JSONObject();
        param.put("company", StringUtils.trimToEmpty(logisticsLog.getComOld()).toLowerCase());
        param.put("number", StringUtils.trimToEmpty(logisticsLog.getLogisticNo()));
        param.put("from", logisticsLog.getFrom());
        param.put("to", logisticsLog.getTo());
        param.put("key", response.getDeliveryKey());
        param.put("parameters", parameters);

        String paramStr = String.format("schema=json&param=%s", param.toJSONString());

        Map<String, Object> resMap = new HashMap<>();
        try {
            String res = WebUtil.post(KUAIDI_URL, paramStr);
            if(StringUtils.isBlank(res)){
                resMap.put("result", false);
                resMap.put("returnCode", "500");
                resMap.put("message", "快递100服务没有返回信息");
                return resMap;
            }
            return JSON.parseObject(res);
        } catch (IOException e) {
            resMap.put("result", false);
            resMap.put("returnCode", "500");
            resMap.put("message", e.getMessage());
        }
        return resMap;
    }

    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param log
     */
    public LogisticsLog addLogisticsLog(LogisticsLog log) {
        logisticsLogRepository.save(log);
        return log;
    }

    /**
     * 修改文档
     * 专门用于数据修改服务,不允许数据新增的时候调用
     *
     * @param log
     */
    public LogisticsLog update(LogisticsLog log) {
        logisticsLogRepository.save(log);
        return log;
    }
}
