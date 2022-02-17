package com.wanmi.sbc.goods.mini.wx.callback.parser;

import com.wanmi.sbc.common.util.SpringContextHolder;
import com.wanmi.sbc.goods.mini.wx.callback.handler.CallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.*;

@Slf4j
@Component
public class WxAuditCallbackParser {

    private static final byte[] rootPrefix = "<root>".getBytes();
    private static final byte[] rootSuffix = "</root>".getBytes();
    private static final Collection<CallbackHandler> handlers = SpringContextHolder.getApplicationContext().getBeansOfType(CallbackHandler.class).values();

    public void dealCallback(InputStream inputStream) {
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
                String s = IOUtils.toString(inputStream);
                log.error("解析微信审核回调失败:" + s, e);
            } catch (IOException ioException) {
                log.error("解析微信审核回调失败!", e);
            }
        }
    }

    private Map<String, Object> parseXML(InputStream inputStream) throws DocumentException {
        Enumeration<InputStream> streams = Collections.enumeration(Arrays.asList(new ByteArrayInputStream(rootPrefix),
                inputStream, new ByteArrayInputStream(rootSuffix)));
        SequenceInputStream in = new SequenceInputStream(streams);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(in);
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
}
