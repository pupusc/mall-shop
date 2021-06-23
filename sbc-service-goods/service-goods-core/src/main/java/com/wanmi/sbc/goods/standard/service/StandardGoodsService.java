package com.wanmi.sbc.goods.standard.service;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.GoodsBrandErrorCode;
import com.wanmi.sbc.goods.api.constant.GoodsCateErrorCode;
import com.wanmi.sbc.goods.api.constant.StandardGoodsErrorCode;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.bean.enums.UnAddedFlagReason;
import com.wanmi.sbc.goods.bean.vo.StandardSkuVO;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.brand.repository.GoodsBrandRepository;
import com.wanmi.sbc.goods.brand.request.GoodsBrandQueryRequest;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.cate.request.GoodsCateQueryRequest;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardGoodsRel;
import com.wanmi.sbc.goods.standard.model.root.StandardPropDetailRel;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRelRepository;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRepository;
import com.wanmi.sbc.goods.standard.repository.StandardPropDetailRelRepository;
import com.wanmi.sbc.goods.standard.repository.StandardSkuRepository;
import com.wanmi.sbc.goods.standard.request.StandardQueryRequest;
import com.wanmi.sbc.goods.standard.request.StandardSaveRequest;
import com.wanmi.sbc.goods.standard.request.StandardSkuQueryRequest;
import com.wanmi.sbc.goods.standard.response.StandardEditResponse;
import com.wanmi.sbc.goods.standard.response.StandardQueryResponse;
import com.wanmi.sbc.goods.standardimages.model.root.StandardImage;
import com.wanmi.sbc.goods.standardimages.repository.StandardImageRepository;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSkuSpecDetailRel;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpec;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpecDetail;
import com.wanmi.sbc.goods.standardspec.repository.StandardSkuSpecDetailRelRepository;
import com.wanmi.sbc.goods.standardspec.repository.StandardSpecDetailRepository;
import com.wanmi.sbc.goods.standardspec.repository.StandardSpecRepository;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品库服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Transactional(readOnly = true)
public class StandardGoodsService {

    @Autowired
    private StandardGoodsRepository standardGoodsRepository;

    @Autowired
    private StandardSkuRepository standardSkuRepository;

    @Autowired
    private StandardSpecRepository standardSpecRepository;

    @Autowired
    private StandardSpecDetailRepository standardSpecDetailRepository;

    @Autowired
    private StandardSkuSpecDetailRelRepository standardSkuSpecDetailRelRepository;

    @Autowired
    private StandardImageRepository standardImageRepository;

    @Autowired
    private GoodsBrandRepository goodsBrandRepository;

    @Autowired
    private GoodsCateRepository goodsCateRepository;

    @Autowired
    private StandardPropDetailRelRepository standardPropDetailRelRepository;

    @Autowired
    private StandardGoodsRelRepository standardGoodsRelRepository;

    @Autowired
    private GoodsCateService goodsCateService;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private EntityManager entityManager;

    public static Integer BOSS_CODE = 3;


