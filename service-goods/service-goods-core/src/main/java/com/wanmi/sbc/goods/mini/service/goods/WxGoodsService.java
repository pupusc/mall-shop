package com.wanmi.sbc.goods.mini.service.goods;

import com.soybean.mall.wx.mini.goods.bean.request.WxAddProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
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
        if(goodsExist(createRequest)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品已上架");
        WxGoodsModel wxGoodsModel = new WxGoodsModel();
        wxGoodsModel.setGoodsInfoId(createRequest.getGoodsId());
        wxGoodsModel.setGoodsInfoId(createRequest.getGoodsInfoId());
        wxGoodsModel.setWxCategory(createRequest.getWxCategory());
        wxGoodsModel.setStatus(WxGoodsStatus.ON_UPLOAD);
        wxGoodsModel.setAuditStatus(WxGoodsEditStatus.WAIT_CHECK);
        wxGoodsModel.setAuditTimes(0);
        LocalDateTime now = LocalDateTime.now();
        wxGoodsModel.setCreateTime(now);
        wxGoodsModel.setUpdateTime(now);
        wxGoodsModel.setDelFlag(DeleteFlag.NO);
        wxGoodsRepository.save(wxGoodsModel);

        Goods goods = goodsService.findByGoodsId(createRequest.getGoodsId());
        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsId(createRequest.getGoodsId()).build());
        BaseResponse<Boolean> baseResponse = wxGoodsApiController.addGoods(createWxAddProductRequestByGoods(goods, goodsInfos, createRequest.getWxCategory()));
        if(!baseResponse.getContext()){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "上传微信商品失败");
        }
    }

    @Transactional
    public void deleteGoods(WxDeleteProductRequest wxDeleteProductRequest){
        Goods goods = goodsService.findByGoodsId(wxDeleteProductRequest.getOutProductId());
        if(goods != null){
            goods.setDelFlag(DeleteFlag.NO);
            goodsService.save(goods);
        }
        BaseResponse<Boolean> response = wxGoodsApiController.deleteGoods(wxDeleteProductRequest);
        if(!response.getContext()){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "删除微信商品失败");
        }
    }

    @Transactional
    public void updateGoodsAndReaudit(WxGoodsCreateRequest createRequest){
        Optional<WxGoodsModel> opt = wxGoodsRepository.findById(createRequest.getId());
        if(opt.isPresent()){
            WxGoodsModel wxGoodsModel = opt.get();
            boolean updated = false;
            if(!createRequest.getWxCategory().equals(wxGoodsModel.getWxCategory())){
                wxGoodsModel.setWxCategory(createRequest.getWxCategory());
                updated = true;
            }
            if(updated){
                wxGoodsModel.setUpdateTime(LocalDateTime.now());
                wxGoodsModel.setAuditStatus(WxGoodsEditStatus.WAIT_CHECK);
//                wxGoodsModel.setStatus(WxGoodsStatus.ON_SHELF);
                wxGoodsRepository.save(wxGoodsModel);
            }

            Goods goods = goodsService.findByGoodsId(createRequest.getGoodsId());
            List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsId(createRequest.getGoodsId()).build());
            BaseResponse baseResponse = wxGoodsApiController.addGoods(createWxAddProductRequestByGoods(goods, goodsInfos, createRequest.getWxCategory()));

            if(!CommonErrorCode.SUCCESSFUL.equals(baseResponse.getCode())){
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "上传微信商品失败");
            }
        }
    }

    public void auditCallback(Map<String, Object> paramMap){
        Map<String, String> auditResult = (Map<String, String>) paramMap.get("OpenProductSpuAudit");
        String wxStatus = auditResult.get("status");
        String goodsId = auditResult.get("out_product_id");
        if("4".equals(wxStatus)){
            //成功
            WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(goodsId, DeleteFlag.NO);
            wxGoodsModel.setStatus(WxGoodsStatus.ON_SHELF);
            wxGoodsModel.setAuditStatus(WxGoodsEditStatus.CHECK_SUCCESS);
            wxGoodsModel.setAuditTimes(wxGoodsModel.getAuditTimes() + 1);
            wxGoodsRepository.save(wxGoodsModel);

            WxReviewLogModel wxReviewLogModel = new WxReviewLogModel();
            wxReviewLogModel.setRelateId(wxGoodsModel.getId());
            wxReviewLogModel.setReviewedTime(LocalDateTime.now());
            wxReviewLogModel.setReviewResult(WxReviewResult.SUCCESS);
            wxReviewLogRepository.save(wxReviewLogModel);

            /*WxGoodsStatus wxGoodsStatus = wxGoodsModel.getStatus();
            WxGoodsEditStatus editStatus = wxGoodsModel.getEditStatus();
            if(wxGoodsStatus.equals(WxGoodsStatus.ON_UPLOAD) && editStatus.equals(WxGoodsEditStatus.ON_CHECK)){
                //第一次审核成功

            }else if(wxGoodsStatus.equals(WxGoodsStatus.UPLOAD) && editStatus.equals(WxGoodsEditStatus.ON_CHECK)){

            }*/
        }else if("3".equals(wxStatus)){
            //失败
            String rejectReason = auditResult.get("reject_reason");
            WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(goodsId, DeleteFlag.NO);
            wxGoodsModel.setStatus(WxGoodsStatus.UPLOAD);
            wxGoodsModel.setAuditStatus(WxGoodsEditStatus.CHECK_FAILED);
            wxGoodsRepository.save(wxGoodsModel);

            WxReviewLogModel wxReviewLogModel = new WxReviewLogModel();
            wxReviewLogModel.setRelateId(wxGoodsModel.getId());
            wxReviewLogModel.setReviewedTime(LocalDateTime.now());
            wxReviewLogModel.setReviewReason(rejectReason);
            wxReviewLogModel.setReviewResult(WxReviewResult.FAILED);
            wxReviewLogRepository.save(wxReviewLogModel);
        }
    }

    private boolean goodsExist(WxGoodsCreateRequest createRequest){
        Integer onShelfCount = wxGoodsRepository.getOnShelfCount(createRequest.getGoodsId());
        return onShelfCount > 0;
    }

    public WxAddProductRequest createWxAddProductRequestByGoods(Goods goods, List<GoodsInfo> goodsInfos, Integer thirdCatId){
        WxAddProductRequest addProductRequest = new WxAddProductRequest();
        addProductRequest.setOutProductId(goods.getGoodsId());
        addProductRequest.setTitle(goods.getGoodsSubtitle());
        addProductRequest.setPath("http://www.baidu.com");
        addProductRequest.setHeadImg(Collections.singletonList("https://mmecimage.cn/p/wx77e672d6d34a4bed/HNTiaPWTllJ5R2pq9Jv9jRD5bZOWmq2svUUzJcZbcg"));
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
                        detailImgs.add(s2.substring(0, s2.indexOf("\"")));
                    }
                }
            }
            addProductRequest.setDescInfo(WxAddProductRequest.DescInfo.builder().imgs(detailImgs).desc("").build());
        }
        //todo 改成变量
        addProductRequest.setThirdCatId(378031);
        addProductRequest.setBrandId(2100000000);
        addProductRequest.setInfoVersion("1");
        addProductRequest.setSkus(createSkus(goods, goodsInfos));
        return addProductRequest;
    }

    private List<WxAddProductRequest.Sku> createSkus(Goods goods, List<GoodsInfo> goodsInfos){
        List<WxAddProductRequest.Sku> skus = new ArrayList<>();
        for (GoodsInfo goodsInfo : goodsInfos) {
            WxAddProductRequest.Sku sku = new WxAddProductRequest.Sku();
            sku.setOutProductId(goods.getGoodsId());
            sku.setOutSkuId(goodsInfo.getGoodsInfoId());
            sku.setThumbImg(goodsInfo.getGoodsInfoImg());
            sku.setSalePrice(goodsInfo.getMarketPrice());
            sku.setMarketPrice(goodsInfo.getMarketPrice());
            sku.setStockNum(goodsInfo.getStock().intValue());
//            sku.setSkuAttrs();
            skus.add(sku);
        }
        return skus;
    }
}
