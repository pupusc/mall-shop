package com.wanmi.sbc.goods.info.service;

import com.google.common.collect.Lists;
import com.sbc.wanmi.erp.bean.vo.ERPGoodsInfoVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.SynGoodsInfoRequest;
import com.wanmi.sbc.erp.api.response.SyncGoodsInfoResponse;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoPageRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsMinusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsPlusStockDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.info.model.entity.GoodsStockInfo;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsPriceSync;
import com.wanmi.sbc.goods.info.model.root.GoodsStockSync;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsPriceSyncRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.repository.GoodsStockSyncRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsPriceSyncQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsStockSyncQueryRequest;
import com.wanmi.sbc.goods.redis.RedisHIncrBean;
import com.wanmi.sbc.goods.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 商品缓存服务
 * Created by daiyitian on 2017/4/11.
 */
@Slf4j
@Service
public class GoodsStockService {

    //代发商品编码前缀
    private static final String AGENCY_PRODUCT_PREFIX = "DF";

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
    private GoodsService goodsService;

    @Autowired
    private GoodsStockSyncRepository goodsStockSyncRepository;

    private static final Integer MAX_THREADS_POOL = 50;

    @Autowired
    private GoodsStockService goodsStockService;

    @Autowired
    private GoodsPriceSyncRepository goodsPriceSyncRepository;


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
    public Map<String, Map<String, Integer>> syncERPGoodsStock(String skuNo, int pageNum, int pageSize) {
        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        infoQueryRequest.setGoodsType(GoodsType.VIRTUAL_COUPON.toValue());
        infoQueryRequest.setAddedFlags(Lists.newArrayList(AddedFlag.YES.toValue(), AddedFlag.PART.toValue()));
        if (StringUtils.isNoneBlank(skuNo)) {
            infoQueryRequest.setLikeGoodsInfoNo(skuNo);
        }
        if (pageNum >= 0) {
            infoQueryRequest.setPageNum(pageNum);
        }
        infoQueryRequest.setPageSize(pageSize);
        Page<GoodsInfo> goodsInfoPage = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria(),
                infoQueryRequest.getPageRequest());
        List<GoodsInfo> goodsInfoList = goodsInfoPage.getContent();
        // 3 遍历商品列表
        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
        Map<String, Integer> skuStockMap = new HashMap<>();
        goodsInfoList.forEach(goodsInfo -> {
            //判断是否是组合商品，组合商品的库存不同步
            log.info("========GoodsInfoId:{} ========  GoodsId：{}==========", goodsInfo.getGoodsInfoId(), goodsInfo.getGoodsId());
            if (Objects.nonNull(goodsInfo.getCombinedCommodity()) && !goodsInfo.getCombinedCommodity()) {
                SynGoodsInfoRequest erpSynGoodsStockRequest =
                        SynGoodsInfoRequest.builder().skuCode(goodsInfo.getErpGoodsInfoNo()).build();
                BaseResponse<SyncGoodsInfoResponse> erpSyncGoodsStockResponseBaseResponse =
                        guanyierpProvider.syncGoodsStock(erpSynGoodsStockRequest);
                if (!ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse) && !ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse.getContext())) {
                    List<ERPGoodsInfoVO> erpGoodsStockVOList =
                            erpSyncGoodsStockResponseBaseResponse.getContext().getErpGoodsInfoVOList();
                    if (CollectionUtils.isNotEmpty(erpGoodsStockVOList)) {
                        //根据SKU维度查询库存,接口只会返回一条数据,所有只需要取List.get(0)
                        ERPGoodsInfoVO erpGoodsInfoVO = erpGoodsStockVOList.stream().max(Comparator.comparing(ERPGoodsInfoVO::getSalableQty)).get();
                        log.info("========GoodsInfoId:{} ========  erpGoodsInfoVO：{}==========", goodsInfo.getGoodsInfoId(), erpGoodsInfoVO);
                        // 只有大于0的库存才能同步
                        if (Long.valueOf(erpGoodsInfoVO.getSalableQty()) > 0) {
                            Long actualStock = goodsInfoStockService.getActualStock(Long.valueOf(erpGoodsInfoVO.getSalableQty()), goodsInfo.getGoodsInfoId());
                            goodsInfoStockService.resetGoodsById(Long.valueOf(erpGoodsInfoVO.getSalableQty()), goodsInfo.getGoodsInfoId(), actualStock);
                            skuStockMap.put(goodsInfo.getGoodsInfoId(), erpGoodsInfoVO.getSalableQty());
                        }
                    }
                }
            }
        });

        //同步更新SPU库存
        Map<String, Integer> spuStockMap = new HashMap<>();
        List<String> goodsIds = goodsInfoList.stream().map(GoodsInfo::getGoodsId).distinct().collect(Collectors.toList());
        List<Goods> goodsList = goodsService.listByGoodsIds(goodsIds);
        goodsList.stream().forEach(goodsVO -> {
            List<GoodsInfo> goodsInfos = goodsInfoRepository.findByGoodsIds(Lists.newArrayList(goodsVO.getGoodsId()));
            List<Long> salableQty = new ArrayList<>();
            goodsInfos.forEach(goodsInfo -> {
                log.info("========同步spu库存GoodsInfoId:{} ==================", goodsInfo.getGoodsInfoId());
                if (Objects.nonNull(goodsInfo.getCombinedCommodity()) && !goodsInfo.getCombinedCommodity()) {
                    SynGoodsInfoRequest erpSynGoodsStockRequest =
                            SynGoodsInfoRequest.builder().skuCode(goodsInfo.getErpGoodsInfoNo()).build();
                    BaseResponse<SyncGoodsInfoResponse> erpSyncGoodsStockResponseBaseResponse =
                            guanyierpProvider.syncGoodsStock(erpSynGoodsStockRequest);
                    if (!ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse)
                            && Objects.nonNull(erpSyncGoodsStockResponseBaseResponse.getContext())) {

                        List<ERPGoodsInfoVO> erpGoodsStockVOList =
                                erpSyncGoodsStockResponseBaseResponse.getContext().getErpGoodsInfoVOList();
                        if (CollectionUtils.isNotEmpty(erpGoodsStockVOList)) {
                            ERPGoodsInfoVO erpGoodsInfoVO = erpGoodsStockVOList.stream().max(Comparator.comparing(ERPGoodsInfoVO::getSalableQty)).get();
                            // 只有大于0的库存才能同步
                            log.info("========同步spu库存GoodsInfoId:{} ========  ERPGoodsInfoVO：{}==========", goodsInfo.getGoodsInfoId(), erpGoodsInfoVO);
                            if (Long.valueOf(erpGoodsInfoVO.getSalableQty()) > 0) {
                                salableQty.add(Long.valueOf(erpGoodsInfoVO.getSalableQty()));
                            }
                        }
                    }
                } else {
                    salableQty.add(goodsInfo.getStock());
                    log.info("========同步spu库存组合购商品GoodsInfoId:{}==========", goodsInfo.getGoodsInfoId());
                }
            });
            //计算spu的商品总数
            if (CollectionUtils.isNotEmpty(salableQty)) {
                Long salableQtySum = salableQty.stream().reduce(Long::sum).get();
                if (salableQtySum > 0) {
                    goodsRepository.resetGoodsStockById(Long.valueOf(salableQtySum), goodsVO.getGoodsId());
                    spuStockMap.put(goodsVO.getGoodsId(), salableQtySum.intValue());
                    log.info("========goodsVOId:{} ========  salableQtySum：{}==========", goodsVO.getGoodsId(), salableQtySum);
                }
            }
        });
        resultMap.put("skus", skuStockMap);
        resultMap.put("spus", spuStockMap);
        log.info("resultMap====================>:{}", resultMap);
        return resultMap;
    }

    public Map<String, Map<String, Integer>> partialUpdateStock(String erpGoodInfoNo) {
        String lastSyncTime = redisService.getString(RedisKeyConstant.STOCK_SYNC_TIME_PREFIX);
        Date currentDate = new Date();
        if(lastSyncTime == null){
            lastSyncTime = "2020-01-01 00:00:00";
        }
        if(erpGoodInfoNo != null && erpGoodInfoNo.equals("initial")){
            lastSyncTime = "";
            erpGoodInfoNo = "";
        }
        BaseResponse<List<ERPGoodsInfoVO>> updatedStock = guanyierpProvider.getUpdatedStock(lastSyncTime, erpGoodInfoNo);
        Map<String, Integer> erpSkuStockMap = new HashMap<>();
        Map<String, String> stockStatusMap = new HashMap<>();
        HashSet<String> erpGoodsNos = new HashSet<>();
        for (ERPGoodsInfoVO erpGoodsInfoVO : updatedStock.getContext()) {
            int salableQty = erpGoodsInfoVO.getSalableQty();
            if(BooleanUtils.isTrue(erpGoodsInfoVO.getDel())){
                //停用商品库存设置为0
                salableQty = 0;
                log.info("{}停用,库存清零", erpGoodsInfoVO.getSkuCode());
            }else {
                if(salableQty < 6){
                    if(erpGoodsInfoVO.getItemCode().startsWith(AGENCY_PRODUCT_PREFIX)){
                        salableQty = 99;
                    }
                    String stockStatus = stockStatusMap.get(erpGoodsInfoVO.getSkuCode());
                    if(stockStatus == null){
                        BaseResponse<List<ERPGoodsInfoVO>> erpGoodsInfoWithoutStock = guanyierpProvider.getErpGoodsInfoWithoutStock(erpGoodsInfoVO.getItemCode());
                        for (ERPGoodsInfoVO goodsInfoVO : erpGoodsInfoWithoutStock.getContext()) {
                            stockStatusMap.put(goodsInfoVO.getSkuCode(), goodsInfoVO.getStockStatusCode());
                        }
                        stockStatus = stockStatusMap.get(erpGoodsInfoVO.getSkuCode());
                    }
                    if(stockStatus == null) {
                        log.info("{}库存状态为空,视为正常补货", erpGoodsInfoVO.getSkuCode());
//                        salableQty = 0;
                    }else if(stockStatus == "0"){
                        salableQty = 99;
                    }
                }
            }
            erpGoodsNos.add(erpGoodsInfoVO.getItemCode());
            erpSkuStockMap.put(erpGoodsInfoVO.getSkuCode(), salableQty);
        }
//        Map<String, Integer> goodsInfoStockMap = new HashMap<>();
//        Map<String, Integer> goodsStockMap = new HashMap<>();
        List<GoodsInfo> goodsInfos = null;
        if(!erpSkuStockMap.isEmpty()){
            GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
            infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            infoQueryRequest.setErpGoodsNos(new ArrayList<>(erpGoodsNos));
//            infoQueryRequest.setErpGoodsInfoNos(new ArrayList<>(erpSkuStockMap.keySet()));
            goodsInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
//            for (GoodsInfo goodsInfo : goodsInfos) {
//                Integer erpGoodsInfoStock = erpSkuStockMap.get(goodsInfo.getErpGoodsInfoNo());
////                Long actualStock = goodsInfoStockService.getActualStock(Long.valueOf(erpGoodsInfoStock), goodsInfo.getGoodsInfoId());
//                goodsStockMap.compute(goodsInfo.getGoodsId(), (k, v) -> {
//                    if(v == null) return erpGoodsInfoStock;
//                    return v + erpGoodsInfoStock;
//                });
//                goodsInfoStockMap.put(goodsInfo.getGoodsInfoId(), erpGoodsInfoStock);
//            }
//            Map<String, Map<String, Integer>> resultMap = goodsInfoStockService.batchUpdateGoodsInfoStock(goodsInfos, erpSkuStockMap);
//            goodsInfoStockService.batchUpdateGoodsStock(goodsStockMap);
        }
//        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
//        resultMap.put("skus", goodsInfoStockMap);
//        resultMap.put("spus", goodsStockMap);
        Map<String, Map<String, Integer>> resultMap = goodsInfoStockService.batchUpdateGoodsInfoStock(goodsInfos, erpSkuStockMap);
        if(StringUtils.isEmpty(erpGoodInfoNo) && CollectionUtils.isNotEmpty(goodsInfos)){
            redisService.setString(RedisKeyConstant.STOCK_SYNC_TIME_PREFIX, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDate));
        }
        return resultMap;
    }

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
    public Map<String, Map<String, Integer>> syncGoodsStock(int pageNum, int pageSize) {
        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
        GoodsStockSyncQueryRequest stockSyncQueryRequest = new GoodsStockSyncQueryRequest();
        stockSyncQueryRequest.setStatus(0);
        if (pageNum >= 0) {
            stockSyncQueryRequest.setPageNum(pageNum);
        }
        stockSyncQueryRequest.setPageSize(pageSize);
        Page<GoodsStockSync> stockPage = goodsStockSyncRepository.findAll(stockSyncQueryRequest.getWhereCriteria(),
                stockSyncQueryRequest.getPageRequest());
        if(stockPage == null || CollectionUtils.isEmpty(stockPage.getContent())){
            return resultMap;
        }
        List<GoodsStockSync> goodsStockList = stockPage.getContent();
        // 3 遍历商品列表并更新sku库存
        Map<String, Integer> skuStockMap = new HashMap<>();
        Map<String, Integer> spuStockMap = new HashMap<>();
        goodsStockList.forEach(stock -> {
            goodsStockService.updateGoodsStockSingle(stock,skuStockMap,spuStockMap);
        });
        resultMap.put("skus", skuStockMap);
        resultMap.put("spus", spuStockMap);
        log.info("resultMap====================>:{}", resultMap);
        return resultMap;
    }

    @Transactional
    public void updateGoodsStockSingle(GoodsStockSync stock,Map<String, Integer> skuStockMap,Map<String, Integer> spuStockMap){
        //查询sku信息
        GoodsStockInfo goodsInfo = goodsInfoRepository.findGoodsInfoId(stock.getGoodsNo());
        if (goodsInfo ==null) {
            goodsStockSyncRepository.updateStatus(stock.getId());
            log.info("there is no sku,stock:{}", stock);
            return;
        }
        //更新后的库存
        Long actualStock = goodsInfoStockService.getActualStock(Long.valueOf(stock.getStock()), goodsInfo.getGoodsInfoId());
        goodsInfoStockService.resetGoodsById(stock.getStock().longValue(), goodsInfo.getGoodsInfoId(),actualStock);
        skuStockMap.put(goodsInfo.getGoodsInfoId(), actualStock.intValue());
        //更新spu库存
        goodsRepository.resetGoodsStockById(actualStock, goodsInfo.getGoodsId());
        spuStockMap.put(goodsInfo.getGoodsId(), actualStock.intValue());
        //更新状态
        goodsStockSyncRepository.updateStatus(stock.getId());
    }

}
