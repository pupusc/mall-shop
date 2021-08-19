package com.wanmi.sbc.goods.flashsalegoods.service;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.constant.FlashSaleErrorCode;
import com.wanmi.sbc.goods.api.constant.GoodsStockErrorCode;
import com.wanmi.sbc.goods.api.request.flashsalegoods.*;
import com.wanmi.sbc.goods.api.response.flashsalegoods.IsFlashSaleResponse;
import com.wanmi.sbc.goods.bean.enums.FlashSaleGoodsStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.FlashSaleCateVO;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.flashsalecate.model.root.FlashSaleCate;
import com.wanmi.sbc.goods.flashsalecate.repository.FlashSaleCateRepository;
import com.wanmi.sbc.goods.flashsalegoods.model.root.FlashSaleGoods;
import com.wanmi.sbc.goods.flashsalegoods.repository.FlashSaleGoodsRepository;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.redis.RedisService;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>抢购商品表业务逻辑</p>
 *
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@Service("FlashSaleGoodsService")
public class FlashSaleGoodsService {

    @Autowired
    private FlashSaleGoodsRepository flashSaleGoodsRepository;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private FlashSaleGoodsService flashSaleGoodsService;

    @Autowired
    private FlashSaleCateRepository flashSaleCateRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GoodsInfoSpecDetailRelRepository goodsInfoSpecDetailRelRepository;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    /**
     * 新增抢购商品表
     *
     * @author bob
     */
    @Transactional
    public List<FlashSaleGoods> batchAdd(List<FlashSaleGoods> entity) throws SbcRuntimeException {
        List<String> skuIds = entity.stream().map(FlashSaleGoods::getGoodsInfoId).collect(Collectors.toList());
        String date = entity.get(0).getActivityDate();
        String time = entity.get(0).getActivityTime();
        LocalDateTime queryTime =
                DateUtil.parse(date.concat(" ").concat(time),DateUtil.FMT_TIME_2);
        List<FlashSaleGoods> flashSaleGoodsList = flashSaleGoodsRepository.queryBySkuIdsAndDate(skuIds,
                queryTime.minusHours(Constants.FLASH_SALE_LAST_HOUR),
                queryTime.plusHours(Constants.FLASH_SALE_LAST_HOUR));
        //判断商品重复添加
        if (CollectionUtils.isNotEmpty(flashSaleGoodsList)) {
            skuIds = flashSaleGoodsList.stream().map(FlashSaleGoods::getGoodsInfoId).collect(Collectors.toList());
            List<String> goodsInfoNo =
                    goodsInfoRepository.findByGoodsInfoIds(skuIds).stream().map(GoodsInfo::getGoodsInfoNo)
                            .collect(Collectors.toList());
            throw new SbcRuntimeException(FlashSaleErrorCode.REPEATEDLY_ADDED, new Object[]{goodsInfoNo.toString()});
        }

        //校验分类是否已删除
        List<Long> cateIds = entity.stream().map(FlashSaleGoods::getCateId).collect(Collectors.toList());
        List<FlashSaleCate> flashSaleCates =
                flashSaleCateRepository.findAllById(cateIds).stream().filter(r -> r.getDelFlag().equals(DeleteFlag.YES))
                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(flashSaleCates)) {
            List<String> cateNames =
                    flashSaleCates.stream().map(FlashSaleCate::getCateName).collect(Collectors.toList());
            throw new SbcRuntimeException(FlashSaleErrorCode.CATE_DELETE, new Object[]{cateNames.toString()});
        }

