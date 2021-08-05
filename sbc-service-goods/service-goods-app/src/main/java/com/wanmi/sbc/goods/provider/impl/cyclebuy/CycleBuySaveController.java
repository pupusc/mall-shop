package com.wanmi.sbc.goods.provider.impl.cyclebuy;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.provider.cyclebuy.CycleBuySaveProvider;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyAddRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyDelByIdRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyModifyRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuySaleRequest;
import com.wanmi.sbc.goods.api.response.cyclebuy.CycleBuyAddResponse;
import com.wanmi.sbc.goods.api.response.cyclebuy.CycleBuyByIdResponse;
import com.wanmi.sbc.goods.api.response.cyclebuy.CycleBuyModifyResponse;
import com.wanmi.sbc.goods.bean.dto.CycleBuyGiftDTO;
import com.wanmi.sbc.goods.bean.enums.*;
import com.wanmi.sbc.goods.bean.vo.CycleBuyVO;
import com.wanmi.sbc.goods.cyclebuy.model.root.CycleBuy;
import com.wanmi.sbc.goods.cyclebuy.model.root.CycleBuyGift;
import com.wanmi.sbc.goods.cyclebuy.repository.CycleBuyGiftRepository;
import com.wanmi.sbc.goods.cyclebuy.service.CycleBuyService;
import com.wanmi.sbc.goods.images.GoodsImage;
import com.wanmi.sbc.goods.images.GoodsImageRepository;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import com.wanmi.sbc.goods.info.repository.GoodsPropDetailRelRepository;
import com.wanmi.sbc.goods.info.request.GoodsSaveRequest;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpec;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpecDetail;
import com.wanmi.sbc.goods.storecate.model.root.StoreCateGoodsRela;
import com.wanmi.sbc.goods.storecate.repository.StoreCateGoodsRelaRepository;
import com.wanmi.sbc.goods.storegoodstab.model.root.GoodsTabRela;
import com.wanmi.sbc.goods.storegoodstab.repository.GoodsTabRelaRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>周期购活动保存服务接口实现</p>
 *
 * @author weiwenhao
 * @date 2021-01-21 09:15:37
 */
@RestController
@Validated
public class CycleBuySaveController implements CycleBuySaveProvider {


    @Autowired
    private CycleBuyService cycleBuyService;


    @Autowired
    private GoodsService goodsService;


    @Autowired
    private GoodsImageRepository goodsImageRepository;

    @Autowired
    private GoodsPropDetailRelRepository goodsPropDetailRelRepository;

    @Autowired
    private CycleBuyGiftRepository cycleBuyGiftRepository;

    @Autowired
    private GoodsTabRelaRepository goodsTabRelaRepository;

    @Autowired
    private StoreCateGoodsRelaRepository storeCateGoodsRelaRepository;

