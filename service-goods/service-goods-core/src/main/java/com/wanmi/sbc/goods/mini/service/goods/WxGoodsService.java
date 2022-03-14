package com.wanmi.sbc.goods.mini.service.goods;

import com.alibaba.fastjson.JSONObject;
import com.soybean.mall.wx.mini.common.bean.request.WxUploadImageRequest;
import com.soybean.mall.wx.mini.common.bean.response.WxUploadImageResponse;
import com.soybean.mall.wx.mini.goods.bean.request.WxAddProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxUpdateProductWithoutAuditRequest;
import com.soybean.mall.wx.mini.goods.bean.response.WxAddProductResponse;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.goods.controller.WxGoodsApiController;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsSearchRequest;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.mini.enums.goods.WxGoodsEditStatus;
import com.wanmi.sbc.goods.mini.enums.goods.WxGoodsStatus;
import com.wanmi.sbc.goods.mini.enums.review.WxReviewResult;
import com.wanmi.sbc.goods.mini.model.goods.WxGoodsModel;
import com.wanmi.sbc.goods.mini.model.review.WxReviewLogModel;
import com.wanmi.sbc.goods.mini.repository.goods.WxGoodsRepository;
import com.wanmi.sbc.goods.mini.repository.review.WxReviewLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@RefreshScope
@Service
@Slf4j
public class WxGoodsService {

    @Value("${wx.mini.goods.path}")
    private String wxGoodsPath;

    @Autowired
    private WxGoodsRepository wxGoodsRepository;
    @Autowired
    private WxGoodsApiController wxGoodsApiController;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsInfoService goodsInfoService;
    @Autowired
    private WxReviewLogRepository wxReviewLogRepository;
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Page<WxGoodsModel> listGoods(WxGoodsSearchRequest wxGoodsSearchRequest){
        StringBuilder select = new StringBuilder(128);
        StringBuilder condition = new StringBuilder(128).append(" where tg.del_flag=0");
        Map<String, Object> params = new HashMap<>();
        select.append("select tg.* from t_wx_goods as tg ");
        if(wxGoodsSearchRequest.getSaleStatus() != null){
            select.append(" join goods as g on tg.goods_id=g.goods_id");
            if(wxGoodsSearchRequest.getSaleStatus() == 1){
                condition.append(" and tg.platform_product_id is not null and g.added_flag=1");
            }else{
                condition.append(" and (tg.platform_product_id is null or g.added_flag=0)");
            }
        }
        if(wxGoodsSearchRequest.getGoodsIds() != null){
            condition.append(" and tg.goods_id in :goodsIds");
            params.put("goodsIds", wxGoodsSearchRequest.getGoodsIds());
        }
        if (wxGoodsSearchRequest.getAuditStatus() != null) {
            condition.append(" and tg.audit_status=:auditStatus");
            params.put("auditStatus", wxGoodsSearchRequest.getAuditStatus());
        }
        String sql = select.append(condition).toString();
        String countSql = new StringBuilder(128).append("select count(*) from (").append(sql).append(") t").toString();
        Query nativeQuery = entityManager.createNativeQuery(sql, WxGoodsModel.class);
        Query countQuery = entityManager.createNativeQuery(countSql);
        if(!params.isEmpty()) {
            params.forEach((k, v) -> {
                nativeQuery.setParameter(k, v);
                countQuery.setParameter(k, v);
            });
        }
        nativeQuery.setFirstResult(wxGoodsSearchRequest.getPageNum() * wxGoodsSearchRequest.getPageSize());
        nativeQuery.setMaxResults(wxGoodsSearchRequest.getPageSize());
        List<WxGoodsModel> resultList = nativeQuery.getResultList();
        Long count = ((BigInteger) countQuery.getSingleResult()).longValue();
        MicroServicePage<WxGoodsModel> page = new MicroServicePage<>();
        page.setContent(resultList);
        page.setTotal(count);
        return page;
    }

    @Transactional
    public void addGoods(WxGoodsCreateRequest createRequest){

        LocalDateTime now = LocalDateTime.now();
        String goodsIdStr = createRequest.getGoodsId();
        String[] split = goodsIdStr.split(",");
        List<WxGoodsModel> wxGoodsModels = new ArrayList<>();
        for (String goodsId : split) {
            if(goodsExist(goodsId)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品重复添加: " + goodsId);
            WxGoodsModel wxGoodsModel = new WxGoodsModel();
            wxGoodsModel.setGoodsId(goodsId);
            wxGoodsModel.setStatus(WxGoodsStatus.UPLOAD);
            wxGoodsModel.setAuditStatus(WxGoodsEditStatus.WAIT_CHECK);
            wxGoodsModel.setCreateTime(now);
            wxGoodsModel.setUpdateTime(now);
            wxGoodsModel.setNeedToAudit(1);
            wxGoodsModel.setDelFlag(DeleteFlag.NO);
            wxGoodsModels.add(wxGoodsModel);
        }
        wxGoodsRepository.saveAll(wxGoodsModels);
    }

    @Transactional
    public void cancelAudit(String goodsId){
        WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(goodsId, DeleteFlag.NO);
        if(wxGoodsModel == null) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品不存在");

        wxGoodsModel.setAuditStatus(WxGoodsEditStatus.CHECK_CANCEL);
        wxGoodsModel.setNeedToAudit(1);
        wxGoodsRepository.save(wxGoodsModel);

        BaseResponse<WxResponseBase> wxResponseBaseBaseResponse = wxGoodsApiController.cancelAudit(goodsId);
        if(!wxResponseBaseBaseResponse.getContext().isSuccess()) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED,
                wxResponseBaseBaseResponse.getContext().getErrmsg());
    }

