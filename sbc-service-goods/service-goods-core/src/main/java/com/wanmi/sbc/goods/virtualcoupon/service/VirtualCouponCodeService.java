package com.wanmi.sbc.goods.virtualcoupon.service;

import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponCodeQueryRequest;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeListResponse;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponCodeVO;
import com.wanmi.sbc.goods.virtualcoupon.model.request.LinkOrderRequest;
import com.wanmi.sbc.goods.virtualcoupon.model.request.VirtualCouponStockUpdateRequest;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCoupon;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCouponCode;
import com.wanmi.sbc.goods.virtualcoupon.repository.VirtualCouponCodeRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>券码业务逻辑</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@Service("VirtualCouponCodeService")
public class VirtualCouponCodeService {

    @Value("classpath:virtual_coupon_code0.xlsx")
    private Resource virtualCouponTemplate0;

    @Value("classpath:virtual_coupon_code1.xlsx")
    private Resource virtualCouponTemplate1;

    @Value("classpath:virtual_coupon_code2.xlsx")
    private Resource virtualCouponTemplate2;

    @Autowired
    private VirtualCouponCodeRepository virtualCouponCodeRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private VirtualCouponService virtualCouponService;

    /**
     * 批次redis-key  =   virtual-coupon-code:batch-no:{storeId}
     */
    private final String BATCH_NO_REDIS_KEY = "virtual-coupon-code:batch-no:";

    /**
     * 下单redis-key  =   virtual-coupon-code:batch-no:{couponId}
     */
    private final String LINK_ORDER_LOCK = "virtual-coupon-code:link-order:";

    /**
     * 更新券码信息lock
     */
    private final String UPDATE_COUPON_CODE_LOCK = "virtual-coupon-code:update:{id}";

