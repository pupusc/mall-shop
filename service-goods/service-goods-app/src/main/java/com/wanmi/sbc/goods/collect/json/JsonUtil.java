package com.wanmi.sbc.goods.collect.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * JsonUtil
 * @author chenzhen
 */
@Slf4j
public class JsonUtil {

    //递归查询,查询所有为key的value
    public static List<String> findJsonGetKey(String fullResponseJson, String key) {

        List<String> list = new ArrayList<>();

        findValueObjectGetKey(JSONObject.parseObject(fullResponseJson), key, list);

        return list;
    }

    //递归查询,查询所有为key的value
    public static List<Map> findJsonGetList(JSONObject jsonObject, String key) {

        List<Map> list = new ArrayList<>();

        findValueObjectGetList(jsonObject, key, list);

        return list;
    }


    //丰富
    public static void richJson(JSONObject jsonObject, String key, Map richMap) {

        List list = findJsonGetList(jsonObject,key);
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            String value = String.valueOf(map.get(key));

            Map findMap = (Map)richMap.get(value);
            if(findMap != null){

                map.put("sale_price",findMap.get("sale_price"));
                map.put("line_price",findMap.get("line_price"));
                map.put("goods_img",findMap.get("goods_img"));

            }
        }

    }

    /**
     * 从json中查找对象
     *
     * @param fullResponse json对象
     * @param key          json key
     */
    private static void findValueObjectGetKey(JSONObject fullResponse, String key,List list) {

        if (fullResponse == null) {
            return;
        }
        fullResponse.keySet().forEach(keyStr -> {
            Object keyvalue = fullResponse.get((String) keyStr);
            if (keyvalue instanceof JSONArray) {
                for (int i = 0; i < ((JSONArray) keyvalue).size(); i++) {
                    Object obj = ((JSONArray) keyvalue).get(i);
                    if (obj instanceof JSONObject) {
                        findValueObjectGetKey(((JSONObject) obj), key,list);
                    }
                }
            } else if (keyvalue instanceof JSONObject) {
                findValueObjectGetKey((JSONObject) keyvalue,key, list);
            } else {
                if (key.equals(keyStr)) {
                    list.add(keyvalue);
                }
            }
        });
    }


    /**
     * 从json中查找对象
     *
     * @param fullResponse json对象
     * @param key          json key
     */
    private static void findValueObjectGetList(JSONObject fullResponse, String key,List list) {

        if (fullResponse == null) {
            return;
        }
        fullResponse.keySet().forEach(keyStr -> {
            Object keyvalue = fullResponse.get((String) keyStr);
            if (keyvalue instanceof JSONArray) {
                for (int i = 0; i < ((JSONArray) keyvalue).size(); i++) {
                    Object obj = ((JSONArray) keyvalue).get(i);
                    if (obj instanceof JSONObject) {
                        findValueObjectGetList(((JSONObject) obj), key,list);
                    }
                }
            } else if (keyvalue instanceof JSONObject) {
                findValueObjectGetList((JSONObject) keyvalue,key, list);
            } else {
                if (key.equals(keyStr)) {
                    list.add(fullResponse);
                }
            }
        });
    }

    public static void main(String[] args) {
        //String json = "{    \"isBook\": \"yes\", \"sku_id\": \"111\",   \"list\": {        \"tags\": [            {                \"sku_id\": \"111\"            }        ]    },    \"tags\": [        {            \"sku_id\": \"222\",            \"order_type\": 10        }    ]}";
        String json = "{\"search\":[{\"name\":\"主标题1\",\"id\":101,\"sub_name\":\"副标题1\",\"list\":[{\"name\":\"关键词1\",\"sku_id\":\"2c90e85979fafe9f017b04cd42a5594d\"},{\"name\":\"关键词2\",\"id\":12173},{\"name\":\"关键词3\",\"id\":12174}]}],\"otherBook\":[{\"goods_name\":\"周期购无图片商品测试\",\"spu_no\":\"P017138043\",\"isbn\":\"9787508694672\",\"sku_id\":\"2c90c8647c6321d9017c7305b9ea0096\",\"book_id\":6997,\"book_name\":\"认知天性\",\"spu_id\":\"2c90c8647c6321d9017c7305b9bb0095\",\"tags\":{\"isBook\":\"yes\",\"tags\":[{\"id\":7820,\"name\":\"以色列耶路撒冷文学奖1\",\"orderType\":40,\"showName\":\"以色列耶路撒冷文学奖1(徐昕/张可)\",\"type\":1}]}}],\"salenum\":null,\"bookDetail\":[{\"medioRecomd\":[{\"descr\":\"获奖理由获奖理由获奖理由获奖理由获奖理由获奖理由获奖理由\",\"biz_type\":1,\"name\":\"奖项14\",\"id\":9408,\"book_id\":7838,\"biz_id\":63,\"job_title\":null},{\"descr\":\"获奖理由15\",\"biz_type\":1,\"name\":\"奖项11\",\"id\":9409,\"book_id\":7838,\"biz_id\":60,\"job_title\":null},{\"descr\":\"媒体媒体媒体媒体媒体媒体媒体媒体推荐语推荐语推荐语推荐语推荐语推荐语\",\"biz_type\":3,\"recomentBookBoList\":[{\"goodsInfoId\":\"2c9a00ca8679216b01867d55b5620110\",\"goodsId\":\"2c9a00c7866d2f14018671fa830c010b\",\"goodsInfoName\":\"21张华的书\",\"tagsDto\":{\"isBook\":\"yes\",\"tags\":[{\"id\":10505,\"name\":\"新品榜\",\"orderType\":20,\"showName\":\"新品榜第6名\",\"type\":1},{\"id\":66,\"name\":\"21诺贝尔文学奖\",\"orderType\":30,\"showName\":\"21诺贝尔文学奖\",\"type\":1},{\"id\":7890,\"name\":\"21诺贝尔文学奖\",\"orderType\":40,\"showName\":\"21诺贝尔文学奖(21张华)\",\"type\":1},{\"id\":7866,\"name\":\"媒体001\",\"orderType\":60,\"showName\":\"媒体001推荐\",\"type\":1},{\"id\":7891,\"name\":\"孟竹\",\"orderType\":70,\"showName\":\"孟竹\",\"type\":1},{\"id\":null,\"name\":\"1~100岁\",\"orderType\":80,\"showName\":\"1~100岁\",\"type\":1},{\"id\":1037,\"name\":\"3-6岁\",\"orderType\":90,\"showName\":\"3-6岁\",\"type\":1},{\"id\":1071,\"name\":\"21标签名称\",\"orderType\":90,\"showName\":\"21标签名称\",\"type\":1},{\"id\":1065,\"name\":\"1234\",\"orderType\":90,\"showName\":\"1234\",\"type\":1},{\"id\":27,\"name\":\"21丛书名称丛书名称\",\"orderType\":110,\"showName\":\"21丛书名称丛书名称\",\"type\":1}]}},{\"goodsInfoId\":\"2c9a00ce86248d5d01862591a683000f\",\"goodsId\":\"2c9a00ce86248d5d01862591a64e000e\",\"goodsInfoName\":\"发布打包商品\",\"tagsDto\":{\"isBook\":\"yes\",\"tags\":[{\"id\":1038,\"name\":\"大促标签\",\"orderType\":10,\"showName\":null,\"type\":1},{\"id\":10537,\"name\":\"小说榜\",\"orderType\":20,\"showName\":\"小说榜第9名\",\"type\":1},{\"id\":65,\"name\":\"t奖项名称\",\"orderType\":30,\"showName\":\"t奖项名称\",\"type\":1},{\"id\":7889,\"name\":\"奥数金靴奖\",\"orderType\":40,\"showName\":\"奥数金靴奖(张华)\",\"type\":1},{\"id\":7866,\"name\":\"媒体001\",\"orderType\":60,\"showName\":\"媒体001推荐\",\"type\":1},{\"id\":6777,\"name\":\"张克群\",\"orderType\":70,\"showName\":\"张克群\",\"type\":1},{\"id\":null,\"name\":\"1~99岁\",\"orderType\":80,\"showName\":\"1~99岁\",\"type\":1},{\"id\":26,\"name\":\"t丛书名称\",\"orderType\":110,\"showName\":\"t丛书名称\",\"type\":1}]}},{\"goodsInfoId\":\"2c90c8647cdf0f1f017cdf406a961380\",\"goodsId\":\"2c90c8647cdf0f1f017cdf406a7e137f\",\"goodsInfoName\":\"学前卫生学2(学前教育专业独立本科段2014年版全国高等教育自学考试指定教材)\",\"tagsDto\":{\"isBook\":\"yes\",\"tags\":[{\"id\":1038,\"name\":\"大促标签\",\"orderType\":10,\"showName\":null,\"type\":1},{\"id\":13,\"name\":\"奥数金靴奖\",\"orderType\":30,\"showName\":\"奥数金靴奖\",\"type\":1},{\"id\":7866,\"name\":\"媒体001\",\"orderType\":60,\"showName\":\"媒体001推荐\",\"type\":1},{\"id\":7828,\"name\":\"林耀华\",\"orderType\":70,\"showName\":\"林耀华\",\"type\":1},{\"id\":7856,\"name\":\"张四\",\"orderType\":70,\"showName\":\"张四\",\"type\":1},{\"id\":null,\"name\":\"12~100岁\",\"orderType\":80,\"showName\":\"12~100岁\",\"type\":1},{\"id\":1037,\"name\":\"3-6岁\",\"orderType\":90,\"showName\":\"3-6岁\",\"type\":1},{\"id\":17,\"name\":\"122\",\"orderType\":110,\"showName\":\"122\",\"type\":1}]}}],\"name\":\"媒体001\",\"id\":9411,\"book_id\":7838,\"biz_id\":7866,\"job_title\":null},{\"descr\":\"专业机构推荐专业机构推荐专业机构推荐专业机构推荐专业机构推荐专业机构推荐推荐语推荐语推荐语推荐语\",\"biz_type\":4,\"recomentBookBoList\":[{\"goodsInfoId\":\"2c9a00ca8679216b01867d55b5620110\",\"goodsId\":\"2c9a00c7866d2f14018671fa830c010b\",\"goodsInfoName\":\"21张华的书\",\"tagsDto\":{\"isBook\":\"yes\",\"tags\":[{\"id\":10505,\"name\":\"新品榜\",\"orderType\":20,\"showName\":\"新品榜第6名\",\"type\":1},{\"id\":66,\"name\":\"21诺贝尔文学奖\",\"orderType\":30,\"showName\":\"21诺贝尔文学奖\",\"type\":1},{\"id\":7890,\"name\":\"21诺贝尔文学奖\",\"orderType\":40,\"showName\":\"21诺贝尔文学奖(21张华)\",\"type\":1},{\"id\":7866,\"name\":\"媒体001\",\"orderType\":60,\"showName\":\"媒体001推荐\",\"type\":1},{\"id\":7891,\"name\":\"孟竹\",\"orderType\":70,\"showName\":\"孟竹\",\"type\":1},{\"id\":null,\"name\":\"1~100岁\",\"orderType\":80,\"showName\":\"1~100岁\",\"type\":1},{\"id\":1037,\"name\":\"3-6岁\",\"orderType\":90,\"showName\":\"3-6岁\",\"type\":1},{\"id\":1071,\"name\":\"21标签名称\",\"orderType\":90,\"showName\":\"21标签名称\",\"type\":1},{\"id\":1065,\"name\":\"1234\",\"orderType\":90,\"showName\":\"1234\",\"type\":1},{\"id\":27,\"name\":\"21丛书名称丛书名称\",\"orderType\":110,\"showName\":\"21丛书名称丛书名称\",\"type\":1}]}},{\"goodsInfoId\":\"2c9a00ce86248d5d01862591a683000f\",\"goodsId\":\"2c9a00ce86248d5d01862591a64e000e\",\"goodsInfoName\":\"发布打包商品\",\"tagsDto\":{\"isBook\":\"yes\",\"tags\":[{\"id\":1038,\"name\":\"大促标签\",\"orderType\":10,\"showName\":null,\"type\":1},{\"id\":10537,\"name\":\"小说榜\",\"orderType\":20,\"showName\":\"小说榜第9名\",\"type\":1},{\"id\":65,\"name\":\"t奖项名称\",\"orderType\":30,\"showName\":\"t奖项名称\",\"type\":1},{\"id\":7889,\"name\":\"奥数金靴奖\",\"orderType\":40,\"showName\":\"奥数金靴奖(张华)\",\"type\":1},{\"id\":7866,\"name\":\"媒体001\",\"orderType\":60,\"showName\":\"媒体001推荐\",\"type\":1},{\"id\":6777,\"name\":\"张克群\",\"orderType\":70,\"showName\":\"张克群\",\"type\":1},{\"id\":null,\"name\":\"1~99岁\",\"orderType\":80,\"showName\":\"1~99岁\",\"type\":1},{\"id\":26,\"name\":\"t丛书名称\",\"orderType\":110,\"showName\":\"t丛书名称\",\"type\":1}]}},{\"goodsInfoId\":\"2c90c8647cdf0f1f017cdf406a961380\",\"goodsId\":\"2c90c8647cdf0f1f017cdf406a7e137f\",\"goodsInfoName\":\"学前卫生学2(学前教育专业独立本科段2014年版全国高等教育自学考试指定教材)\",\"tagsDto\":{\"isBook\":\"yes\",\"tags\":[{\"id\":1038,\"name\":\"大促标签\",\"orderType\":10,\"showName\":null,\"type\":1},{\"id\":13,\"name\":\"奥数金靴奖\",\"orderType\":30,\"showName\":\"奥数金靴奖\",\"type\":1},{\"id\":7866,\"name\":\"媒体001\",\"orderType\":60,\"showName\":\"媒体001推荐\",\"type\":1},{\"id\":7828,\"name\":\"林耀华\",\"orderType\":70,\"showName\":\"林耀华\",\"type\":1},{\"id\":7856,\"name\":\"张四\",\"orderType\":70,\"showName\":\"张四\",\"type\":1},{\"id\":null,\"name\":\"12~100岁\",\"orderType\":80,\"showName\":\"12~100岁\",\"type\":1},{\"id\":1037,\"name\":\"3-6岁\",\"orderType\":90,\"showName\":\"3-6岁\",\"type\":1},{\"id\":17,\"name\":\"122\",\"orderType\":110,\"showName\":\"122\",\"type\":1}]}}],\"name\":\"机构001\",\"id\":9412,\"book_id\":7838,\"biz_id\":7872,\"job_title\":null},{\"descr\":\"名家推荐语名名家推荐语名家推荐语名家推荐语名家推荐语名家推荐语名家推荐语名家推荐语名家推荐语名家推荐语名家推荐语名家推荐语家推荐语\",\"biz_type\":5,\"recomentBookBoList\":[{\"goodsInfoId\":\"2c9a00ce86248d5d01862591a683000f\",\"goodsId\":\"2c9a00ce86248d5d01862591a64e000e\",\"goodsInfoName\":\"发布打包商品\",\"tagsDto\":{\"isBook\":\"yes\",\"tags\":[{\"id\":1038,\"name\":\"大促标签\",\"orderType\":10,\"showName\":null,\"type\":1},{\"id\":10537,\"name\":\"小说榜\",\"orderType\":20,\"showName\":\"小说榜第9名\",\"type\":1},{\"id\":65,\"name\":\"t奖项名称\",\"orderType\":30,\"showName\":\"t奖项名称\",\"type\":1},{\"id\":7889,\"name\":\"奥数金靴奖\",\"orderType\":40,\"showName\":\"奥数金靴奖(张华)\",\"type\":1},{\"id\":7866,\"name\":\"媒体001\",\"orderType\":60,\"showName\":\"媒体001推荐\",\"type\":1},{\"id\":6777,\"name\":\"张克群\",\"orderType\":70,\"showName\":\"张克群\",\"type\":1},{\"id\":null,\"name\":\"1~99岁\",\"orderType\":80,\"showName\":\"1~99岁\",\"type\":1},{\"id\":26,\"name\":\"t丛书名称\",\"orderType\":110,\"showName\":\"t丛书名称\",\"type\":1}]}},{\"goodsInfoId\":\"2c9a00ce86248d5d01862591a683000f\",\"goodsId\":\"2c9a00ce86248d5d01862591a64e000e\",\"goodsInfoName\":\"发布打包商品\",\"tagsDto\":{\"isBook\":\"yes\",\"tags\":[{\"id\":1038,\"name\":\"大促标签\",\"orderType\":10,\"showName\":null,\"type\":1},{\"id\":10537,\"name\":\"小说榜\",\"orderType\":20,\"showName\":\"小说榜第9名\",\"type\":1},{\"id\":65,\"name\":\"t奖项名称\",\"orderType\":30,\"showName\":\"t奖项名称\",\"type\":1},{\"id\":7889,\"name\":\"奥数金靴奖\",\"orderType\":40,\"showName\":\"奥数金靴奖(张华)\",\"type\":1},{\"id\":7866,\"name\":\"媒体001\",\"orderType\":60,\"showName\":\"媒体001推荐\",\"type\":1},{\"id\":6777,\"name\":\"张克群\",\"orderType\":70,\"showName\":\"张克群\",\"type\":1},{\"id\":null,\"name\":\"1~99岁\",\"orderType\":80,\"showName\":\"1~99岁\",\"type\":1},{\"id\":26,\"name\":\"t丛书名称\",\"orderType\":110,\"showName\":\"t丛书名称\",\"type\":1}]}}],\"name\":\"张华\",\"id\":9413,\"book_id\":7838,\"biz_id\":7889,\"job_title\":\"职称头衔C\"}]},{\"tab2\":{\"firstTranslator\":{\"name\":\"张立英\",\"id\":7755,\"Awards\":[],\"type\":1,\"Books\":[{\"goods_name\":\"学前卫生学2(学前教育专业独立本科段2014年版全国高等教育自学考试指定教材)\",\"spu_no\":\"P833113328\",\"isbn\":\"9787040412284\",\"sku_id\":\"2c90c8647cdf0f1f017cdf406a961380\",\"book_id\":7835,\"spu_id\":\"2c90c8647cdf0f1f017cdf406a7e137f\",\"tags\":{\"isBook\":\"yes\",\"tags\":[{\"id\":1038,\"name\":\"大促标签\",\"orderType\":10,\"showName\":null,\"type\":1},{\"id\":13,\"name\":\"奥数金靴奖\",\"orderType\":30,\"showName\":\"奥数金靴奖\",\"type\":1},{\"id\":7866,\"name\":\"媒体001\",\"orderType\":60,\"showName\":\"媒体001推荐\",\"type\":1},{\"id\":7828,\"name\":\"林耀华\",\"orderType\":70,\"showName\":\"林耀华\",\"type\":1},{\"id\":7856,\"name\":\"张四\",\"orderType\":70,\"showName\":\"张四\",\"type\":1},{\"id\":null,\"name\":\"12~100岁\",\"orderType\":80,\"showName\":\"12~100岁\",\"type\":1},{\"id\":1037,\"name\":\"3-6岁\",\"orderType\":90,\"showName\":\"3-6岁\",\"type\":1},{\"id\":17,\"name\":\"122\",\"orderType\":110,\"showName\":\"122\",\"type\":1}]}}],\"introduce\":\"作者简介张立英北京大学逻辑学专业博士，荷兰阿姆斯特丹大学、美国匹兹堡大学访问学者。现为中央财经大学教授，青年龙马学者，北京市逻辑学会副会长。常年从事逻辑学的教学科研工作。个人著作《概称句推理研究》曾获&ldquo;北京市第十四届哲学社会科学优秀成果奖&rdquo;，译著作品有《疯狂的罗素?逻辑学与数学的奇幻之旅》《这是事实吗?用批判性思维评估统计数据和科学信息》等。绘者简介机机先生创作人，国内一线漫画家，曾供职于奥美和陌陌科技等广告、互联网行业，资深美术视觉师。&ldquo;有机形象\\\"系列作者，自主表情IP曾火遍全网，用户端的累积传播和使用次数过亿。\"},\"character\":{\"name\":\"梁锡江/石见穿/龚艳\",\"id\":7727,\"type\":1,\"introduce\":\"介绍介绍介绍介绍介绍介绍介绍梁锡江/石见穿/龚艳，介绍介绍介绍介绍介绍介绍介绍介绍介绍介绍介绍介绍介绍介绍\"},\"library\":{\"name\":\"t丛书名称\",\"libraryNum\":[{\"num\":4}],\"id\":26,\"Books\":[{\"goods_name\":\"发布打包商品\",\"spu_no\":\"P665547865\",\"isbn\":\"ISBN_C_T002\",\"name\":\"图书名称C\",\"sku_id\":\"2c9a00ce86248d5d01862591a683000f\",\"id\":7836,\"spu_id\":\"2c9a00ce86248d5d01862591a64e000e\",\"tags\":{\"isBook\":\"yes\",\"tags\":[{\"id\":1038,\"name\":\"大促标签\",\"orderType\":10,\"showName\":null,\"type\":1},{\"id\":10537,\"name\":\"小说榜\",\"orderType\":20,\"showName\":\"小说榜第9名\",\"type\":1},{\"id\":65,\"name\":\"t奖项名称\",\"orderType\":30,\"showName\":\"t奖项名称\",\"type\":1},{\"id\":7889,\"name\":\"奥数金靴奖\",\"orderType\":40,\"showName\":\"奥数金靴奖(张华)\",\"type\":1},{\"id\":7866,\"name\":\"媒体001\",\"orderType\":60,\"showName\":\"媒体001推荐\",\"type\":1},{\"id\":6777,\"name\":\"张克群\",\"orderType\":70,\"showName\":\"张克群\",\"type\":1},{\"id\":null,\"name\":\"1~99岁\",\"orderType\":80,\"showName\":\"1~99岁\",\"type\":1},{\"id\":26,\"name\":\"t丛书名称\",\"orderType\":110,\"showName\":\"t丛书名称\",\"type\":1}]}}]},\"producer\":{\"name\":\"t出品方名称\",\"id\":29,\"Books\":[{\"goods_name\":\"学前卫生学2(学前教育专业独立本科段2014年版全国高等教育自学考试指定教材)\",\"spu_no\":\"P833113328\",\"isbn\":\"9787040412284\",\"name\":\"图书生命周期4\",\"sku_id\":\"2c90c8647cdf0f1f017cdf406a961380\",\"id\":7835,\"spu_id\":\"2c90c8647cdf0f1f017cdf406a7e137f\",\"tags\":{\"isBook\":\"yes\",\"tags\":[{\"id\":1038,\"name\":\"大促标签\",\"orderType\":10,\"showName\":null,\"type\":1},{\"id\":13,\"name\":\"奥数金靴奖\",\"orderType\":30,\"showName\":\"奥数金靴奖\",\"type\":1},{\"id\":7866,\"name\":\"媒体001\",\"orderType\":60,\"showName\":\"媒体001推荐\",\"type\":1},{\"id\":7828,\"name\":\"林耀华\",\"orderType\":70,\"showName\":\"林耀华\",\"type\":1},{\"id\":7856,\"name\":\"张四\",\"orderType\":70,\"showName\":\"张四\",\"type\":1},{\"id\":null,\"name\":\"12~100岁\",\"orderType\":80,\"showName\":\"12~100岁\",\"type\":1},{\"id\":1037,\"name\":\"3-6岁\",\"orderType\":90,\"showName\":\"3-6岁\",\"type\":1},{\"id\":17,\"name\":\"122\",\"orderType\":110,\"showName\":\"122\",\"type\":1}]}},{\"goods_name\":\"发布打包商品\",\"spu_no\":\"P665547865\",\"isbn\":\"ISBN_C_T002\",\"name\":\"图书名称C\",\"sku_id\":\"2c9a00ce86248d5d01862591a683000f\",\"id\":7836,\"spu_id\":\"2c9a00ce86248d5d01862591a64e000e\",\"tags\":{\"isBook\":\"yes\",\"tags\":[{\"id\":1038,\"name\":\"大促标签\",\"orderType\":10,\"showName\":null,\"type\":1},{\"id\":10537,\"name\":\"小说榜\",\"orderType\":20,\"showName\":\"小说榜第9名\",\"type\":1},{\"id\":65,\"name\":\"t奖项名称\",\"orderType\":30,\"showName\":\"t奖项名称\",\"type\":1},{\"id\":7889,\"name\":\"奥数金靴奖\",\"orderType\":40,\"showName\":\"奥数金靴奖(张华)\",\"type\":1},{\"id\":7866,\"name\":\"媒体001\",\"orderType\":60,\"showName\":\"媒体001推荐\",\"type\":1},{\"id\":6777,\"name\":\"张克群\",\"orderType\":70,\"showName\":\"张克群\",\"type\":1},{\"id\":null,\"name\":\"1~99岁\",\"orderType\":80,\"showName\":\"1~99岁\",\"type\":1},{\"id\":26,\"name\":\"t丛书名称\",\"orderType\":110,\"showName\":\"t丛书名称\",\"type\":1}]}}],\"producerNum\":[{\"num\":3}]},\"firstWriter\":{\"name\":\"张四\",\"id\":7856,\"Awards\":[{\"name\":\"t奖项名称\",\"id\":65},{\"name\":\"图书奖项名称\",\"id\":67}],\"type\":3,\"Books\":[{\"goods_name\":\"多商品购买\",\"spu_no\":\"P577718825\",\"isbn\":\"123\",\"sku_id\":\"2c90e85979fafe9f017b04cd42a5594d\",\"book_id\":7827,\"spu_id\":\"2c90c8647d2d3dce017d474002a000be\",\"tags\":{\"isBook\":\"yes\",\"tags\":[{\"id\":48,\"name\":\"奖项1\",\"orderType\":30,\"showName\":\"奖项1\",\"type\":1},{\"id\":7821,\"name\":\"行星小说奖\",\"orderType\":40,\"showName\":\"行星小说奖(于谦)\",\"type\":1},{\"id\":7875,\"name\":\"编辑001\",\"orderType\":60,\"showName\":\"编辑001推荐\",\"type\":1},{\"id\":7881,\"name\":\"名家003\",\"orderType\":70,\"showName\":\"名家003\",\"type\":1},{\"id\":7882,\"name\":\"名家4\",\"orderType\":70,\"showName\":\"名家4\",\"type\":1},{\"id\":7868,\"name\":\"作者0031\",\"orderType\":70,\"showName\":\"作者0031\",\"type\":1},{\"id\":7875,\"name\":\"编辑001\",\"orderType\":70,\"showName\":\"编辑001\",\"type\":1},{\"id\":789,\"name\":\"隔代教育\",\"orderType\":90,\"showName\":\"隔代教育\",\"type\":1}]}},{\"goods_name\":\"多商品购买\",\"spu_no\":\"P577718825\",\"isbn\":\"123\",\"sku_id\":\"2c90c8647d2d3dce017d474002c700bf\",\"book_id\":7827,\"spu_id\":\"2c90c8647d2d3dce017d474002a000be\",\"tags\":{\"isBook\":\"yes\",\"tags\":[{\"id\":48,\"name\":\"奖项1\",\"orderType\":30,\"showName\":\"奖项1\",\"type\":1},{\"id\":7821,\"name\":\"行星小说奖\",\"orderType\":40,\"showName\":\"行星小说奖(于谦)\",\"type\":1},{\"id\":7875,\"name\":\"编辑001\",\"orderType\":60,\"showName\":\"编辑001推荐\",\"type\":1},{\"id\":7881,\"name\":\"名家003\",\"orderType\":70,\"showName\":\"名家003\",\"type\":1},{\"id\":7882,\"name\":\"名家4\",\"orderType\":70,\"showName\":\"名家4\",\"type\":1},{\"id\":7868,\"name\":\"作者0031\",\"orderType\":70,\"showName\":\"作者0031\",\"type\":1},{\"id\":7875,\"name\":\"编辑001\",\"orderType\":70,\"showName\":\"编辑001\",\"type\":1},{\"id\":789,\"name\":\"隔代教育\",\"orderType\":90,\"showName\":\"隔代教育\",\"type\":1}]}}],\"introduce\":\"简介11......\"},\"content\":{\"type\":1,\"content\":\"简介1111\"},\"goodsDetail\":\"<p><span style=\\\"color: rgb(245, 108, 29); font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;; font-size: 14px; background-color: rgba(245, 108, 29, 0.06);\\\">商品详情介绍<span style=\\\"color: rgb(245, 108, 29); font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;; font-size: 14px; background-color: rgba(245, 108, 29, 0.06);\\\">商品详情介绍</span><span style=\\\"color: rgb(245, 108, 29); font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;; font-size: 14px; background-color: rgba(245, 108, 29, 0.06);\\\">商品详情介绍</span><span style=\\\"color: rgb(245, 108, 29); font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;; font-size: 14px; background-color: rgba(245, 108, 29, 0.06);\\\">商品详情介绍<img src=\\\"https://shangcheng-resource.oss-cn-hangzhou.aliyuncs.com/b343305db480ac95937ae48a29af61f2.jpg\\\" title=\\\"\\\"alt=\\\"undefined/\\\"/><imgsrc=\\\"https: //shangcheng-resource.oss-cn-hangzhou.aliyuncs.com/045e629d1cc9a14a666890887a8362fa.jpg\\\"title=\\\"\\\" alt=\\\"undefined/\\\"/><img src=\\\"https://shangcheng-resource.oss-cn-hangzhou.aliyuncs.com/82343921488afd783ec92bf721361ad7.jpg\\\" title=\\\"\\\"alt=\\\"undefined/\\\"/><imgsrc=\\\"https: //shangcheng-resource.oss-cn-hangzhou.aliyuncs.com/18540cab3e9c7a563f7451d622174558.jpeg\\\"title=\\\"\\\" alt=\\\"undefined/\\\"/></span><span style=\\\"color: rgb(245, 108, 29); font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;; font-size: 14px; background-color: rgba(245, 108, 29, 0.06);\\\">商品详情介绍</span><span style=\\\"color: rgb(245, 108, 29); font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;; font-size: 14px; background-color: rgba(245, 108, 29, 0.06);\\\">商品详情介绍</span><span style=\\\"color: rgb(245, 108, 29); font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;; font-size: 14px; background-color: rgba(245, 108, 29, 0.06);\\\">商品详情介绍</span></span></p>\"}}]}";
        JSONObject jsonObject = JSONObject.parseObject(json);

        //输出测试
        String jsonString1 = JSONObject.toJSONString(jsonObject);
        System.out.println(jsonString1);

        List list = JsonUtil.findJsonGetKey(json,"sku_id");
        System.out.println(list);

        /**begin 封装goods**/
        Map goodMap1 = new HashMap();
        goodMap1.put("sale_price","111");
        goodMap1.put("line_price","55");
        goodMap1.put("goods_img","http://xxx1.img");


        Map goodMap2 = new HashMap();
        goodMap2.put("sale_price","100");
        goodMap2.put("line_price","50");
        goodMap2.put("goods_img","http://xxx2.img");

        Map goodMap3 = new HashMap();
        goodMap3.put("sale_price","100");
        goodMap3.put("line_price","50");
        goodMap3.put("goods_img","http://xxx3.img");


        Map map = new HashMap();
        map.put("111",goodMap1);
        map.put("2c90e85979fafe9f017b04cd42a5594d",goodMap2);
        map.put("333",goodMap2);
        /**end   封装goods**/
        System.out.println(map);

        JsonUtil.richJson(jsonObject,"sku_id",map);

        String jsonString2 = JSONObject.toJSONString(jsonObject);
        System.out.println(jsonString2);
    }

}

