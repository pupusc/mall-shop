package com.soybean.mall.order.provider.impl.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soybean.mall.order.api.provider.order.ProcessDateProvider;
import com.soybean.mall.order.api.request.process.AppIdProcessReq;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.paycallbackresult.model.root.PayCallBackResult;
import com.wanmi.sbc.order.paycallbackresult.repository.PayCallBackResultRepository;
import com.wanmi.sbc.order.redis.RedisService;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
        String key = "TMP_PROCESS_APP_ID_FROM_id";
        String redisValue = redisService.getString(key);
        if (StringUtils.isNotBlank(redisValue)) {
            try {
                maxId = Long.parseLong(redisValue);
            } catch (Exception ex) {
                log.error("ProcessDateController processAppId exception", ex);
            }
        }
        if (appIdProcessReq.getMaxId() != null) {
            maxId = appIdProcessReq.getMaxId().longValue();
        }
        List<PayCallBackResult> payCallBackResults = payCallBackResultRepository.listByMaxId(maxId.intValue(), appIdProcessReq.getSize());

        for (PayCallBackResult payCallBackResult : payCallBackResults) {
            if (payCallBackResult.getId() > maxId) {
                maxId = payCallBackResult.getId();
            }
            String transaction_idStr ="";
            String appId = "";
            //微信
            try {
                if (Objects.equals(payCallBackResult.getResultStatus(), 0)) {
                    SAXReader saxReader = new SAXReader();
                    Document read = saxReader.read(new ByteArrayInputStream(payCallBackResult.getResultContext().getBytes(StandardCharsets.UTF_8)));
                    Element rootElement = read.getRootElement();
                    Element mch_id = rootElement.element("mch_id");
                    Element transaction_id = rootElement.elementByID("transaction_id");
                    appId = mch_id.getStringValue();
                    transaction_idStr = transaction_id.getStringValue();
                }
            } catch (Exception ex) {
                log.error("processAppId error 微信", ex);
            }
            try {
                //支付宝
                if (Objects.equals(payCallBackResult.getResultStatus(), 1)) {
                    if (StringUtils.isEmpty(payCallBackResult.getResultContext())) {
                        continue;
                    }
                    JSONObject resultContextJson = JSON.parseObject(payCallBackResult.getResultContext());
                    appId = resultContextJson.getString("app_id");
                    transaction_idStr = resultContextJson.getString("trade_no");
                }
            } catch (Exception ex) {
                log.error("processAppId error 支付宝", ex);
            }

            if (StringUtils.isBlank(appId) || StringUtils.isBlank(transaction_idStr)) {
                log.info("processAppId businessId:{} appId:{} or transactionId:{} isEmpty", payCallBackResult.getBusinessId(), appId, transaction_idStr);
                continue;
            }

            //修改
            payProvider.saveAppId(appId, transaction_idStr);
        }

        redisService.setString(key, "" + maxId, 24 * 60 * 60);
        return BaseResponse.SUCCESSFUL();
    }


}
