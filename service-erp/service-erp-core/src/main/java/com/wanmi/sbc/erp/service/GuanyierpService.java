package com.wanmi.sbc.erp.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbc.wanmi.erp.bean.vo.ERPGoodsInfoVO;
import com.sbc.wanmi.erp.bean.vo.ErpStockVo;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.erp.api.constant.ErpErrorCode;
import com.wanmi.sbc.erp.request.*;
import com.wanmi.sbc.erp.response.*;
import com.wanmi.sbc.erp.util.GuanyierpContants;
import com.wanmi.sbc.erp.util.GuanyierpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.util.*;

/**
 * @program: sbc-background
 * @description: 管易ERP接口服务
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-26 17:26
 **/
@Service
@Slf4j
public class GuanyierpService {

    /**
     * 管易云ERP平台申请的APPKEY
     */
    @Value("${guanyierp_appKey}")
    private String appkey;

    /**
     * 管易云ERP平台申请的sessionkey
     */
    @Value("${guanyierp_sessionKey}")
    private String sessionkey;

    /**
     * 管易云ERP接口地址
     */
    @Value("${guanyierp_path}")
    private String path;

    /**
     * ERP店铺编号
     */
    @Value("${guanyierp_shopCode}")
    private String erpShopCode;

    /**
     * ERP推送订单已发货物流编号
     */
    @Value("${guanyierp_expressCode}")
    private String expressCode;

    /**
     * ERP推送订单已发货物流编号
     */
    @Value("${guanyierp_warehouseCode}")
    private String warehouseCode;

    /**
     * ERP同步库存的仓库编号
     */
    @Value("${guanyierp_stockWarehouseCode}")
    private String stockwarehouseCode;

    @Autowired
    private GuanyierpUtil guanyierpUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private static String buildSignParams = StringUtils.EMPTY;

    private static String requestParamsJson = StringUtils.EMPTY;

    private static String requestParamsEncode = StringUtils.EMPTY;

