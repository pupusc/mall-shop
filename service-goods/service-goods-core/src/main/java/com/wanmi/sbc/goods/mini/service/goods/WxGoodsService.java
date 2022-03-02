package com.wanmi.sbc.goods.mini.service.goods;

import com.soybean.mall.wx.mini.common.bean.request.WxUploadImageRequest;
import com.soybean.mall.wx.mini.common.bean.response.WxUploadImageResponse;
import com.soybean.mall.wx.mini.goods.bean.request.WxAddProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxUpdateProductWithoutAuditRequest;
import com.soybean.mall.wx.mini.goods.bean.response.WxAddProductResponse;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.goods.controller.WxGoodsApiController;
import com.wanmi.sbc.common.base.BaseResponse;
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
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class WxGoodsService {

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

    @Transactional
    public Page<WxGoodsModel> listGoods(WxGoodsSearchRequest wxGoodsSearchRequest){
        Specification<WxGoodsModel> specification = wxGoodsRepository.buildSearchCondition(wxGoodsSearchRequest);
        return wxGoodsRepository.findAll(specification, PageRequest.of(wxGoodsSearchRequest.getPageNum(), wxGoodsSearchRequest.getPageSize()));
    }

    @Transactional
    public void addGoods(WxGoodsCreateRequest createRequest){

        LocalDateTime now = LocalDateTime.now();
        String goodsIdStr = createRequest.getGoodsId();
        String[] split = goodsIdStr.split(",");
        List<WxGoodsModel> wxGoodsModels = new ArrayList<>();
        for (String goodsId : split) {
            if(goodsExist(goodsId)) throw new SbcRuntimeException("商品重复添加" + goodsId);
            WxGoodsModel wxGoodsModel = new WxGoodsModel();
            wxGoodsModel.setGoodsId(goodsId);
            wxGoodsModel.setStatus(WxGoodsStatus.UPLOAD);
            wxGoodsModel.setAuditStatus(WxGoodsEditStatus.WAIT_CHECK);
            wxGoodsModel.setUploadTime(now);
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
        if(wxGoodsModel.getWxCategory() == null) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "请填写微信商品类目");
        if(wxGoodsModel.getNeedToAudit() == 0) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品无需审核");

        Goods goods = goodsService.findByGoodsId(createRequest.getGoodsId());
        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsId(createRequest.getGoodsId()).delFlag(DeleteFlag.NO.toValue()).build());
        if(wxGoodsModel.getNeedToAudit() == 1) {
            WxGoodsEditStatus auditStatus = wxGoodsModel.getAuditStatus();
            if(auditStatus.equals(WxGoodsEditStatus.WAIT_CHECK)){
                //初次提审
                BaseResponse<WxAddProductResponse> baseResponse = wxGoodsApiController.addGoods(createWxAddProductRequestByGoods(goods, goodsInfos, wxGoodsModel.getWxCategory()));
                if(!baseResponse.getContext().isSuccess()){
                    throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, baseResponse.getContext().getErrmsg());
                }
                wxGoodsModel.setAuditStatus(WxGoodsEditStatus.ON_CHECK);
                wxGoodsRepository.save(wxGoodsModel);
            }else if(auditStatus.equals(WxGoodsEditStatus.ON_CHECK)){

                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品正在审核中");
            }else if(auditStatus.equals(WxGoodsEditStatus.CHECK_FAILED) || auditStatus.equals(WxGoodsEditStatus.CHECK_CANCEL)){
                //更新商品,重新提审
                BaseResponse<WxAddProductResponse> baseResponse = wxGoodsApiController.updateGoods(createWxAddProductRequestByGoods(goods, goodsInfos, wxGoodsModel.getWxCategory()));
                if(!baseResponse.getContext().isSuccess()){
                    throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, baseResponse.getContext().getErrmsg());
                }
                wxGoodsModel.setAuditStatus(WxGoodsEditStatus.ON_CHECK);
                wxGoodsRepository.save(wxGoodsModel);
            }
        }else if(wxGoodsModel.getNeedToAudit() == 2){
            //需要免审更新
            BaseResponse<WxResponseBase> baseResponse = wxGoodsApiController.updateGoodsWithoutAudit(createWxUpdateProductWithoutAuditRequest(goods, goodsInfos));
            if(!baseResponse.getContext().isSuccess()){
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, baseResponse.getContext().getErrmsg());
            }
            wxGoodsModel.setAuditStatus(WxGoodsEditStatus.CHECK_SUCCESS);
            wxGoodsRepository.save(wxGoodsModel);
        }
    }

    public void update(WxGoodsCreateRequest createRequest){
        WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(createRequest.getGoodsId(), DeleteFlag.NO);
        if(wxGoodsModel == null) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品不存在");

        if(createRequest.getWxCategory() != null){
            wxGoodsModel.setWxCategory(createRequest.getWxCategory());
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
    public void auditCallback(Map<String, Object> paramMap){
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
        }
    }

    private boolean goodsExist(String goodsId){
        Integer onShelfCount = wxGoodsRepository.getOnShelfCount(goodsId);
        return onShelfCount > 0;
    }

    public WxAddProductRequest createWxAddProductRequestByGoods(Goods goods, List<GoodsInfo> goodsInfos, Integer thirdCatId){
        WxAddProductRequest addProductRequest = new WxAddProductRequest();
        addProductRequest.setOutProductId(goods.getGoodsId());
        addProductRequest.setTitle(goods.getGoodsSubtitle());
        addProductRequest.setPath("http://www.baidu.com");
        addProductRequest.setHeadImg(Collections.singletonList(exchangeWxImgUrl(goods.getGoodsImg())));
//        addProductRequest.setHeadImg(Collections.singletonList(goods.getGoodsImg()));
//        addProductRequest.setQualificationics(Collections.singletonList(goods.getGoodsImg()));
        //商品详情截取图片
        String detail = goods.getGoodsMobileDetail();
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
        //todo 改成变量
//        addProductRequest.setThirdCatId(378031);
        addProductRequest.setThirdCatId(thirdCatId);
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
            BigDecimal marketPrice = goodsInfo.getMarketPrice();
            if(marketPrice == null) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "sku缺少价格信息:" + goodsInfo.getGoodsInfoId());
            sku.setMarketPrice(marketPrice);
            sku.setSalePrice(marketPrice);
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
            sku.setSalePrice(goodsInfo.getMarketPrice());
            sku.setMarketPrice(goodsInfo.getMarketPrice());
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