    @Override
    @Transactional
    public BaseResponse<CycleBuyAddResponse> add(@RequestBody @Valid CycleBuyAddRequest cycleBuyAddRequest) {

        cycleBuyAddRequest.getGoodsInfoDTOS().forEach(goodsInfoDTO -> {
            if (goodsInfoDTO.getCycleNum()<=0) {
                throw new SbcRuntimeException(GoodsErrorCode.NUMBER_PERIODS);
            }
        });

        //根据商品Id查询商品信息
        Goods goods = goodsService.getGoodsById(cycleBuyAddRequest.getOriginGoodsId());
        if (Objects.isNull(goods)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }


        GoodsSaveRequest goodsSaveRequest = new GoodsSaveRequest();

        Goods newGoods = KsBeanUtil.convert(goods, Goods.class);
        newGoods.setGoodsId(null);
        newGoods.setGoodsNo(cycleBuyAddRequest.getGoodsNo());
        //商品类型：周期购商品
        newGoods.setGoodsType(GoodsType.CYCLE_BUY.toValue());
        //审核状态：已审核
        newGoods.setAuditStatus(CheckStatus.CHECKED);

        //上下架：已上架
        newGoods.setAddedFlag(AddedFlag.YES.toValue());

        //是否定时上下架
        newGoods.setAddedTimingFlag(Boolean.FALSE);

        newGoods.setMoreSpecFlag(Constants.yes);

        //购买方式：1 立即购买
        newGoods.setGoodsBuyTypes(Constants.yes.toString());

        //周期购商品必须是零售
        newGoods.setSaleType(SaleType.RETAIL.toValue());

        //周期购商品设价方式
        newGoods.setPriceType(Constants.PRICETYPE);

        //设置ERP编码
        newGoods.setErpGoodsNo(cycleBuyAddRequest.getErpGoodsNo());

        //周期购商品必须是零售
        newGoods.setSaleType(1);

        goodsSaveRequest.setGoods(newGoods);

        //查询原商品信息
        List<GoodsImage> goodsImages = goodsImageRepository.findByGoodsId(cycleBuyAddRequest.getOriginGoodsId());
        if (CollectionUtils.isNotEmpty(goodsImages)) {
            List<GoodsImage> goodsImageList=new ArrayList<>();
            goodsImages.forEach(goodsImage -> {
                GoodsImage image= KsBeanUtil.convert(goodsImage,GoodsImage.class);
                image.setImageId(null);
                goodsImageList.add(image);
            });
            goodsSaveRequest.setImages(goodsImageList);
        }
        //查询商品的基础属性
        List<GoodsPropDetailRel> goodsPropDetailRels = goodsPropDetailRelRepository.queryByGoodsId(cycleBuyAddRequest.getOriginGoodsId());
        if (CollectionUtils.isNotEmpty(goodsPropDetailRels)) {
            List<GoodsPropDetailRel> goodsPropDetailRelsList=new ArrayList<>();
            goodsPropDetailRels.forEach(goodsPropDetailRel -> {
                GoodsPropDetailRel propDetailRel= KsBeanUtil.convert(goodsPropDetailRel,GoodsPropDetailRel.class);
                propDetailRel.setRelId(null);
                goodsPropDetailRelsList.add(propDetailRel);
            });
            goodsSaveRequest.setGoodsPropDetailRels(goodsPropDetailRelsList);
        }
        List<GoodsTabRela> goodsTabRelas = goodsTabRelaRepository.findByGoodsId(cycleBuyAddRequest.getOriginGoodsId());
        if (CollectionUtils.isNotEmpty(goodsTabRelas)) {
            goodsSaveRequest.setGoodsTabRelas(goodsTabRelas);
        }

        //sku列表
        List<GoodsInfo> goodsInfos = KsBeanUtil.convert(cycleBuyAddRequest.getGoodsInfoDTOS(), GoodsInfo.class);
        goodsInfos.forEach(goodsInfo -> {
            goodsInfo.setGoodsType(GoodsType.CYCLE_BUY.toValue());
            //周期购商品必须是零售
            goodsInfo.setSaleType(SaleType.RETAIL.toValue());
            //如果选择的是企业购商品，需要设置成不是企业购的商品
            goodsInfo.setEnterPriseAuditState(EnterpriseAuditState.INIT);

            //设置erp编码spu
            goodsInfo.setErpGoodsNo(cycleBuyAddRequest.getErpGoodsNo());

            //设置是否组合商品
            goodsInfo.setCombinedCommodity(Boolean.FALSE);

        });
        goodsSaveRequest.setGoodsInfos(goodsInfos);

        //商品规格
        List<GoodsSpec> goodsSpecs = KsBeanUtil.convert( cycleBuyAddRequest.getGoodsSpecs(), GoodsSpec.class);
        if(CollectionUtils.isNotEmpty(goodsSpecs)){
            goodsSpecs.forEach(goodsSpec -> {
                goodsSpec.setSpecId(null);
            });
            goodsSaveRequest.setGoodsSpecs(goodsSpecs);
        }
        //商品规格值列表
        List<GoodsSpecDetail> goodsSpecDetails = KsBeanUtil.convert( cycleBuyAddRequest.getGoodsSpecDetails(), GoodsSpecDetail.class);

        if(CollectionUtils.isNotEmpty(goodsSpecDetails)){
            goodsSpecDetails.forEach(goodsSpecDetail -> {
                goodsSpecDetail.setSpecDetailId(null);
            });
            goodsSaveRequest.setGoodsSpecDetails(goodsSpecDetails);
        }

        //多个店铺分类编号
        List<StoreCateGoodsRela> storeCateGoodsRelas= storeCateGoodsRelaRepository.selectByGoodsId(Arrays.asList(goods.getGoodsId()));
        List<Long> storeCate=storeCateGoodsRelas.stream().map(StoreCateGoodsRela::getStoreCateId).collect(Collectors.toList());
        newGoods.setStoreCateIds(storeCate);

        //新增商品信息
        String goodsId = goodsService.add(goodsSaveRequest);

        CycleBuy cycleBuy = new CycleBuy();
        KsBeanUtil.copyPropertiesThird(cycleBuyAddRequest, cycleBuy);
        cycleBuy.setGoodsId(goodsId);
        cycleBuy.setSendDateRule(cycleBuyAddRequest.getSendDateRules());
        //新增活动信息
        CycleBuy cycleBuy1 = cycleBuyService.add(cycleBuy);

        //保存赠品
        if (CollectionUtils.isNotEmpty(cycleBuyAddRequest.getCycleBuyGiftDTOList())) {
            //判断赠品是否大于库存
           List<String> goosInfoIds= cycleBuyAddRequest.getCycleBuyGiftDTOList().stream().map(CycleBuyGiftDTO::getGoodsInfoId).collect(Collectors.toList());
           List<GoodsInfo> goodsInfoList= goodsService.findGoodsInfo(goosInfoIds);
            cycleBuyAddRequest.getCycleBuyGiftDTOList().forEach(cycleBuyGiftDTO -> {
                goodsInfoList.forEach(goodsInfo -> {
                    if (Objects.equals(goodsInfo.getGoodsInfoId(),cycleBuyGiftDTO.getGoodsInfoId()) && cycleBuyGiftDTO.getFreeQuantity()>goodsInfo.getStock()) {
                        throw new SbcRuntimeException(GoodsErrorCode.GIFT_INVENTORY);
                    }
                });
                cycleBuyGiftDTO.setCycleBuyId(cycleBuy1.getId());
            });
            List<CycleBuyGift> cycleBuyGifts = KsBeanUtil.copyListProperties(cycleBuyAddRequest.getCycleBuyGiftDTOList(), CycleBuyGift.class);
            cycleBuyGiftRepository.saveAll(cycleBuyGifts);
        }

        return BaseResponse.success(new CycleBuyAddResponse(
                cycleBuyService.wrapperVo(cycleBuy1)));
    }

