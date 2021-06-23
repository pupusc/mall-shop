package com.wanmi.sbc.goods.virtualcoupon.service;

import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponGoodsRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.info.reponse.GoodsDetailResponse;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsInfoStockService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.redis.RedisHIncrBean;
import com.wanmi.sbc.goods.redis.RedisService;
import com.wanmi.sbc.goods.virtualcoupon.model.request.VirtualCouponStockUpdateRequest;
import com.wanmi.sbc.goods.virtualcoupon.repository.VirtualCouponCodeRepository;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.goods.virtualcoupon.repository.VirtualCouponRepository;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCoupon;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponQueryRequest;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.enums.DeleteFlag;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>卡券业务逻辑</p>
 *
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@Service("VirtualCouponService")
public class VirtualCouponService {
    @Autowired
    private VirtualCouponRepository virtualCouponRepository;

    /**
     * 可用库存redis-key
     * virtual-coupon-code:stock:{couponId}
     */
    private final String COUPON_CODE_STOCK_REDIS_KEY = "virtual-coupon-code:stock:";

    /**
     * 已售库存redis-key
     * virtual-coupon-code:expire-stock:{couponId}
     */
    private final String COUPON_CODE_EXPIRE_STOCK_REDIS_KEY = "virtual-coupon-code:expire-stock:";

    /**
     * 更新卡券信息 redis-key
     * virtual-coupon:coupon_stock_update_lock:{couponId}
     */
    private final String COUPON_STOCK_UPDATE_LOCK = "virtual-coupon:coupon_stock_update_lock:";

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private VirtualCouponCodeRepository virtualCouponCodeRepository;

    @Autowired
    private GoodsInfoStockService goodsInfoStockService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;



    /**
     * 新增卡券
     *
     * @author 梁善
     */
    @Transactional
    public VirtualCoupon add(VirtualCoupon entity) {

        long count = virtualCouponRepository.countAllByNameAndDelFlag(entity.getName(), DeleteFlag.NO);
        if (count > 0) {
            throw new SbcRuntimeException("k-220001");
        }

        virtualCouponRepository.save(entity);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(COUPON_CODE_STOCK_REDIS_KEY + entity.getId(), "0");
        redisTemplate.opsForValue().set(COUPON_CODE_EXPIRE_STOCK_REDIS_KEY + entity.getId(), "0");
        return entity;
    }

