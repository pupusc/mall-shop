package com.wanmi.sbc.goods.mini.wx.callback.parser;

import com.wanmi.sbc.common.util.SpringContextHolder;
import com.wanmi.sbc.goods.mini.wx.callback.handler.CallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.*;


/*<ToUserName>gh_abcdefg</ToUserName>
<FromUserName>oABCD</FromUserName>
<CreateTime>12344555555</CreateTime>
<MsgType>event</MsgType>
<Event>open_product_spu_audit</Event>
<OpenProductSpuAudit>
<out_product_id>spu_123</out_product_id>
<product_id>38249023</product_id>
<status>3</status>
<reject_reason>xxx原因</reject_reason>
</OpenProductSpuAudit>*/

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
            log.error("解析微信审核回调失败:");
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
