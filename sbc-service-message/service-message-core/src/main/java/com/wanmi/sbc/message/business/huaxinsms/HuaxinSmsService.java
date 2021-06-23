package com.wanmi.sbc.message.business.huaxinsms;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.message.bean.enums.SmsType;
import com.wanmi.sbc.setting.api.provider.syssms.SysSmsQueryProvider;
import com.wanmi.sbc.setting.api.response.syssms.SmsSupplierRopResponse;
import com.wanmi.sbc.message.SmsBaseResponse;
import com.wanmi.sbc.message.SmsBaseService;
import com.wanmi.sbc.message.bean.constant.SmsResponseCode;
import com.wanmi.sbc.message.bean.dto.SmsTemplateParamDTO;
import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import com.wanmi.sbc.message.smssign.model.root.SmsSign;
import com.wanmi.sbc.message.smstemplate.model.root.SmsTemplate;
import com.wanmi.sbc.message.smstemplate.repository.SmsTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @ClassName HuaxinSmsService
 * @Description 华信短信平台接口调用
 * @Author lvzhenwei
 * @Date 2019/12/4 10:01
 **/
@Service("huaxinSmsService")
@Slf4j
public class HuaxinSmsService implements SmsBaseService {

    @Autowired
    private SysSmsQueryProvider sysSmsQueryProvider;

    @Autowired
    private SmsTemplateRepository smsTemplateRepository;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    /**
     * @return com.aliyuncs.CommonResponse
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--新增短信签名接口
     * @Date 10:51 2019/12/4
     * @Param [smsSignQueryRequest]
     **/
    @Override
    public SmsBaseResponse addSmsSign(SmsSign smsSign) {
        return success();
    }

    /**
     * @return com.aliyuncs.CommonResponse
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--编辑短信签名接口
     * @Date 10:51 2019/12/4
     * @Param [smsSignQueryRequest]
     **/
    @Override
    public SmsBaseResponse modifySmsSign(SmsSign smsSign) {
        return success();
    }

    /**
     * @return com.aliyuncs.CommonResponse
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--删除短信签名接口
     * @Date 10:51 2019/12/4
     * @Param [smsSignQueryRequest]
     **/
    @Override
    public SmsBaseResponse deleteSmsSign(SmsSign smsSign) {
        return success();
    }

    /**
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--查询短信签名接口
     * @Date 19:31 2019/12/9
     * @Param [smsSign]
     * @return com.wanmi.sbc.message.SmsBaseResponse
     **/
    @Override
    public SmsBaseResponse querySmsSign(SmsSign smsSign){
        return error();
    }

    /**
     * @return com.aliyuncs.CommonResponse
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--新增短信模板接口
     * @Date 10:51 2019/12/4
     * @Param [smsTemplateByIdRequest]
     **/
    @Override
    public SmsBaseResponse addSmsTemplate(SmsTemplate smsTemplate) {
        return success();
    }

    /**
     * @return com.aliyuncs.CommonResponse
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--新增短信模板接口
     * @Date 10:51 2019/12/4
     * @Param [smsTemplateByIdRequest]
     **/
    @Override
    public SmsBaseResponse modifySmsTemplate(SmsTemplate smsTemplate) {
        return success();
    }

    /**
     * @return com.aliyuncs.CommonResponse
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--删除短信模板接口
     * @Date 10:51 2019/12/4
     * @Param [smsTemplateByIdRequest]
     **/
    @Override
    public SmsBaseResponse deleteSmsTemplate(SmsTemplate smsTemplate) {
        return success();
    }

    /**
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--查询短信签名接口
     * @Date 19:31 2019/12/9
     * @Param [smsSign]
     * @return com.wanmi.sbc.message.SmsBaseResponse
     **/
    @Override
    public SmsBaseResponse querySmsTemplate(SmsTemplate smsTemplate){
        return error();
    }