    /**
     * 单个查询卡券
     *
     * @author 梁善
     */
    public VirtualCoupon getOne(Long id, Long storeId) {
        return virtualCouponRepository.findByIdAndStoreIdAndDelFlag(id, storeId, DeleteFlag.NO)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "卡券不存在"));
    }

    /**
     * 分页查询卡券
     *
     * @author 梁善
     */
    public Page<VirtualCoupon> page(VirtualCouponQueryRequest queryReq) {
        return virtualCouponRepository.findAll(
                VirtualCouponWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询卡券
     *
     * @author 梁善
     */
    public List<VirtualCoupon> list(VirtualCouponQueryRequest queryReq) {
        return virtualCouponRepository.findAll(VirtualCouponWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     *
     * @author 梁善
     */
    public VirtualCouponVO wrapperVo(VirtualCoupon virtualCoupon) {
        if (virtualCoupon != null) {
            return KsBeanUtil.convert(virtualCoupon, VirtualCouponVO.class);
        }
        return null;
    }


    /**
     * 增加的库存
     */
    @Transactional
    public void addCouponCode(VirtualCouponStockUpdateRequest request) {
        String key = COUPON_STOCK_UPDATE_LOCK + request.getCouponId();
        RLock lock = redissonClient.getLock(key);
        VirtualCoupon virtualCoupon = virtualCouponRepository.getOne(request.getCouponId());

        try {
            //增加总数量
            if (request.getAddCount() != 0) {
                virtualCoupon.setSumNumber(virtualCoupon.getSumNumber() + request.getAddCount());
            }
            //增加已售数量
            if (request.getSaledCount() != 0) {
                virtualCoupon.setSaledNumber(virtualCoupon.getSaledNumber() + request.getSaledCount());
            }
            virtualCoupon.setUpdatePerson(request.getUpdatePerson());
            virtualCoupon.setUpdateTime(LocalDateTime.now());
            virtualCouponRepository.save(virtualCoupon);

            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());

            //增加可用库存
            if (request.getAddAbleCount() != 0) {
                String tempKey = COUPON_CODE_STOCK_REDIS_KEY + request.getCouponId();
                redisTemplate.opsForValue().setIfAbsent(tempKey, String.valueOf(getAbleStock(request.getCouponId())));
                redisTemplate.opsForValue().increment(tempKey, request.getAddAbleCount());
            }
            //增加过期商品数量
            if (request.getExpireCount() != 0) {
                String tempKey = COUPON_CODE_EXPIRE_STOCK_REDIS_KEY + request.getCouponId();
                redisTemplate.opsForValue().setIfAbsent(tempKey, String.valueOf(getExpireStock(request.getCouponId())));
                redisTemplate.opsForValue().increment(tempKey, request.getExpireCount());
            }
            if (virtualCoupon.getPublishStatus() == 1 && StringUtils.isNotBlank(virtualCoupon.getSkuId()) && !request.isOrderFlag()) {
                //增加商品库存
                goodsInfoStockService.addStockById((long) request.getAddAbleCount(), virtualCoupon.getSkuId());
                GoodsVO detail = goodsService.findGoodsInfoDetail(virtualCoupon.getSkuId());
                List<RedisHIncrBean> beans = new ArrayList<>();
                List<RedisHIncrBean> skuBeans = new ArrayList<>();
                if (detail!=null){
                    beans.add(new RedisHIncrBean(detail.getGoodsId(),(long) -request.getAddAbleCount()));
                    skuBeans.add(new RedisHIncrBean(virtualCoupon.getSkuId(),(long) -request.getAddAbleCount()));
                    redisService.hincrPipeline(CacheKeyConstant.GOODS_STOCK_SUB_CACHE, beans);
                    redisService.hincrPipeline(CacheKeyConstant.GOODS_STOCK_SUB_CACHE_SKU, skuBeans);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 根据ID查询 卡券的可用数量
     * 可用卡券--->  状态为未使用+领取结束时间>=now()
     *
     * @param couponId 卡券ID
     */
    public int getAbleStock(Long couponId) {
        String key = COUPON_CODE_STOCK_REDIS_KEY + couponId;
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey != null && hasKey) {
            Object stock = redisTemplate.opsForValue().get(key);
            return stock == null ? 0 : Integer.parseInt(stock.toString());
        } else {
            Integer stock = virtualCouponCodeRepository.findAbleStock(couponId);
            redisTemplate.opsForValue().set(key, stock + "");
            return stock;
        }
    }

    /**
     * 根据ID查询 卡券的过期数据
     * 过期卡券--->  状态为未使用+领取结束时间<now()
     *
     * @param couponId 卡券ID
     */
    public int getExpireStock(Long couponId) {
        String key = COUPON_CODE_EXPIRE_STOCK_REDIS_KEY + couponId;
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey != null && hasKey) {
            Object stock = redisTemplate.opsForValue().get(key);
            return stock == null ? 0 : Integer.parseInt(stock.toString());
        } else {
            Integer stock = virtualCouponCodeRepository.findExpireStock(couponId);
            redisTemplate.opsForValue().set(key, stock + "");
            return stock;
        }
    }

    /**
     * 卡券关联商品
     *
     * @param request
     */
    public int linkGoods(VirtualCouponGoodsRequest request) {
        VirtualCoupon one = getOne(request.getCouponId(), request.getStoreId());
        if (one.getPublishStatus() == 1 || StringUtils.isNotBlank(one.getSkuId())) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        one.setSkuId(request.getSkuId());
        one.setPublishStatus(1);
        one.setUpdateTime(LocalDateTime.now());
        one.setUpdatePerson(request.getUpdatePerson());
        return getAbleStock(request.getCouponId());
    }

    /**
     * 卡券取消关联商品
     *
     * @param request
     */
    public void unlinkGoods(VirtualCouponGoodsRequest request) {
        VirtualCoupon one = getOne(request.getCouponId(), request.getStoreId());
        if (one.getPublishStatus() == 0) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        one.setSkuId(null);
        one.setPublishStatus(0);
        one.setUpdateTime(LocalDateTime.now());
        one.setUpdatePerson(request.getUpdatePerson());
    }

    public List<VirtualCoupon> findAll() {
        return virtualCouponRepository.findAll();
    }
}