    @Override
    public BaseResponse<CycleBuyModifyResponse> modify(@RequestBody @Valid CycleBuyModifyRequest cycleBuyModifyRequest) {

        cycleBuyModifyRequest.getGoodsInfoDTOS().forEach(goodsInfoDTO -> {
            if (goodsInfoDTO.getCycleNum()<=0) {
                throw new SbcRuntimeException(GoodsErrorCode.NUMBER_PERIODS);
            }
        });


        CycleBuy cycleBuy = cycleBuyService.getById(cycleBuyModifyRequest.getId());

        if (Objects.nonNull(cycleBuyModifyRequest.getActivityName())) {
            cycleBuy.setActivityName(cycleBuyModifyRequest.getActivityName());
        }

        if (Objects.nonNull(cycleBuyModifyRequest.getRecordActivities())) {
            cycleBuy.setRecordActivities(cycleBuyModifyRequest.getRecordActivities());
        }

        if (Objects.nonNull(cycleBuyModifyRequest.getDeliveryPlan())) {
            cycleBuy.setDeliveryPlan(cycleBuyModifyRequest.getDeliveryPlan());
        }

        if (Objects.nonNull(cycleBuyModifyRequest.getGiftGiveMethod())) {
            cycleBuy.setGiftGiveMethod(cycleBuyModifyRequest.getGiftGiveMethod());
        }


        if (Objects.nonNull(cycleBuyModifyRequest.getCycleFreightType())) {
            cycleBuy.setCycleFreightType(cycleBuyModifyRequest.getCycleFreightType());
        }

        if (Objects.nonNull(cycleBuyModifyRequest.getDeliveryCycle())) {
            cycleBuy.setDeliveryCycle(cycleBuyModifyRequest.getDeliveryCycle());
        }

        //发货日期
        cycleBuy.setSendDateRule(cycleBuyModifyRequest.getSendDateRules());

        CycleBuy  cycleBuyModify=cycleBuyService.modify(cycleBuy);

        //编辑赠品
        List<CycleBuyGift> cycleBuyGiftList=cycleBuyGiftRepository.findByCycleBuyId(cycleBuyModify.getId());
        if (CollectionUtils.isNotEmpty(cycleBuyGiftList)) {
            cycleBuyGiftRepository.deleteAll(cycleBuyGiftList);
        }
        if (CollectionUtils.isNotEmpty(cycleBuyModifyRequest.getCycleBuyGiftDTOList())) {

            //判断赠品是否大于库存
            List<String> goosInfoIds= cycleBuyModifyRequest.getCycleBuyGiftDTOList().stream().map(CycleBuyGiftDTO::getGoodsInfoId).collect(Collectors.toList());
            List<GoodsInfo> goodsInfoList= goodsService.findGoodsInfo(goosInfoIds);

            cycleBuyModifyRequest.getCycleBuyGiftDTOList().forEach(cycleBuyGiftDTO -> {

                goodsInfoList.forEach(goodsInfo -> {
                    if (Objects.equals(goodsInfo.getGoodsInfoId(),cycleBuyGiftDTO.getGoodsInfoId()) && cycleBuyGiftDTO.getFreeQuantity()>goodsInfo.getStock()) {
                        throw new SbcRuntimeException(GoodsErrorCode.GIFT_INVENTORY);
                    }
                });

                cycleBuyGiftDTO.setCycleBuyId(cycleBuyModify.getId());
            });
            List<CycleBuyGift> cycleBuyGifts = KsBeanUtil.copyListProperties(cycleBuyModifyRequest.getCycleBuyGiftDTOList(), CycleBuyGift.class);
            cycleBuyGiftRepository.saveAll(cycleBuyGifts);
        }
        return BaseResponse.success(new CycleBuyModifyResponse(
                cycleBuyService.wrapperVo(cycleBuyModify)));
    }


