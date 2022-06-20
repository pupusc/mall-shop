package com.wanmi.sbc.setting.kuaidi100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.MD5Util;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.DeliveryQueryRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.LogisticsRopResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 快递100
 * Created by CHENLI on 2017/5/23.
 */
@Slf4j
@Service
public class DeliveryService {


    @Autowired
    private ConfigService configService;

    /**
     * kuaidi100 请求地址
     */
    private static final String KUAIDI_URL = "http://poll.kuaidi100.com/poll/query.do";

    /**
     * 根据快递公司及快递单号查询物流详情
     *
     * @param queryRequest
     * @return
     */
    public List<Map<Object, Object>> queryExpressInfoUrl(DeliveryQueryRequest queryRequest) throws Exception {
        List<Map<Object, Object>> orderList = new ArrayList<Map<Object, Object>>();

        //获取快递100的key
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setConfigType(ConfigType.KUAIDI100.toValue());
        LogisticsRopResponse response = configService.findKuaiDiConfig(request.getConfigType(), DeleteFlag.NO);
        if (Objects.isNull(response)) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000001);
        }
        String customer = response.getCustomerKey();
        String kuaidiKey = response.getDeliveryKey();
        //查询参数
        String param = "{\"com\":\"" + queryRequest.getCompanyCode() + "\",\"num\":\"" + queryRequest.getDeliveryNo() + "\"}";
        //加密的签名
        String sign = (MD5Util.md5Hex(param + kuaidiKey + customer, "utf-8")).toUpperCase();
        //查询所需的参数
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("param", param);
        params.put("sign", sign);
        params.put("customer", customer);

        String result = "";
        try {
            result = HttpRequest.postData(KUAIDI_URL, params, "utf-8").toString();
            // 格式化数据
            JSONObject reslut = JSONObject.parseObject(result);
            JSONArray kuaidiList = JSONArray.parseArray(reslut.get("data").toString());
            if (kuaidiList != null && kuaidiList.size() > 0) {
                for (int i = 0; i < kuaidiList.size(); i++) {
                    JSONObject jobj = JSON.parseObject(kuaidiList.get(i).toString(), JSONObject.class);
                    Map<Object, Object> map = new HashMap<Object, Object>();
                    map.put("time", jobj.get("ftime"));
                    map.put("context", jobj.get("context"));
                    orderList.add(map);
                }
            }
        } catch (Exception e) {
            log.error("调用失败-返回值：{}", result);
            throw e;
        }

        return orderList;
    }
}