    /**
     * 分页查询商品库
     *
     * @param request 参数
     * @return list
     */
    public StandardQueryResponse page(StandardQueryRequest request) {
        StandardQueryResponse response = new StandardQueryResponse();

        List<StandardSku> standardSkus = new ArrayList<>();
        List<StandardSkuSpecDetailRel> standardSkuSpecDetails = new ArrayList<>();
        List<GoodsBrand> goodsBrandList = new ArrayList<>();
        List<GoodsCate> goodsCateList = new ArrayList<>();

        //获取该分类的所有子分类
        if (request.getCateId() != null) {
            request.setCateIds(goodsCateService.getChlidCateId(request.getCateId()));
            if (CollectionUtils.isNotEmpty(request.getCateIds())) {
                request.getCateIds().add(request.getCateId());
                request.setCateId(null);
            }
        }
        if(request.getToLeadType()!=null && request.getToLeadType() != -1){
            List<String> standardIds = standardGoodsRelRepository.
                    findByStoreIds(Collections.singletonList(
                            request.getStoreId())).stream().map(s -> s.getStandardId()).collect(Collectors.toList());
            request.setGoodsIds(standardIds);
        }

        //根据SKU模糊查询SKU，获取SKU编号
        StandardSkuQueryRequest standardSkuQueryRequest = new StandardSkuQueryRequest();

        if (StringUtils.isNotBlank(request.getLikeGoodsInfoNo())) {
            standardSkuQueryRequest.setLikeGoodsInfoNo(request.getLikeGoodsInfoNo());
            standardSkuQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            List<StandardSku> infos = standardSkuRepository.findAll(standardSkuQueryRequest.getWhereCriteria());
              //不知道这是要干嘛，全表查一遍又不用。。先优化掉
//            standardSkuRepository.findAll();
            if (CollectionUtils.isNotEmpty(infos)) {
                request.setGoodsIds(infos.stream().map(StandardSku::getGoodsId).collect(Collectors.toList()));
            } else {
                return response;
            }
        }

        Page<StandardGoods> goodsPage = standardGoodsRepository.findAll(request.getWhereCriteria(), request.getPageRequest());
        if (CollectionUtils.isNotEmpty(goodsPage.getContent())) {
            List<String> goodsIds = goodsPage.getContent().stream().map(StandardGoods::getGoodsId).collect(Collectors.toList());
            //查询所有SKU
            StandardSkuQueryRequest skuQueryRequest = new StandardSkuQueryRequest();
            skuQueryRequest.setGoodsIds(goodsIds);
            skuQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            standardSkus.addAll(standardSkuRepository.findAll(skuQueryRequest.getWhereCriteria()));
            //查询所有SKU规格值关联
            standardSkuSpecDetails.addAll(standardSkuSpecDetailRelRepository.findByGoodsIds(goodsIds));

            //填充每个SKU的规格关系
            standardSkus.forEach(standardSku -> {
                //为空，则以商品库主图
                if (StringUtils.isBlank(standardSku.getGoodsInfoImg())) {
                    standardSku.setGoodsInfoImg(goodsPage.getContent().stream().filter(goods -> goods.getGoodsId().equals(standardSku.getGoodsId())).findFirst().orElse(new StandardGoods()).getGoodsImg());
                }
                standardSku.setSpecDetailRelIds(standardSkuSpecDetails.stream().filter(specDetailRel -> specDetailRel.getGoodsInfoId().equals(standardSku.getGoodsInfoId())).map(StandardSkuSpecDetailRel::getSpecDetailRelId).collect(Collectors.toList()));
                updateStandardSkuStockAndSupplyPrice(standardSku);
            });

            //填充每个SKU的SKU关系
            goodsPage.getContent().forEach(goods -> {
                goods.setGoodsInfoIds(standardSkus.stream().filter(standardSku -> standardSku.getGoodsId().equals(goods.getGoodsId())).map(StandardSku::getGoodsInfoId).collect(Collectors.toList()));
                //取SKU最小市场价
                goods.setMarketPrice(standardSkus.stream().filter(goodsInfo -> goods.getGoodsId().equals(goodsInfo.getGoodsId())).filter(goodsInfo -> Objects.nonNull(goodsInfo.getMarketPrice())).map(StandardSku::getMarketPrice).min(BigDecimal::compareTo).orElse(goods.getMarketPrice()));
                goods.setSupplyPrice(standardSkus.stream().filter(goodsInfo -> goods.getGoodsId().equals(goodsInfo.getGoodsId())).filter(goodsInfo -> Objects.nonNull(goodsInfo.getSupplyPrice())).map(StandardSku::getSupplyPrice).min(BigDecimal::compareTo).orElse(goods.getSupplyPrice()));
                //合计库存
                goods.setStock(standardSkus.stream().filter(goodsInfo -> goodsInfo.getGoodsId().equals(goods.getGoodsId()) && Objects.nonNull(goodsInfo.getStock())).mapToLong(StandardSku::getStock).sum());
            });

            //获取所有品牌
            GoodsBrandQueryRequest brandRequest = new GoodsBrandQueryRequest();
            brandRequest.setDelFlag(DeleteFlag.NO.toValue());
            brandRequest.setBrandIds(goodsPage.getContent().stream().filter(goods -> goods.getBrandId() != null).map(StandardGoods::getBrandId).distinct().collect(Collectors.toList()));
            goodsBrandList.addAll(goodsBrandRepository.findAll(brandRequest.getWhereCriteria()));

            //获取所有分类
            GoodsCateQueryRequest cateRequest = new GoodsCateQueryRequest();
            cateRequest.setCateIds(goodsPage.getContent().stream().filter(goods -> goods.getCateId() != null).map(StandardGoods::getCateId).distinct().collect(Collectors.toList()));
            goodsCateList.addAll(goodsCateRepository.findAll(cateRequest.getWhereCriteria()));
        }

        response.setStandardGoodsPage(goodsPage);
        response.setStandardSkuList(standardSkus);
        response.setStandardSkuSpecDetails(standardSkuSpecDetails);
        response.setGoodsBrandList(goodsBrandList);
        response.setGoodsCateList(goodsCateList);
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = response.getStandardGoodsPage().stream()
                .filter(v -> Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(v.getGoodsSource()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .collect(Collectors.toList());
        List<QueryItemInventoryResponse.Item> stocks = null;
        if (itemIds.size() > 0) {
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
        }
        if (stocks != null) {
            for (StandardSku standardSku : response.getStandardSkuList()) {
                for (QueryItemInventoryResponse.Item spuStock : stocks) {
                    Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList().stream()
                            .filter(v -> String.valueOf(spuStock.getItemId()).equals(standardSku.getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(standardSku.getThirdPlatformSkuId()))
                            .findFirst();
                    if (stock.isPresent()) {
                        standardSku.setStock(stock.get().getInventory().getQuantity());
                    }
                }
            }
            for (StandardGoods standardGoods : response.getStandardGoodsPage()) {
                Long spuStock = response.getStandardSkuList().stream()
                        .filter(v -> v.getGoodsId().equals(standardGoods.getGoodsId()) && v.getStock() != null)
                        .map(v -> v.getStock()).reduce(0L, (aLong, aLong2) -> aLong + aLong2);
                standardGoods.setStock(spuStock);
            }

        }
        return response;
    }

    /**
     * 分页查询商品库
     *
     * @param request 参数
     * @return list
     */
    public Page<StandardGoods> simplePage(StandardQueryRequest request) {
        //分页优化，当百万数据时，先分页提取goodsId
        if (CollectionUtils.isEmpty(request.getGoodsIds())) {
            Page<String> ids = this.pageCols(request);
            if (CollectionUtils.isEmpty(ids.getContent())) {
                return new PageImpl<>(Collections.emptyList(), request.getPageable(), ids.getTotalElements());
            }
            request.setGoodsIds(ids.getContent());
            List<StandardGoods> goods = standardGoodsRepository.findAll(request.getWhereCriteria(), request.getSort());
            return new PageImpl<>(goods, request.getPageable(), ids.getTotalElements());
        }
        return standardGoodsRepository.findAll(request.getWhereCriteria(), request.getPageRequest());
    }

    private void updateStandardSkuStockAndSupplyPrice(StandardSku standardSku) {
        if (StringUtils.isNotBlank(standardSku.getProviderGoodsInfoId()) && !Integer.valueOf(GoodsSource.LINKED_MALL.ordinal()).equals(standardSku.getGoodsSource())) {
            GoodsInfo providerGoodsInfo = goodsInfoRepository.findById(standardSku.getProviderGoodsInfoId()).orElse(null);
            if(providerGoodsInfo!=null){
                standardSku.setStock(providerGoodsInfo.getStock());
                standardSku.setSupplyPrice(providerGoodsInfo.getSupplyPrice());
            }
        }
    }
    /**
     * 根据ID查询商品库
     *
     * @param goodsId 商品库ID
     * @return list
     */
    public StandardEditResponse findInfoById(String goodsId) throws SbcRuntimeException {
        StandardEditResponse response = new StandardEditResponse();
        StandardGoods goods = standardGoodsRepository.findById(goodsId).orElseThrow(() -> new SbcRuntimeException(StandardGoodsErrorCode.NOT_EXIST));

        if(goods!=null && goods.getGoodsSource() == GoodsSource.PROVIDER.toValue() && goods.getStoreId() ==null){
            List<StandardGoodsRel> standardGoodsRelList = standardGoodsRelRepository.findByStandardId(goods.getGoodsId());
            if(CollectionUtils.isNotEmpty(standardGoodsRelList)){
                standardGoodsRelList= standardGoodsRelList.stream().filter(standardGoodsRel -> standardGoodsRel.getDelFlag()==DeleteFlag.NO).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(standardGoodsRelList)){
                    goods.setStoreId(standardGoodsRelList.get(0).getStoreId());
                }
            }
        }

        if(goods!=null && (goods.getGoodsSource() == GoodsSource.PROVIDER.toValue()||goods.getGoodsSource() == GoodsSource.LINKED_MALL.toValue()) && goods.getStoreId()!=null){
            StandardGoodsRel standardGoodsRel = standardGoodsRelRepository.findByStandardIdAndStoreId(goodsId,goods.getStoreId());
            String providerGoodsId = standardGoodsRel.getGoodsId();
            goods.setProviderGoodsId(providerGoodsId);
            goods.setStoreId(standardGoodsRel.getStoreId());
        }

        response.setGoods(goods);

        //查询商品库图片
        response.setImages(standardImageRepository.findByGoodsId(goods.getGoodsId()));

        //查询SKU列表
        StandardSkuQueryRequest infoQueryRequest = new StandardSkuQueryRequest();
        infoQueryRequest.setGoodsId(goods.getGoodsId());
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        List<StandardSku> standardSkus = standardSkuRepository.findAll(infoQueryRequest.getWhereCriteria());

        //商品库属性
        response.setGoodsPropDetailRels(standardPropDetailRelRepository.queryByGoodsId(goods.getGoodsId()));

        //如果是多规格
        if (Constants.yes.equals(goods.getMoreSpecFlag())) {
            response.setGoodsSpecs(standardSpecRepository.findByGoodsId(goods.getGoodsId()));
            response.setGoodsSpecDetails(standardSpecDetailRepository.findByGoodsId(goods.getGoodsId()));

            //对每个规格填充规格值关系
            response.getGoodsSpecs().forEach(standardSpec -> {
                standardSpec.setSpecDetailIds(response.getGoodsSpecDetails().stream().filter(specDetail -> specDetail.getSpecId().equals(standardSpec.getSpecId())).map(StandardSpecDetail::getSpecDetailId).collect(Collectors.toList()));
            });

            //对每个SKU填充规格和规格值关系
            Map<String, List<StandardSkuSpecDetailRel>> standardSkuSpecDetailRels = standardSkuSpecDetailRelRepository.findByGoodsId(goods.getGoodsId()).stream().collect(Collectors.groupingBy(StandardSkuSpecDetailRel::getGoodsInfoId));
            standardSkus.forEach(standardSku -> {
                standardSku.setMockSpecIds(standardSkuSpecDetailRels.getOrDefault(standardSku.getGoodsInfoId(), new ArrayList<>()).stream().map(StandardSkuSpecDetailRel::getSpecId).collect(Collectors.toList()));
                standardSku.setMockSpecDetailIds(standardSkuSpecDetailRels.getOrDefault(standardSku.getGoodsInfoId(), new ArrayList<>()).stream().map(StandardSkuSpecDetailRel::getSpecDetailId).collect(Collectors.toList()));
            });
        }
        response.setGoodsInfos(standardSkus);
        //如果是linkedmall商品，实时查库存
        if (Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(goods.getGoodsSource())) {
            List<QueryItemInventoryResponse.Item> sotckList = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(Collections.singletonList(Long.valueOf(goods.getThirdPlatformSpuId())), "0", null))
                    .getContext();
            if (sotckList != null && sotckList.size() > 0) {
                QueryItemInventoryResponse.Item stock = sotckList.get(0);
                for (StandardSku goodsInfo : response.getGoodsInfos()) {
                    for (QueryItemInventoryResponse.Item.Sku sku : stock.getSkuList()) {
                        if (stock.getItemId().equals(Long.valueOf(goodsInfo.getThirdPlatformSpuId())) && sku.getSkuId().equals(Long.valueOf(goodsInfo.getThirdPlatformSkuId()))) {
                            goodsInfo.setStock(sku.getInventory().getQuantity());
                        }
                    }
                }
            }
        }
        return response;
    }

    /**
     * 商品库新增
     *
     * @param saveRequest
     * @return SPU编号
     * @throws SbcRuntimeException
     */
    @Transactional
    public String add(StandardSaveRequest saveRequest) throws SbcRuntimeException {
        List<StandardImage> standardImages = saveRequest.getImages();
        List<StandardSku> standardSkus = saveRequest.getGoodsInfos();
        StandardGoods goods = saveRequest.getGoods();

        //验证商品库相关基础数据
        this.checkBasic(goods);
        goods.setGoodsSource(BOSS_CODE);
        goods.setDelFlag(DeleteFlag.NO);
        goods.setAddedFlag(AddedFlag.NO.toValue());
        goods.setCreateTime(LocalDateTime.now());
        goods.setUpdateTime(goods.getCreateTime());
        if (CollectionUtils.isNotEmpty(standardImages)) {
            goods.setGoodsImg(standardImages.get(0).getArtworkUrl());
        }
        if (goods.getMoreSpecFlag() == null) {
            goods.setMoreSpecFlag(Constants.no);
        }

        final String goodsId = standardGoodsRepository.save(goods).getGoodsId();

        //新增图片
        if (CollectionUtils.isNotEmpty(standardImages)) {
            standardImages.forEach(standardImage -> {
                standardImage.setCreateTime(goods.getCreateTime());
                standardImage.setUpdateTime(goods.getUpdateTime());
                standardImage.setGoodsId(goodsId);
                standardImage.setDelFlag(DeleteFlag.NO);
                standardImage.setImageId(standardImageRepository.save(standardImage).getImageId());
            });
        }

        //保存商品库属性
        List<StandardPropDetailRel> standardPropDetailRels = saveRequest.getGoodsPropDetailRels();
        //如果是修改则设置修改时间，如果是新增则设置创建时间，
        if (CollectionUtils.isNotEmpty(standardPropDetailRels)) {
            standardPropDetailRels.forEach(standardPropDetailRel -> {
                standardPropDetailRel.setDelFlag(DeleteFlag.NO);
                standardPropDetailRel.setCreateTime(LocalDateTime.now());
                standardPropDetailRel.setGoodsId(goodsId);
            });
            standardPropDetailRelRepository.saveAll(standardPropDetailRels);
        }

        List<StandardSpec> specs = saveRequest.getGoodsSpecs();
        List<StandardSpecDetail> specDetails = saveRequest.getGoodsSpecDetails();

        List<StandardSkuSpecDetailRel> specDetailRels = new ArrayList<>();

        //如果是多规格
        if (Constants.yes.equals(goods.getMoreSpecFlag())) {
            /**
             * 填放可用SKU相应的规格\规格值
             * （主要解决SKU可以前端删除，但相应规格值或规格仍然出现的情况）
             */
            Map<Long, Integer> isSpecEnable = new HashMap<>();
            Map<Long, Integer> isSpecDetailEnable = new HashMap<>();
            standardSkus.forEach(standardSku -> {
                standardSku.getMockSpecIds().forEach(specId -> {
                    isSpecEnable.put(specId, Constants.yes);
                });
                standardSku.getMockSpecDetailIds().forEach(detailId -> {
                    isSpecDetailEnable.put(detailId, Constants.yes);
                });
            });

            //新增规格
            specs.stream()
                    .filter(standardSpec -> Constants.yes.equals(isSpecEnable.get(standardSpec.getMockSpecId()))) //如果SKU有这个规格
                    .forEach(standardSpec -> {
                        standardSpec.setCreateTime(goods.getCreateTime());
                        standardSpec.setUpdateTime(goods.getUpdateTime());
                        standardSpec.setGoodsId(goodsId);
                        standardSpec.setDelFlag(DeleteFlag.NO);
                        standardSpec.setSpecId(standardSpecRepository.save(standardSpec).getSpecId());
                    });
            //新增规格值
            specDetails.stream()
                    .filter(standardSpecDetail -> Constants.yes.equals(isSpecDetailEnable.get(standardSpecDetail.getMockSpecDetailId()))) //如果SKU有这个规格值
                    .forEach(standardSpecDetail -> {
                        Optional<StandardSpec> specOpt = specs.stream().filter(standardSpec -> standardSpec.getMockSpecId().equals(standardSpecDetail.getMockSpecId())).findFirst();
                        if (specOpt.isPresent()) {
                            standardSpecDetail.setCreateTime(goods.getCreateTime());
                            standardSpecDetail.setUpdateTime(goods.getUpdateTime());
                            standardSpecDetail.setGoodsId(goodsId);
                            standardSpecDetail.setDelFlag(DeleteFlag.NO);
                            standardSpecDetail.setSpecId(specOpt.get().getSpecId());
                            standardSpecDetail.setSpecDetailId(standardSpecDetailRepository.save(standardSpecDetail).getSpecDetailId());
                        }
                    });
        }

        for (StandardSku sku : standardSkus) {
            sku.setGoodsId(goodsId);
            sku.setGoodsInfoName(goods.getGoodsName());
            sku.setCostPrice(goods.getCostPrice());
            sku.setCreateTime(goods.getCreateTime());
            sku.setUpdateTime(goods.getUpdateTime());
            sku.setDelFlag(goods.getDelFlag());
            sku.setGoodsSource(goods.getGoodsSource());
            sku.setAddedFlag(goods.getAddedFlag());
            String goodsInfoId = standardSkuRepository.save(sku).getGoodsInfoId();
            sku.setGoodsInfoId(goodsInfoId);
            //如果是多规格,新增SKU与规格明细值的关联表
            if (Constants.yes.equals(goods.getMoreSpecFlag())) {
                if (CollectionUtils.isNotEmpty(specs)) {
                    for (StandardSpec spec : specs) {
                        if (sku.getMockSpecIds().contains(spec.getMockSpecId())) {
                            for (StandardSpecDetail detail : specDetails) {
                                if (spec.getMockSpecId().equals(detail.getMockSpecId()) && sku.getMockSpecDetailIds().contains(detail.getMockSpecDetailId())) {
                                    StandardSkuSpecDetailRel detailRel = new StandardSkuSpecDetailRel();
                                    detailRel.setGoodsId(goodsId);
                                    detailRel.setGoodsInfoId(goodsInfoId);
                                    detailRel.setSpecId(spec.getSpecId());
                                    detailRel.setSpecDetailId(detail.getSpecDetailId());
                                    detailRel.setDetailName(detail.getDetailName());
                                    detailRel.setCreateTime(detail.getCreateTime());
                                    detailRel.setUpdateTime(detail.getUpdateTime());
                                    detailRel.setDelFlag(detail.getDelFlag());
                                    specDetailRels.add(standardSkuSpecDetailRelRepository.save(detailRel));
                                }
                            }
                        }
                    }
                }
            }
        }
        return goodsId;
    }

    /**
     * 商品库更新
     *
     * @param saveRequest
     * @throws SbcRuntimeException
     */
    @Transactional
    public Map<String, Object> edit(StandardSaveRequest saveRequest) throws SbcRuntimeException {
        StandardGoods newGoods = saveRequest.getGoods();
        StandardGoods oldGoods = standardGoodsRepository.findById(newGoods.getGoodsId()).orElse(null);
        if (oldGoods == null || oldGoods.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(StandardGoodsErrorCode.NOT_EXIST);
        }
        //验证商品库相关基础数据
        this.checkBasic(newGoods);

        List<StandardImage> standardImages = saveRequest.getImages();
        if (CollectionUtils.isNotEmpty(standardImages)) {
            newGoods.setGoodsImg(standardImages.get(0).getArtworkUrl());
        } else {
            newGoods.setGoodsImg(null);
        }
        if (newGoods.getMoreSpecFlag() == null) {
            newGoods.setMoreSpecFlag(Constants.no);
        }

        LocalDateTime currDate = LocalDateTime.now();
        //更新商品库
        newGoods.setUpdateTime(currDate);
        KsBeanUtil.copyProperties(newGoods, oldGoods);
        standardGoodsRepository.save(oldGoods);

        //更新图片
        List<StandardImage> oldImages = standardImageRepository.findByGoodsId(newGoods.getGoodsId());
        if (CollectionUtils.isNotEmpty(oldImages)) {
            for (StandardImage oldImage : oldImages) {
                if (CollectionUtils.isNotEmpty(standardImages)) {
                    Optional<StandardImage> imageOpt = standardImages.stream().filter(standardImage -> oldImage.getImageId().equals(standardImage.getImageId())).findFirst();
                    //如果图片存在，更新
                    if (imageOpt.isPresent()) {
                        KsBeanUtil.copyProperties(imageOpt.get(), oldImage);
                    } else {
                        oldImage.setDelFlag(DeleteFlag.YES);
                    }
                } else {
                    oldImage.setDelFlag(DeleteFlag.YES);
                }
                oldImage.setUpdateTime(currDate);
                standardImageRepository.saveAll(oldImages);
            }
        }

        //新增图片
        if (CollectionUtils.isNotEmpty(standardImages)) {
            standardImages.stream().filter(standardImage -> standardImage.getImageId() == null).forEach(standardImage -> {
                standardImage.setCreateTime(currDate);
                standardImage.setUpdateTime(currDate);
                standardImage.setGoodsId(newGoods.getGoodsId());
                standardImage.setDelFlag(DeleteFlag.NO);
                standardImageRepository.save(standardImage);
            });
        }

        //保存商品库属性
        List<StandardPropDetailRel> goodsPropDetailRels = saveRequest.getGoodsPropDetailRels();
        //修改设置修改时间
        if (CollectionUtils.isNotEmpty(goodsPropDetailRels)) {
            //修改设置修改时间
            goodsPropDetailRels.forEach(goodsPropDetailRel -> {
                goodsPropDetailRel.setDelFlag(DeleteFlag.NO);
                if (goodsPropDetailRel.getRelId() != null) {
                    goodsPropDetailRel.setUpdateTime(LocalDateTime.now());
                }
            });
            //  先获取商品下所有的属性id，与前端传来的对比，id存在的做更新操作反之做保存操作
            List<StandardPropDetailRel> oldPropList = standardPropDetailRelRepository.queryByGoodsId(newGoods.getGoodsId());
            List<StandardPropDetailRel> insertList = new ArrayList<>();
            if (oldPropList.isEmpty()) {
                standardPropDetailRelRepository.saveAll(goodsPropDetailRels);
            } else {
                oldPropList.forEach(value -> {
                    goodsPropDetailRels.forEach(goodsProp -> {
                        if (value.getPropId().equals(goodsProp.getPropId())) {
                            standardPropDetailRelRepository.updateByGoodsIdAndPropId(goodsProp.getDetailId(), goodsProp.getGoodsId(), goodsProp.getPropId());
                        } else {
                            goodsProp.setCreateTime(LocalDateTime.now());
                            insertList.add(goodsProp);
                        }
                    });
                });
                standardPropDetailRelRepository.saveAll(insertList);
            }
        }
        List<StandardSpec> specs = saveRequest.getGoodsSpecs();
        List<StandardSpecDetail> specDetails = saveRequest.getGoodsSpecDetails();

        //如果是多规格
        if (Constants.yes.equals(newGoods.getMoreSpecFlag())) {

            /**
             * 填放可用SKU相应的规格\规格值
             * （主要解决SKU可以前端删除，但相应规格值或规格仍然出现的情况）
             */
            Map<Long, Integer> isSpecEnable = new HashMap<>();
            Map<Long, Integer> isSpecDetailEnable = new HashMap<>();
            saveRequest.getGoodsInfos().forEach(standardSku -> {
                standardSku.getMockSpecIds().forEach(specId -> {
                    isSpecEnable.put(specId, Constants.yes);
                });
                standardSku.getMockSpecDetailIds().forEach(detailId -> {
                    isSpecDetailEnable.put(detailId, Constants.yes);
                });
            });

            if (Constants.yes.equals(oldGoods.getMoreSpecFlag())) {
                //更新规格
                List<StandardSpec> standardSpecs = standardSpecRepository.findByGoodsId(oldGoods.getGoodsId());
                if (CollectionUtils.isNotEmpty(standardSpecs)) {
                    for (StandardSpec oldSpec : standardSpecs) {
                        if (CollectionUtils.isNotEmpty(specs)) {
                            Optional<StandardSpec> specOpt = specs.stream().filter(spec -> oldSpec.getSpecId().equals(spec.getSpecId())).findFirst();
                            //如果规格存在且SKU有这个规格，更新
                            if (specOpt.isPresent() && Constants.yes.equals(isSpecEnable.get(specOpt.get().getMockSpecId()))) {
                                KsBeanUtil.copyProperties(specOpt.get(), oldSpec);
                            } else {
                                oldSpec.setDelFlag(DeleteFlag.YES);
                            }
                        } else {
                            oldSpec.setDelFlag(DeleteFlag.YES);
                        }
                        oldSpec.setUpdateTime(currDate);
                        standardSpecRepository.save(oldSpec);
                    }
                }

                //更新规格值
                List<StandardSpecDetail> standardSpecDetails = standardSpecDetailRepository.findByGoodsId(oldGoods.getGoodsId());
                if (CollectionUtils.isNotEmpty(standardSpecDetails)) {

                    for (StandardSpecDetail oldSpecDetail : standardSpecDetails) {
                        if (CollectionUtils.isNotEmpty(specDetails)) {
                            Optional<StandardSpecDetail> specDetailOpt = specDetails.stream().filter(specDetail -> oldSpecDetail.getSpecDetailId().equals(specDetail.getSpecDetailId())).findFirst();
                            //如果规格值存在且SKU有这个规格值，更新
                            if (specDetailOpt.isPresent() && Constants.yes.equals(isSpecDetailEnable.get(specDetailOpt.get().getMockSpecDetailId()))) {
                                KsBeanUtil.copyProperties(specDetailOpt.get(), oldSpecDetail);

                                //更新SKU规格值表的名称备注
                                standardSkuSpecDetailRelRepository.updateNameBySpecDetail(specDetailOpt.get().getDetailName(), oldSpecDetail.getSpecDetailId(), oldGoods.getGoodsId());
                            } else {
                                oldSpecDetail.setDelFlag(DeleteFlag.YES);
                            }
                        } else {
                            oldSpecDetail.setDelFlag(DeleteFlag.YES);
                        }
                        oldSpecDetail.setUpdateTime(currDate);
                        standardSpecDetailRepository.save(oldSpecDetail);
                    }
                }
            }

            //新增规格
            if (CollectionUtils.isNotEmpty(specs)) {
                specs.stream().filter(standardSpec -> standardSpec.getSpecId() == null && Constants.yes.equals(isSpecEnable.get(standardSpec.getMockSpecId()))).forEach(standardSpec -> {
                    standardSpec.setCreateTime(currDate);
                    standardSpec.setUpdateTime(currDate);
                    standardSpec.setGoodsId(newGoods.getGoodsId());
                    standardSpec.setDelFlag(DeleteFlag.NO);
                    standardSpec.setSpecId(standardSpecRepository.save(standardSpec).getSpecId());
                });
            }
            //新增规格值
            if (CollectionUtils.isNotEmpty(specDetails)) {
                specDetails.stream().filter(standardSpecDetail -> standardSpecDetail.getSpecDetailId() == null && Constants.yes.equals(isSpecDetailEnable.get(standardSpecDetail.getMockSpecDetailId()))).forEach(standardSpecDetail -> {
                    Optional<StandardSpec> specOpt = specs.stream().filter(standardSpec -> standardSpec.getMockSpecId().equals(standardSpecDetail.getMockSpecId())).findFirst();
                    if (specOpt.isPresent()) {
                        standardSpecDetail.setCreateTime(currDate);
                        standardSpecDetail.setUpdateTime(currDate);
                        standardSpecDetail.setGoodsId(newGoods.getGoodsId());
                        standardSpecDetail.setDelFlag(DeleteFlag.NO);
                        standardSpecDetail.setSpecId(specOpt.get().getSpecId());
                        standardSpecDetail.setSpecDetailId(standardSpecDetailRepository.save(standardSpecDetail).getSpecDetailId());
                    }
                });
            }
        } else {//修改为单规格
            //如果老数据为多规格
            if (Constants.yes.equals(oldGoods.getMoreSpecFlag())) {
                //删除规格
                standardSpecRepository.deleteByGoodsId(newGoods.getGoodsId());

                //删除规格值
                standardSpecDetailRepository.deleteByGoodsId(newGoods.getGoodsId());

                //删除商品库规格值
                standardSkuSpecDetailRelRepository.deleteByGoodsId(newGoods.getGoodsId());
            }
        }

        //只存储新增的SKU数据，用于当修改价格及订货量设置为否时，只为新SKU增加相关的价格数据
        List<StandardSku> newStandardSku = new ArrayList<>();//需要被添加的sku信息

        //更新原有的SKU列表
        List<StandardSku> standardSkus = saveRequest.getGoodsInfos();
        List<StandardSku> oldStandardSkus = new ArrayList<>();//需要被更新的sku信息
        List<String> delInfoIds = new ArrayList<>();//需要被删除的sku信息
        if (CollectionUtils.isNotEmpty(standardSkus)) {
            StandardSkuQueryRequest infoQueryRequest = new StandardSkuQueryRequest();
            infoQueryRequest.setGoodsId(newGoods.getGoodsId());
            infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            List<StandardSku> oldInfos = standardSkuRepository.findAll(infoQueryRequest.getWhereCriteria());

            if (CollectionUtils.isNotEmpty(oldInfos)) {
                for (StandardSku oldInfo : oldInfos) {
                    Optional<StandardSku> infoOpt = standardSkus.stream().filter(standardSku -> oldInfo.getGoodsInfoId().equals(standardSku.getGoodsInfoId())).findFirst();
                    //如果SKU存在，更新
                    if (infoOpt.isPresent()) {
                        infoOpt.get().setCostPrice(newGoods.getCostPrice());
                        KsBeanUtil.copyProperties(infoOpt.get(), oldInfo);
                        oldStandardSkus.add(oldInfo);//修改前后都存在的数据--加入需要被更新的sku中
                    } else {
                        oldInfo.setDelFlag(DeleteFlag.YES);
                        delInfoIds.add(oldInfo.getGoodsInfoId());//修改后不存在的数据--加入需要被删除的sku中
                    }
                    oldInfo.setGoodsInfoName(newGoods.getGoodsName());
                    oldInfo.setUpdateTime(currDate);
                    standardSkuRepository.save(oldInfo);
                }

                //删除SKU相关的规格关联表
                if (!delInfoIds.isEmpty()) {
                    standardSkuSpecDetailRelRepository.deleteByGoodsInfoIds(delInfoIds, newGoods.getGoodsId());
                }
            }

            //只保存新SKU
            for (StandardSku sku : standardSkus) {
                sku.setGoodsId(newGoods.getGoodsId());
                sku.setGoodsInfoName(newGoods.getGoodsName());
                sku.setCreateTime(currDate);
                sku.setUpdateTime(currDate);
                sku.setDelFlag(DeleteFlag.NO);
                //只处理新增的SKU
                if (sku.getGoodsInfoId() != null) {
                    continue;
                }
                sku.setCostPrice(oldGoods.getCostPrice());
                String standardSkuId = standardSkuRepository.save(sku).getGoodsInfoId();
                sku.setGoodsInfoId(standardSkuId);

                //如果是多规格,新增SKU与规格明细值的关联表
                if (Constants.yes.equals(newGoods.getMoreSpecFlag())) {
                    if (CollectionUtils.isNotEmpty(specs)) {
                        for (StandardSpec spec : specs) {
                            if (sku.getMockSpecIds().contains(spec.getMockSpecId())) {
                                for (StandardSpecDetail detail : specDetails) {
                                    if (spec.getMockSpecId().equals(detail.getMockSpecId()) && sku.getMockSpecDetailIds().contains(detail.getMockSpecDetailId())) {
                                        StandardSkuSpecDetailRel detailRel = new StandardSkuSpecDetailRel();
                                        detailRel.setGoodsId(newGoods.getGoodsId());
                                        detailRel.setGoodsInfoId(standardSkuId);
                                        detailRel.setSpecId(spec.getSpecId());
                                        detailRel.setSpecDetailId(detail.getSpecDetailId());
                                        detailRel.setDetailName(detail.getDetailName());
                                        detailRel.setCreateTime(currDate);
                                        detailRel.setUpdateTime(currDate);
                                        detailRel.setDelFlag(DeleteFlag.NO);
                                        standardSkuSpecDetailRelRepository.save(detailRel);
                                    }
                                }
                            }
                        }
                    }
                }
                newStandardSku.add(sku);//修改后才存在(新出现)的数据--加入需要被添加的sku中
            }
        }

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("newStandardSku", newStandardSku);
        returnMap.put("delInfoIds", delInfoIds);
        returnMap.put("oldStandardSkus", oldStandardSkus);
        return returnMap;
    }

    /**
     * 商品库删除
     *
     * @param goodsIds 多个商品库
     * @throws SbcRuntimeException
     */
    @Transactional
    public void delete(List<String> goodsIds) throws SbcRuntimeException {

        if (standardGoodsRelRepository.countByStandardIds(goodsIds) > 0) {
            throw new SbcRuntimeException(StandardGoodsErrorCode.COMPANY_USED);
        }
        standardGoodsRelRepository.deleteByStandardIds(goodsIds);
        standardGoodsRepository.deleteByGoodsIds(goodsIds);
        standardSkuRepository.deleteByGoodsIds(goodsIds);
        standardPropDetailRelRepository.deleteByGoodsIds(goodsIds);
        standardSpecRepository.deleteByGoodsIds(goodsIds);
        standardSpecDetailRepository.deleteByGoodsIds(goodsIds);
        standardSkuSpecDetailRelRepository.deleteByGoodsIds(goodsIds);
    }


    /**
     * 供货商品库删除
     *
     * @param goodsIds 多个商品库
     * @throws SbcRuntimeException
     */
    @Transactional
    public void deleteProvider(List<String> goodsIds) throws SbcRuntimeException {

        // 相关商品下架
        List<StandardGoodsRel> standardGoodsRels = standardGoodsRelRepository.findByStandardIds(goodsIds);
        List<String> standardGoodsIds = standardGoodsRels.stream().map(StandardGoodsRel::getGoodsId).collect(Collectors.toList());

        //删除供应商商品库商品

        goodsRepository.updateAddedFlagByGoodsIds(AddedFlag.NO.toValue(), standardGoodsIds,UnAddedFlagReason.BOSSDELETE.toString());
        goodsInfoRepository.updateAddedFlagByGoodsIds(AddedFlag.NO.toValue(), standardGoodsIds);

        standardGoodsRelRepository.deleteByStandardIds(goodsIds);
        standardGoodsRepository.deleteByGoodsIds(goodsIds);
        standardSkuRepository.deleteByGoodsIds(goodsIds);
        standardPropDetailRelRepository.deleteByGoodsIds(goodsIds);
        standardSpecRepository.deleteByGoodsIds(goodsIds);
        standardSpecDetailRepository.deleteByGoodsIds(goodsIds);
        standardSkuSpecDetailRelRepository.deleteByGoodsIds(goodsIds);


    }


    /**
     * 供货商品库删除
     *
     * @param standardGoodsIds  多个商品库
     * @param deleteReason
     * @throws SbcRuntimeException
     */
    @Transactional
    public List<String> deleteProviderAddReason(List<String> standardGoodsIds, String deleteReason) throws SbcRuntimeException {

        // 相关商品下架
        List<StandardGoodsRel> standardGoodsRels = standardGoodsRelRepository.findByStandardIds(standardGoodsIds);
        List<String> goodsIds = standardGoodsRels.stream().map(StandardGoodsRel::getGoodsId).collect(Collectors.toList());

        //删除供应商商品库商品
        standardGoodsRepository.deleteByGoodsIds(standardGoodsIds);
        standardSkuRepository.deleteByGoodsIds(standardGoodsIds);
        standardPropDetailRelRepository.deleteByGoodsIds(standardGoodsIds);
        standardSpecRepository.deleteByGoodsIds(standardGoodsIds);
        standardSpecDetailRepository.deleteByGoodsIds(standardGoodsIds);
        standardSkuSpecDetailRelRepository.deleteByGoodsIds(standardGoodsIds);

        //供货商商品和商家商品 都下架
        goodsRepository.updateAddedFlagByGoodsIdsAddDeleteReason(AddedFlag.NO.toValue(), goodsIds, UnAddedFlagReason.BOSSDELETE.toString(),deleteReason);
        goodsInfoRepository.updateAddedFlagByGoodsIds(AddedFlag.NO.toValue(), goodsIds);


        List<Goods> allGoods = goodsRepository.findAllByGoodsIdIn(goodsIds);
        List<String> providerGoodsIds = allGoods.stream().filter(goods ->  0 == goods.getGoodsSource()).map(Goods::getGoodsId).collect(Collectors.toList());
        //删除商品库与供应商商品的关联
        if(CollectionUtils.isNotEmpty(providerGoodsIds)){
            standardGoodsRelRepository.deleteByGoodsIds(providerGoodsIds);
        }

        return allGoods.stream().filter(goods -> goods.getGoodsSource() == 1).map(Goods::getGoodsId).collect(Collectors.toList());

    }

    /**
     * 列出已被导入的商品库ID
     *
     * @param standardIds 商品库Id
     */
    public List<String> getUsedStandard(List<String> standardIds, List<Long> storeIds) {
        return standardGoodsRelRepository.findByStandardIds(standardIds, storeIds).stream().map(StandardGoodsRel::getStandardId).distinct().collect(Collectors.toList());
    }

    /**
     * 列出已被导入的商品ID
     *
     * @param goodsIds 商品Id（非商品库）
     */
    public List<String> getUsedGoods(List<String> goodsIds) {
//        return standardGoodsRepository.findByGoodsIds(goodsIds).stream().map(StandardGoods::getGoodsId).distinct().collect(Collectors.toList());
        return standardGoodsRelRepository.findByGoodsIds(goodsIds).stream().map(StandardGoodsRel::getGoodsId).distinct().collect(Collectors.toList());
    }

    /**
     * 检测商品库公共基础类
     * 如分类、品牌、店铺分类
     *
     * @param goods 商品库信息
     */
    private void checkBasic(StandardGoods goods) {
        GoodsCate cate = this.goodsCateRepository.findById(goods.getCateId()).orElse(null);
        if (Objects.isNull(cate) || Objects.equals(DeleteFlag.YES, cate.getDelFlag())) {
            throw new SbcRuntimeException(GoodsCateErrorCode.NOT_EXIST);
        }

        if (goods.getBrandId() != null) {
            GoodsBrand brand = this.goodsBrandRepository.findById(goods.getBrandId()).orElse(null);
            if (Objects.isNull(brand) || Objects.equals(DeleteFlag.YES, brand.getDelFlag())) {
                throw new SbcRuntimeException(GoodsBrandErrorCode.NOT_EXIST);
            }
        }
    }


    /**
     * 列出已被导入的SKUID
     *
     * @param standardIds 商品库Id
     */
    public List<String> getUsedGoodsId(List<String> standardIds, List<Long> storeIds) {
        return standardGoodsRelRepository.findByStandardIds(
                standardIds, storeIds).stream().map(StandardGoodsRel::getGoodsId).distinct().collect(Collectors.toList());
    }

    /**
     * 列出已被导入的商品库ID
     *
     * @param goodsIds 商品Id（非商品库）
     */
    public List<String> getUsedStandardByGoodsIds(List<String> goodsIds) {
        return standardGoodsRelRepository.findByGoodsIds(goodsIds).stream().map(StandardGoodsRel::getStandardId).distinct().collect(Collectors.toList());
    }

    /**
     * 列出已被导入的商品库ID
     *
     * @param standardIds 商品库Id
     */
    public List<String> getNeedSynStandard(List<String> standardIds, List<Long> storeIds, BoolFlag flag) {
        return standardGoodsRelRepository.findByNeedSynStandardIds(standardIds, storeIds, flag).stream().map(StandardGoodsRel::getStandardId).distinct().collect(Collectors.toList());
    }


    public List<StandardGoodsRel> getGoodsIdByStandardId(String goodsId) {
        return standardGoodsRelRepository.findByStandardId(goodsId);
    }

    public List<StandardGoodsRel> listRelByStandardIds(List<String> standardIds) {
        return standardGoodsRelRepository.findByStandardIds(standardIds);
    }

    /**
     * 自定义字段的列表查询
     * @param request 参数
     * @param cols 列名
     * @return 列表
     */
    public Page<String> pageCols(StandardQueryRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<StandardGoods> rt = cq.from(StandardGoods.class);
        cq.select(rt.get("goodsId"));
        Specification<StandardGoods> spec = request.getWhereCriteria();
        Predicate predicate = spec.toPredicate(rt, cq, cb);
        if (predicate != null) {
            cq.where(predicate);
        }
        Sort sort = request.getSort();
        if (sort.isSorted()) {
            cq.orderBy(QueryUtils.toOrders(sort, rt, cb));
        }
        cq.orderBy(QueryUtils.toOrders(request.getSort(), rt, cb));
        TypedQuery<String> query = entityManager.createQuery(cq);
        query.setFirstResult((int) request.getPageRequest().getOffset());
        query.setMaxResults(request.getPageRequest().getPageSize());

        return PageableExecutionUtils.getPage(query.getResultList(), request.getPageable(), () -> {
            CriteriaBuilder countCb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> countCq = countCb.createQuery(Long.class);
            Root<StandardGoods> crt = countCq.from(StandardGoods.class);
            countCq.select(countCb.count(crt));
            if (predicate != null) {
                countCq.where(predicate);
            }
            return entityManager.createQuery(countCq).getResultList().stream().filter(Objects::nonNull).mapToLong(s -> s).sum();
        });
    }
}
