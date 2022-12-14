package com.wanmi.sbc.common.util;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.constant.FeiShuConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/12/8 7:17 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
public class FeiShuUtil {


    public static void sendFeiShuMessageDefault(String message) {
        sendFeiShuMessage(message, FeiShuConstant.FEISHU_NOTICE_URL, FeiShuConstant.FEISHU_USERID_DUANLONGSHAN, "欧巴");
    }


    public static void sendFeiShuMessage(String message, String noticeUrl, String userId, String userName) {
        log.info("FeiShuUtil sendFeiShuMessage message:{} noticeUrl:{} userId:{} userName:{}",
                message, noticeUrl, userId, userName);
        if (StringUtils.isNotBlank(message) && StringUtils.isNotBlank(noticeUrl) && StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userName)) {

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-type", "application/json; charset=utf-8");
            headers.put("Accept", "application/json");

            Map<String, String> contentMap = new HashMap<>();
            contentMap.put("text", String.format(FeiShuConstant.DIRECT_USER_SEND_MSG, userId, userName, message));


            Map<String, Object> body = new HashMap<>();
            body.put("msg_type", "text");
//                   body.put("receive_id", FeiShuConstant.FEISHU_USERID_DUANLONGSHAN);
            body.put("content", contentMap);


            log.info("FeiShuUtil sendMessage requestBody {}", body);
            try {
                HttpResponse res = HttpUtil.doPost(noticeUrl, "", "", headers, null, JSON.toJSONString(body));
                String result = "";
                if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toString(res.getEntity());
                }
                log.info("FeiShuUtil sendMessage requestBody {} response status {} result:{} ",body, res.getStatusLine().getStatusCode(), result);
            } catch (Exception ex) {
                log.error("FeiShuUtil sendMessage error", ex);
            }
        }
    }
}
