package com.wanmi.sbc.message.aliyunsmshttppush;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.message.aliyunsmshttppush.bean.SignSmsReportResponse;
import com.wanmi.sbc.message.aliyunsmshttppush.bean.TemplateSmsReportResponse;
import com.wanmi.sbc.message.api.provider.smssign.SmsSignSaveProvider;
import com.wanmi.sbc.message.api.provider.smstemplate.SmsTemplateSaveProvider;
import com.wanmi.sbc.message.api.request.smssign.ModifyReviewStatusByNameRequest;
import com.wanmi.sbc.message.api.request.smstemplate.ModifySmsTemplateReviewStatusByTemplateCodeRequest;
import com.wanmi.sbc.message.bean.constant.AliyunReviewStatusConstant;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * @ClassName AliyunSmsHttpPush
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/12/5 11:34
 **/
@Api(tags = "AliyunSmsHttpPush", description = "短信事件回调")
@RestController
@RequestMapping("/aliyunSmsCallback")
@Slf4j
public class AliyunSmsHttpPush {

    @Autowired
    private SmsSignSaveProvider smsSignSaveProvider;

    @Autowired
    private SmsTemplateSaveProvider smsTemplateSaveProvider;

    @ApiOperation(value = "短信签名审核消息回调")
    @RequestMapping(value = "/signSmsReport", method = RequestMethod.POST)
    public void signSmsReport(HttpServletRequest request, HttpServletResponse response) {
        log.info("===============短信签名审核回调开始==============");
        try {
            //短信签名审核回调数据输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            StringBuilder signSmsReportResultStr = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                signSmsReportResultStr.append(line);
            }
            //短信签名审核消息回调结果
            log.info("短信签名审核消息回调signSmsReportResultStr====" + signSmsReportResultStr);
            JSONArray jsonArray = JSONArray.parseArray(signSmsReportResultStr.toString());
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                SignSmsReportResponse signSmsReportResponse = JSONObject.toJavaObject(jsonObject, SignSmsReportResponse.class);
                smsSignSaveProvider.modifyReviewStatusByName(ModifyReviewStatusByNameRequest.builder()
                        .smsSignName(signSmsReportResponse.getSign_name())
                        .reviewStatus(conversionReviewStatus(signSmsReportResponse.getSign_status()))
                        .reviewReason(signSmsReportResponse.getReason())
                        .build());
            }
            response.getWriter().print("{\"code\": 0,\"msg\": \"成功\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("===============短信签名审核回调结束==============");
    }

    @ApiOperation(value = "短信模板审核状态消息回调")
    @RequestMapping(value = "/templateSmsReport", method = RequestMethod.POST)
    public void templateSmsReport(HttpServletRequest request, HttpServletResponse response) {
        log.info("===============短信模板审核状态消息回调开始==============");
        try {
            //短信模板审核回调数据输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            StringBuilder templateSmsReportResultStr = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                templateSmsReportResultStr.append(line);
            }
            //短信模板审核消息回调结果
            log.info("短信模板审核状态消息回调templateSmsReportResultStr====" + templateSmsReportResultStr);
            JSONArray jsonArray = JSONArray.parseArray(templateSmsReportResultStr.toString());
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TemplateSmsReportResponse templateSmsReportResponse = JSONObject.toJavaObject(jsonObject, TemplateSmsReportResponse.class);
                smsTemplateSaveProvider.modifySmsTemplateReviewStatusByTemplateCode(ModifySmsTemplateReviewStatusByTemplateCodeRequest.builder()
                        .reviewStatus(conversionReviewStatus(templateSmsReportResponse.getTemplate_status()))
                        .reviewReason(templateSmsReportResponse.getReason())
                        .templateCode(templateSmsReportResponse.getTemplate_code())
                        .build());
            }
            response.getWriter().print("{\"code\": 0,\"msg\": \"成功\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("===============短信模板审核状态消息回调结束==============");
    }

    /**
     * @return com.wanmi.sbc.message.bean.enums.ReviewStatus
     * @Author lvzhenwei
     * @Description 根据接口返回审核状态码转换审核状态
     * @Date 13:54 2019/12/10
     * @Param [template_status]
     **/
    private ReviewStatus conversionReviewStatus(String template_status) {
        ReviewStatus reviewStatus = ReviewStatus.REVIEWPASS;
        if (template_status.equals(AliyunReviewStatusConstant.PENDINGREVIEW)) {
            reviewStatus = ReviewStatus.PENDINGREVIEW;
        } else if (template_status.equals(AliyunReviewStatusConstant.REVIEWPASS)) {
            reviewStatus = ReviewStatus.REVIEWPASS;
        } else if (template_status.equals(AliyunReviewStatusConstant.REVIEWFAILED)) {
            reviewStatus = ReviewStatus.REVIEWFAILED;
        }
        return reviewStatus;
    }
}
