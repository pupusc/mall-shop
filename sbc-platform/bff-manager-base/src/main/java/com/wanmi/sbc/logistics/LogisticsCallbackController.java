package com.wanmi.sbc.logistics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.api.provider.logistics.LogisticsLogSaveProvider;
import com.wanmi.sbc.order.api.request.logistics.LogisticsLogNoticeForKuaidiHundredRequest;
import com.wanmi.sbc.order.bean.dto.KuaidiHundredNoticeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 物流订阅回调
 * Created by dyt on 2020/04/17.
 */
@Api(tags = "LogisticsCallbackController", description = "交易回调")
@RestController
@RequestMapping("/logisticsCallback")
@Slf4j
public class LogisticsCallbackController {

    public static final Logger LOGGER = LoggerFactory.getLogger(LogisticsCallbackController.class);

    @Autowired
    private LogisticsLogSaveProvider logisticsLogSaveProvider;

    /**
     * 物流订阅异步回调
     * @param request
     * @param response
     */
    @ApiOperation(value = "微信支付退款成功异步回调")
    @RequestMapping(value = "/kuaidi100/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public void callBackByKuaidi100(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) throws IOException {
        String param = request.getParameter("param");
        LOGGER.info(param);

        if(StringUtils.isBlank(param)){
            error(response, "参数为空");
            return;
        }
        param = StringEscapeUtils.unescapeHtml(param);
        try {
            KuaidiHundredNoticeDTO dto = JSON.parseObject(param, KuaidiHundredNoticeDTO.class);
            dto.setId(id);
            logisticsLogSaveProvider.modifyForKuaidiHundred(LogisticsLogNoticeForKuaidiHundredRequest.builder().kuaidiHundredNoticeDTO(dto).build());
            success(response);
        }catch (SbcRuntimeException e){
            LOGGER.error("物流订阅异常", e);
            error(response, "物流订阅异常");
        }catch (Exception e){
            LOGGER.error("物流订阅异常", e);
            error(response, "物流订阅异常");
        }
    }

    private void success(HttpServletResponse response) throws IOException {
        response.getWriter().print(resultParam(true, "200", "成功"));
    }

    private void error(HttpServletResponse response, String message) throws IOException {
        response.getWriter().print(resultParam(false, "500", message));
    }

    private String resultParam(boolean result, String returnCode, String message) {
        JSONObject p = new JSONObject();
        p.put("result", result);
        p.put("returnCode", returnCode);
        p.put("message", message);
        return p.toJSONString();
    }
}