    @Transactional
    public void toAudit(WxGoodsCreateRequest createRequest){
        WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(createRequest.getGoodsId(), DeleteFlag.NO);
        if(wxGoodsModel == null) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品不存在");
        if(wxGoodsModel.getWxCategory() == null || wxGoodsModel.getIsbnImg() == null || wxGoodsModel.getPublisherImg() == null)
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品类目、ISBN图、出版社图必填");
        if(wxGoodsModel.getNeedToAudit() == 0) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品无需审核");

        Goods goods = goodsService.findByGoodsId(createRequest.getGoodsId());
        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsId(createRequest.getGoodsId()).delFlag(DeleteFlag.NO.toValue()).build());
        if(wxGoodsModel.getNeedToAudit() == 1) {
            WxGoodsEditStatus auditStatus = wxGoodsModel.getAuditStatus();
            if(auditStatus.equals(WxGoodsEditStatus.WAIT_CHECK)){
                //初次提审
                BaseResponse<WxAddProductResponse> baseResponse = wxGoodsApiController.addGoods(createWxAddProductRequestByGoods(goods, goodsInfos, wxGoodsModel));
                if(!baseResponse.getContext().isSuccess()){
                    throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, baseResponse.getContext().getErrmsg());
                }
                wxGoodsModel.setUploadTime(LocalDateTime.now());
                wxGoodsModel.setAuditStatus(WxGoodsEditStatus.ON_CHECK);
                wxGoodsRepository.save(wxGoodsModel);
            }else if(auditStatus.equals(WxGoodsEditStatus.ON_CHECK)){

                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品正在审核中");
            }else if(auditStatus.equals(WxGoodsEditStatus.CHECK_FAILED) || auditStatus.equals(WxGoodsEditStatus.CHECK_CANCEL)){
                //更新商品,重新提审
                BaseResponse<WxAddProductResponse> baseResponse = wxGoodsApiController.updateGoods(createWxAddProductRequestByGoods(goods, goodsInfos, wxGoodsModel));
                if(!baseResponse.getContext().isSuccess()){
                    throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, baseResponse.getContext().getErrmsg());
                }
                wxGoodsModel.setUploadTime(LocalDateTime.now());
                wxGoodsModel.setAuditStatus(WxGoodsEditStatus.ON_CHECK);
                wxGoodsRepository.save(wxGoodsModel);
            }
        }else if(wxGoodsModel.getNeedToAudit() == 2){
            //需要免审更新
            BaseResponse<WxResponseBase> baseResponse = wxGoodsApiController.updateGoodsWithoutAudit(createWxUpdateProductWithoutAuditRequest(goods, goodsInfos));
            if(!baseResponse.getContext().isSuccess()){
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, baseResponse.getContext().getErrmsg());
            }
            wxGoodsModel.setUploadTime(LocalDateTime.now());
            wxGoodsModel.setAuditStatus(WxGoodsEditStatus.CHECK_SUCCESS);
            wxGoodsRepository.save(wxGoodsModel);
        }
    }

    /**
     * 微信免审
     */
    public void toAudit(Goods goods){
        WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(goods.getGoodsId(), DeleteFlag.NO);
        if(wxGoodsModel != null && wxGoodsModel.getPlatformProductId() != null){
            //需要免审更新
            List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsId(goods.getGoodsId()).delFlag(DeleteFlag.NO.toValue()).build());
            BaseResponse<WxResponseBase> baseResponse = wxGoodsApiController.updateGoodsWithoutAudit(createWxUpdateProductWithoutAuditRequest(goods, goodsInfos));
            if(!baseResponse.getContext().isSuccess()){
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, baseResponse.getContext().getErrmsg());
            }
            wxGoodsModel.setUploadTime(LocalDateTime.now());
            wxGoodsModel.setAuditStatus(WxGoodsEditStatus.CHECK_SUCCESS);
            wxGoodsRepository.save(wxGoodsModel);
        }
    }

    public void update(WxGoodsCreateRequest createRequest){
        WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(createRequest.getGoodsId(), DeleteFlag.NO);
        if(wxGoodsModel == null) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品不存在");

        if(createRequest.getWxCategory() != null){
            wxGoodsModel.setWxCategory(JSONObject.toJSONString(createRequest.getWxCategory()));
            wxGoodsModel.setNeedToAudit(1);
        }
        if(createRequest.getIsbnImg() != null){
            wxGoodsModel.setIsbnImg(createRequest.getIsbnImg());
            wxGoodsModel.setNeedToAudit(1);
        }
        if(createRequest.getPublisherImg() != null){
            wxGoodsModel.setPublisherImg(createRequest.getPublisherImg());
            wxGoodsModel.setNeedToAudit(1);
        }
        wxGoodsRepository.save(wxGoodsModel);
    }

    @Transactional
    public void deleteGoods(WxDeleteProductRequest wxDeleteProductRequest){
        WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(wxDeleteProductRequest.getOutProductId(), DeleteFlag.NO);
        if(wxGoodsModel != null){
            wxGoodsModel.setDelFlag(DeleteFlag.YES);
            wxGoodsModel.setUpdateTime(LocalDateTime.now());
            wxGoodsRepository.save(wxGoodsModel);
            BaseResponse<Boolean> response = wxGoodsApiController.deleteGoods(wxDeleteProductRequest);
            if(!response.getContext()){
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "删除微信商品失败");
            }
        }
    }

    @Transactional
    public boolean auditCallback(Map<String, Object> paramMap){
        Map<String, String> auditResult = (Map<String, String>) paramMap.get("OpenProductSpuAudit");
        String wxStatus = auditResult.get("status");
        String goodsId = auditResult.get("out_product_id");
        String productId = auditResult.get("product_id");
        String spuStatus = auditResult.get("spu_status"); //上下架状态 5-上架 11、13-下架
        if("4".equals(wxStatus)){
            //成功
            WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(goodsId, DeleteFlag.NO);
            wxGoodsModel.setStatus(WxGoodsStatus.ON_SHELF);
            wxGoodsModel.setAuditStatus(WxGoodsEditStatus.CHECK_SUCCESS);
            wxGoodsModel.setPlatformProductId(Long.parseLong(productId));
            wxGoodsModel.setRejectReason("");
            if(wxGoodsModel.getNeedToAudit() == 3){
                wxGoodsModel.setNeedToAudit(1);
            }else {
                wxGoodsModel.setNeedToAudit(0);
            }
            wxGoodsRepository.save(wxGoodsModel);

            WxReviewLogModel wxReviewLogModel = new WxReviewLogModel();
            wxReviewLogModel.setRelateId(wxGoodsModel.getId());
            wxReviewLogModel.setReviewedTime(LocalDateTime.now());
            wxReviewLogModel.setReviewResult(WxReviewResult.SUCCESS);
            wxReviewLogModel.setReviewType(0);
            wxReviewLogRepository.save(wxReviewLogModel);

            return true;
        }else if("3".equals(wxStatus)){
            //失败
            String rejectReason = auditResult.get("reject_reason");
            WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(goodsId, DeleteFlag.NO);
            wxGoodsModel.setStatus(WxGoodsStatus.UPLOAD);
            wxGoodsModel.setAuditStatus(WxGoodsEditStatus.CHECK_FAILED);
            if("11".equals(spuStatus) || "13".equals(spuStatus)) wxGoodsModel.setStatus(WxGoodsStatus.OFF_SHELF);
            wxGoodsModel.setNeedToAudit(1);
            wxGoodsModel.setRejectReason(rejectReason);
            wxGoodsRepository.save(wxGoodsModel);

            WxReviewLogModel wxReviewLogModel = new WxReviewLogModel();
            wxReviewLogModel.setRelateId(wxGoodsModel.getId());
            wxReviewLogModel.setReviewedTime(LocalDateTime.now());
            wxReviewLogModel.setReviewReason(rejectReason);
            wxReviewLogModel.setReviewResult(WxReviewResult.FAILED);
            wxReviewLogModel.setReviewType(0);
            wxReviewLogRepository.save(wxReviewLogModel);
            return false;
        }
        return false;
    }

    private boolean goodsExist(String goodsId){
        Integer onShelfCount = wxGoodsRepository.getOnShelfCount(goodsId);
        return onShelfCount > 0;
    }

    public WxAddProductRequest createWxAddProductRequestByGoods(Goods goods, List<GoodsInfo> goodsInfos, WxGoodsModel wxGoodsModel){
        WxAddProductRequest addProductRequest = new WxAddProductRequest();
        addProductRequest.setOutProductId(goods.getGoodsId());
        addProductRequest.setTitle(goods.getGoodsName());
        addProductRequest.setPath(wxGoodsPath.concat("?spuId=").concat(goods.getGoodsId()));
        addProductRequest.setHeadImg(Collections.singletonList(exchangeWxImgUrl(goods.getGoodsImg())));
        List<String> qualificationics = new ArrayList<>();
        qualificationics.add(exchangeWxImgUrl(wxGoodsModel.getIsbnImg()));
        qualificationics.add(exchangeWxImgUrl(wxGoodsModel.getPublisherImg()));
        addProductRequest.setQualificationics(qualificationics);
        //商品详情截取图片
        String detail;
        if(StringUtils.isNotEmpty(goods.getGoodsMobileDetail())){
            detail = goods.getGoodsMobileDetail();
        }else {
            detail = goods.getGoodsDetail();
        }
        if(detail != null){
            List<String> detailImgs = new ArrayList<>();
            String[] split = detail.split("<img");
            for (String s1 : split) {
                String[] split1 = s1.split("src=\"");
                for (String s2 : split1) {
                    if(s2.contains("http")){
                        String url = s2.substring(0, s2.indexOf("\""));
                        String wxImgUrl = exchangeWxImgUrl(url);
                        if(wxImgUrl != null) detailImgs.add(wxImgUrl);
                    }
                }
            }
            if(detailImgs.size() > 0) addProductRequest.setDescInfo(new WxAddProductRequest.DescInfo("", detailImgs));
        }
        String ids = JSONObject.parseObject(wxGoodsModel.getWxCategory()).getString("id");
        addProductRequest.setThirdCatId(Integer.parseInt(ids.substring(ids.lastIndexOf(",") + 1)));
        addProductRequest.setBrandId(2100000000);
        addProductRequest.setInfoVersion("1");
        addProductRequest.setSkus(createSkus(goods, goodsInfos));
        return addProductRequest;
    }

    public WxUpdateProductWithoutAuditRequest createWxUpdateProductWithoutAuditRequest(Goods goods, List<GoodsInfo> goodsInfos){
        WxUpdateProductWithoutAuditRequest wxUpdateProductWithoutAuditRequest = new WxUpdateProductWithoutAuditRequest();
        wxUpdateProductWithoutAuditRequest.setOutProductId(goods.getGoodsId());
        wxUpdateProductWithoutAuditRequest.setSkus(createSkusWithoutAudit(goodsInfos));
        return wxUpdateProductWithoutAuditRequest;
    }

    public List<WxUpdateProductWithoutAuditRequest.Sku> createSkusWithoutAudit(List<GoodsInfo> goodsInfos){
        List<WxUpdateProductWithoutAuditRequest.Sku> skus = new ArrayList<>();
        for (GoodsInfo goodsInfo : goodsInfos) {
            WxUpdateProductWithoutAuditRequest.Sku sku = new WxUpdateProductWithoutAuditRequest.Sku();
            sku.setOutSkuId(goodsInfo.getGoodsInfoId());
            BigDecimal price = goodsInfo.getMarketPrice().multiply(BigDecimal.valueOf(100));
            sku.setMarketPrice(price);
            sku.setSalePrice(price);
            sku.setStockNum(goodsInfo.getStock() == null ? 0 : goodsInfo.getStock().intValue());
            skus.add(sku);
        }
        return skus;
    }

    private List<WxAddProductRequest.Sku> createSkus(Goods goods, List<GoodsInfo> goodsInfos){
        List<WxAddProductRequest.Sku> skus = new ArrayList<>();
        for (GoodsInfo goodsInfo : goodsInfos) {
            WxAddProductRequest.Sku sku = new WxAddProductRequest.Sku();
            sku.setOutProductId(goods.getGoodsId());
            sku.setOutSkuId(goodsInfo.getGoodsInfoId());
            sku.setThumbImg(exchangeWxImgUrl(goodsInfo.getGoodsInfoImg()));
            BigDecimal price = goodsInfo.getMarketPrice().multiply(BigDecimal.valueOf(100));
            sku.setSalePrice(price);
            sku.setMarketPrice(price);
            sku.setStockNum(goodsInfo.getStock().intValue());
//            sku.setSkuAttrs();
            skus.add(sku);
        }
        return skus;
    }

    private String exchangeWxImgUrl(String imgUrl){
        if(imgUrl == null) return null;
        WxUploadImageRequest wxUploadImageRequest = new WxUploadImageRequest();
        wxUploadImageRequest.setImgUrl(imgUrl);
        BaseResponse<WxUploadImageResponse> wxUploadImageResponseBaseResponse = wxGoodsApiController.uploadImg(wxUploadImageRequest);
        WxUploadImageResponse uploadImageResponse = wxUploadImageResponseBaseResponse.getContext();
        if(uploadImageResponse.isSuccess()){
            return uploadImageResponse.getImgInfo().getTempImgUrl();
        }
        return null;
    }
}
