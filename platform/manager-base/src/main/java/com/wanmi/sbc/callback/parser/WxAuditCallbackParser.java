package com.wanmi.sbc.callback.parser;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.callback.handler.CallbackHandler;
import com.wanmi.sbc.common.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.*;

@Slf4j
@Component
public class WxAuditCallbackParser implements CommandLineRunner {

    private static final byte[] rootPrefix = "<root>".getBytes();
    private static final byte[] rootSuffix = "</root>".getBytes();
    private static Collection<CallbackHandler> handlers;

    public String dealCallback(InputStream inputStream) {
        try {
            Map<String, Object> paramMap = parseXML(inputStream);
            for (CallbackHandler handler : handlers) {
                if(handler.support((String) paramMap.get("Event"))){
                    handler.handle(paramMap);
                    break;
                }
            }
        }catch (Exception e){
            try {
                inputStream.reset();
                String s = IOUtils.toString(inputStream);
                log.error("解析微信审核回调失败:" + s, e);
            } catch (IOException ioException) {
                log.error("解析微信审核回调失败!", e);
            }
            return "fail";
        }
        return "success";
    }

    private Map<String, Object> parseXML(InputStream inputStream) throws DocumentException {
//        Enumeration<InputStream> streams = Collections.enumeration(Arrays.asList(new ByteArrayInputStream(rootPrefix),
//                inputStream, new ByteArrayInputStream(rootSuffix)));
//        SequenceInputStream in = new SequenceInputStream(streams);
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
        log.info("wx callback params: {}", JSONObject.toJSONString(paramMap));
        return paramMap;
    }


    @Override
    public void run(String... args) {
        handlers = SpringContextHolder.getApplicationContext().getBeansOfType(CallbackHandler.class).values();
    }
}