    /**
     * 短信发送接口
     * @param smsSendDetail
     * @return
     */
    @Override
    public SmsBaseResponse sendSms(SmsSendDetail smsSendDetail){
        List<SmsSupplierRopResponse> smsSupplierRopResponses = sysSmsQueryProvider.list().getContext().getSmsSupplierRopResponses();
        if(CollectionUtils.isEmpty(smsSupplierRopResponses)
                || smsSupplierRopResponses.stream().filter(s -> Constants.yes.toString().equals(s.getStatus())).count() < 1){
            log.error("没有配置华信配置sys_sms");
            return error();
        }

        SmsTemplate smsTemplate = smsTemplateRepository.findByBusinessTypeAndDelFlag(smsSendDetail.getBusinessType(), DeleteFlag.NO);
        if(Objects.isNull(smsTemplate)){
            log.error("找不到业务相关的短信模板".concat(smsSendDetail.getBusinessType()));
            return error();
        }

        //通知类短信验证开关状态
        if (SmsType.NOTICE.equals(smsTemplate.getTemplateType())
                && (Objects.isNull(smsTemplate.getOpenFlag()) || Boolean.FALSE.equals(smsTemplate.getOpenFlag()))) {
            log.error("短信模板被关闭".concat(smsSendDetail.getBusinessType()));
            return success();
        }

        SmsSupplierRopResponse smsSupplier = smsSupplierRopResponses.stream().filter(s -> Constants.yes.toString().equals(s.getStatus())).findFirst().get();
        long begin = System.currentTimeMillis();
        // 封装参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("action", "send");
        paramMap.put("account", smsSupplier.getAccount());
        paramMap.put("password", smsSupplier.getPassword());
        paramMap.put("mobile", smsSendDetail.getPhoneNumbers());
        String templateContent = smsTemplate.getTemplateContent();
        if(StringUtils.isNotBlank(smsSendDetail.getTemplateParam())) {
            SmsTemplateParamDTO param = JSON.parseObject(smsSendDetail.getTemplateParam(), SmsTemplateParamDTO.class);
            templateContent = templateContent.replaceAll("\\$\\{code\\}", Objects.toString(param.getCode(), StringUtils.EMPTY))
                    .replaceAll("\\$\\{account\\}", Objects.toString(param.getAccount(), StringUtils.EMPTY))
                    .replaceAll("\\$\\{password\\}", Objects.toString(param.getPassword(), StringUtils.EMPTY))
                    .replaceAll("\\$\\{name\\}", Objects.toString(param.getName(), StringUtils.EMPTY))
                    .replaceAll("\\$\\{number\\}", Objects.toString(param.getNumber(), StringUtils.EMPTY))
                    .replaceAll("\\$\\{remark\\}", Objects.toString(param.getRemark(), StringUtils.EMPTY))
                    .replaceAll("\\$\\{product\\}", Objects.toString(param.getProduct(), StringUtils.EMPTY))
                    .replaceAll("\\$\\{money\\}", Objects.toString(param.getMoney(), StringUtils.EMPTY))
                    .replaceAll("\\$\\{price\\}", Objects.toString(param.getPrice(), StringUtils.EMPTY));
        }
        String[] template = smsSupplier.getTemplate().split("\\|");
        String smsSuffix = "";
        if (template.length == 2) {
            smsSuffix = template[1];
        }

        String smsContent = templateContent.concat(smsSuffix);
        paramMap.put("content", smsContent);
        paramMap.put("sendTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        String result = null;
        try {
            result = restTemplateBuilder.build().getForObject(smsSupplier.getInterfaceUrl()
                            + "?action={action}&userid=&account={account}&password={password}&mobile={mobile}&content={content}&sendTime={sendTime}",
                    String.class, paramMap);
            return pasreResult(result);
        } catch (Exception ex) {
            log.warn("发送短信失败, paramMap:{}, exMsg:{}; costTime:{}", paramMap, ex.getMessage(), (System.currentTimeMillis() - begin));
        } finally {
            log.info("发送短信结束; paramMap:{}; result:{}; costTime:{}", paramMap, result, (System.currentTimeMillis() - begin));
        }
        return success();
    }

    private SmsBaseResponse error(){
        return SmsBaseResponse.builder().Code("error").build();
    }

    private SmsBaseResponse error(String msg){
        return SmsBaseResponse.builder().Code("error").Message(msg).build();
    }

    private SmsBaseResponse success(){
        return SmsBaseResponse.builder().Code(SmsResponseCode.SUCCESS).build();
    }

    /**
     * 解析xml字符串
     */
    private SmsBaseResponse pasreResult(String xml) {
        Document doc;
        try {
            // 将字符转化为XML
            doc = DocumentHelper.parseText(xml);
            // 获取根节点
            Element rootElt = doc.getRootElement();
            // 获取根节点下的子节点的值
            String returnstatus = rootElt.elementText("returnstatus").trim();
            String message = rootElt.elementText("message").trim();
            if("Success".equals(returnstatus)){
                return success();
            }
            log.error("短信发送失败，失败原因：", message);
            return error(message);
        } catch (DocumentException e) {
            log.error("解析xml字符串异常", e);
            return error();
        }
    }
}