    /**
     * 单个删除券码
     *
     * @author 梁善
     */
    @Transactional
    public void deleteById(Long id, Long couponId, String updatePerson) {
        RLock lock = redissonClient.getLock(UPDATE_COUPON_CODE_LOCK + id);
        lock.lock();
        try {
            Optional<VirtualCouponCode> opt = virtualCouponCodeRepository.findByCouponIdAndIdAndDelFlag(couponId, id, DeleteFlag.NO);
            if (opt.isPresent()) {
                VirtualCouponCode virtualCouponCode = opt.get();
                if (virtualCouponCode.getStatus() == 0) {
                    virtualCouponCodeRepository.deleteCouponCode(virtualCouponCode.getCouponId(), virtualCouponCode.getId(), updatePerson);
                } else {
                    throw new SbcRuntimeException(CommonErrorCode.STATUS_HAS_BEEN_CHANGED_ERROR);
                }
                VirtualCouponStockUpdateRequest build = VirtualCouponStockUpdateRequest.builder().couponId(couponId)
                        .updatePerson(updatePerson)
                        .addCount(-1)
                        .addAbleCount(-1)
                        .expireCount(0)
                        .saledCount(0).build();
                virtualCouponService.addCouponCode(build);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 批量删除券码
     *
     * @author 梁善
     */
    @Transactional
    public void deleteByIdList(List<Long> ids, Long couponId, String updatePerson) {
        ids.forEach(id -> {
            RLock lock = redissonClient.getLock(UPDATE_COUPON_CODE_LOCK + id);
            lock.lock();
            try {
                Optional<VirtualCouponCode> opt = virtualCouponCodeRepository.findByCouponIdAndIdAndDelFlag(couponId, id, DeleteFlag.NO);
                if (opt.isPresent()) {
                    VirtualCouponCode virtualCouponCode = opt.get();
                    if (virtualCouponCode.getStatus() == 0) {
                        virtualCouponCodeRepository.deleteCouponCode(virtualCouponCode.getCouponId(), virtualCouponCode.getId(), updatePerson);
                    } else {
                        throw new SbcRuntimeException(CommonErrorCode.STATUS_HAS_BEEN_CHANGED_ERROR);
                    }
                }
            } finally {
                lock.unlock();
            }
        });

        VirtualCouponStockUpdateRequest build = VirtualCouponStockUpdateRequest.builder().couponId(couponId)
                .updatePerson(updatePerson)
                .addCount(-ids.size())
                .addAbleCount(-ids.size())
                .expireCount(0)
                .saledCount(0).build();
        virtualCouponService.addCouponCode(build);
    }

    /**
     * 单个查询券码
     *
     * @author 梁善
     */
    public VirtualCouponCode getOne(Long id) {
        return virtualCouponCodeRepository.findByIdAndDelFlag(id, DeleteFlag.NO)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "券码不存在"));
    }

    /**
     * 分页查询券码
     *
     * @author 梁善
     */
    public Page<VirtualCouponCode> page(VirtualCouponCodeQueryRequest queryReq) {
        return virtualCouponCodeRepository.findAll(
                VirtualCouponCodeWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询券码
     *
     * @author 梁善
     */
    public List<VirtualCouponCode> list(VirtualCouponCodeQueryRequest queryReq) {
        Page<VirtualCouponCode> all = virtualCouponCodeRepository.findAll(VirtualCouponCodeWhereCriteriaBuilder.build(queryReq), queryReq.getPageRequest());
        return all.toList();
    }

    /**
     * 将实体包装成VO
     *
     * @author 梁善
     */
    public VirtualCouponCodeVO wrapperVo(VirtualCouponCode virtualCouponCode) {
        if (virtualCouponCode != null) {
            return KsBeanUtil.convert(virtualCouponCode, VirtualCouponCodeVO.class);
        }
        return null;
    }

    /**
     * 导出模板
     *
     * @return base64位文件字符串
     */
    public String exportTemplate(int provideType, Long couponId) {

        Resource file = null;

        switch (provideType) {
            case 0:
                file = virtualCouponTemplate0;
                break;
            case 1:
                file = virtualCouponTemplate1;
                break;
            case 2:
                file = virtualCouponTemplate2;
                break;
        }

        if (file == null || !file.exists()) {
            throw new SbcRuntimeException(GoodsImportErrorCode.NOT_SETTING);
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); InputStream is = file.getInputStream(); Workbook wk = new XSSFWorkbook(is)) {
            wk.write(baos);
            return new BASE64Encoder().encode(baos.toByteArray());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }
    }

    /**
     * 批量导入
     *
     * @param codes
     * @param storeId
     * @param couponId
     */
    @Transactional
    public void batchAdd(List<VirtualCouponCode> codes, Long storeId, Long couponId) {
        String batchNo = getBatchNo(storeId);
        AtomicInteger addCount = new AtomicInteger();
        AtomicInteger ableCount = new AtomicInteger();
        AtomicInteger expireCount = new AtomicInteger();

        LocalDateTime now = LocalDateTime.now();
        AtomicReference<String> updatePerson = new AtomicReference<>();
        codes.forEach(e -> {
            e.setCouponId(couponId);
            e.setBatchNo(batchNo);
            if (e.getReceiveEndTime().isAfter(now)) {
                e.setStatus(0);
                ableCount.incrementAndGet();
            } else {
                e.setStatus(2);
                expireCount.incrementAndGet();
            }
            e.setDelFlag(DeleteFlag.NO);
            addCount.incrementAndGet();
            updatePerson.set(e.getUpdatePerson());
        });
        //保存数据库
        virtualCouponCodeRepository.saveAll(codes);

        VirtualCouponStockUpdateRequest build = VirtualCouponStockUpdateRequest.builder().couponId(couponId)
                .updatePerson(updatePerson.get())
                .addCount(addCount.get())
                .addAbleCount(ableCount.get())
                .expireCount(expireCount.get())
                .saledCount(0).build();
        virtualCouponService.addCouponCode(build);
        String key = RedisKeyConstant.VIRTUAL_COUPON_NO_CHECK_KEY + couponId;
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.delete(key);
    }

    /**
     * 获取批次号
     *
     * @param storeId
     * @return
     */
    private String getBatchNo(Long storeId) {
        String key = BATCH_NO_REDIS_KEY + storeId;

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        LocalDateTime now = LocalDateTime.now();
        String prefix = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey != null && hasKey) {
            return prefix + String.format("%04d", redisTemplate.opsForValue().increment(key));
        } else {
            LocalDate nextDay = now.toLocalDate().plusDays(1);
            LocalDateTime nextTime = LocalDateTime.of(nextDay, LocalTime.MIN);
            redisTemplate.opsForValue().set(key, "1", Duration.between(now, nextTime));
            return prefix + "0001";
        }
    }

    /**
     * 下单发券
     *
     * @param tid
     * @param couponIds
     * @return
     */
    @Transactional
    public VirtualCouponCodeListResponse linkOrder(String tid, List<Long> couponIds, String updatePerson) {
        Map<Long, AtomicInteger> map = new HashMap<>();

        couponIds.forEach(e -> {
            AtomicInteger atomicInteger = map.get(e);
            if (atomicInteger == null) {
                map.put(e, new AtomicInteger(1));
            } else {
                atomicInteger.incrementAndGet();
            }
        });

        VirtualCouponCodeListResponse response = new VirtualCouponCodeListResponse();
        List<VirtualCouponCode> codes = new ArrayList<>();

        List<RLock> locks = new ArrayList<>();
        List<Long> collect = couponIds.stream().distinct().sorted().collect(Collectors.toList());
        try {
            collect.forEach(key -> {
                RLock lock = redissonClient.getLock(LINK_ORDER_LOCK + key);
                lock.lock();
                locks.add(lock);

                VirtualCouponCodeQueryRequest build = VirtualCouponCodeQueryRequest.builder().couponId(key).receiveEndTimeBegin(LocalDateTime.now()).build();
                build.putSort("receiveEndTime", "asc");
                build.setPageSize(map.get(key).get());
                build.setPageNum(0);
                build.setStatus(0);
                List<VirtualCouponCode> list = virtualCouponCodeRepository.findAll(VirtualCouponCodeWhereCriteriaBuilder.build(build), build.getPageRequest()).getContent();
                if (list.size() != map.get(key).get()) {
                    throw new SbcRuntimeException(CommonErrorCode.DATA_OUT_LIIT, "卡券不足");
                }
                codes.addAll(list);
                List<Long> ids = list.stream().map(VirtualCouponCode::getId).collect(Collectors.toList());
                LinkOrderRequest request = LinkOrderRequest.builder().couponId(key).tid(tid).ids(ids).updatePerson(updatePerson).now(LocalDateTime.now()).build();
                virtualCouponCodeRepository.linkOrder(request);
            });
            //更新库存
            collect.forEach(key -> {
                int count = map.get(key).get();
                VirtualCouponStockUpdateRequest build = VirtualCouponStockUpdateRequest.builder().couponId(key)
                        .updatePerson(updatePerson)
                        .addCount(0)
                        .addAbleCount(-count)
                        .expireCount(0)
                        .saledCount(count)
                        .orderFlag(true)
                        .build();
                virtualCouponService.addCouponCode(build);
            });
        } finally {
            locks.forEach(RLock::unlock);
        }
        response.setVirtualCouponCodeVOList(KsBeanUtil.convertList(codes, VirtualCouponCodeVO.class));
        return response;
    }

    /**
     * 取消发券
     *
     * @param list
     */
    @Transactional
    public void unlinkOrder(List<VirtualCouponCodeVO> list) {
        list.forEach(e -> {
            Optional<VirtualCouponCode> code = virtualCouponCodeRepository.findByCouponIdAndIdAndDelFlag(e.getCouponId(), e.getId(), DeleteFlag.NO);
            if (code.isPresent()) {
                VirtualCouponCode virtualCouponCode = code.get();
                if (virtualCouponCode.getStatus() != 1 || StringUtils.isBlank(virtualCouponCode.getTid())) {
                    return;
                }
                virtualCouponCode.setTid(null);
                virtualCouponCode.setUpdateTime(LocalDateTime.now());
                virtualCouponCode.setStatus(0);
                virtualCouponCode.setUpdatePerson(e.getUpdatePerson());
                virtualCouponCodeRepository.save(virtualCouponCode);
                VirtualCouponStockUpdateRequest build = VirtualCouponStockUpdateRequest.builder().couponId(e.getCouponId())
                        .updatePerson(e.getUpdatePerson())
                        .addCount(0)
                        .addAbleCount(1)
                        .expireCount(0)
                        .saledCount(-1)
                        .orderFlag(true)
                        .build();
                virtualCouponService.addCouponCode(build);
            }
        });
    }

    /**
     * 初始化缓存 不根据卡券ID
     *
     * @param couponId
     */
    public void initCouponNo(Long couponId) {
        String key = RedisKeyConstant.VIRTUAL_COUPON_NO_CHECK_KEY ;
        List<String> allCouponNo = virtualCouponCodeRepository.findAllCouponNo(couponId);
        if (!allCouponNo.isEmpty()) {
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            redisTemplate.opsForSet().add(key,allCouponNo.toArray() );
            redisTemplate.expire(key, 1, TimeUnit.HOURS);
        }
    }

    /**
     * 处理过期卡券
     */
    @Transactional
    public void expireVirtualCouponCode() {
        RLock lock = redissonClient.getLock("expireVirtualCouponCode");
        if (lock.isLocked()) {
            return;
        }
        lock.lock();
        try {
            List<VirtualCoupon> coupons = virtualCouponService.findAll();
            coupons.forEach(coupon -> {
                if (coupon.getDelFlag() == DeleteFlag.YES) {
                    return;
                }
                RLock couponLock = redissonClient.getLock(LINK_ORDER_LOCK + coupon.getId());
                couponLock.lock();
                try {
                    List<VirtualCouponCode> list = virtualCouponCodeRepository.findExpireCouponCode(coupon.getId(), LocalDateTime.now());

                    if (!list.isEmpty()) {
                        List<Long> collect = list.stream().map(VirtualCouponCode::getId).collect(Collectors.toList());
                        virtualCouponCodeRepository.expireCouponCode(coupon.getId(), collect, LocalDateTime.now());

                        VirtualCouponStockUpdateRequest build = VirtualCouponStockUpdateRequest.builder().couponId(coupon.getId())
                                .updatePerson("system")
                                .addCount(0)
                                .addAbleCount(-list.size())
                                .expireCount(list.size())
                                .saledCount(0)
                                .orderFlag(false)
                                .build();
                        virtualCouponService.addCouponCode(build);
                    }
                } finally {
                    couponLock.unlock();
                }
            });

        } finally {
            lock.unlock();
        }
    }
}

