package com.ofpay.rex;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.ofpay.rex.control.HardControlHelper;
import com.ofpay.rex.control.HardVO;
import com.ofpay.rex.control.helper.RSAUtil;
import com.ofpay.rex.security.validation.ValidationPattern;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.security.MessageDigest;

/**
 * Created by chengyong on 17/8/10.
 */

public class Test {

    public static void main(String args[]) {

//        int sum = 1;
//
//        String str = "8f56969152558a1da191034db3b70f20d5cdc17d93845e94600df90852c31c468b3eacedd3726ee15a36307eafec4f93f5ee848ab9ba56d1284c803a4096e900";
//        //String str = "d6ad97646009f51c797db6738d478eb3c99a8887aa83d81e2301bc48985d5486f432781d85bfece39c05fcc8aef5b3de396fb562afeb5329888884cf345b12ad";
//
//        while (sum > 0) {
//            try {
//                sum--;
//                byte[] input = Hex.decode(str);
//                String sv = RSAUtil.decryptByPrivateKey(input);
//                System.out.println("sv:" + sv.replace("1214cc807a744d1db35f323cc3d48152", ""));
//                Thread.sleep(100);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        try {
//            byte[] input = Hex.decode("8f56969152558a1da191034db3b70f20d5cdc17d93845e94600df90852c31c468b3eacedd3726ee15a36307eafec4f93f5ee848ab9ba56d1284c803a4096e900");
//            String ret = RSAUtil.decryptByPrivateKey(input);
//            System.out.print("ret:" + ret);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String ss = "AAD3F45459EC544FED81761E503167FB37EBE63CBEC046744AC0A4DEF6D2B7679263175E4B79B8DAFF51ED1FB8C3773D0914DE07A247918088FB632054D589AC5DD82F5D680DAC1B8DB081BAD5A91D61268E347267B3884F22C8B5DDF8FD456C96CB3C07AE554783D85A43F05C1DF725AB3DCF414DCBF769164D81DB0E6A8232";
//
//        HardVO hv = HardControlHelper.decodeHardInfo(ss);
//        System.out.println("=======================");
//
//
//        System.out.println(hv.getBiosId());
//        System.out.println(hv.getCpuId());
//        System.out.println(hv.getDiskId());
//        System.out.println(hv.getMac());
//        System.out.println(hv.getMainboard());
//        System.out.println(hv.getSerialNumber());
//
//        String jsonstr = "{\"id\":\"600521\",\n" +
//                "\"goodsListJson\":\"[{\\\"barCode\\\":\\\"<script>\\\",\\\"minOrderQuantity\\\":1,\\\"price\\\":19}]\",\n" +
//                "\"channel\":{\"test\":\"<script>\"}}\n";

//        String jstr2 = "{\"name\":\"yong\"}";
//
//        String str1 = jsonstr;
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
//        try {
//
//            if (str1.startsWith("[")) {
//                List<Object> beanList = objectMapper.readValue(str1, new TypeReference<List<Object>>() {
//                });
//                for (Object item : (List) beanList) {
//
//
//                    if (item instanceof List) {
//                        System.out.println("list:"+item);
//                    } else if (item instanceof Map) {
//                        System.out.println("map:"+item);
//                    } else {
//                        System.out.println("string:"+item);
//                    }
//
//                    System.out.println("obj:" +item.toString() );
//
//                }
//            } else {
//                Map<Object, Object> jsonMap = objectMapper.readValue(str1, new TypeReference<Map<Object, Object>>() {
//                });
//                for (Map.Entry<Object, Object> entry : jsonMap.entrySet()) {
//                    String key = String.valueOf(entry.getKey());
//                    Object value = entry.getValue();
//
//                    System.out.println("key:" + key + "  value:" + value);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        String[] excludeFields = {"attributes"};
        String[] rtfNames = {};
//
//
        String bodyData = "{\"attributes\":[{\"groupName\":\"阿斯顿发\",\"paramList\":[{\"name\":\"阿斯顿发\",\"value\":\"1111\"}]}]}";
        System.out.println("bodyData1:" + bodyData);


        try {
            if (StringUtils.isNotBlank(bodyData)) {
                if (bodyData.startsWith("[")) {
                    List<Object> beanList = objectMapper.readValue(bodyData, new TypeReference<List<Object>>() {
                    });
                    beanList = ValidationPattern.stripJsonList(beanList, excludeFields, rtfNames, objectMapper);
                    bodyData = objectMapper.writeValueAsString(beanList);
                } else {
                    Map<Object, Object> jsonMap = objectMapper.readValue(bodyData, new TypeReference<Map<Object, Object>>() {
                    });
                    jsonMap = ValidationPattern.stripJsonMap(jsonMap, excludeFields, rtfNames, objectMapper);
                    bodyData = objectMapper.writeValueAsString(jsonMap);
                }
            }
            System.out.println("bodyData2:" + bodyData);

        } catch (Exception e) {
            e.printStackTrace();

        }


        String cont = "<iframe src=\"https://pic.qianmi.com/getcookie/getcookie.html.html?t=1511933748287\" id=\"tvp_login_cross_domin_frame\" style=\"display: none;\"></iframe>" +

                "<a href=\"http://qianmi.com/sdf\" target=\"_top\">啊啊</a>\n";

        String ret = ValidationPattern.rtfXSS(cont);

        System.out.println(ret);

        String sss="13702084757" + "bfff699ee66c0a2f97af1500beb9c1dc8997052b" + "461147" + "223.104.7.56";


        String tt = DigestUtils.md5Hex(sss);

        System.out.println(tt);


        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(sss.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            tt= new BigInteger(1, md.digest()).toString(16);

            System.out.println(tt);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
