package com.wanmi.sbc.goods.priceadjustmentrecorddetail.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelListRequest;
import com.wanmi.sbc.customer.api.response.store.StoreByIdResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailQueryRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.AdjustPriceExecuteResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsIntervalPriceDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsLevelPriceDTO;
import com.wanmi.sbc.goods.bean.enums.*;
import com.wanmi.sbc.goods.bean.vo.GoodsAdjustIntervalPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsAdjustLevelPriceVO;
import com.wanmi.sbc.goods.bean.vo.PriceAdjustmentRecordDetailVO;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoResponse;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoRequest;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.price.model.root.GoodsIntervalPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsLevelPrice;
import com.wanmi.sbc.goods.price.repository.GoodsIntervalPriceRepository;
import com.wanmi.sbc.goods.price.repository.GoodsLevelPriceRepository;
import com.wanmi.sbc.goods.priceadjustmentrecord.model.root.PriceAdjustmentRecord;
import com.wanmi.sbc.goods.priceadjustmentrecord.repository.PriceAdjustmentRecordRepository;
import com.wanmi.sbc.goods.priceadjustmentrecorddetail.model.root.PriceAdjustmentRecordDetail;
import com.wanmi.sbc.goods.priceadjustmentrecorddetail.repository.PriceAdjustmentRecordDetailRepository;
import com.wanmi.sbc.goods.standard.repository.StandardSkuRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>调价单详情表业务逻辑</p>
 *
 * @author chenli
 * @date 2020-12-09 19:55:41
 */
@Service("PriceAdjustmentRecordDetailService")
@Slf4j
public class PriceAdjustmentRecordDetailService {
    @Autowired
    private PriceAdjustmentRecordDetailRepository priceAdjustmentRecordDetailRepository;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private PriceAdjustmentRecordRepository priceAdjustmentRecordRepository;

    @Autowired
    private GoodsLevelPriceRepository levelPriceRepository;

    @Autowired
    private GoodsIntervalPriceRepository intervalPriceRepository;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @Autowired
    private StandardSkuRepository standardSkuRepository;


    /**
     * 调价单次执行条数上限
     */
    private static final Integer _ADJUST_PRICE_FETCH_SIZE = 500;

    private static final String _GOODS_NOT_EXISTS_TEXT = "商品不存在";

    private static final String DEFAULT_LEVEL_NAME = "全平台客户";

    /**
     * 新增调价单详情表
     *
     * @author chenli
     */
    @Transactional
    public PriceAdjustmentRecordDetail add(PriceAdjustmentRecordDetail entity) {
        priceAdjustmentRecordDetailRepository.save(entity);
        return entity;
    }

    /**
     * 批量新增调价单详情
     *
     * @author chenli
     */
    @Transactional
    public void addBatch(List<PriceAdjustmentRecordDetail> dataList) {
        priceAdjustmentRecordDetailRepository.saveAll(dataList);
    }

    /**
     * 修改调价单详情表
     *
     * @author chenli
     */
    @Transactional
    public PriceAdjustmentRecordDetail modify(PriceAdjustmentRecordDetail entity) {
        priceAdjustmentRecordDetailRepository.save(entity);
        return entity;
    }