    /**
     * ERP订单推送接口
     * @param request
     * @return
     */
    public Optional<ERPPushTradeResponse> pushTrade(ERPPushTradeRequest request){
        //TODO: 2021/1/27
        request.setAppkey(appkey);
        request.setSessionkey(sessionkey);
        request.setMethod(GuanyierpContants.PUSH_ORDER_METHOD);
        //配置店铺编号
        request.setShopCode(erpShopCode);
        try {
            buildSignParams = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("#订单:{}推送接口生成Sign签名参数,转换JSON失败,异常信息:{}", request.getPlatformCode(),e.getMessage());
            return Optional.empty();
        }
        String response = StringUtils.EMPTY;
        String buildSign = guanyierpUtil.buildSign(buildSignParams);
        request.setSign(buildSign);
        try {
            requestParamsJson =objectMapper.writeValueAsString(request);
            log.info("requestParamsJson=====>:{}",requestParamsJson);
            // 将传参编为urlencode
            requestParamsEncode = URLEncoder.encode(requestParamsJson, "UTF-8");
            response = guanyierpUtil.execute(path, requestParamsEncode);
            log.info("response=====>:{}",response);
        }catch (Exception e) {
            log.error("#订单推送异常,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(ErpErrorCode.ERP_SERVICE_ERROR);
        }
        ERPPushTradeResponse tradeResponse = JSONObject.parseObject(response, ERPPushTradeResponse.class);
        if (tradeResponse.isSuccess()){
            if (tradeResponse.getId() == null) {
                log.info("#订单:{}推送管易云ERP成功,返回值显示为空!", request.getPlatformCode());
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"ERP返回数据为空"});
            }else{
                log.info("#订单:{}推送管易云ERP成功,返回值:{}", request.getPlatformCode(), tradeResponse.toString());
                return Optional.of(tradeResponse);
            }
        }else {
            log.info("#订单:{}推送接口调用失败,返回状态码:{}", request.getPlatformCode(),tradeResponse.getErrorCode());
            return Optional.empty();
        }
    }


    /**
     * ERP订单推送接口
     * @param request
     * @return
     */
    public Optional<ERPPushTradeResponse> pushTradeDelivered(ERPPushTradeRequest request){
        request.setExpressCode(expressCode);
        request.setWarehouseCode(warehouseCode);
        log.info("ERP订单推送接口已发货接口=====>:{}",request);
        //TODO: 2021/1/27
        request.setAppkey(appkey);
        request.setSessionkey(sessionkey);
        request.setMethod(GuanyierpContants.PUSH_ORDER_METHOD_DELIVERED);
        //配置店铺编号
        request.setShopCode(erpShopCode);
        try {
            buildSignParams = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("#订单:{}推送接口生成Sign签名参数,转换JSON失败,异常信息:{}", request.getPlatformCode(),e.getMessage());
            return Optional.empty();
        }
        String response = StringUtils.EMPTY;
        String buildSign = guanyierpUtil.buildSign(buildSignParams);
        request.setSign(buildSign);
        try {
            requestParamsJson =objectMapper.writeValueAsString(request);
            log.info("requestParamsJson=====>:{}",requestParamsJson);
            // 将传参编为urlencode
            requestParamsEncode = URLEncoder.encode(requestParamsJson, "UTF-8");
            response = guanyierpUtil.execute(path, requestParamsEncode);
            log.info("response=====>:{}",response);
        }catch (Exception e) {
            log.error("#订单推送异常,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(ErpErrorCode.ERP_SERVICE_ERROR);
        }
        ERPPushTradeResponse tradeResponse = JSONObject.parseObject(response, ERPPushTradeResponse.class);
        if (tradeResponse.isSuccess()){
            if (tradeResponse.getId() == null) {
                log.info("#订单:{}推送管易云ERP成功,返回值显示为空!", request.getPlatformCode());
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"ERP返回数据为空"});
            }else{
                log.info("#订单:{}推送管易云ERP成功,返回值:{}", request.getPlatformCode(), tradeResponse.toString());
                return Optional.of(tradeResponse);
            }
        }else {
            log.info("#订单:{}推送接口调用失败,返回状态码:{}", request.getPlatformCode(),tradeResponse.getErrorCode());
            return Optional.empty();
        }
    }

    /**
     * ERP商品库存查询接口
     * @param request
     * @return
     */
    public Optional<List<ERPGoodsInfoVO>> getERPGoodsStock(ERPGoodsStockQueryRequest request){
        // TODO: 2021/1/27
        request.setAppkey(appkey);
        request.setSessionkey(sessionkey);
        request.setMethod(GuanyierpContants.GOODS_STOCK_METHOD);
        //获取库存最大商品数量
        request.setWarehouseCode(stockwarehouseCode);
        try {
            buildSignParams = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("#库存查询接口生成Sign签名转换JSON失败,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"生成Sign参数已成"});
        }
        String response = StringUtils.EMPTY;
        String buildSign = guanyierpUtil.buildSign(buildSignParams);
        request.setSign(buildSign);
        try {
            requestParamsJson = objectMapper.writeValueAsString(request);
            response = guanyierpUtil.execute(path, requestParamsJson);
        } catch (Exception e) {
            log.error("#库存查询接口调用异常,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(ErpErrorCode.ERP_SERVICE_ERROR);
        }
        ERPGoodsStockQueryResponse goodsStockResponse = JSONObject.parseObject(response, ERPGoodsStockQueryResponse.class);
/*        if (CollectionUtils.isNotEmpty(goodsStockResponse.getStocks())){
            ERPGoodsInfoStock erpGoodsInfoStock = goodsStockResponse.getStocks().stream().max(Comparator.comparing(ERPGoodsInfoStock::getSalableQty)).get();
            List<ERPGoodsInfoStock> erpGoodsInfoStocks = goodsStockResponse.getStocks().stream().filter(stock ->
                    stock.getSalableQty() == erpGoodsInfoStock.getSalableQty()).collect(Collectors.toList());
            goodsStockResponse.setStocks(erpGoodsInfoStocks);
        }*/
        if (goodsStockResponse.isSuccess()){
            if (goodsStockResponse.getStocks() == null) {
                log.info("#商品SKU:{}库存查询接口调用成功,返回值显示为空!", request.getItemSkuCode());
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"ERP返回数据为空"});
            }else{
                log.info("#库存查询接口调用成功,返回值:{}", goodsStockResponse.toString());
                List<ERPGoodsInfoVO> erpGoodsInfoVOList = KsBeanUtil.convert(goodsStockResponse.getStocks(),
                        ERPGoodsInfoVO.class);
                return Optional.of(erpGoodsInfoVOList);
            }
        }else {
            log.info("#库存查询接口调用失败,返回状态码:{}",goodsStockResponse.getErrorCode());
            return Optional.empty();
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        String url = "http://v2.api.guanyierp.com/rest/erp_open";

        String req = stock();
//        String req = wareStatus();
//        String req = buyOrder();

        String res = new GuanyierpUtil().execute(url, req);
        System.out.println(res);
    }

    private static String stock() throws JsonProcessingException {
        Map<String, String> param = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        param.put("appkey", "185394");
        param.put("sessionkey", "87cd1d253240400691ff62ff3a6ded77");
        param.put("method", "gy.erp.new.stock.get");
        param.put("warehouse_code", "SSJC");
        param.put("start_date", "2020-12-15 20:30:00");
        param.put("page_no", "2");
        param.put("page_size", "15");
//        param.put("end_date", "2021-12-15 23:30:00");
//        param.put("item_code", );
//        param.put("item_sku_code", "20200512163008");

        String buildSignParams = objectMapper.writeValueAsString(param);

        StringBuilder  enValue = new StringBuilder();
        enValue.append("a18b79905fb34c0da3684a374b28889e");
        enValue.append(buildSignParams);
        enValue.append("a18b79905fb34c0da3684a374b28889e");
        String buildSign = MD5Util.md5Hex(enValue.toString(),"utf-8");
//        String buildSign = toHexString(md5(enValue.toString(), "utf-8"));
        param.put("sign", buildSign);

        buildSignParams = objectMapper.writeValueAsString(param);
        return buildSignParams;
    }

    public ErpStockVo getUpdatedStock(String startTime, String erpGoodInfoNo, String pageNum, String pageSize) {
        log.info("getUpdatedStock获取库存,参数:{},{}", startTime, erpGoodInfoNo);
        Map<String, String> request = new HashMap<>();
        request.put("appkey", appkey);
        request.put("sessionkey", sessionkey);
        request.put("method", GuanyierpContants.GOODS_STOCK_METHOD);
        request.put("warehouse_code", stockwarehouseCode);
        if(StringUtils.isNotEmpty(pageNum)) request.put("page_no", pageNum);
        if(StringUtils.isNotEmpty(pageSize)) request.put("page_size", pageSize);
        if(StringUtils.isNotEmpty(erpGoodInfoNo)){
            request.put("item_sku_code", erpGoodInfoNo);
        }else {
            request.put("start_date", startTime);
        }
        String paramStr;
        try {
            paramStr = objectMapper.writeValueAsString(request);
            String buildSign = guanyierpUtil.buildSign(paramStr);
            request.put("sign", buildSign);
            paramStr = objectMapper.writeValueAsString(request);
        }catch (Exception e) {
            log.error("getUpdatedStock获取库存生成签名错误", e);
            return new ErpStockVo();
        }
        String response = guanyierpUtil.execute(path, paramStr);
        ERPGoodsStockQueryResponse goodsStockResponse = JSONObject.parseObject(response, ERPGoodsStockQueryResponse.class);
        if (goodsStockResponse.isSuccess()){
            if (goodsStockResponse.getStocks() == null) {
                log.info("getUpdatedStock获取库存返回为空,参数:{}", startTime);
                return new ErpStockVo();
            }else{
                return KsBeanUtil.convert(goodsStockResponse, ErpStockVo.class);
//                List<ERPGoodsInfoVO> list = KsBeanUtil.convert(goodsStockResponse.getStocks(), ERPGoodsInfoVO.class);
//                ErpStockVo erpStockVo = new ErpStockVo();
//                erpStockVo.setTotal(goodsStockResponse.getTotal());
//                erpStockVo.setErpGoodsInfoVOList(list);
//                return erpStockVo;
            }
        }else {
            log.error("getUpdatedStock获取库存报错,参数:{},返回状态码:{}", startTime, goodsStockResponse.getErrorCode());
            return new ErpStockVo();
        }
    }

    /**
     * ERP商品基础信息查询接口
     * @param request
     * @return
     */
    public Optional<ERPGoodsQueryResponse> getERPGoodsInfo(ERPGoodsQueryRequest request){
        request.setAppkey(appkey);
        request.setSessionkey(sessionkey);
        request.setMethod(GuanyierpContants.GOODS_QUERY_METHOD);
        try {
            buildSignParams = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("#商品查询接口生成Sign签名转换JSON失败,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"生成Sign参数已成"});
        }
        String response = StringUtils.EMPTY;
        String buildSign = guanyierpUtil.buildSign(buildSignParams);
        request.setSign(buildSign);
        try {
            requestParamsJson = objectMapper.writeValueAsString(request);
            response = guanyierpUtil.execute(path, requestParamsJson);
        } catch (Exception e) {
            log.error("#商品查询接口调用失败,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(ErpErrorCode.ERP_SERVICE_ERROR);
        }
        ERPGoodsQueryResponse goodsQueryResponse = JSONObject.parseObject(response, ERPGoodsQueryResponse.class);
        if (goodsQueryResponse.isSuccess()){
            if (CollectionUtils.isEmpty(goodsQueryResponse.getItems())) {
                log.info("#商品SPU:{}查询接口调用成功,返回值显示为空!", request.getCode());
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"ERP内不存在该SPU编码商品"});
            }else{
                log.info("#商品SPU:{}查询接口调用成功,返回值:{}", request.getCode(),goodsQueryResponse.toString());
                return Optional.of(goodsQueryResponse);
            }
        }else {
            log.info("#商品查询接口调用失败,返回状态码:{}",goodsQueryResponse.getErrorCode());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{goodsQueryResponse.getErrorDesc()});
        }
    }

    /**
     * ERP发货单查询接口
     * @param request
     * @return
     */
    public Optional<ERPDeliveryQueryResponse> getERPDeliveryInfo(ERPDeliveryQueryRequest request){
        // TODO: 2021/1/27
        request.setAppkey(appkey);
        request.setSessionkey(sessionkey);
        request.setMethod(GuanyierpContants.DELIVERY_QUERY_METHOD);
        //设置查询发货的状态
        if (Objects.nonNull(request.getDelivery())) {
            request.setDelivery(request.getDelivery());
        }
        try {
            buildSignParams = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("#发货单查询接口生成Sign签名转换JSON失败,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"生成Sign参数已成"});
        }
        String response = StringUtils.EMPTY;
        String buildSign = guanyierpUtil.buildSign(buildSignParams);
        request.setSign(buildSign);
        try {
            requestParamsJson = objectMapper.writeValueAsString(request);
            response = guanyierpUtil.execute(path, requestParamsJson);
        } catch (Exception e) {
            log.error("#发货单查询接口调用失败,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(ErpErrorCode.ERP_SERVICE_ERROR);
        }
        log.info("response=========>:{}",response);
        ERPDeliveryQueryResponse deliveryQueryResponse = JSONObject.parseObject(response, ERPDeliveryQueryResponse.class);
        log.info("deliveryQueryResponse=========>:{}",deliveryQueryResponse);
        if (deliveryQueryResponse.isSuccess()){
            if (deliveryQueryResponse == null) {
                log.info("#订单:{}发货单查询接口调用成功,返回值显示为空!", request.getOuterCode());
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"ERP返回数据为空"});
            }else{
                log.info("#订单:{}发货单查询接口调用成功,返回值:{}", request.getOuterCode(),deliveryQueryResponse.toString());
                return Optional.of(deliveryQueryResponse);
            }
        }else {
            log.info("#发货单查询接口调用成功,返回状态码:{}",deliveryQueryResponse.getErrorCode());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{deliveryQueryResponse.getErrorDesc()});
        }
    }

    /**
     * ERP订单退款状态修改接口
     * @param request
     * @return
     */
    public Optional<ERPRefundUpdateResponse> refundOrderUpdate(ERPRefundUpdateRequest request){
        // TODO: 2021/1/27
        request.setAppkey(appkey);
        request.setSessionkey(sessionkey);
        request.setMethod(GuanyierpContants.REFUND_UPDATE_METHOD);
        try {
            buildSignParams = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("#主订单号{},子订单号:{},退款状态修改接口生成Sign签名转换JSON失败,异常信息:{}",request.getTid(),request.getOid(),e.getMessage());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"生成Sign参数已成"});
        }
        String response = StringUtils.EMPTY;
        String buildSign = guanyierpUtil.buildSign(buildSignParams);
        request.setSign(buildSign);
        try {
            requestParamsJson = objectMapper.writeValueAsString(request);
            response = guanyierpUtil.execute(path, requestParamsJson);
        } catch (Exception e) {
            log.error("#退款状态修改接口调用失败,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(ErpErrorCode.ERP_SERVICE_ERROR);
        }
        ERPRefundUpdateResponse refundUpdateResponse = JSONObject.parseObject(response, ERPRefundUpdateResponse.class);
        if (refundUpdateResponse.isSuccess()){
            if (refundUpdateResponse == null) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"ERP返回数据为空"});
            }else{
                return Optional.of(refundUpdateResponse);
            }
        }else {
            log.info("#退款状态修改接口调用成功,返回状态码:{}",refundUpdateResponse.getErrorCode());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{refundUpdateResponse.getErrorDesc()});
        }
    }

    /**
     * 订单拦截接口
     * @return
     */
    public Optional<ERPBaseResponse> interceptTrade(ERPTradeInterceptRequest request){
        // TODO: 2021/1/27
        request.setAppkey(appkey);
        request.setSessionkey(sessionkey);
        request.setMethod(GuanyierpContants.TRADE_INTERCEPT_METHOD);
        try {
            buildSignParams = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("#主订单号{},订单拦截接口生成Sign签名转换JSON失败,异常信息:{}",request.getPlatformCode(),e.getMessage());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"生成Sign参数已成"});
        }
        String response = StringUtils.EMPTY;
        String buildSign = guanyierpUtil.buildSign(buildSignParams);
        request.setSign(buildSign);
        try {
            requestParamsJson = objectMapper.writeValueAsString(request);
            log.info("requestParamsJson=====>:{}",requestParamsJson);
            response = guanyierpUtil.execute(path, requestParamsJson);
        } catch (Exception e) {
            log.error("#订单:{},拦截查询接口调用异常,异常信息:{}",request.getPlatformCode(),e.getMessage());
            throw new SbcRuntimeException(ErpErrorCode.ERP_SERVICE_ERROR);
        }
        log.info("response=====>:{}",response);
        ERPBaseResponse erpBaseResponse = JSONObject.parseObject(response, ERPBaseResponse.class);
        if (erpBaseResponse.isSuccess()){
            if (erpBaseResponse == null) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"ERP返回数据为空"});
            }else{
                return Optional.of(erpBaseResponse);
            }
        }else {
            log.info("#订单:{},拦截接口调用失败,返回状态码:{}",request.getPlatformCode(),erpBaseResponse.getErrorCode());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{erpBaseResponse.getErrorDesc()});
        }
    }


    /**
     * ERP创建退货单接口
     * @param request
     * @return
     */
    public Optional<ERPBaseResponse> createReturnTrade(ERPReturnTradeCreateRequest request){
        //TODO: 2021/1/28
        request.setAppkey(appkey);
        request.setSessionkey(sessionkey);
        request.setMethod(GuanyierpContants.RETURN_TRADE_ADD_METHOD);
        //配置店铺编号
        request.setShopCode(erpShopCode);
        try {
            buildSignParams = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("#订单号{},ERP退货单创建接口生成Sign签名转换JSON失败,异常信息:{}",request.getTradePlatformCode(),e.getMessage());
            return Optional.empty();
        }
        String response = StringUtils.EMPTY;
        String buildSign = guanyierpUtil.buildSign(buildSignParams);
        request.setSign(buildSign);
        try {
            requestParamsJson = objectMapper.writeValueAsString(request);
            log.info("requestParamsJson=====>:{}",requestParamsJson);
            // 将传参编为urlencode
            requestParamsEncode = URLEncoder.encode(requestParamsJson, "UTF-8");
            response = guanyierpUtil.execute(path, requestParamsEncode);
        } catch (Exception e) {
            log.error("#订单:{},拦截查询接口调用异常,异常信息:{}",request.getTradePlatformCode(),e.getMessage());
            throw new SbcRuntimeException(ErpErrorCode.ERP_SERVICE_ERROR);
        }
        log.info("response=====>:{}",response);
        ERPBaseResponse erpBaseResponse = JSONObject.parseObject(response, ERPBaseResponse.class);
        if (erpBaseResponse.isSuccess()){
            if (erpBaseResponse == null) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"ERP返回数据为空"});
            }else{
                return Optional.of(erpBaseResponse);
            }
        }else {
            log.info("#订单号{},ERP退货单创建接口调用失败,返回状态码:{}",request.getTradePlatformCode(),erpBaseResponse.getErrorCode());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{erpBaseResponse.getErrorDesc()});
        }
    }

    /**
     * 退货单查询接口
     * @param request
     * @return
     */
    public Optional<ERPReturnTradeResponse> getReturnTrade(ERPReturnTradeQueryRequest request){
        // TODO: 2021/2/7
        request.setAppkey(appkey);
        request.setSessionkey(sessionkey);
        request.setMethod(GuanyierpContants.RETURN_TRADE_GET_METHOD);
        try {
            buildSignParams = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("#订单号{},ERP退货单查询接口生成Sign签名转换JSON失败,异常信息:{}",request.getPlatformCode(),e.getMessage());
            return Optional.empty();
        }
        String response = StringUtils.EMPTY;
        String buildSign = guanyierpUtil.buildSign(buildSignParams);
        request.setSign(buildSign);
        try {
            requestParamsJson = objectMapper.writeValueAsString(request);
            response = guanyierpUtil.execute(path, requestParamsJson);
        } catch (Exception e) {
            log.error("#订单:{},ERP退货单查询调用异常,异常信息:{}",request.getPlatformCode(),e.getMessage());
            throw new SbcRuntimeException(ErpErrorCode.ERP_SERVICE_ERROR);
        }
        ERPReturnTradeResponse returnTradeResponse = JSONObject.parseObject(response, ERPReturnTradeResponse.class);
        if (returnTradeResponse.isSuccess()){
            if (returnTradeResponse == null) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"ERP返回数据为空"});
            }else{
                return Optional.of(returnTradeResponse);
            }
        }else {
            log.info("#订单号{},ERP退货单查询接口调用失败,返回状态码:{}",request.getPlatformCode(),
                    returnTradeResponse.getErrorCode());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{returnTradeResponse.getErrorDesc()});
        }
    }

    /**
     * 仓库查询接口
     * @param request
     * @return
     */
    public Optional<ERPWareHouseQueryResponse> getWarehouseList(ERPWareHouseQueryRequest request){
        // TODO: 2021/1/27
        request.setAppkey(appkey);
        request.setSessionkey(sessionkey);
        request.setMethod(GuanyierpContants.WAREHOUSE_QUERY_METHOD);
        try {
            buildSignParams = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("#发货单查询接口生成Sign签名转换JSON失败,异常信息:{}",e.getMessage());
            return Optional.empty();
        }
        String buildSign = guanyierpUtil.buildSign(buildSignParams);
        request.setSign(buildSign);
        try {
            requestParamsJson = objectMapper.writeValueAsString(request);
            String response = guanyierpUtil.execute(path, requestParamsJson);
            ERPWareHouseQueryResponse warehouseQueryResponse = JSONObject.parseObject(response, ERPWareHouseQueryResponse.class);
            if (warehouseQueryResponse.isSuccess()){
                if (warehouseQueryResponse == null) {
                    throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"ERP返回数据为空"});
                }else{
                    return Optional.of(warehouseQueryResponse);
                }
            }else {
                log.info("#仓库列表查询接口调用成功,返回状态码:{}",warehouseQueryResponse.getErrorCode());
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{warehouseQueryResponse.getErrorDesc()});
            }
        } catch (Exception e) {
            log.error("#仓库列表查询接口调用失败,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(ErpErrorCode.ERP_SERVICE_ERROR);
        }
    }

    /**
     * ERP历史发货单查询接口
     * @param request
     * @return
     */
    public Optional<ERPDeliveryQueryResponse> getERPHistoryDeliveryInfo(ERPHistoryDeliveryInfoRequest request){
        // TODO: 2021/1/27
        request.setAppkey(appkey);
        request.setSessionkey(sessionkey);
        request.setMethod(GuanyierpContants.DELIVERY_HISTORY_QUERY_METHOD);
        try {
            buildSignParams = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("#【历史发货单】发货单查询接口生成Sign签名转换JSON失败,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"生成Sign参数已成"});
        }
        String response = StringUtils.EMPTY;
        String buildSign = guanyierpUtil.buildSign(buildSignParams);
        request.setSign(buildSign);
        try {
            requestParamsJson = objectMapper.writeValueAsString(request);
            log.info("#【历史发货单】发货单组装传参,requestParamsJson:{}",requestParamsJson);
            response = guanyierpUtil.execute(path, requestParamsJson);
        } catch (Exception e) {
            log.error("#【历史发货单】发货单查询接口调用失败,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(ErpErrorCode.ERP_SERVICE_ERROR);
        }
        log.info("response=========>:{}",response);
        ERPDeliveryQueryResponse deliveryQueryResponse = JSONObject.parseObject(response, ERPDeliveryQueryResponse.class);
        log.info("deliveryQueryResponse=========>:{}",deliveryQueryResponse);
        if (deliveryQueryResponse.isSuccess()){
            if (deliveryQueryResponse == null) {
                log.info("#【历史发货单】订单:{}发货单查询接口调用成功,返回值显示为空!", request.getOuterCode());
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"ERP返回数据为空"});
            }else{
                log.info("#【历史发货单】订单:{}发货单查询接口调用成功,返回值:{}", request.getOuterCode(),deliveryQueryResponse.toString());
                return Optional.of(deliveryQueryResponse);
            }
        }else {
            log.info("#【历史发货单】发货单查询接口调用成功,返回状态码:{}",deliveryQueryResponse.getErrorCode());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{deliveryQueryResponse.getErrorDesc()});
        }
    }

    /**
     * ERP订单查询接口
     * @param request
     * @return
     */
    public Optional<ERPTradeQueryResponse> getErpTradeInfo(ERPTradeQueryRequest request,int flag){
        // TODO: 2021/1/27
        request.setAppkey(appkey);
        request.setSessionkey(sessionkey);
        if (flag == 0){
            request.setMethod(GuanyierpContants.TRADE_GET_METHOD);
        }else {
            request.setMethod(GuanyierpContants.TRADE_HISTORY_GET_METHOD);
        }
        try {
            buildSignParams = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("#【订单查询】发货单查询接口生成Sign签名转换JSON失败,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"生成Sign参数已成"});
        }
        String response = StringUtils.EMPTY;
        String buildSign = guanyierpUtil.buildSign(buildSignParams);
        request.setSign(buildSign);
        try {
            requestParamsJson = objectMapper.writeValueAsString(request);
            log.info("#【订单查询】组装传参,requestParamsJson:{}",requestParamsJson);
            response = guanyierpUtil.execute(path, requestParamsJson);
        } catch (Exception e) {
            log.error("#【订单查询】接口调用失败,异常信息:{}",e.getMessage());
            throw new SbcRuntimeException(ErpErrorCode.ERP_SERVICE_ERROR);
        }
        log.info("response=========>:{}",response);
        ERPTradeQueryResponse erpTradeQueryResponse = JSONObject.parseObject(response, ERPTradeQueryResponse.class);
        log.info("erpTradeQueryResponse=========>:{}",erpTradeQueryResponse);
        if (erpTradeQueryResponse.isSuccess()){
            if (erpTradeQueryResponse == null) {
                log.info("#【订单查询】订单:{}接口调用成功,返回值显示为空!", request.getPlatformCode());
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"ERP返回数据为空"});
            }else{
                log.info("#【订单查询】订单:{}接口调用成功,返回值:{}", request.getPlatformCode(),erpTradeQueryResponse.toString());
                return Optional.of(erpTradeQueryResponse);
            }
        }else {
            log.info("#【订单查询】接口调用成功,返回状态码:{}",erpTradeQueryResponse.getErrorCode());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{erpTradeQueryResponse.getErrorDesc()});
        }
    }

    /**
     * 接口测试方法
     */
    @PostConstruct
    public void test(){

        /**
         * 商品信息同步
         */
  /*      ERPGoodsQueryRequest goodsQueryRequest = ERPGoodsQueryRequest.builder().code("1312").build();
        Optional<ERPGoodsQueryResponse> erpGoodsInfo = this.getERPGoodsInfo(goodsQueryRequest);
        log.info(erpGoodsInfo.get().toString());*/

        /**
         * 新库存查询接口
         */
/*        ERPGoodsStockQueryRequest goodsStockQueryRequest = ERPGoodsStockQueryRequest.builder().itemSkuCode("zslk").build();
        Optional<List<ERPGoodsInfoVO>> goodsInfoVOList = this.getERPGoodsStock(goodsStockQueryRequest);
        log.info(goodsInfoVOList.toString());*/


        /**
         * ERP订单推送接口测试
         */
/*        List<ERPTradeItem> tradeItemList = new ArrayList<>();
        ERPTradeItem erpTradeItem = ERPTradeItem.builder()
                .itemCode("kd0001")
                .skuCode("003")
                .originPrice("100")
                .price("80")
                .qty(1)
                .refund(0)
                .oid("OD202003282312516357").build();
        tradeItemList.add(erpTradeItem);

        List<ERPTradePayment> erpTradePaymentList = new ArrayList<>();
        ERPTradePayment erpTradePayment = ERPTradePayment.builder()
                .account("18996073379")
                .payCode("123123123")
                .payTypeCode("weixin")
                .payment("80")
                .paytime(System.currentTimeMillis() / 1000L)
                .build();
        erpTradePaymentList.add(erpTradePayment);

        ERPPushTradeRequest erpPushTradeRequest = ERPPushTradeRequest.builder()
                .shopCode("99999")
                .vipCode("18996073379")
                .platformCode("P202003282312514243")
                .dealDatetime(DateUtil.nowTime())
                .orderTypeCode("Sales")
                .details(tradeItemList)
                .payments(erpTradePaymentList)
                .receiverName("吴功江")
                .receiverMobile("15951873451")
                .receiverProvince("江苏省")
                .receiverCity("南京市")
                .receiverDistrict("雨花区")
                .receiverAddress("软件大道100号")
                .build();
        Optional<ERPPushTradeResponse> erpPushTradeResponse = this.pushTrade(erpPushTradeRequest);
        log.info(erpPushTradeResponse.get().toString());*/

        /**
         * 仓库列表接口测试
         */
/*      ERPWarehouseQueryRequest warehouseQueryRequest = ERPWarehouseQueryRequest.builder().hasDelData(false).build();
        Optional<ERPWarehouseQueryResponse> responseOptional = this.getWarehouseList(warehouseQueryRequest);
        log.info(responseOptional.get().toString());*/

        /**
         * 发货单查询接口测试
         */
/*      ERPDeliveryQueryRequest deliveryQueryRequest = ERPDeliveryQueryRequest.builder().outerCode(
                "P202103091622498412473").build();
        Optional<ERPDeliveryQueryResponse> deliveryQueryResponse = this.getERPDeliveryInfo(deliveryQueryRequest);
        log.info(deliveryQueryResponse.get().toString());*/

        /**
         * 订单退款状态修改接口测试
         */
/*        ERPRefundUpdateRequest refundUpdateRequest = ERPRefundUpdateRequest.builder().tid("P202003282312514242").oid(
                "OD202003282312516356").refundState(2).build();
        Optional<ERPRefundUpdateResponse> erpRefundUpdateResponse = this.refundOrderUpdate(refundUpdateRequest);
        log.info(erpRefundUpdateResponse.get().toString());*/

        /**
         * 拦截订单接口测试
         */
/*        ERPTradeInterceptRequest interceptRequest =
                ERPTradeInterceptRequest.builder().platformCode("P202003282312514242").operateType(1).tradeHoldCode(
                        "01").tradeHoldReason("不发货").build();
        Optional<ERPBaseResponse> erpBaseResponse = this.interceptTrade(interceptRequest);
        log.info(erpBaseResponse.get().toString());*/

        /**
         * 创建退货单接口测试
         */
/*        List<ERPReturnTradeItem> returnTradeItems = new ArrayList<>();
        ERPReturnTradeItem returnTradeItem = ERPReturnTradeItem.builder()
                .itemCode("kd0001")
                .skuCode("003")
                .qty(1).build();;
        returnTradeItems.add(returnTradeItem);
        ERPReturnTradeCreateRequest returnTradeCreateRequest = ERPReturnTradeCreateRequest.builder().shopCode("99999")
                .vipCode("18996073379")
                .tradePlatformCode("P202003282312514243")
                .itemDetail(returnTradeItems)
                .typeCode("01")
                .build();
        Optional<ERPBaseResponse> optionalERPBaseResponse = this.createReturnTrade(returnTradeCreateRequest);
        log.info(optionalERPBaseResponse.get().toString());*/

        /**
         * 退货单查询接口测试
         */
/*        ERPReturnTradeQueryRequest returnTradeQueryRequest = ERPReturnTradeQueryRequest.builder()
                .platformCode("P202003282312514243")
                .receive(1).build();
        Optional<ERPReturnTradeResponse> returnTrade = this.getReturnTrade(returnTradeQueryRequest);
        log.info(returnTrade.get().toString());*/

        /**
         * 订单查询接口测试
         */
/*        ERPTradeQueryRequest build = ERPTradeQueryRequest.builder().platformCode("P202106290947571141071").build();
        Optional<ERPTradeQueryResponse> erpTradeInfo = this.getErpTradeInfo(build, 0);
        log.info("erpTradeInfo===>:{}",erpTradeInfo.get());*/
    }
}