        return flashSaleGoodsRepository.saveAll(entity);
    }

    /**
     * 修改抢购商品表
     *
     * @author bob
     */
    @Transactional
    public FlashSaleGoods modify(FlashSaleGoods entity) {
        LocalDateTime activityFullTime = entity.getActivityFullTime();
        // 活动时间开始前一个小时后无法编辑
        if (!activityFullTime.minusHours(1).isAfter(LocalDateTime.now())) {
            throw new SbcRuntimeException(FlashSaleErrorCode.NOT_MODIFY);
        }
        flashSaleGoodsRepository.save(entity);
        return entity;
    }

    /**
     * 单个删除抢购商品表
     *
     * @author bob
     */
    @Transactional
    public void deleteById(Long id) {
        FlashSaleGoods flashSaleGoods = getById(id);
        LocalDateTime activityFullTime = flashSaleGoods.getActivityFullTime();
        // 活动时间开始前一个小时后无法删除
        if (!activityFullTime.minusHours(1).isAfter(LocalDateTime.now())) {
            throw new SbcRuntimeException(FlashSaleErrorCode.NOT_DELETE);
        }
        flashSaleGoodsRepository.modifyDelFlagById(id);
    }

    /**
     * 批量删除抢购商品表
     *
     * @author bob
     */
    @Transactional
    public void deleteByIdList(List<Long> ids) {
        flashSaleGoodsRepository.deleteByIdList(ids);
    }

    /**
     * 批量删除抢购商品表
     *
     * @author bob
     */
    @Transactional
    public void deleteByTimeList(List<String> activityTimeList) {
        flashSaleGoodsRepository.deleteByTimeList(activityTimeList);
    }

    /**
     * 单个查询抢购商品表
     *
     * @author bob
     */
    public FlashSaleGoods getById(Long id) {
        return flashSaleGoodsRepository.findById(id).get();
    }

    /**
     * 分页查询抢购商品表
     *
     * @author bob
     */
    public Page<FlashSaleGoods> page(FlashSaleGoodsQueryRequest queryReq) {
        Page<FlashSaleGoods> page = flashSaleGoodsRepository.findAll(
                FlashSaleGoodsWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
        //如果是linkedmall商品，实时查库存
        List<GoodsInfo> goodsInfos = page.getContent().stream().map(v -> v.getGoodsInfo()).collect(Collectors.toList());
        List<Long> itemIds = goodsInfos.stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());
        if (itemIds.size() > 0) {
            List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
            if (stocks != null) {
                for (GoodsInfo goodsInfo : goodsInfos) {
                    if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
                        Optional<QueryItemInventoryResponse.Item> optional = stocks.stream()
                                .filter(v -> String.valueOf(v.getItemId()).equals(goodsInfo.getThirdPlatformSpuId()))
                                .findFirst();
                        if (optional.isPresent()) {
                            Optional<QueryItemInventoryResponse.Item.Sku> skuStock = optional.get().getSkuList().stream()
                                    .filter(v -> String.valueOf(v.getSkuId()).equals(goodsInfo.getThirdPlatformSkuId())).findFirst();
                            if (skuStock.isPresent()) {
                                Long quantity = skuStock.get().getInventory().getQuantity();
                                goodsInfo.setStock(quantity);
                                if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                                    goodsInfo.setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                                }
                            }
                        }

                    }
                }
            }
    }
        return page;
    }

    /**
     * 列表查询抢购商品表
     *
     * @author bob
     */
    public List<FlashSaleGoods> list(FlashSaleGoodsQueryRequest queryReq) {
        Sort sort = queryReq.getSort();
        if(Objects.nonNull(sort)) {
            return flashSaleGoodsRepository.findAll(FlashSaleGoodsWhereCriteriaBuilder.build(queryReq), sort);
        }else {
            return flashSaleGoodsRepository.findAll(FlashSaleGoodsWhereCriteriaBuilder.build(queryReq));
        }

    }
    /**
     * 秒杀设置参与商家数量
     */
    public Long storeCount() {
        return flashSaleGoodsRepository.storeCount();
    }


    /**
     * 将实体包装成VO
     *
     * @author bob
     */
    public FlashSaleGoodsVO wrapperVo(FlashSaleGoods flashSaleGoods) {
        if (flashSaleGoods != null) {
            FlashSaleGoodsVO flashSaleGoodsVO = new FlashSaleGoodsVO();
            KsBeanUtil.copyPropertiesThird(flashSaleGoods, flashSaleGoodsVO);
            FlashSaleCate flashSaleCate = flashSaleGoods.getFlashSaleCate();
            if (Objects.nonNull(flashSaleCate)) {
                FlashSaleCateVO flashSaleCateVO = new FlashSaleCateVO();
                KsBeanUtil.copyPropertiesThird(flashSaleCate, flashSaleCateVO);
                flashSaleGoodsVO.setFlashSaleCateVO(flashSaleCateVO);
            }
            Goods goods = flashSaleGoods.getGoods();
            if (Objects.nonNull(goods)) {
                GoodsVO goodsVO = new GoodsVO();
                KsBeanUtil.copyPropertiesThird(goods, goodsVO);
                flashSaleGoodsVO.setGoods(goodsVO);
            }
            GoodsInfo goodsInfo = flashSaleGoods.getGoodsInfo();
            if (Objects.nonNull(goodsInfo)) {
                GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
                KsBeanUtil.copyPropertiesThird(goodsInfo, goodsInfoVO);
                StoreVO storeVO = storeQueryProvider.getById(StoreByIdRequest.builder()
                        .storeId(goodsInfo.getStoreId())
                        .build()).getContext().getStoreVO();
                // 店铺名称
                goodsInfoVO.setStoreName(storeVO.getStoreName());
                // 最大可兑换库存
                flashSaleGoodsVO.setMaxStock(goodsInfo.getStock() + flashSaleGoods.getStock());
                flashSaleGoodsVO.setGoodsInfo(goodsInfoVO);
            }
            // 商品状态
            FlashSaleGoodsStatus flashSaleGoodsStatus = getFlashSaleGoodsStatus(flashSaleGoods);
            flashSaleGoodsVO.setFlashSaleGoodsStatus(flashSaleGoodsStatus);
            // 是否可修改(活动开始前一个小时后无法修改)
            if (flashSaleGoods.getActivityFullTime().minusHours(1).isAfter(LocalDateTime.now())) {
                flashSaleGoodsVO.setModifyFlag(BoolFlag.YES);
            } else {
                flashSaleGoodsVO.setModifyFlag(BoolFlag.NO);
            }
            String goodsInfoId = flashSaleGoods.getGoodsInfoId();
            List<GoodsInfoSpecDetailRel> GoodsInfoSpecDetailRels = goodsInfoSpecDetailRelRepository.findByGoodsInfoId(goodsInfoId);
            flashSaleGoodsVO.setSpecText(StringUtils.join(GoodsInfoSpecDetailRels.stream()
                    .map(GoodsInfoSpecDetailRel::getDetailName)
                    .collect(Collectors.toList()), " "));
            return flashSaleGoodsVO;
        }
        return null;
    }

    /**
     * 获取秒杀商品活动状态
     *
     * @param flashSaleGoods
     * @return
     */
    public FlashSaleGoodsStatus getFlashSaleGoodsStatus(FlashSaleGoods flashSaleGoods) {
        if (LocalDateTime.now().isBefore(flashSaleGoods.getActivityFullTime())) {
            return FlashSaleGoodsStatus.NOT_START;
        } else if (LocalDateTime.now().isAfter(flashSaleGoods.getActivityFullTime().plusHours(2))) {
            return FlashSaleGoodsStatus.ENDED;
        } else {
            return FlashSaleGoodsStatus.STARTED;
        }
    }

    /**
     * 商品是否在指定时间内
     *
     * @author bob
     */
    public List<FlashSaleGoods> getByGoodsIdAndFullTime(IsInProgressReq isInProgressReq) {
        return flashSaleGoodsRepository.queryByGoodsIdAndActivityFullTime(isInProgressReq.getGoodsId()
                , isInProgressReq.getBegin(), isInProgressReq.getEnd());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 扣减库存
     * @Date 11:12 2019/6/21
     * @Param [request]
     **/
    @Transactional
    public void batchMinusStock(FlashSaleGoodsBatchMinusStockRequest request) {
        flashSaleGoodsRepository.subStockById(request.getStock(), request.getId());
    }

    /**
     * @Author lvzhenwei
     * @Description 增加秒杀商品库存
     * @Date 16:03 2019/7/1
     * @Param [request]
     * @return void
     **/
    @Transactional
    public void addStockById(FlashSaleGoodsBatchAddStockRequest request){
        flashSaleGoodsRepository.addStockById(request.getStock(),request.getId());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 增加销量
     * @Date 10:15 2019/6/22
     * @Param [request]
     **/
    @Transactional
    public void batchPlusSalesVolumeById(FlashSaleGoodsBatchPlusSalesVolumeRequest request) {
        flashSaleGoodsRepository.plusSalesVolumeById(request.getSalesVolume().longValue(), request.getId());
    }

    /**
     * @Author lvzhenwei
     * @Description 减少销量
     * @Date 14:41 2019/8/5
     * @Param [request]
     * @return void
     **/
    @Transactional
    public void subSalesVolumeById(FlashSaleGoodsBatchStockAndSalesVolumeRequest request){
        flashSaleGoodsRepository.subSalesVolumeById(request.getNum().longValue(),request.getId());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 扣减库存增加销量
     * @Date 10:15 2019/6/22
     * @Param [request]
     **/
    @Transactional
    public void batchStockAndSalesVolume(FlashSaleGoodsBatchStockAndSalesVolumeRequest request) {
        int updateRowNum = flashSaleGoodsRepository.subStockById(request.getNum(), request.getId());
        if(updateRowNum<=0){
            throw new SbcRuntimeException(GoodsStockErrorCode.LACK_ERROR);
        }
        flashSaleGoodsRepository.plusSalesVolumeById(request.getNum().longValue(), request.getId());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 增加库存扣减销量
     * @Date 10:15 2019/6/22
     * @Param [request]
     **/
    @Transactional
    public void subStockAndSalesVolume(FlashSaleGoodsBatchStockAndSalesVolumeRequest request) {
        flashSaleGoodsRepository.addStockById(request.getNum(), request.getId());
        flashSaleGoodsRepository.subSalesVolumeById(request.getNum().longValue(), request.getId());
    }

    /**
     * @Author minchen
     * @Description 秒杀活动结束后商品还库存
     **/
    @Transactional
    public void activityEndReturnStock(FlashSaleGoodsQueryRequest request) {
        List<FlashSaleGoods> flashSaleGoodsList = flashSaleGoodsService.list(request);
        // 抢购资格只保存5分钟，活动结束5分钟后不会有任何方式去修改库存，还库存不用考虑抢购商品表的并发
        flashSaleGoodsList.forEach(flashSaleGoods -> {
            Long stock = flashSaleGoods.getStock().longValue();
            if (stock > 0) {
                // 抢购商品表库存清0
                flashSaleGoods.setStock(0);
                flashSaleGoodsRepository.save(flashSaleGoods);
            }
            // 删除redis key
            String flashSaleGoodsInfoKey = RedisKeyConstant.FLASH_SALE_GOODS_INFO + flashSaleGoods.getId();
            redisService.delete(flashSaleGoodsInfoKey);
        });
    }

    /**
     * 未结束活动关联的商品(只查一条)
     *
     * @author bob
     */
    public IsFlashSaleResponse getByActivityTime(IsFlashSaleRequest isFlashSaleRequest) {
        Object byActivityTime = flashSaleGoodsRepository.queryByActivityTime(isFlashSaleRequest.getActivityTime());
        return new IsFlashSaleResponse().convertFromNativeSQLResult(byActivityTime);
    }
}