    /**
     * 单个查询调价单详情表
     *
     * @author chenli
     */
    public PriceAdjustmentRecordDetail getOne(Long id) {
        return priceAdjustmentRecordDetailRepository.findById(id)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "调价单详情表不存在"));
    }

    /**
     * 分页查询调价单详情表
     *
     * @author chenli
     */
    public Page<PriceAdjustmentRecordDetail> page(PriceAdjustmentRecordDetailQueryRequest queryReq) {
        return priceAdjustmentRecordDetailRepository.findAll(
                PriceAdjustmentRecordDetailWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 分页查询调价单详情表,商品实时数据
     *
     * @author chenli
     */
    public Page<PriceAdjustmentRecordDetail> pageForConfirm(PriceAdjustmentRecordDetailQueryRequest queryReq) {
        return this.fillGoodsInfo(queryReq.getPriceAdjustmentNo(), queryReq.getPageRequest(), queryReq.getBaseStoreId(), false);
    }


    /**
     * 列表查询调价单详情表
     *
     * @author chenli
     */
    public List<PriceAdjustmentRecordDetail> list(PriceAdjustmentRecordDetailQueryRequest queryReq) {
        return priceAdjustmentRecordDetailRepository.findAll(PriceAdjustmentRecordDetailWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     *
     * @author chenli
     */
    public PriceAdjustmentRecordDetailVO wrapperVo(PriceAdjustmentRecordDetail priceAdjustmentRecordDetail) {
        if (priceAdjustmentRecordDetail != null) {
            PriceAdjustmentRecordDetailVO priceAdjustmentRecordDetailVO = KsBeanUtil.convert(priceAdjustmentRecordDetail, PriceAdjustmentRecordDetailVO.class);
            // 转换客户等级价
            String levelPriceStr = priceAdjustmentRecordDetailVO.getLeverPrice();
            if (StringUtils.isNotBlank(levelPriceStr)) {
                List<GoodsAdjustLevelPriceVO> parseArray = JSON.parseArray(levelPriceStr, GoodsAdjustLevelPriceVO.class);
                priceAdjustmentRecordDetailVO.setLeverPriceList(parseArray);
            }

            // 转换阶梯价
            String intervalPriceStr = priceAdjustmentRecordDetailVO.getIntervalPrice();
            if (StringUtils.isNotBlank(intervalPriceStr)) {
                List<GoodsAdjustIntervalPriceVO> parseArray = JSON.parseArray(intervalPriceStr, GoodsAdjustIntervalPriceVO.class);
                priceAdjustmentRecordDetailVO.setIntervalPriceList(parseArray);
            }
            return priceAdjustmentRecordDetailVO;
        }
        return null;
    }


    @Transactional
    public void deleteDetail(Long adjustDetailId, String adjustDetailNo) {
        priceAdjustmentRecordDetailRepository.deleteByIdAndPriceAdjustmentNo(adjustDetailId, adjustDetailNo);
        priceAdjustmentRecordRepository.reduceGoodsNum(adjustDetailNo);
    }

//    public Page<PriceAdjustmentRecordDetail> page(String adjustNo, PriceAdjustmentType type, PageRequest pageRequest) {
//        return fillGoodsInfo(adjustNo, pageRequest, type, false);
//    }


    /**
     * 修改调价详情
     *
     * @param param
     */
    @Transactional
    public void modifyDetail(PriceAdjustmentRecordDetail param) {
        PriceAdjustmentRecord record = priceAdjustmentRecordRepository.findById(param.getPriceAdjustmentNo())
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS));
        PriceAdjustmentRecordDetail detail = priceAdjustmentRecordDetailRepository.getOne(param.getId());
        if (!detail.getPriceAdjustmentNo().equals(param.getPriceAdjustmentNo())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        switch (record.getPriceAdjustmentType()) {
            case SUPPLY:
                detail.setAdjustSupplyPrice(param.getAdjustSupplyPrice());
                detail.setPriceDifference(param.getAdjustSupplyPrice().subtract(detail.getSupplyPrice()));
                break;
            case LEVEL:
                detail.setAdjustedMarketPrice(param.getAdjustedMarketPrice());
                List<GoodsLevelPriceDTO> levelPriceList = new ArrayList<>(JSON.parseArray(param.getLeverPrice(), GoodsLevelPriceDTO.class));
                levelPriceList.forEach(i -> i.setType(PriceType.SKU));
                detail.setLeverPrice(JSONObject.toJSONString(levelPriceList));
                break;
            case MARKET:
                detail.setAdjustedMarketPrice(param.getAdjustedMarketPrice());
                detail.setPriceDifference(param.getAdjustedMarketPrice().subtract(detail.getOriginalMarketPrice()));
                break;
            case STOCK:
                detail.setAdjustedMarketPrice(param.getAdjustedMarketPrice());
                List<GoodsIntervalPriceDTO> intervalPriceList = new ArrayList<>(JSON.parseArray(param.getIntervalPrice(), GoodsIntervalPriceDTO.class));
                intervalPriceList.forEach(i -> i.setType(PriceType.SKU));
                detail.setIntervalPrice(JSONObject.toJSONString(intervalPriceList));
                break;
        }
        priceAdjustmentRecordDetailRepository.save(detail);

    }


    /**
     * 定时调价任务
     *
     * @param adjustNo
     * @param storeId
     */
    @Transactional
    public AdjustPriceExecuteResponse adjustPriceTaskExecute(String adjustNo, Long storeId) {
        AdjustPriceExecuteResponse response = new AdjustPriceExecuteResponse();
        PriceAdjustmentRecord record = priceAdjustmentRecordRepository.findByIdAndStoreId(adjustNo, storeId);
        if (Objects.isNull(record)) {
            log.error("调价执行失败，该调价单不存在，adjustNo={}", adjustNo);
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        if (DefaultFlag.YES != record.getConfirmFlag()) {
            log.error("调价执行失败，该调价单未确认，adjustNo={}", adjustNo);
            throw new SbcRuntimeException(CommonErrorCode.SYSTEM_UNKNOWN_ERROR);
        }
        List<String> skuIds = adjustPrice(adjustNo, record);
        response.setSkuIds(skuIds);
        response.setType(record.getPriceAdjustmentType());
        return response;
    }

    /**
     * 立即调价
     *
     * @param adjustNo
     * @param storeId
     */
    @Transactional
    public void adjustPriceNow(String adjustNo, Long storeId) {
        PriceAdjustmentRecord record = priceAdjustmentRecordRepository.findByIdAndStoreId(adjustNo, storeId);
        if (Objects.isNull(record)) {
            log.error("调价执行失败，该调价单不存在，adjustNo={}", adjustNo);
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        confirmAdjust(adjustNo, storeId, LocalDateTime.now());
    }

    /**
     * 改价确认
     *
     * @param adjustNo
     * @param storeId
     * @param effectiveTime
     */
    @Transactional
    public void confirmAdjust(String adjustNo, Long storeId, LocalDateTime effectiveTime) {
        int pageNum = 0;
        boolean isNext;
        List<PriceAdjustmentRecordDetail> details;
        do {
            //同步最新市场价信息与差异，更新确认状态
            details = this.fillGoodsInfo(adjustNo, PageRequest.of(pageNum, _ADJUST_PRICE_FETCH_SIZE, Sort.unsorted()),
                    storeId, true).getContent();
            if (isNext = !details.isEmpty()) {
                priceAdjustmentRecordDetailRepository.saveAll(details);
            }
            //pageNum++;
        } while (isNext);
        //修改调价记录
        priceAdjustmentRecordRepository.confirmAdjustRecord(adjustNo, storeId, effectiveTime);
    }

    /**
     * 调价操作
     *
     * @param adjustNo
     * @param record
     */
    private List<String> adjustPrice(String adjustNo, PriceAdjustmentRecord record) {
        List<String> ids = new ArrayList<>();
        PriceAdjustmentRecordDetailQueryRequest request = new PriceAdjustmentRecordDetailQueryRequest();
        request.setAdjustResult(0);
        request.setPriceAdjustmentNo(adjustNo);
        request.setPageSize(_ADJUST_PRICE_FETCH_SIZE);
        int num = 0;
        boolean isNext;
        Page<PriceAdjustmentRecordDetail> page;
        // 批量调价spu的第一个sku对应的调价信息
        Map<String, PriceAdjustmentRecordDetail> priceAdjustMap = new HashMap<>();
        // spuId -> skuId
        Map<String, List<String>> goodsRelMap = new HashMap<>();
        do {
            request.setPageNum(num);
            page = this.page(request);
            if (isNext = !page.isEmpty()) {
                //获取详情
                List<PriceAdjustmentRecordDetail> list = page.getContent();
                //获取SKU list
                Map<String, GoodsInfo> goodsInfoMap = goodsInfoService.findByIds(list.stream().map(PriceAdjustmentRecordDetail
                        ::getGoodsInfoId).collect(Collectors.toList())).stream().collect(Collectors.toMap(
                        GoodsInfo::getGoodsInfoId, Function.identity()));
                //获取SPU list
                GoodsQueryRequest goodsIds = GoodsQueryRequest.builder()
                        .goodsIds(goodsInfoMap.values().stream().map(GoodsInfo::getGoodsId).distinct().collect(Collectors.toList()))
                        .delFlag(DeleteFlag.NO.toValue())
                        .build();
                Map<String, Goods> goodsMap = goodsService.findAll(goodsIds).stream().collect(Collectors.toMap(
                        Goods::getGoodsId, Function.identity()));
                //记录不存在的商品
                List<String> failGoodsInfoIds = new ArrayList<>();
                //查询商品信息，
                switch (record.getPriceAdjustmentType()) {
                    case MARKET:
                        adjustMarketPrice(list, goodsInfoMap, goodsMap, failGoodsInfoIds, priceAdjustMap);
                        break;
                    case LEVEL:
                        adjustLevelPrice(list, goodsInfoMap, goodsMap, failGoodsInfoIds, priceAdjustMap);
                        break;
                    case STOCK:
                        adjustIntervalPrice(list, goodsInfoMap, goodsMap, failGoodsInfoIds, priceAdjustMap, goodsRelMap);
                        break;
                    case SUPPLY:
                        adjustSupplyPrice(list, goodsInfoMap, goodsMap, failGoodsInfoIds);
                        break;
                }
                //批量保存改价详情
                priceAdjustmentRecordDetailRepository.saveAll(list);

                //SKU更新
                goodsInfoRepository.saveAll(goodsInfoMap.values());

                //SPU更新
                goodsRepository.saveAll(goodsMap.values());

                //更新当前SPU下其他的SKU（不在excel）
                this.modifyOtherSku(goodsRelMap, priceAdjustMap);

                //只返回成功的id
                List<String> allIds = new ArrayList<>(goodsInfoMap.keySet());
                List<String> successIds = allIds.stream().filter(id -> !failGoodsInfoIds.contains(id)).collect(Collectors.toList());
                ids.addAll(successIds);
            }
            //num++;
        } while (isNext);

        return ids;
    }

    private void adjustSupplyPrice(List<PriceAdjustmentRecordDetail> list, Map<String, GoodsInfo> goodsInfoMap, Map<String, Goods> goodsMap, List<String> failGoodsInfoIds) {
        list.forEach(i -> {
            GoodsInfo goodsInfo = goodsInfoMap.get(i.getGoodsInfoId());
            if (Objects.isNull(goodsInfo) || DeleteFlag.YES.equals(goodsInfo.getDelFlag())) {
                failGoodsInfoIds.add(i.getGoodsInfoId());
                i.setAdjustResult(PriceAdjustmentResult.FAIL);
                i.setFailReason(_GOODS_NOT_EXISTS_TEXT);
                return;
            }

            Goods goods = goodsMap.get(goodsInfo.getGoodsId());
            if (Objects.isNull(goods)) {
                failGoodsInfoIds.add(i.getGoodsInfoId());
                i.setAdjustResult(PriceAdjustmentResult.FAIL);
                i.setFailReason(_GOODS_NOT_EXISTS_TEXT);
                return;
            }

            goodsInfo.setSupplyPrice(i.getAdjustSupplyPrice());
            i.setAdjustResult(PriceAdjustmentResult.DONE);

            //更新代销商品的供货价
            goodsInfoRepository.updateSupplyPriceByProviderGoodsInfoId(goodsInfo.getGoodsInfoId(), goodsInfo.getSupplyPrice());
            standardSkuRepository.updateSupplyPriceByProviderGoodsInfoId(goodsInfo.getGoodsInfoId(), goodsInfo.getSupplyPrice());
        });

    }

    private void adjustIntervalPrice(List<PriceAdjustmentRecordDetail> list, Map<String, GoodsInfo> goodsInfoMap,
                                     Map<String, Goods> goodsMap, List<String> failGoodsInfoIds, Map<String, PriceAdjustmentRecordDetail> priceAdjustMap,
                                     Map<String, List<String>> goodsRelMap) {
        List<String> goodsInfoIds = new ArrayList<>(goodsInfoMap.keySet());
        List<GoodsIntervalPrice> allIntervalPriceList = new ArrayList<>();
        // spu下第一个sku的阶梯价
        List<GoodsIntervalPrice> firstSkuIntervalPrices = new ArrayList<>();

        List<String> delIds = new ArrayList<>();
        list.forEach(i -> {
            GoodsInfo goodsInfo = goodsInfoMap.get(i.getGoodsInfoId());
            if (Objects.isNull(goodsInfo) || goodsInfo.getDelFlag() == DeleteFlag.YES) {
                failGoodsInfoIds.add(i.getGoodsInfoId());
                i.setAdjustResult(PriceAdjustmentResult.FAIL);
                i.setFailReason(_GOODS_NOT_EXISTS_TEXT);
                return;
            }
            Goods goods = goodsMap.get(goodsInfo.getGoodsId());
            if (Objects.isNull(goods)) {
                failGoodsInfoIds.add(i.getGoodsInfoId());
                i.setAdjustResult(PriceAdjustmentResult.FAIL);
                i.setFailReason(_GOODS_NOT_EXISTS_TEXT);
                return;
            }
            //修改销售类型
            goodsInfo.setSaleType(i.getSaleType().ordinal());
            goods.setSaleType(i.getSaleType().ordinal());
            //修改市场价
            if(Objects.nonNull(i.getAdjustedMarketPrice())) {
                goodsInfo.setMarketPrice(i.getAdjustedMarketPrice());
            }
            //是否独立设价
            goodsInfo.setAloneFlag(i.getAloneFlag());
            //原设价方式不是订货量
            if(GoodsPriceType.STOCK.ordinal() != goods.getPriceType()) {
                List<String> skuIds = goodsRelMap.get(goods.getGoodsId());
                if(CollectionUtils.isNotEmpty(skuIds)) {
                    skuIds.add(goodsInfo.getGoodsInfoId());
                } else {
                    goodsRelMap.put(goods.getGoodsId(), Lists.newArrayList(goodsInfo.getGoodsInfoId()));
                }
            }
            //设价类型
            goodsInfo.setPriceType(GoodsPriceType.STOCK.ordinal());
            goods.setPriceType(GoodsPriceType.STOCK.ordinal());
            //SKU设置独立设价，置SPU允许独立设价
            goods.setAllowPriceSet(i.getAloneFlag() ? DefaultFlag.YES.ordinal() : goods.getAllowPriceSet());
            i.setAdjustResult(PriceAdjustmentResult.DONE);
            delIds.add(i.getGoodsInfoId());
            //过滤出有效的区间价
            List<GoodsIntervalPrice> intervalPriceList = JSON.parseArray(i.getIntervalPrice(), GoodsIntervalPrice.class)
                    .stream().filter(t -> Objects.nonNull(t.getPrice()) && Objects.nonNull(t.getCount())).collect(Collectors.toList());
            allIntervalPriceList.addAll(intervalPriceList);

            // spu下第一个sku
            PriceAdjustmentRecordDetail firstRecordDetail = priceAdjustMap.get(goods.getGoodsId());
            if (Objects.isNull(firstRecordDetail)) {
                priceAdjustMap.put(goods.getGoodsId(), i);
                firstRecordDetail = i;
                // 给spu设置该spu下第一个sku的阶梯价
                List<GoodsIntervalPrice> intervalPrices = JSON.parseArray(firstRecordDetail.getIntervalPrice(), GoodsIntervalPrice.class)
                        .stream().filter(t -> Objects.nonNull(t.getPrice()) && Objects.nonNull(t.getCount())).collect(Collectors.toList());
                intervalPrices.forEach(interval -> {
                    interval.setGoodsInfoId("");
                    interval.setType(PriceType.SPU);
                    firstSkuIntervalPrices.add(interval);
                });

                //修改同一个spu下，不在调价列表中的其他的sku销售类型
                goodsInfoRepository.updateSaleTypeByGoodsId(goods.getGoodsId(), i.getSaleType().ordinal());
                // 根据spuId先删除spu的阶梯价
                intervalPriceRepository.deleteByGoodsId(goods.getGoodsId());
                // 保存spu的阶梯价
                intervalPriceRepository.saveAll(firstSkuIntervalPrices);
            }
        });
        //设置区间价,采取先删后增的方式
        intervalPriceRepository.deleteByGoodsInfoIds(delIds);
        intervalPriceRepository.saveAll(allIntervalPriceList);
        //保存intervalPrice
        goodsInfoIds.clear();
    }

    private void adjustLevelPrice(List<PriceAdjustmentRecordDetail> list, Map<String, GoodsInfo> goodsInfoMap,
                                  Map<String, Goods> goodsMap, List<String> failGoodsInfoIds,
                                  Map<String, PriceAdjustmentRecordDetail> priceAdjustMap) {
        List<String> goodsInfoIds = new ArrayList<>(goodsInfoMap.keySet());
        List<GoodsLevelPrice> levelPriceListAll = new ArrayList<>();
        // spu下第一个sku的等级价
        List<GoodsLevelPrice> firstSkuLevelPrices = new ArrayList<>();

        list.forEach(i -> {
            GoodsInfo goodsInfo = goodsInfoMap.get(i.getGoodsInfoId());
            if (Objects.isNull(goodsInfo) || goodsInfo.getDelFlag() == DeleteFlag.YES) {
                failGoodsInfoIds.add(i.getGoodsInfoId());
                i.setAdjustResult(PriceAdjustmentResult.FAIL);
                i.setFailReason(_GOODS_NOT_EXISTS_TEXT);
                return;
            }
            Goods goods = goodsMap.get(goodsInfo.getGoodsId());
            if (Objects.isNull(goods)) {
                failGoodsInfoIds.add(i.getGoodsInfoId());
                i.setAdjustResult(PriceAdjustmentResult.FAIL);
                i.setFailReason(_GOODS_NOT_EXISTS_TEXT);
                return;
            }

            //spu下第一个sku
            PriceAdjustmentRecordDetail firstRecordDetail = priceAdjustMap.get(goods.getGoodsId());
            if (Objects.isNull(firstRecordDetail)) {
                priceAdjustMap.put(goods.getGoodsId(), i);
                firstRecordDetail = i;

                // 给spu设置该spu下第一个sku的等级价
                List<GoodsLevelPrice> levelPriceList = JSON.parseArray(i.getLeverPrice(), GoodsLevelPrice.class)
                        .stream().filter(t -> Objects.nonNull(t.getPrice())).collect(Collectors.toList());
                levelPriceList.forEach(level -> {
                    level.setType(PriceType.SPU);
                    level.setGoodsInfoId("");
                    firstSkuLevelPrices.add(level);
                });

                //修改同一个spu下，不在调价列表中的其他的sku销售类型
                goodsInfoRepository.updateSaleTypeByGoodsId(goods.getGoodsId(), i.getSaleType().ordinal());
                // 根据spuId先删除spu的客户等级价
                levelPriceRepository.deleteByGoodsId(goods.getGoodsId());
                // 保存spu的等级价
                levelPriceRepository.saveAll(firstSkuLevelPrices);
                goods.setMarketPrice(firstRecordDetail.getAdjustedMarketPrice());
            }
            goodsInfo.setMarketPrice(i.getAdjustedMarketPrice());
            //修改销售类型
            goodsInfo.setSaleType(i.getSaleType().ordinal());
            goods.setSaleType(i.getSaleType().ordinal());
            //是否独立设价
            goodsInfo.setAloneFlag(i.getAloneFlag());
            //SKU设置独立设价，置SPU允许独立设价
            goods.setAllowPriceSet(i.getAloneFlag() ? DefaultFlag.YES.ordinal() : goods.getAllowPriceSet());
            //设价类型
            goods.setPriceType(GoodsPriceType.CUSTOMER.ordinal());
            //设置等级价
            List<GoodsLevelPrice> levelPriceList = JSON.parseArray(i.getLeverPrice(), GoodsLevelPrice.class)
                    .stream().filter(t -> Objects.nonNull(t.getPrice())).collect(Collectors.toList());
            levelPriceListAll.addAll(levelPriceList);

            i.setAdjustResult(PriceAdjustmentResult.DONE);
        });
        // 根据skuIds先删除客户等级价
        levelPriceRepository.deleteByGoodsInfoIds(goodsInfoIds);
        //保存levelPrice
        levelPriceRepository.saveAll(levelPriceListAll);
        goodsInfoIds.clear();
    }

    private void adjustMarketPrice(List<PriceAdjustmentRecordDetail> list, Map<String, GoodsInfo> goodsInfoMap,
                                   Map<String, Goods> goodsMap, List<String> failGoodsInfoIds,
                                   Map<String, PriceAdjustmentRecordDetail> priceAdjustMap) {
        list.forEach(i -> {
            GoodsInfo goodsInfo = goodsInfoMap.get(i.getGoodsInfoId());
            if (Objects.isNull(goodsInfo) || goodsInfo.getDelFlag() == DeleteFlag.YES) {
                failGoodsInfoIds.add(i.getGoodsInfoId());
                i.setAdjustResult(PriceAdjustmentResult.FAIL);
                i.setFailReason(_GOODS_NOT_EXISTS_TEXT);
                return;
            }
            Goods goods = goodsMap.get(goodsInfo.getGoodsId());
            if (Objects.isNull(goods)) {
                failGoodsInfoIds.add(i.getGoodsInfoId());
                i.setAdjustResult(PriceAdjustmentResult.FAIL);
                i.setFailReason(_GOODS_NOT_EXISTS_TEXT);
                return;
            }
            //修改销售类型
            goodsInfo.setSaleType(i.getSaleType().ordinal());
            goods.setSaleType(i.getSaleType().ordinal());
            //修改市场价
            goodsInfo.setMarketPrice(i.getAdjustedMarketPrice());
            //spu统一市场价
            PriceAdjustmentRecordDetail firstRecordDetail = priceAdjustMap.get(goods.getGoodsId());
            if (Objects.isNull(firstRecordDetail)) {
                priceAdjustMap.put(goods.getGoodsId(), i);
//                firstRecordDetail = i;
                //修改同一个spu下，不在调价列表中的其他的sku销售类型
//                goodsInfoRepository.updateSaleTypeByGoodsId(goods.getGoodsId(), i.getSaleType().ordinal());

//                goods.setMarketPrice(firstRecordDetail.getAdjustedMarketPrice());
                //修改设价方式
                //归属于同一个spu下的sku选择的设价方式不一致，以第一个sku的设价方式为准
                //按订货量设价在调整市场价时如果更改来销售类型（批发->零售），设计方式则改为市场价
//                if(GoodsPriceType.STOCK.ordinal() == goods.getPriceType() && SaleType.RETAIL.equals(i.getSaleType())) {
//                    goods.setPriceType(GoodsPriceType.MARKET.ordinal());
//                } else {
//                    goods.setPriceType(firstRecordDetail.getPriceType() == null ? goods.getPriceType() : firstRecordDetail.getPriceType().ordinal());
//                }
            }

            i.setAdjustResult(PriceAdjustmentResult.DONE);
        });
    }

    /**
     * 改价详情商品信息同步
     *
     * @param adjustNo
     * @param pageRequest
     * @param storeId
     * @param isConfirmAdjustPrice
     * @return
     */
    private Page<PriceAdjustmentRecordDetail> fillGoodsInfo(String adjustNo, PageRequest pageRequest, Long storeId, boolean isConfirmAdjustPrice) {
        PriceAdjustmentRecord record = priceAdjustmentRecordRepository.findByIdAndStoreId(adjustNo, storeId);
        PriceAdjustmentRecordDetailQueryRequest queryRequest = new PriceAdjustmentRecordDetailQueryRequest();
        queryRequest.setPriceAdjustmentNo(adjustNo);
        queryRequest.setAdjustResult(PriceAdjustmentResult.UNDO.toValue());
        queryRequest.setConfirmFlag(DefaultFlag.NO.toValue());
        Page<PriceAdjustmentRecordDetail> pageResult = priceAdjustmentRecordDetailRepository.findAll(
                PriceAdjustmentRecordDetailWhereCriteriaBuilder.build(queryRequest), pageRequest);
        //重置最新商品信息
        List<String> skuIds = pageResult.get().map(PriceAdjustmentRecordDetail::getGoodsInfoId).collect(Collectors.toList());
        GoodsInfoResponse response = goodsInfoService.findSkuByIds(GoodsInfoRequest.builder().goodsInfoIds(skuIds).isHavSpecText(NumberUtils.INTEGER_ONE).build());
        Map<String, GoodsInfo> goodsInfoMap = response.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfo::getGoodsInfoId, Function.identity()));
        Map<String, GoodsPriceType> priceTypeMap = new HashMap<>();
        pageResult.forEach(i -> {
            GoodsInfo sku = goodsInfoMap.get(i.getGoodsInfoId());
            i.setGoodsInfoName(sku.getGoodsInfoName());
            i.setGoodsInfoImg(sku.getGoodsInfoImg());
            i.setGoodsSpecText(sku.getSpecText());
            i.setSaleType(Objects.nonNull(i.getSaleType()) ? i.getSaleType() : SaleType.fromValue(sku.getSaleType()));
            i.setAloneFlag(Objects.nonNull(i.getAloneFlag()) ? i.getAloneFlag() : sku.getAloneFlag());

            //供应商商品没有设价方式
            if (!PriceAdjustmentType.SUPPLY.equals(record.getPriceAdjustmentType())) {
                //设价方式未设置时取商品原设置
                i.setPriceType(Objects.nonNull(i.getPriceType()) ? i.getPriceType() : GoodsPriceType.fromValue(sku.getPriceType()));
            }
            if (isConfirmAdjustPrice) {
                i.setOriginalMarketPrice(sku.getMarketPrice());
                i.setSupplyPrice(sku.getSupplyPrice());
                i.setConfirmFlag(DefaultFlag.YES);
            }
            if (PriceAdjustmentType.SUPPLY.equals(record.getPriceAdjustmentType())) {
                i.setPriceDifference(i.getAdjustSupplyPrice().subtract(i.getSupplyPrice()));
            } else if (PriceAdjustmentType.MARKET.equals(record.getPriceAdjustmentType())) {
                i.setPriceDifference(i.getAdjustedMarketPrice().subtract(i.getOriginalMarketPrice()));
                //归属于同一个spu下的sku,选择的设价方式不一致，以第一个sku的设价方式为准
                GoodsPriceType goodsPriceType = priceTypeMap.get(sku.getGoodsId());
                if (goodsPriceType == null) {
                    priceTypeMap.put(sku.getGoodsId(), i.getPriceType());
                    goodsPriceType = i.getPriceType();
                }
                i.setPriceType(goodsPriceType);
            }

            // 转换客户等级价名称
            String levelPriceStr = i.getLeverPrice();
            if (StringUtils.isNotBlank(levelPriceStr)) {
                List<GoodsLevelPriceDTO> levelPriceList = JSON.parseArray(levelPriceStr, GoodsLevelPriceDTO.class);
                StoreByIdResponse store =
                        storeQueryProvider.getById(StoreByIdRequest.builder().storeId(storeId).build()).getContext();
                if (Objects.nonNull(store) && Objects.nonNull(store.getStoreVO())) {
                    BoolFlag companyType = store.getStoreVO().getCompanyType();
                    if (BoolFlag.NO.equals(companyType)) {
                        //平台客户等级
                        List<CustomerLevelVO> customerLevelVOList =
                                customerLevelQueryProvider.listAllCustomerLevel().getContext().getCustomerLevelVOList();
                        if (CollectionUtils.isNotEmpty(customerLevelVOList)) {
                            levelPriceList.forEach(levelPrice -> {
                                if(levelPrice.getLevelId().equals(NumberUtils.LONG_ZERO)) {
                                    levelPrice.setLevelName(DEFAULT_LEVEL_NAME);
                                } else {
                                    customerLevelVOList.stream().filter(level -> Objects.equals(level.getCustomerLevelId(), levelPrice.getLevelId()))
                                            .findFirst().ifPresent(customerLevelVO -> levelPrice.setLevelName(customerLevelVO.getCustomerLevelName()));
                                }
                            });
                        }
                    } else {
                        //商家
                        StoreLevelListRequest storeLevelListRequest =
                                StoreLevelListRequest.builder().storeId(storeId).build();
                        List<StoreLevelVO> storeLevelVOList = storeLevelQueryProvider
                                .listAllStoreLevelByStoreId(storeLevelListRequest)
                                .getContext().getStoreLevelVOList();
                        if (CollectionUtils.isNotEmpty(storeLevelVOList)) {
                            levelPriceList.forEach(levelPrice -> {
                                if(levelPrice.getLevelId().equals(NumberUtils.LONG_ZERO)) {
                                    levelPrice.setLevelName(DEFAULT_LEVEL_NAME);
                                } else {
                                    storeLevelVOList.stream().filter(level -> Objects.equals(level.getStoreLevelId(), levelPrice.getLevelId()))
                                            .findFirst().ifPresent(storeLevelVO -> levelPrice.setLevelName(storeLevelVO.getLevelName()));
                                }
                            });
                        }
                    }
                }
                i.setLeverPrice(JSONObject.toJSONString(levelPriceList));
            }
        });
        return pageResult;
    }

    /**
     * 调价执行失败,更新执行状态
     *
     * @param adjustNo
     * @param result
     * @param failReason
     */
    @Transactional
    public void executeFail(String adjustNo, PriceAdjustmentResult result, String failReason) {
        priceAdjustmentRecordDetailRepository.executeFail(adjustNo, result, failReason);
    }

    /**
     * 更新其他sku的阶梯价
     * @param goodsRelMap
     */
    @Transactional
    public void modifyOtherSku(Map<String, List<String>> goodsRelMap, Map<String, PriceAdjustmentRecordDetail> priceAdjustMap){
        List<GoodsIntervalPrice> saveList = new ArrayList<>();
        goodsRelMap.forEach((key, value) -> {
            List<String> goodsInfoIds = goodsInfoRepository.findOtherGoodsInfoByGoodsInfoIds(key, value);

            // 根据skuId先删除sku的阶梯价
            intervalPriceRepository.deleteByGoodsInfoIds(goodsInfoIds);
            PriceAdjustmentRecordDetail detail = priceAdjustMap.get(key);
            goodsInfoIds.forEach(id -> {
                List<GoodsIntervalPrice> intervalPriceList = JSON.parseArray(detail.getIntervalPrice(), GoodsIntervalPrice.class)
                        .stream().filter(t -> Objects.nonNull(t.getPrice()) && Objects.nonNull(t.getCount())).collect(Collectors.toList());
                intervalPriceList.forEach(intervalPrice -> {
                    intervalPrice.setGoodsInfoId(id);
                });
                saveList.addAll(intervalPriceList);
            });
        });
        // 保存sku的阶梯价
        intervalPriceRepository.saveAll(saveList);
    }

}

