package com.fangdeng.server.util;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;



@Slf4j
public class XmlUtil {

    public static String pretty(String xml) {
        try {
            Document document = DocumentHelper.parseText(xml);
            // 格式化输出格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            StringWriter writer = new StringWriter();
            // 格式化输出流
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            // 将document写入到输出流
            xmlWriter.write(document);
            xmlWriter.close();
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Jaxb工具类 xml和java类相互转换
     *
     * @date 2017年4月17日
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * pojo转换成xml 默认编码UTF-8
     *
     * @param obj 待转化的对象
     * @return xml格式字符串
     * @throws Exception JAXBException
     */
    public static String convertToXml(Object obj) {
        try {
            return convertToXml(obj, DEFAULT_ENCODING);
        } catch (Exception e) {
            log.error("【convertToXml】 {}", e);
            return null;
        }

    }

    /**
     * pojo转换成xml
     *
     * @param obj      待转化的对象
     * @param encoding 编码
     * @return xml格式字符串
     * @throws Exception JAXBException
     */
    public static String convertToXml(Object obj, String encoding) throws Exception {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        // 指定是否使用换行和缩排对已编组 XML 数据进行格式化的属性名称。
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }

    /**
     * xml转换成JavaBean
     *
     * @param xml xml格式字符串
     * @param t   待转化的对象
     * @return 转化后的对象
     * @throws Exception JAXBException
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertToJavaBean(String xml, Class<T> t) throws Exception {
        JAXBContext context = JAXBContext.newInstance(t);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xml));
    }

    public static Map<String, String> parse(String soap) throws DocumentException {
        Map<String, String> map = new HashMap<>(6);
        //报文转成doc对象
        Document doc = DocumentHelper.parseText(soap);
        //获取根元素，准备递归解析这个XML树
        Element root = doc.getRootElement();
        getAttribute(root, map);
        return map;
    }

    public static void getAttribute(Element root, Map<String, String> map) {
        if (root.elements() != null) {
            //如果当前跟节点有子节点，找到子节点
            List<Element> list = root.elements();
            //遍历每个节点
            for (Element e : list) {
                if (e.elements().size() > 0) {
                    //当前节点不为空的话，递归遍历子节点；
                    getAttribute(e, map);
                }
                //如果为叶子节点，那么直接把名字和值放入map
                if (e.elements().size() == 0) {
                    map.put(e.getName(), e.getTextTrim());
                }
            }
        }
    }

}
