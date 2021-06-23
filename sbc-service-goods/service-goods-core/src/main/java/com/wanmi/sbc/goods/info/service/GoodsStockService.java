package com.wanmi.sbc.goods.info.service;

import com.google.common.collect.Lists;
import com.sbc.wanmi.erp.bean.vo.ERPGoodsInfoVO;
import com.wanmi.sbc.common.base.BaseResponse;
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
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.redis.RedisHIncrBean;
import com.wanmi.sbc.goods.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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
    private GoodsQueryProvider goodsQueryProvider;

    private static final Integer MAX_THREADS_POOL = 50;

    /**
     * 批量加库存
     *
     * @param dtoList 增量库存参数
     */
    public void batchAddStock(List<GoodsPlusStockDTO> dtoList) {
        if(CollectionUtils.isEmpty(dtoList)){
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
     * @param dtoList       减量库存参数
     */
    public void batchSubStock(List<GoodsMinusStockDTO> dtoList) {
        if(CollectionUtils.isEmpty(dtoList)){
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
                    }catch (Exception e){
                        log.error("扣除spu库存异常", e);
                        errStock.add(new GoodsMinusStockDTO(tmpStock, goodsId));
                    }
                }
            });


            LocalTime nowTime = LocalTime.now();
            if(nowTime.getHour() > 0 && nowTime.getHour() < 8){
                //如果在夜间，全清
                redisService.delete(CacheKeyConstant.GOODS_STOCK_SUB_CACHE);
                //保留错误数据，以便下次同步继续
                batchSubStock(errStock);
            }else{//重置-扣除redis
                batchAddStock(resetStock);
            }
        }
    }

    /**
     * 同步ERP商品库存
     */
//    @Transactional
    public Map<String,Map<String,Integer>> syncERPGoodsStock(String skuNo,int pageNum,int pageSize){
        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        infoQueryRequest.setGoodsType(GoodsType.VIRTUAL_COUPON.toValue());
        if (StringUtils.isNoneBlank(skuNo)){
            infoQueryRequest.setLikeGoodsInfoNo(skuNo);
        }
        if (pageNum >=0){
            infoQueryRequest.setPageNum(pageNum);
        }
        infoQueryRequest.setPageSize(pageSize);
        Page<GoodsInfo> goodsInfoPage = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria(),
                infoQueryRequest.getPageRequest());
        List<GoodsInfo> goodsInfoList = goodsInfoPage.getContent();
        log.info("==========goodsInfoList：{}==========",goodsInfoList.size());
        List<String> goodsInfoErpNoList = goodsInfoList.stream().map(GoodsInfo::getErpGoodsInfoNo).distinct().collect(Collectors.toList());
        // 3 遍历商品列表
        Map<String,Map<String,Integer>> resultMap = new HashMap<>();
        Map<String,Integer> skuStockMap = new HashMap<>();
        goodsInfoList.forEach(goodsInfo -> {
            SynGoodsInfoRequest erpSynGoodsStockRequest =
                    SynGoodsInfoRequest.builder().skuCode(goodsInfo.getErpGoodsInfoNo()).build();
            BaseResponse<SyncGoodsInfoResponse> erpSyncGoodsStockResponseBaseResponse =
                    guanyierpProvider.syncGoodsStock(erpSynGoodsStockRequest);
            if (!ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse) && !ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse.getContext())){
                List<ERPGoodsInfoVO> erpGoodsStockVOList =
                        erpSyncGoodsStockResponseBaseResponse.getContext().getErpGoodsInfoVOList();
                if (CollectionUtils.isNotEmpty(erpGoodsStockVOList)){
                    //根据SKU维度查询库存,接口只会返回一条数据,所有只需要取List.get(0)
                    ERPGoodsInfoVO erpGoodsInfoVO = erpGoodsStockVOList.stream().max(Comparator.comparing(ERPGoodsInfoVO::getSalableQty)).get();
                    // 只有大于0的库存才能同步
                    if (Long.valueOf(erpGoodsInfoVO.getSalableQty()) > 0){
                        goodsInfoStockService.resetGoodsById(Long.valueOf(erpGoodsInfoVO.getSalableQty()),
                                goodsInfo.getGoodsInfoId());
                        skuStockMap.put(goodsInfo.getGoodsInfoId(),erpGoodsInfoVO.getSalableQty());
                    }
                }
            }
        });

        //同步更新SPU库存
        Map<String,Integer> spuStockMap = new HashMap<>();
        List<String> goodsIds = goodsInfoList.stream().map(GoodsInfo::getGoodsId).distinct().collect(Collectors.toList());
        GoodsListByIdsRequest goodsListByIdsRequest = GoodsListByIdsRequest.builder().goodsIds(goodsIds).build();
        BaseResponse<GoodsListByIdsResponse> goodsResponse = goodsQueryProvider.listByIds(goodsListByIdsRequest);
        goodsResponse.getContext().getGoodsVOList().stream().forEach(goodsVO -> {
            SynGoodsInfoRequest erpSynGoodsStockRequest =
                    SynGoodsInfoRequest.builder().spuCode(goodsVO.getErpGoodsNo()).build();
            BaseResponse<SyncGoodsInfoResponse> erpSyncGoodsStockResponseBaseResponse =
                    guanyierpProvider.syncGoodsStock(erpSynGoodsStockRequest);
            if (!ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse)
                    && !ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse.getContext().getErpGoodsInfoVOList())){
                //排除掉erp当中存在商城没有的sku
                //查询spu下面的sku
                List<String> erpGoodsInfoNos= goodsInfoRepository.findByGoodsIds(Lists.newArrayList(goodsVO.getGoodsId())).stream().map(GoodsInfo::getErpGoodsInfoNo).collect(Collectors.toList());
                log.info("查询当前spu:{},skuId:{}",goodsVO.getGoodsId(),erpGoodsInfoNos);
                List<ERPGoodsInfoVO> erpGoodsStockVOList =
                        erpSyncGoodsStockResponseBaseResponse.getContext().getErpGoodsInfoVOList()
                        .stream().filter(erpGoodsInfoVO -> erpGoodsInfoNos.contains(erpGoodsInfoVO.getSkuCode()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(erpGoodsStockVOList)){
                    //按照SKU编码分组
                    Map<String, List<ERPGoodsInfoVO>> collect = erpGoodsStockVOList.stream().collect(Collectors.groupingBy(ERPGoodsInfoVO::getSkuCode));
                    List<ERPGoodsInfoVO> newErpGoodsStockVOList = new ArrayList<>();
                    collect.entrySet().stream().forEach(entry->{
                        ERPGoodsInfoVO erpGoodsInfoVO = entry.getValue().stream().max(Comparator.comparing(ERPGoodsInfoVO::getSalableQty)).get();
                        newErpGoodsStockVOList.add(erpGoodsInfoVO);
                    });
                    //ERP获取SPU对应的所有SKU列表的库存求和
                    Integer spuStock = newErpGoodsStockVOList.stream()
                            .map(goods -> goods.getSalableQty())
                            .reduce(0, (stock1, stock2) -> stock1 + stock2);
                    if (spuStock > 0){
                        goodsRepository.resetGoodsStockById(Long.valueOf(spuStock),goodsVO.getGoodsId());
                        spuStockMap.put(goodsVO.getGoodsId(),spuStock);
                    }
                }
            }
        });
        resultMap.put("skus",skuStockMap);
        resultMap.put("spus",spuStockMap);
        log.info("resultMap====================>:{}",resultMap);
        return resultMap;
    }
}
