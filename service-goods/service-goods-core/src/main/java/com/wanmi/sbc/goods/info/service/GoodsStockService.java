package com.wanmi.sbc.goods.info.service;



import com.alibaba.fastjson.JSON;
import com.sbc.wanmi.erp.bean.vo.ERPGoodsInfoVO;
import com.sbc.wanmi.erp.bean.vo.ErpStockVo;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncMaxIdProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncProviderResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsMinusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsPlusStockDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.blacklist.service.GoodsBlackListService;
import com.wanmi.sbc.goods.bookuu.BooKuuSupplierClient;
import com.wanmi.sbc.goods.bookuu.request.GoodsStockRequest;
import com.wanmi.sbc.goods.bookuu.response.GoodsStockResponse;
import com.wanmi.sbc.goods.info.model.entity.GoodsStockInfo;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsStockSync;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.repository.GoodsStockSyncRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsInfoStockSyncRequest;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsStockSyncQueryRequest;
import com.wanmi.sbc.goods.redis.RedisHIncrBean;
import com.wanmi.sbc.goods.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品缓存服务
 * Created by daiyitian on 2017/4/11.
 */
@Slf4j
@Service
public class GoodsStockService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsInfoStockService goodsInfoStockService;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private GuanyierpProvider guanyierpProvider;

    @Autowired
    private GoodsStockSyncRepository goodsStockSyncRepository;

    @Autowired
    private GoodsStockService goodsStockService;

    @Autowired
    private GoodsBlackListService goodsBlackListService;

    @Autowired
    private BooKuuSupplierClient booKuuSupplierClient;

    @Value("${default.providerId}")
    private String defaultProviderId;



    /**
     * 批量加库存
     *
     * @param dtoList 增量库存参数
     */
    public void batchAddStock(List<GoodsPlusStockDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        //缓存是扣库存性缓存，加库存则扣除
        List<RedisHIncrBean> beans = dtoList.stream().map(g -> new RedisHIncrBean(g.getGoodsId(), -g.getStock()))
                .collect(Collectors.toList());
        redisService.hincrPipeline(CacheKeyConstant.GOODS_STOCK_SUB_CACHE, beans);
    }

    /**
     * 批量减库存
     *
     * @param dtoList 减量库存参数
     */
    public void batchSubStock(List<GoodsMinusStockDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        List<RedisHIncrBean> beans = dtoList.stream().map(g -> new RedisHIncrBean(g.getGoodsId(), g.getStock()))
                .collect(Collectors.toList());
        redisService.hincrPipeline(CacheKeyConstant.GOODS_STOCK_SUB_CACHE, beans);
    }

    @Transactional
    public void syncStock() {
        log.info("更新spu库存");
        Map<String, String> res = redisService.hgetAll(CacheKeyConstant.GOODS_STOCK_SUB_CACHE);
        List<GoodsMinusStockDTO> errStock = new ArrayList<>();//保存更新异常的数据
        List<GoodsPlusStockDTO> resetStock = new ArrayList<>();//重置
        if (MapUtils.isNotEmpty(res)) {
            res.forEach((goodsId, stock) -> {
                log.info("更新spu库存，扣除{} -> spuId:{}", stock, goodsId);
                Long tmpStock = NumberUtils.toLong(stock, 0);
                if (tmpStock != 0) {
                    try {
                        //持久至mysql
                        goodsRepository.subStockById(tmpStock, goodsId);
                        resetStock.add(new GoodsPlusStockDTO(tmpStock, goodsId));
                    } catch (Exception e) {
                        log.error("扣除spu库存异常", e);
                        errStock.add(new GoodsMinusStockDTO(tmpStock, goodsId));
                    }
                }
            });


            LocalTime nowTime = LocalTime.now();
            if (nowTime.getHour() > 0 && nowTime.getHour() < 8) {
                //如果在夜间，全清
                redisService.delete(CacheKeyConstant.GOODS_STOCK_SUB_CACHE);
                //保留错误数据，以便下次同步继续
                batchSubStock(errStock);
            } else {//重置-扣除redis
                batchAddStock(resetStock);
            }
        }
    }

    /**
     * 同步ERP商品库存
     */
