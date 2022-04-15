package com.wanmi.sbc.callback.parser;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.callback.encrypt.WXBizMsgCrypt;
import com.wanmi.sbc.callback.handler.CallbackHandler;
import com.wanmi.sbc.common.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

@Slf4j
@Component
@RefreshScope
public class WxAuditCallbackParser implements CommandLineRunner {

    @Value("${wx.mini.appid:}")
    private String wxAppid;
    @Value("${wx.mini.callback.token:}")
    private String wxCallbackToken;
    @Value("${wx.mini.callback.aes.key:}")
    private String wxCallbackAesKey;

    private static Collection<CallbackHandler> handlers;
    DocumentBuilderFactory dbf;
    WXBizMsgCrypt pc;

    public String dealCallback(String encryptStr, String timestamp, String nonce, String msgSignature) throws Exception {
        String decrypt = decrypt(encryptStr, timestamp, nonce, msgSignature);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decrypt.getBytes());
        Map<String, Object> paramMap = parseXML(inputStream);
        log.info("WxAuditCallBackParser dealCallback paramMap: {}", paramMap);
        String result = "success";
        for (CallbackHandler handler : handlers) {
            //消息服务
            if (handler.support((String) paramMap.get("MsgType"))){
                result = handler.handle(paramMap);
                break;
            }
            if(handler.support((String) paramMap.get("Event"))){
                handler.handle(paramMap);
                break;
            }
        }
        return result;
    }

    private Map<String, Object> parseXML(InputStream inputStream) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        Iterator<Element> it = rootElement.elementIterator();
        Map<String, Object> paramMap = new HashMap<>();
        while (it.hasNext()){
            Element next = it.next();
            if(next.hasMixedContent()){
                Map<String, Object> paramMap2 = new HashMap<>();
                Iterator<Element> it2 = next.elementIterator();
                while (it2.hasNext()){
                    Element next2 = it2.next();
                    paramMap2.put(next2.getName(), next2.getText());
                }
                paramMap.put(next.getName(), paramMap2);
            }else{
                paramMap.put(next.getName(), next.getText());
            }
        }
        return paramMap;
    }

    public String decrypt(String encryptStr, String timestamp, String nonce, String msgSignature) throws Exception {

        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(encryptStr);
        InputSource is = new InputSource(sr);
        org.w3c.dom.Document document = db.parse(is);

        org.w3c.dom.Element root = document.getDocumentElement();
        NodeList nodelist1 = root.getElementsByTagName("Encrypt");

        String encrypt = nodelist1.item(0).getTextContent();

        String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
        String fromXML = String.format(format, encrypt);

        String xml = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
        log.info("解密后内容: {}", xml);
        return xml;
    }

    @Override
    public void run(String... args) throws Exception {
        handlers = SpringContextHolder.getApplicationContext().getBeansOfType(CallbackHandler.class).values();

        if(StringUtils.isNotEmpty(wxCallbackToken)){
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);

            pc = new WXBizMsgCrypt(wxCallbackToken, wxCallbackAesKey, wxAppid);
            log.info("微信回调配置加载完成");
        }
    }
}
