package com.fangdeng.server.client;

import com.alibaba.fastjson.*;
import com.fangdeng.server.client.request.bookuu.*;
import com.fangdeng.server.client.response.bookuu.*;
import com.fangdeng.server.util.OkHttpUtil;
import com.fangdeng.server.util.XmlUtil;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 博库借口
 */
@Slf4j
@Service
public class BookuuClient {

    /**
     * 博库合作伙伴ID
     */
    //@Value("${bookuu.channelId}")
    private String channelID = "4005421";//"4004061";

    /**
     * 接口地址
     */
    //@Value("${bookuu.url}")
    private String path = "http://api.bookuu.com/open/external.php?type=%s&sign=f54a9e518762dafdbc816d6fb4d35654";//"http://api.bookuu.com/open_test/external.php?type=%s&sign=6aa51007f3fa72a6c9d3308f6d1f0e6d";


    /**
     * ERP商品基础信息查询接口
     *
     * @param request
     * @return
     */
    public BookuuGoodsQueryResponse getGoodsList(BookuuGoodsQueryRequest request) {

        request.setChannelID(channelID);
        request.setTimeStamp(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));

        String url = String.format(path, "product");

        String result = null;
        BookuuGoodsQueryResponse response = new BookuuGoodsQueryResponse();
        try {
            result = OkHttpUtil.postXml(url, XmlUtil.convertToXml(request));
            log.info("get bookuu goods info request:{}, response: {}", JSONObject.toJSONString(request), result);
        } catch (Exception e) {
            log.warn("get bookuu goods info error,request:{},error:{}", request, e.getMessage());
            response.setFlag(1);
            return response;
        }
        try {
            response = XmlUtil.convertToJavaBean(result, BookuuGoodsQueryResponse.class);
        } catch (Exception e) {
            log.warn("getGoodsList xml to bean error,request:{}",request,e);
            response.setFlag(1);
        }
        return response;
    }

    /**
     * 库存信息查询
     *
     * @param request
     * @return
     */
    public BookuuStockQueryResponse queryStock(BookuuStockQueryRequest request) {

        request.setChannelID(channelID);
        request.setTimeStamp(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));

        String url = String.format(path, "maxstock");

        String result = null;
        try {
            result = OkHttpUtil.postXml(url, XmlUtil.convertToXml(request));
            log.info("get bookuu stock  request:{}, response: {}", JSONObject.toJSONString(request), result);
        } catch (Exception e) {
            log.warn("get bookuu stock error,request:{},error:{}", request, e.getMessage());
        }
        BookuuStockQueryResponse response = null;
        try {
            response = XmlUtil.convertToJavaBean(result, BookuuStockQueryResponse.class);
        } catch (Exception e) {
            log.warn("get bookuu stock reponse error", e);
        }
        return response;
    }


    /**
     * 价格变动情况查询
     *
     * @param request
     * @return
     */
    public BookuuPriceChangeResponse queryPriceChange(BookuuPriceChangeRequest request) {

        request.setChannelID(channelID);
        request.setTimeStamp(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));

        String url = String.format(path, "pricechange");

        String result = null;
        try {
            result = OkHttpUtil.postXml(url, XmlUtil.convertToXml(request));
            log.info("get bookuu price change  request:{}, response: {}", JSONObject.toJSONString(request), result);
        } catch (Exception e) {
            log.warn("get bookuu price change error,request:{},error:{}", request, e.getMessage());
        }
        BookuuPriceChangeResponse response = null;
        try {
            response = XmlUtil.convertToJavaBean(result, BookuuPriceChangeResponse.class);
        } catch (Exception e) {

        }
        return response;
    }

    /**
     * 价格查询
     *
     * @param request
     * @return
     */
    public BookuuPriceQueryResponse queryPrice(BookuuPriceQueryRequest request) {

        request.setChannelID(channelID);
        request.setTimeStamp(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));

        String url = String.format(path, "price");

        String result = null;
        try {
            result = OkHttpUtil.postXml(url, XmlUtil.convertToXml(request));
            log.info("get bookuu price   request:{}, response: {}", JSONObject.toJSONString(request), result);
        } catch (Exception e) {
            log.warn("get bookuu price  error,request:{},error:{}", request, e.getMessage());
        }
        BookuuPriceQueryResponse response = null;
        try {
            response = XmlUtil.convertToJavaBean(result, BookuuPriceQueryResponse.class);
        } catch (Exception e) {

        }
        return response;
    }

    /**
     * 提交订单接口
     *
     * @param request
     * @return
     */
    public BookuuOrderAddResponse addOrder(BookuuOrderAddRequest request) {

        request.setChannelID(channelID);
        request.setTimeStamp(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));

        String url = String.format(path, "addorder");
        String result = null;
        try {
            result = OkHttpUtil.postXml(url, XmlUtil.convertToXml(request));
            log.info("bookuu add order request:{}, response: {}", JSONObject.toJSONString(request), result);
        } catch (Exception e) {
            log.warn("bookuu add order error,request:{},error:{}", request, e.getMessage());
        }
        BookuuOrderAddResponse response = null;
        try {
            response = XmlUtil.convertToJavaBean(result, BookuuOrderAddResponse.class);
        } catch (Exception e) {
            log.warn("book add order xml to response error,result:{}",result,e);
        }
        if(response == null){
            response = new BookuuOrderAddResponse();
            response.setStatus(1);
            response.setStatusDesc(StringUtils.isNotEmpty(result)?result:"add order fail:result is empty");
        }
        return response;
    }


    /**
     * 物流状态查询接口
     *
     * @param request
     * @return
     */
    public BookuuOrderStatusQueryResponse queryOrderStatus(BookuuOrderStatusQueryRequest request) {
        request.setChannelID(channelID);
        request.setTimeStamp(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));

        String url = String.format(path, "status");
        String result = null;
        try {
            result = OkHttpUtil.postXml(url, XmlUtil.convertToXml(request));
            log.info("bookuu order status request:{}, response: {}", JSONObject.toJSONString(request), result);
        } catch (Exception e) {
            log.warn("bookuu order status error,request:{},error:{}", request, e.getMessage());
        }
        BookuuOrderStatusQueryResponse response = null;
        try {
             response = XmlUtil.convertToJavaBean(result, BookuuOrderStatusQueryResponse.class);
        } catch (Exception e) {
            log.warn("bookuu order status convert error,request:{},error",request,e);
        }
        log.info("bookuu order status request last:{}, response: {}",JSONObject.toJSONString(request),JSONObject.toJSONString(response));
        return response;
    }

    /**
     * 订单取消接口
     *
     * @param request
     * @return
     */
    public BookuuOrderCancelResponse cancelOrder(BookuuOrderCancelRequest request) {
        request.setChannelID(channelID);
        request.setTimeStamp(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));
        request.setType(2);

        String url = String.format(path, "ordercancel");
        String result = null;
        try {
            result = OkHttpUtil.postXml(url, XmlUtil.convertToXml(request));
            log.info("bookuu cancel order  request:{}, response: {}", JSONObject.toJSONString(request), result);
        } catch (Exception e) {
            log.warn("bookuu cancel order error,request:{},error:{}", request, e.getMessage());
        }
        BookuuOrderCancelResponse response = null;
        try {
            response = XmlUtil.convertToJavaBean(result, BookuuOrderCancelResponse.class);
            log.info("bookuu cancel order  request:{}, response: {}",request,response);
        } catch (Exception e) {
            log.warn("bookuu order status convert error,request:{},error",request,e);
        }
        return response;
    }

    public BookuuPackStatusQueryResponse queryPackageStatus(BookuuPackStatusQueryRequest request) {
        request.setChannelID(channelID);
        request.setTimeStamp(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));
        String url = String.format(path, "packstatus");
        String result = null;
        try {
            result = OkHttpUtil.postXml(url, XmlUtil.convertToXml(request));
            log.info("bookuu query pack status request:{}, response: {}", JSONObject.toJSONString(request), result);
        } catch (Exception e) {
            log.warn("bookuu query pack status error,request:{},error:{}", request, e.getMessage());
        }
        BookuuPackStatusQueryResponse response = null;
        try {
            response = XmlUtil.convertToJavaBean(result, BookuuPackStatusQueryResponse.class);
            log.info("bookuu query pack status  request:{}, response: {}",request,response);
        } catch (Exception e) {
            log.warn("bookuu query pack status convert error,request:{},error",request,e);
        }
        return response;
    }

    /**
     * 促销价格查询，只能根据时间查询
     *
     * @param request
     * @return
     */
    public BookuuSpecialPriceQueryResponse querySpecialPrice(BookuuSpecialPriceQueryRequest request) {

        request.setChannelID(channelID);
        request.setTimeStamp(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));

        String url = String.format(path, "special");

        String result = null;
        try {
            result = OkHttpUtil.postXml(url, XmlUtil.convertToXml(request));
            log.info("get bookuu special price   request:{}, response: {}", JSONObject.toJSONString(request), result);
        } catch (Exception e) {
            log.warn("get bookuu special price  error,request:{},error:{}", request, e.getMessage());
        }
        BookuuSpecialPriceQueryResponse response = null;
        try {
            response = XmlUtil.convertToJavaBean(result, BookuuSpecialPriceQueryResponse.class);
        } catch (Exception e) {

        }
        return response;
    }

}