//    @Transactional
//    public Map<String, Map<String, Integer>> syncERPGoodsStock(String skuNo, int pageNum, int pageSize) {
//        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
//        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
//        infoQueryRequest.setGoodsType(GoodsType.VIRTUAL_COUPON.toValue());
//        infoQueryRequest.setAddedFlags(Lists.newArrayList(AddedFlag.YES.toValue(), AddedFlag.PART.toValue()));
//        if (StringUtils.isNoneBlank(skuNo)) {
//            infoQueryRequest.setLikeGoodsInfoNo(skuNo);
//        }
//        if (pageNum >= 0) {
//            infoQueryRequest.setPageNum(pageNum);
//        }
//        infoQueryRequest.setPageSize(pageSize);
//        Page<GoodsInfo> goodsInfoPage = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria(),
//                infoQueryRequest.getPageRequest());
//        List<GoodsInfo> goodsInfoList = goodsInfoPage.getContent();
//        // 3 遍历商品列表
//        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
//        Map<String, Integer> skuStockMap = new HashMap<>();
//        goodsInfoList.forEach(goodsInfo -> {
//            //判断是否是组合商品，组合商品的库存不同步
//            log.info("========GoodsInfoId:{} ========  GoodsId：{}==========", goodsInfo.getGoodsInfoId(), goodsInfo.getGoodsId());
//            if (Objects.nonNull(goodsInfo.getCombinedCommodity()) && !goodsInfo.getCombinedCommodity()) {
//                SynGoodsInfoRequest erpSynGoodsStockRequest =
//                        SynGoodsInfoRequest.builder().skuCode(goodsInfo.getErpGoodsInfoNo()).build();
//                BaseResponse<SyncGoodsInfoResponse> erpSyncGoodsStockResponseBaseResponse =
//                        guanyierpProvider.syncGoodsStock(erpSynGoodsStockRequest);
//                if (!ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse) && !ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse.getContext())) {
//                    List<ERPGoodsInfoVO> erpGoodsStockVOList =
//                            erpSyncGoodsStockResponseBaseResponse.getContext().getErpGoodsInfoVOList();
//                    if (CollectionUtils.isNotEmpty(erpGoodsStockVOList)) {
//                        //根据SKU维度查询库存,接口只会返回一条数据,所有只需要取List.get(0)
//                        ERPGoodsInfoVO erpGoodsInfoVO = erpGoodsStockVOList.stream().max(Comparator.comparing(ERPGoodsInfoVO::getSalableQty)).get();
//                        log.info("========GoodsInfoId:{} ========  erpGoodsInfoVO：{}==========", goodsInfo.getGoodsInfoId(), erpGoodsInfoVO);
//                        // 只有大于0的库存才能同步
//                        if (Long.valueOf(erpGoodsInfoVO.getSalableQty()) > 0) {
//                            Long actualStock = goodsInfoStockService.getActualStock(Long.valueOf(erpGoodsInfoVO.getSalableQty()), goodsInfo.getGoodsInfoId());
//                            goodsInfoStockService.resetGoodsById(Long.valueOf(erpGoodsInfoVO.getSalableQty()), goodsInfo.getGoodsInfoId(), actualStock);
//                            skuStockMap.put(goodsInfo.getGoodsInfoId(), erpGoodsInfoVO.getSalableQty());
//                        }
//                    }
//                }
//            }
//        });
//
//        //同步更新SPU库存
//        Map<String, Integer> spuStockMap = new HashMap<>();
//        List<String> goodsIds = goodsInfoList.stream().map(GoodsInfo::getGoodsId).distinct().collect(Collectors.toList());
//        List<Goods> goodsList = goodsService.listByGoodsIds(goodsIds);
//        goodsList.stream().forEach(goodsVO -> {
//            List<GoodsInfo> goodsInfos = goodsInfoRepository.findByGoodsIds(Lists.newArrayList(goodsVO.getGoodsId()));
//            List<Long> salableQty = new ArrayList<>();
//            goodsInfos.forEach(goodsInfo -> {
//                log.info("========同步spu库存GoodsInfoId:{} ==================", goodsInfo.getGoodsInfoId());
//                if (Objects.nonNull(goodsInfo.getCombinedCommodity()) && !goodsInfo.getCombinedCommodity()) {
//                    SynGoodsInfoRequest erpSynGoodsStockRequest =
//                            SynGoodsInfoRequest.builder().skuCode(goodsInfo.getErpGoodsInfoNo()).build();
//                    BaseResponse<SyncGoodsInfoResponse> erpSyncGoodsStockResponseBaseResponse =
//                            guanyierpProvider.syncGoodsStock(erpSynGoodsStockRequest);
//                    if (!ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse)
//                            && Objects.nonNull(erpSyncGoodsStockResponseBaseResponse.getContext())) {
//
//                        List<ERPGoodsInfoVO> erpGoodsStockVOList =
//                                erpSyncGoodsStockResponseBaseResponse.getContext().getErpGoodsInfoVOList();
//                        if (CollectionUtils.isNotEmpty(erpGoodsStockVOList)) {
//                            ERPGoodsInfoVO erpGoodsInfoVO = erpGoodsStockVOList.stream().max(Comparator.comparing(ERPGoodsInfoVO::getSalableQty)).get();
//                            // 只有大于0的库存才能同步
//                            log.info("========同步spu库存GoodsInfoId:{} ========  ERPGoodsInfoVO：{}==========", goodsInfo.getGoodsInfoId(), erpGoodsInfoVO);
//                            if (Long.valueOf(erpGoodsInfoVO.getSalableQty()) > 0) {
//                                salableQty.add(Long.valueOf(erpGoodsInfoVO.getSalableQty()));
//                            }
//                        }
//                    }
//                } else {
//                    salableQty.add(goodsInfo.getStock());
//                    log.info("========同步spu库存组合购商品GoodsInfoId:{}==========", goodsInfo.getGoodsInfoId());
//                }
//            });
//            //计算spu的商品总数
//            if (CollectionUtils.isNotEmpty(salableQty)) {
//                Long salableQtySum = salableQty.stream().reduce(Long::sum).get();
//                if (salableQtySum > 0) {
//                    goodsRepository.resetGoodsStockById(Long.valueOf(salableQtySum), goodsVO.getGoodsId());
//                    spuStockMap.put(goodsVO.getGoodsId(), salableQtySum.intValue());
//                    log.info("========goodsVOId:{} ========  salableQtySum：{}==========", goodsVO.getGoodsId(), salableQtySum);
//                }
//            }
//        });
//        resultMap.put("skus", skuStockMap);
//        resultMap.put("spus", spuStockMap);
//        log.info("resultMap====================>:{}", resultMap);
//        return resultMap;
//    }


    /**
     * 批量更新库存 根据频率计算，此处可以考虑更新 前200个商品 以及剩余的商品
     * @param startTime
     */
    public GoodsInfoStockSyncMaxIdProviderResponse batchUpdateStock(List<String> goodsIdList, String startTime, long maxTmpId, int pageSize) {
        long beginTime = System.currentTimeMillis();
        String providerId = defaultProviderId; //管易云
        List<GoodsInfoStockSyncProviderResponse> tmpResult = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsIdList)) {
            GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
            goodsQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            goodsQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
            goodsQueryRequest.setGoodsIds(goodsIdList);
            if (StringUtils.isNotBlank(providerId)) {
                goodsQueryRequest.setProviderId(providerId);
            }
            List<Goods> goodsList = goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());

            for (Goods goodsParam : goodsList) {
                if (StringUtils.isBlank(goodsParam.getGoodsNo())) {
                    List<GoodsInfo> goodsInfoList = goodsInfoRepository.findByGoodsIdIn(Collections.singletonList(goodsParam.getGoodsId()));
                    for (GoodsInfo goodsInfo : goodsInfoList) {
                        if (StringUtils.isNotBlank(goodsInfo.getErpGoodsNo())) {
                            goodsParam.setErpGoodsNo(goodsInfo.getErpGoodsNo());
                            break;
                        }
                    }
                }
                tmpResult.addAll(this.executeBatchUpdateStock(goodsParam.getGoodsId(), goodsParam.getErpGoodsNo(), startTime));
            }
        } else {
            List<Map<String, Object>> goodsList = goodsRepository.listByMaxAutoId(providerId, maxTmpId, pageSize);
            for (Map<String, Object> goodsParam : goodsList) {
                String erpGoodsNo = "";
                String goodsId = "";
                Long tmpId = null;
                for (Map.Entry<String, Object> entry : goodsParam.entrySet()) {
                    if ("erp_goods_no".equals(entry.getKey())) {
                        erpGoodsNo = entry.getValue().toString();
                    }
                    if ("tmp_id".equals(entry.getKey())) {
                        tmpId = Long.parseLong(entry.getValue().toString());
                    }
                    if ("goods_id".equals(entry.getKey()))  {
                        goodsId = entry.getValue().toString();
                    }
                }

                if (StringUtils.isBlank(erpGoodsNo)) {
                    List<GoodsInfo> goodsInfoList = goodsInfoRepository.findByGoodsIdIn(Collections.singletonList(erpGoodsNo));
                    for (GoodsInfo goodsInfo : goodsInfoList) {
                        if (StringUtils.isNotBlank(goodsInfo.getErpGoodsNo())) {
                            erpGoodsNo = goodsInfo.getErpGoodsNo();
                            break;
                        }
                    }
                }
                tmpResult.addAll(this.executeBatchUpdateStock(goodsId, erpGoodsNo, startTime));
                if (tmpId!= null && maxTmpId < tmpId) {
                    maxTmpId = tmpId;
                }
            }

        }

        if (CollectionUtils.isNotEmpty(tmpResult)) {
            //更新goods库存
            Map<String, Integer> spuId2ActualStockQtyMap =
                    tmpResult.stream().collect(Collectors.groupingBy(GoodsInfoStockSyncProviderResponse::getSpuId, Collectors.summingInt(GoodsInfoStockSyncProviderResponse::getActualStockQty)));
            spuId2ActualStockQtyMap.forEach((K, V) -> {
                goodsRepository.updateStockByGoodsId(Long.valueOf(V), K);
            });
        }
        GoodsInfoStockSyncMaxIdProviderResponse result = new GoodsInfoStockSyncMaxIdProviderResponse();
        result.setMaxTmpId(maxTmpId);
        result.setGoodsInfoStockSyncList(tmpResult);
        log.info("GoodsStockService batchUpdateStock complete all cost: {} ms", (System.currentTimeMillis() - beginTime));
        return result;
    }

    /**
     * 执行批量更新库存
     * @param erpGoodsCodeNo
     * @param startTime
     */
    private List<GoodsInfoStockSyncProviderResponse> executeBatchUpdateStock(String goodsId, String erpGoodsCodeNo, String startTime) {
        if (StringUtils.isBlank(erpGoodsCodeNo)) {
            log.info("GoodsStockService batchUpdateStock erpGoodsCodeNo:{} goodsId:{} erpGoodsCodeNo is null return ", erpGoodsCodeNo, goodsId);
            return new ArrayList<>();
        }
        //获取仓库黑名单
        List<String> unStaticsKey = new ArrayList<>();
        GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest = new GoodsBlackListPageProviderRequest();
        goodsBlackListPageProviderRequest.setBusinessCategoryColl(
                Collections.singletonList(GoodsBlackListCategoryEnum.UN_SHOW_WAREHOUSE.getCode()));
        GoodsBlackListPageProviderResponse goodsBlackListPageProviderResponse = goodsBlackListService.listNoPage(goodsBlackListPageProviderRequest);
        if (goodsBlackListPageProviderResponse.getWareHouseListModel() != null && !CollectionUtils.isEmpty(goodsBlackListPageProviderResponse.getWareHouseListModel().getNormalList())) {
            unStaticsKey.addAll(goodsBlackListPageProviderResponse.getWareHouseListModel().getNormalList());
        }

        int pageNum = 1;
        int pageSize = 100;
        List<ERPGoodsInfoVO> result = new ArrayList<>(100);
        while (true) {
            BaseResponse<ErpStockVo> listWareHoseStock = guanyierpProvider.listWareHoseStock(pageNum, pageSize, erpGoodsCodeNo);
            ErpStockVo erpStockInfo = listWareHoseStock.getContext();
            if (erpStockInfo.getStocks() == null) {
                erpStockInfo.setStocks(new ArrayList<>());
            }
            log.info("GoodsStockService batchUpdateStock pull warehostStock erpGoodsCodeNo:{} goodsId:{} pageNum:{} pageSize:{} total:{}", erpGoodsCodeNo, goodsId, pageNum, pageSize, erpStockInfo.getTotal());

            int cycleCount = erpStockInfo.getTotal() / pageSize;
            int remainder = erpStockInfo.getTotal() % pageSize;
            cycleCount += remainder > 0 ? 1 : 0;
            pageNum++;
            result.addAll(erpStockInfo.getStocks());
            if (pageNum > cycleCount) {
                break;
            }
        }

        Map<String, Integer> erpSkuCode2ErpStockQtyMap = new HashMap<>();
        for (ERPGoodsInfoVO erpGoodsInfoVo : result) {
            if (erpGoodsInfoVo.getDel() || unStaticsKey.contains(erpGoodsInfoVo.getWarehouseCode())) {
                log.info("GoodsStockService batchUpdateStock erpGoodsCodeNo:{}  itemCode:{} itemName:{} skuCode:{} skuName:{} warehouseCode:{} is del or blackList contain this so continue",
                        erpGoodsCodeNo, erpGoodsInfoVo.getItemCode(), erpGoodsInfoVo.getItemName(), erpGoodsInfoVo.getItemCode(), erpGoodsInfoVo.getItemSkuName(), erpGoodsInfoVo.getWarehouseCode());
                continue;
            }
            Integer saleableStockQty = erpSkuCode2ErpStockQtyMap.get(erpGoodsInfoVo.getSkuCode());
            int tmpStockQty = saleableStockQty == null ? erpGoodsInfoVo.getSalableQty() : saleableStockQty + erpGoodsInfoVo.getSalableQty();
            erpSkuCode2ErpStockQtyMap.put(erpGoodsInfoVo.getSkuCode(), tmpStockQty);
        }

        //获取sku 库存状态
        List<ERPGoodsInfoVO> erpGoodsInfoVOList = new ArrayList<>();
        try {
            BaseResponse<List<ERPGoodsInfoVO>> erpGoodsInfoWithoutStock = guanyierpProvider.getErpGoodsInfoWithoutStock(erpGoodsCodeNo);
            List<ERPGoodsInfoVO> erpGoodsInfoVOListTmp = erpGoodsInfoWithoutStock.getContext();
            if (!CollectionUtils.isEmpty(erpGoodsInfoVOListTmp)) {
                erpGoodsInfoVOList = erpGoodsInfoVOListTmp;
            }
        } catch (Exception ex) {
            log.warn("GoodsStockService batchUpdateStock erpGoodsCodeNo:{} 访问管易异常", erpGoodsCodeNo, ex);
        }


        List<GoodsInfoStockSyncRequest> goodsInfoStockSyncRequestList = new ArrayList<>();
        for (ERPGoodsInfoVO erpGoodsInfoParam : erpGoodsInfoVOList) {
            // todo 判断当前的库存状态，来确定当前 库存数量的值
            boolean isCalculateStock = true;

            int erpStockQty = 9999;
            if (Arrays.asList("1", "3", "2").contains(erpGoodsInfoParam.getStockStatusCode())) {
                erpStockQty = erpSkuCode2ErpStockQtyMap.get(erpGoodsInfoParam.getSkuCode()) == null
                        ? 0 : erpSkuCode2ErpStockQtyMap.get(erpGoodsInfoParam.getSkuCode());
            } else if (Objects.equals("0", erpGoodsInfoParam.getStockStatusCode())) {
                isCalculateStock = false;
            } else {
                log.info("GoodsStockService batchUpdateStock erpGoodsCodeNo:{} stockStatusCode is not 1、2、3、4", erpGoodsCodeNo);
                isCalculateStock = false;
                erpStockQty = 0;
            }

            GoodsInfoStockSyncRequest goodsInfoStockSyncRequest = new GoodsInfoStockSyncRequest();
            goodsInfoStockSyncRequest.setSpuId(goodsId);
            goodsInfoStockSyncRequest.setErpSpuCode(erpGoodsInfoParam.getItemCode());
            goodsInfoStockSyncRequest.setErpSkuCode(erpGoodsInfoParam.getSkuCode());
            goodsInfoStockSyncRequest.setIsCalculateStock(isCalculateStock);
            goodsInfoStockSyncRequest.setErpStockQty(erpStockQty);
            goodsInfoStockSyncRequest.setErpCostPrice(erpGoodsInfoParam.getCostPrice() == null ? BigDecimal.ZERO : erpGoodsInfoParam.getCostPrice());
            goodsInfoStockSyncRequestList.add(goodsInfoStockSyncRequest);
        }

        log.info("GoodsStockService batchUpdateStock erpGoodsCodeNo:{} goodsId:{} goodsInfoStockSyncRequest:{}", erpGoodsCodeNo, goodsId, JSON.toJSONString(goodsInfoStockSyncRequestList));
        if (CollectionUtils.isEmpty(goodsInfoStockSyncRequestList)) {
            GoodsInfoStockSyncRequest goodsInfoStockSyncRequest = new GoodsInfoStockSyncRequest();
            goodsInfoStockSyncRequest.setSpuId(goodsId);
            goodsInfoStockSyncRequest.setErpSpuCode(erpGoodsCodeNo);
//            goodsInfoStockSyncRequest.setErpSkuCode(erpGoodsInfoParam.getSkuCode());
            goodsInfoStockSyncRequest.setIsCalculateStock(true);
            goodsInfoStockSyncRequest.setErpStockQty(0);
            goodsInfoStockSyncRequest.setErpCostPrice(BigDecimal.ZERO);
            goodsInfoStockSyncRequestList.add(goodsInfoStockSyncRequest);
        }
        return goodsInfoStockService.batchUpdateGoodsInfoStock(goodsInfoStockSyncRequestList);
    }