    @Override
    public BaseResponse<CycleBuyByIdResponse> deleteById(@RequestBody @Valid CycleBuyDelByIdRequest cycleBuyDelByIdRequest) {

        CycleBuy cycleBuy = cycleBuyService.getById(cycleBuyDelByIdRequest.getId());
        if (Objects.isNull(cycleBuy)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST_CYCLEBUY);
        }

        cycleBuyService.deleteById(cycleBuy.getId());

        return BaseResponse.success(CycleBuyByIdResponse.builder().cycleBuyVO(KsBeanUtil.convert(cycleBuy,CycleBuyVO.class)).build());
    }

    @Override
    public BaseResponse<CycleBuyByIdResponse> loading(@Valid CycleBuySaleRequest cycleBuySaleRequest) {
        CycleBuy cycleBuy = cycleBuyService.getById(cycleBuySaleRequest.getId());
        if (Objects.isNull(cycleBuy)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST_CYCLEBUY);
        }
        if (AddedFlag.NO.equals(cycleBuySaleRequest.getAddedFlag())) {
            cycleBuy.setAddedFlag(AddedFlag.NO);
        }
        if (AddedFlag.YES.equals(cycleBuySaleRequest.getAddedFlag())) {
            cycleBuy.setAddedFlag(AddedFlag.YES);
        }
        cycleBuyService.modify(cycleBuy);
        return BaseResponse.success(CycleBuyByIdResponse.builder().cycleBuyVO(KsBeanUtil.convert(cycleBuy,CycleBuyVO.class)).build());
    }

}

