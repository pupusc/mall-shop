package com.wanmi.sbc.setting.kuaidi100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.MD5Util;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.DeliveryQueryRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.LogisticsRopResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 快递100
 * Created by CHENLI on 2017/5/23.
 */
@Slf4j
@Service
@RefreshScope
public class DeliveryService {


    @Autowired
    private ConfigService configService;
    @Value("${openNewDelivery}")
    private String newKuaiDi = "TRUE";

    /**
     * kuaidi100 请求地址
     */
    private static final String KUAIDI_URL = "http://poll.kuaidi100.com/poll/query.do";
    private static final String KUAIDI_NEW_URL = "https://wuliu.market.alicloudapi.com/kdi";

    /**
     * 自定义的物流查询
     * @param queryRequest
     * @return
     * @throws Exception
     */
    public List<Map<Object, Object>> queryNewExpressInfoUrl(DeliveryQueryRequest queryRequest) throws Exception {
        List<Map<Object, Object>> deliverLogisticsList = new ArrayList<>();

        if (StringUtils.isBlank(queryRequest.getDeliveryNo())) {
            return deliverLogisticsList;
        }
        String result = "";
        Map<String, String> headers= new HashMap<>();
        headers.put("Authorization","APPCODE dc811f0f955f41978c754064215e0eb2");

        Map<String, String> querys = new HashMap<>();
        querys.put("no", queryRequest.getDeliveryNo());
        HttpResponse httpResponse = HttpUtil.doGet(KUAIDI_NEW_URL, "", "", headers, querys);
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new SbcRuntimeException("K-999999");
        }
        result = EntityUtils.toString(httpResponse.getEntity());
        log.info("DeliveryService queryExpressInfoUrl 获取结果信息为：{}", result);
        // 格式化数据
        JSONObject reslutJsonAll = JSONObject.parseObject(result);
        if (!Objects.equals(reslutJsonAll.getString("status"), "0")) {
            //表示物流信息没有发货
            if (Objects.equals(reslutJsonAll.getString("status"), "205")) {
                return deliverLogisticsList;
            }
            throw new SbcRuntimeException("K-999999");
        }
        JSONObject resultJson = reslutJsonAll.getJSONObject("result");
        if (!resultJson.containsKey("list")) {
            return deliverLogisticsList;
        }

        JSONArray detailList = resultJson.getJSONArray("list");
        if (detailList != null && detailList.size() > 0) {
            for (int i = 0; i < detailList.size(); i++) {
                JSONObject jobj = detailList.getJSONObject(i);
                Map<Object, Object> map = new HashMap<>();
                map.put("time", jobj.get("time"));
                map.put("context", jobj.get("status"));
                deliverLogisticsList.add(map);
            }
        }
        return deliverLogisticsList;
    }

    /**
     * 根据快递公司及快递单号查询物流详情
     *
     * @param queryRequest
     * @return
     */
    public List<Map<Object, Object>> queryExpressInfoUrl(DeliveryQueryRequest queryRequest) throws Exception {

        if (Objects.equals(newKuaiDi, "TRUE")) {
            return this.queryNewExpressInfoUrl(queryRequest);
        }

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