//    public Map<String, Map<String, Integer>> partialUpdateStock(String erpGoodInfoNo, String lastSyncTime, String pageNum, String pageSize) {
//        BaseResponse<ErpStockVo> updatedStock = guanyierpProvider.getUpdatedStock(lastSyncTime, erpGoodInfoNo, pageNum, pageSize);
//        ErpStockVo erpStockInfo = updatedStock.getContext();
//        if(erpStockInfo == null || erpStockInfo.getTotal() <= 0){
//            Map<String, Map<String, Integer>> resultMap = new HashMap<>();
//            Map<String, Integer> goodsStockMap = new HashMap<>();
//            Map<String, Integer> goodsInfoStockMap = new HashMap<>();
//            Map<String, Integer> itemCountMap = new HashMap<>();
//            itemCountMap.put("total", 0);
//            resultMap.put("skus", goodsInfoStockMap);
//            resultMap.put("spus", goodsStockMap);
//            resultMap.put("total", itemCountMap);
//            return resultMap;
//        }
//        Map<String, Integer> erpSkuStockMap = new HashMap<>();
//        Map<String, String> stockStatusMap = new HashMap<>();
//        HashSet<String> erpGoodsNos = new HashSet<>();
//        for (ERPGoodsInfoVO erpGoodsInfoVO : updatedStock.getContext().getStocks()) {
//            try {
//                if(StringUtils.isEmpty(erpGoodsInfoVO.getSkuCode())) continue;
//                int salableQty = erpGoodsInfoVO.getSalableQty();
//                if(BooleanUtils.isTrue(erpGoodsInfoVO.getDel())){
//                    //停用商品库存设置为0
//                    salableQty = 0;
//                    log.info("{}停用,库存清零", erpGoodsInfoVO.getSkuCode());
//                }else {
//                    String stockStatus = stockStatusMap.get(erpGoodsInfoVO.getSkuCode());
//                    if(StringUtils.isEmpty(stockStatus)){
//                        BaseResponse<List<ERPGoodsInfoVO>> erpGoodsInfoWithoutStock = guanyierpProvider.getErpGoodsInfoWithoutStock(erpGoodsInfoVO.getItemCode());
//                        for (ERPGoodsInfoVO goodsInfoVO : erpGoodsInfoWithoutStock.getContext()) {
//                            stockStatusMap.put(goodsInfoVO.getSkuCode(), goodsInfoVO.getStockStatusCode());
//                        }
//                        stockStatus = stockStatusMap.get(erpGoodsInfoVO.getSkuCode());
//                    }
//                    if(StringUtils.isEmpty(stockStatus) || !Arrays.asList("0","1","2","3").contains(stockStatus)){
//                        log.info("仅状态为0，1，2，3的商品同步库存,sku:{}",erpGoodsInfoVO.getSkuCode());
//                        continue;
//                    }
//                    //虚拟0、代发无所库2：99逻辑，当库存＜10，自动库存变为99
//                    if(Arrays.asList("0","2").contains(stockStatus) && salableQty < 10){
//                        salableQty = 99;
//                    }else if(salableQty < 0){
//                        salableQty = 0;
//                    }
//                }
//                erpGoodsNos.add(erpGoodsInfoVO.getItemCode());
//                erpSkuStockMap.put(erpGoodsInfoVO.getSkuCode(), salableQty);
//            }catch (Exception e) {
//                log.error("partialUpdateStock error :" + JSONObject.toJSON(erpGoodsInfoVO), e);
//            }
//        }
//        List<GoodsInfo> goodsInfos = null;
//        if(!erpSkuStockMap.isEmpty()){
//            GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
//            infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
//            infoQueryRequest.setErpGoodsNos(new ArrayList<>(erpGoodsNos));
//            goodsInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
//        }
//        Map<String, Map<String, Integer>> resultMap = goodsInfoStockService.batchUpdateGoodsInfoStock(goodsInfos, erpSkuStockMap,stockStatusMap);
//        Map<String, Integer> itemCountMap = new HashMap<>();
//        itemCountMap.put("total", erpStockInfo.getTotal());
//        resultMap.put("total", itemCountMap);
//        return resultMap;
//    }

    public long countGoodsStockSync() {
        Specification<GoodsStockSync> request = (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cbuild.equal(root.get("status"), 0));
            predicates.add(cbuild.equal(root.get("deleted"), 0));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
        return goodsStockSyncRepository.count(request);
    }

    /**
     * 查询goods_stock_sync并同步库存
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<GoodsInfoStockSyncProviderResponse> syncGoodsStock(int pageNum, int pageSize) {
        List<GoodsInfoStockSyncProviderResponse> result = new ArrayList<>();
        GoodsStockSyncQueryRequest stockSyncQueryRequest = new GoodsStockSyncQueryRequest();
        stockSyncQueryRequest.setStatus(0);
        if (pageNum >= 0) {
            stockSyncQueryRequest.setPageNum(pageNum);
        }
        stockSyncQueryRequest.setPageSize(pageSize);
        Page<GoodsStockSync> stockPage = goodsStockSyncRepository.findAll(stockSyncQueryRequest.getWhereCriteria(), stockSyncQueryRequest.getPageRequest());
        if(CollectionUtils.isEmpty(stockPage.getContent())){
            return result;
        }
        List<GoodsStockSync> goodsStockList = stockPage.getContent();
        // 3 遍历商品列表并更新sku库存
        for (GoodsStockSync goodsStockSyncParam : goodsStockList) {
            GoodsInfoStockSyncProviderResponse goodsInfoStockSync = goodsStockService.updateGoodsStockSingle(goodsStockSyncParam);
            if (goodsInfoStockSync == null) {
                continue;
            }
            result.add(goodsInfoStockSync);
        }
        return result;
    }

    @Transactional
    public GoodsInfoStockSyncProviderResponse updateGoodsStockSingle(GoodsStockSync stock){
        //查询sku信息
        GoodsStockInfo goodsInfo = goodsInfoRepository.findGoodsInfoId(stock.getGoodsNo());
        if (goodsInfo ==null || Objects.equals(goodsInfo.getStockSyncFlag(),0)) {
            goodsStockSyncRepository.updateStatus(stock.getId());
            log.info("updateGoodsStockSingle there is no sku or stock,stockSyncFlag is false, goods:{},goodsInfo:{}", stock,goodsInfo);
            return null;
        }
        Long actualStock = goodsInfoStockService.getActualStock(stock.getStock().longValue(), goodsInfo.getGoodsInfoId());
        goodsInfoStockService.resetStockByGoodsInfoId(stock.getStock().longValue(), goodsInfo.getGoodsInfoId(),actualStock);
//        skuStockMap.put(goodsInfo.getGoodsInfoId(), actualStock.intValue());
        //更新spu库存
        goodsRepository.resetGoodsStockById(actualStock, goodsInfo.getGoodsId());
//        spuStockMap.put(goodsInfo.getGoodsId(), actualStock.intValue());
        //更新状态
        goodsStockSyncRepository.updateStatus(stock.getId());

        GoodsInfoStockSyncProviderResponse goodsInfoStockSyncProviderResponse = new GoodsInfoStockSyncProviderResponse();
        goodsInfoStockSyncProviderResponse.setSpuId(goodsInfo.getGoodsId());
        goodsInfoStockSyncProviderResponse.setSkuId(goodsInfo.getGoodsInfoId());
//        goodsInfoStockSyncProviderResponse.setErpSpuCode(goodsInfo.getGoodsNo());
//        goodsInfoStockSyncProviderResponse.setErpSkuCode(goodsInfo.ge);
        goodsInfoStockSyncProviderResponse.setSkuNo(goodsInfo.getGoodsInfoNo());
        goodsInfoStockSyncProviderResponse.setSkuName(goodsInfo.getGoodsInfoName());
//        goodsInfoStockSyncProviderResponse.setCanSyncStock(false);
//        goodsInfoStockSyncProviderResponse.setIsCalculateStock(false);
        goodsInfoStockSyncProviderResponse.setActualStockQty(actualStock.intValue());
        goodsInfoStockSyncProviderResponse.setErpStockQty(stock.getStock());
        goodsInfoStockSyncProviderResponse.setCurrentStockQty(goodsInfo.getStockQty().intValue());
//        goodsInfoStockSyncProviderResponse.setCanSyncCostPrice(false);
//        goodsInfoStockSyncProviderResponse.setErpCostPrice(new BigDecimal("0"));
//        goodsInfoStockSyncProviderResponse.setCurrentMarketPrice(new BigDecimal("0"));
//        goodsInfoStockSyncProviderResponse.setCurrentCostPrice(new BigDecimal("0"));
        return goodsInfoStockSyncProviderResponse;
    }


    /**
     * 指定商品同步库存
     */
    @Transactional
    public void bookuuSyncGoodsStock(List<String> goodsIdList) {
        log.info("GoodsStockService bookuuSyncGoodsStock begin param: {}", JSON.toJSONString(goodsIdList));
        long beginTime = System.currentTimeMillis();
        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        infoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
        infoQueryRequest.setGoodsIds(goodsIdList);
        List<GoodsInfo> goodsInfoList = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            return;
        }

        //获取库存
        GoodsStockRequest request = new GoodsStockRequest();
        request.setGoodsIdList(goodsIdList);
        BaseResponse<List<GoodsStockResponse>> goodsStock = booKuuSupplierClient.getGoodsStock(request);
        List<GoodsStockResponse> context = goodsStock.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return;
        }

        Map<String, Integer> erpGoodsNo2StockQtyMap = new HashMap<>();
        for (GoodsStockResponse goodsStockResponse : context) {
            erpGoodsNo2StockQtyMap.put(goodsStockResponse.getErpGoodsNo(), goodsStockResponse.getStockQty());
        }


        for (GoodsInfo goodsInfoParam : goodsInfoList) {
            if (Objects.equals(goodsInfoParam.getStockSyncFlag(),0)) {
                continue;
            }
            Integer stockQty = erpGoodsNo2StockQtyMap.get(goodsInfoParam.getErpGoodsNo());
            stockQty = stockQty == null ? 0 : stockQty;

            Long actualStock = goodsInfoStockService.getActualStock(stockQty.longValue(), goodsInfoParam.getGoodsInfoId());
            goodsInfoStockService.resetStockByGoodsInfoId(stockQty.longValue(), goodsInfoParam.getGoodsInfoId(), actualStock);
            //更新状态
            goodsStockSyncRepository.updateStatusByErpGoodsInfoNo(goodsInfoParam.getErpGoodsInfoNo());

            goodsRepository.updateStockByGoodsId(actualStock, goodsInfoParam.getGoodsId());
        }
        log.info("GoodsStockService bookuuSyncGoodsStock end param: {} cost: {}", JSON.toJSONString(goodsIdList), (System.currentTimeMillis() - beginTime));
