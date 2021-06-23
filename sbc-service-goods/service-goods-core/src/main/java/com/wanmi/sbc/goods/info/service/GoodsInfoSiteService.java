package com.wanmi.sbc.goods.info.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoRequest;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.images.GoodsImageRepository;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoDetailResponse;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsPropDetailRelRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpecDetail;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecDetailRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品SKU服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class GoodsInfoSiteService {

    @Autowired
    private GoodsImageRepository goodsImageRepository;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsPropDetailRelRepository goodsPropDetailRelRepository;

    @Autowired
    private GoodsSpecRepository goodsSpecRepository;

    @Autowired
    private GoodsSpecDetailRepository goodsSpecDetailRepository;

    @Autowired
    private GoodsInfoSpecDetailRelRepository goodsInfoSpecDetailRelRepository;

    /**
     * 获取商品详情
     * 计算会员和订货区间
     * @param infoRequest
     * @return
     * @throws SbcRuntimeException
     */
    public GoodsInfoDetailResponse detail(GoodsInfoRequest infoRequest) throws SbcRuntimeException {
        GoodsInfoDetailResponse response = new GoodsInfoDetailResponse();
        GoodsInfo goodsInfo = goodsInfoRepository.findById(infoRequest.getGoodsInfoId()).orElse(null);
        if (Objects.isNull(goodsInfo)
                || Objects.equals(DeleteFlag.YES, goodsInfo.getDelFlag())
                || Objects.equals(AddedFlag.NO.toValue(), goodsInfo.getAddedFlag())
                || (!Objects.equals(CheckStatus.CHECKED, goodsInfo.getAuditStatus()))) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        Goods goods = goodsRepository.findById(goodsInfo.getGoodsId()).orElse(null);
        if (Objects.isNull(goods)
                || (!Objects.equals(CheckStatus.CHECKED, goods.getAuditStatus()))) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        goodsInfo.setPriceType(goods.getPriceType());
        //销售价
        goodsInfo.setSalePrice(Objects.isNull(goodsInfo.getMarketPrice()) ? BigDecimal.ZERO : goodsInfo.getMarketPrice());
        //SPU图片
        response.setImages(goodsImageRepository.findByGoodsId(goods.getGoodsId()));
        //商品属性
        response.setGoodsPropDetailRels(goodsPropDetailRelRepository.queryByGoodsId(goods.getGoodsId()));

        //如果是多规格
        if (Constants.yes.equals(goods.getMoreSpecFlag())) {
            response.setGoodsSpecs(goodsSpecRepository.findByGoodsId(goods.getGoodsId()));
            response.setGoodsSpecDetails(goodsSpecDetailRepository.findByGoodsId(goods.getGoodsId()));

            //对每个规格填充规格值关系
            response.getGoodsSpecs().stream().forEach(goodsSpec -> {
                goodsSpec.setSpecDetailIds(response.getGoodsSpecDetails().stream().filter(specDetail -> specDetail.getSpecId().equals(goodsSpec.getSpecId())).map(GoodsSpecDetail::getSpecDetailId).collect(Collectors.toList()));
            });

            //对每个SKU填充规格和规格值关系
            List<GoodsInfoSpecDetailRel> goodsInfoSpecDetailRels = goodsInfoSpecDetailRelRepository.findByGoodsId(goods.getGoodsId());
            goodsInfo.setMockSpecIds(goodsInfoSpecDetailRels.stream().filter(detailRel -> detailRel.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).map(GoodsInfoSpecDetailRel::getSpecId).collect(Collectors.toList()));
            goodsInfo.setMockSpecDetailIds(goodsInfoSpecDetailRels.stream().filter(detailRel -> detailRel.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).map(GoodsInfoSpecDetailRel::getSpecDetailId).collect(Collectors.toList()));
            goodsInfo.setSpecText(StringUtils.join(goodsInfoSpecDetailRels.stream().filter(specDetailRel -> goodsInfo.getGoodsInfoId().equals(specDetailRel.getGoodsInfoId())).map(GoodsInfoSpecDetailRel::getDetailName).collect(Collectors.toList()), " "));
        }

        response.setGoodsInfo(goodsInfo);
        response.setGoods(goods);
        return response;
    }
}
