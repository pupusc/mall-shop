package com.soybean.mall.order.provider.impl.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soybean.mall.order.api.provider.order.ProcessDateProvider;
import com.soybean.mall.order.api.request.process.AppIdProcessReq;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.bean.enums.PayCallBackResultStatus;
import com.wanmi.sbc.order.bean.enums.PayCallBackType;
import com.wanmi.sbc.order.paycallbackresult.model.root.PayCallBackResult;
import com.wanmi.sbc.order.paycallbackresult.repository.PayCallBackResultRepository;
import com.wanmi.sbc.order.redis.RedisService;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/22 6:22 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@Slf4j
public class ProcessDateController implements ProcessDateProvider {

    @Autowired
    private PayCallBackResultRepository payCallBackResultRepository;

    @Autowired
    private PayProvider payProvider;

    @Autowired
    private RedisService redisService;

    @Override
    public BaseResponse processAppId(AppIdProcessReq appIdProcessReq) {


        Long maxId = 0L;
        String key = "TMP_PROCESS_APP_ID_FROM_ID";
        String redisValue = redisService.getString(key);
        if (StringUtils.isNotBlank(redisValue)) {
            try {
                maxId = Long.parseLong(redisValue);
            } catch (Exception ex) {
                log.error("ProcessDateController processAppId exception", ex);
            }
        }
        if (appIdProcessReq.getMaxId() != null) {
            maxId = appIdProcessReq.getMaxId();
        }

        List<PayCallBackResult> payCallBackResults;
        if (CollectionUtils.isEmpty(appIdProcessReq.getIds())) {
            payCallBackResults = payCallBackResultRepository.listByMaxId(maxId.intValue(), appIdProcessReq.getSize());
        } else {
            payCallBackResults = payCallBackResultRepository.findAllById(appIdProcessReq.getIds());
        }


        int num = 1;
        for (PayCallBackResult payCallBackResult : payCallBackResults) {
            if (payCallBackResult.getId() > maxId) {
                maxId = payCallBackResult.getId();
            }

            Map<String, String> key2value = this.getAppId(payCallBackResult);
            String appId = key2value.get("appId");
            String transactionId = key2value.get("transactionId");
            if (StringUtils.isBlank(appId) || StringUtils.isBlank(transactionId)) {
                log.info("processAppId businessId:{} appId:{} or transactionId:{} isEmpty", payCallBackResult.getBusinessId(), appId, transactionId);
                continue;
            }
            log.info("ProcessDateController processAppId businessId:{} execute id:{} num:{} ", payCallBackResult.getBusinessId(),payCallBackResult.getId(), num++);
            //修改
            payProvider.saveAppId(appId, transactionId);
        }

        if (CollectionUtils.isEmpty(appIdProcessReq.getIds())) {
            redisService.setString(key, "" + maxId, 24 * 60 * 60);
        }
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse processAppIdByBusinessId(AppIdProcessReq appIdProcessReq){
        if (StringUtils.isBlank(appIdProcessReq.getBusinessId())) {
            throw new SbcRuntimeException("K-999999", "请传递业务id");
        }
        List<PayCallBackResult> payCallBackResults =
                payCallBackResultRepository.list(appIdProcessReq.getBusinessId(), PayCallBackResultStatus.SUCCESS.toValue());
        if (CollectionUtils.isEmpty(payCallBackResults)) {
            throw new SbcRuntimeException("K-999999", "当前订单没有对应的支付信息");
        }

        PayCallBackResult payCallBackResult = payCallBackResults.get(0);
        Map<String, String> key2value = this.getAppId(payCallBackResult);
        String appId = key2value.get("appId");
        String transactionId = key2value.get("transactionId");
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(transactionId)) {
            log.info("processAppIdByBusinessId businessId:{} appId:{} or transactionId:{} isEmpty", payCallBackResult.getBusinessId(), appId, transactionId);
            throw new SbcRuntimeException("K-999999", "当前订单没有对应appId");
        }
        log.info("ProcessDateController processAppIdByBusinessId businessId:{} execute id:{} ", payCallBackResult.getBusinessId(),payCallBackResult.getId());
        //修改
        payProvider.saveAppId(appId, transactionId);
        return BaseResponse.SUCCESSFUL();
    }



    private Map<String, String> getAppId(PayCallBackResult payCallBackResult) {
        Map<String, String> result = new HashMap<>();
        //微信
        try {
            if (Objects.equals(payCallBackResult.getPayType(), PayCallBackType.WECAHT)) {
                SAXReader saxReader = new SAXReader();
                Document read = saxReader.read(new ByteArrayInputStream(payCallBackResult.getResultContext().getBytes(StandardCharsets.UTF_8)));
                Element rootElement = read.getRootElement();
                Element appid = rootElement.element("appid");
                Element transaction_id = rootElement.element("transaction_id");
                result.put("appId", appid.getStringValue());
                result.put("transactionId", transaction_id.getStringValue());
            }
        } catch (Exception ex) {
            log.error("processAppId error 微信", ex);
        }

        try {
            //支付宝
            if (Objects.equals(payCallBackResult.getPayType(), PayCallBackType.ALI)) {
                if (StringUtils.isEmpty(payCallBackResult.getResultContext())) {
                    return result;
                }
                JSONObject resultContextJson = JSON.parseObject(payCallBackResult.getResultContext());
                result.put("appId", resultContextJson.getString("app_id"));
                result.put("transactionId", resultContextJson.getString("trade_no"));
            }
        } catch (Exception ex) {
            log.error("processAppId error 支付宝", ex);
        }
        return result;
    }

}
