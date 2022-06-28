package com.wanmi.sbc.feishu.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.feishu.FeiShuNoticeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/27 12:34 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class FeiShuSendMessageService {


    @Value("${notice.send.message.url}")
    private String noticeSendMsgUrl;

    @Value("${notice.send.message.token}")
    private String noticeSendMsgToken;

    @Value("${notice.send.message.tenantId}")
    private String noticeSendMsgTenantId;

    @Value("${notice.send.message.tenantId2}")
    private String noticeSendMsgTenantId2;

    /**
     * @时艺洪@姜金秀@教恩惠@柏红阳 【成本价】
     */
    @Value("${notice.send.message.noticeId}")
    private Integer noticeSendMsgNoticeId;

    /**
     *  2. @时艺洪@姜金秀@教恩惠@赵润泽@柏洪阳 【库存】
     */
    @Value("${notice.send.message.noticeId2}")
    private Integer noticeSendMsgNoticeId2;

    /**
     * 发送飞书消息
     */
    public void sendMessage(String content, FeiShuNoticeEnum feiShuNoticeEnum) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json; charset=utf-8");
        headers.put("Accept", "application/json");
        headers.put("token", noticeSendMsgToken);


        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("content", content);

        Map<String,Object> body = new HashMap<>();
        body.put("replaceParams", contentMap);

        if (feiShuNoticeEnum == FeiShuNoticeEnum.STOCK) {
            body.put("noticeId",noticeSendMsgNoticeId);
            headers.put("tenantId", noticeSendMsgTenantId);
        } else if (feiShuNoticeEnum == FeiShuNoticeEnum.COST_PRICE) {
            body.put("noticeId",noticeSendMsgNoticeId2);
            headers.put("tenantId", noticeSendMsgTenantId2);
        } else {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }

        log.info("FeiShuSendMessageService sendMessage requestBody {}", body);
        try {
            HttpResponse res = HttpUtil.doPost(noticeSendMsgUrl, "", "", headers, null, JSON.toJSONString(body));
            String result = "";
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(res.getEntity());
            }
            log.info("FeiShuSendMessageService sendMessage requestBody {} response status {} result:{} ",body, res.getStatusLine().getStatusCode(), result);
        } catch (Exception ex) {
            log.error("FeiShuSendMessageService sendMessage error", ex);
        }

    }

}