//        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
//        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
//        infoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
//        infoQueryRequest.setGoodsIds(goodsIdList);
//        List<GoodsInfo> goodsInfoList = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
//
//        List<GoodsInfo> filterGoodsInfoList = new ArrayList<>();
//        List<String> filterErpGoodsInfoNoList = new ArrayList<>();
//        Map<String, Long> goodsId2StockQtyMap = new HashMap<>();
//        for (GoodsInfo goodsInfoParam : goodsInfoList) {
//            if (Objects.equals(goodsInfoParam.getStockSyncFlag(),0)) {
//                Long goodsIdStockQty = goodsId2StockQtyMap.get(goodsInfoParam.getGoodsId());
//                goodsIdStockQty = goodsIdStockQty == null ? 0 : goodsIdStockQty + goodsInfoParam.getStock();
//                goodsId2StockQtyMap.put(goodsInfoParam.getGoodsId(), goodsIdStockQty);
//                goodsStockSyncRepository.updateStatusByErpGoodsInfoNo(goodsInfoParam.getErpGoodsInfoNo());
//                log.info("bookuuSyncGoodsStock goodsInfoId:{} erpGoodsInfoNo:{} stockSyncFlag : 0 continue", JSON.toJSONString(goodsIdList), goodsInfoParam.getErpGoodsInfoNo());
//                continue;
//            }
//            filterErpGoodsInfoNoList.add(goodsInfoParam.getErpGoodsInfoNo());
//            filterGoodsInfoList.add(goodsInfoParam);
//        }
//
//        //同步库存
//        GoodsStockSyncQueryRequest stockSyncQueryRequest = new GoodsStockSyncQueryRequest();
////        stockSyncQueryRequest.setStatus(0);
//        stockSyncQueryRequest.setDeleted(0);
//        stockSyncQueryRequest.setGoodsNos(filterErpGoodsInfoNoList);
//        List<GoodsStockSync> goodsStockSyncList = goodsStockSyncRepository.findAll(stockSyncQueryRequest.getWhereCriteria());
//        Map<String, GoodsStockSync> erpGoodsInfoNo2GoodsStockSyncMap = new HashMap<>();
//        for (GoodsStockSync goodsStockSync : goodsStockSyncList) {
//            erpGoodsInfoNo2GoodsStockSyncMap.put(goodsStockSync.getGoodsNo(), goodsStockSync);
//        }
//
//        for (GoodsInfo goodsInfoParam : filterGoodsInfoList) {
//            GoodsStockSync goodsStockSync = erpGoodsInfoNo2GoodsStockSyncMap.get(goodsInfoParam.getErpGoodsInfoNo());
//            if (goodsStockSync == null) {
//                log.info("bookuuSyncGoodsStock goodsInfoId:{} goodsStockSync is null continue", JSON.toJSONString(goodsInfoIdList));
//                continue;
//            }
//
//            Long actualStock = goodsInfoStockService.getActualStock(goodsStockSync.getStock().longValue(), goodsInfoParam.getGoodsInfoId());
//            goodsInfoStockService.resetStockByGoodsInfoId(goodsStockSync.getStock().longValue(), goodsInfoParam.getGoodsInfoId(), actualStock);
//            //更新状态
//            goodsStockSyncRepository.updateStatusByErpGoodsInfoNo(goodsInfoParam.getErpGoodsInfoNo());
//
//            Long goodsIdStockQty = goodsId2StockQtyMap.get(goodsInfoParam.getGoodsId());
//            goodsIdStockQty = goodsIdStockQty == null ? actualStock : goodsIdStockQty + actualStock;
//            goodsId2StockQtyMap.put(goodsInfoParam.getGoodsId(), goodsIdStockQty);
//        }
//        goodsId2StockQtyMap.forEach((K, V) -> {
//            goodsRepository.updateStockByGoodsId(Long.valueOf(V), K);
//        });
    }

}
