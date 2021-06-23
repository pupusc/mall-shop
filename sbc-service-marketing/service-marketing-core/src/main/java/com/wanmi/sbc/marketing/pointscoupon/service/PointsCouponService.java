package com.wanmi.sbc.marketing.pointscoupon.service;

import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.marketing.api.request.coupon.CouponActivityAddRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponActivityConfigSaveRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponActivityModifyRequest;
import com.wanmi.sbc.marketing.api.request.pointscoupon.PointsCouponFetchRequest;
import com.wanmi.sbc.marketing.api.request.pointscoupon.PointsCouponQueryRequest;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponSendCodeResponse;
import com.wanmi.sbc.marketing.bean.constant.Constant;
import com.wanmi.sbc.marketing.bean.constant.CouponErrorCode;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import com.wanmi.sbc.marketing.bean.enums.PointsCouponStatus;
import com.wanmi.sbc.marketing.bean.enums.RangeDayType;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.marketing.bean.vo.PointsCouponVO;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivity;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivityConfig;
import com.wanmi.sbc.marketing.coupon.model.root.CouponCode;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import com.wanmi.sbc.marketing.coupon.repository.CouponActivityConfigRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponActivityRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponCodeRepository;
import com.wanmi.sbc.marketing.coupon.service.CouponActivityService;
import com.wanmi.sbc.marketing.coupon.service.CouponInfoService;
import com.wanmi.sbc.marketing.pointscoupon.model.root.PointsCoupon;
import com.wanmi.sbc.marketing.pointscoupon.repository.PointsCouponRepository;
import com.wanmi.sbc.marketing.util.common.CodeGenUtil;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>积分兑换券表业务逻辑</p>
 *
 * @author yang
 * @date 2019-06-11 10:07:09
 */
@Service("PointsCouponService")
public class PointsCouponService {
    @Autowired
    private PointsCouponRepository pointsCouponRepository;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private PointsCouponService pointsCouponService;

    @Autowired
    private CouponInfoService couponInfoService;

    @Autowired
    private CouponCodeRepository couponCodeRepository;

    @Autowired
    private CouponActivityService couponActivityService;

    @Autowired
    private CouponActivityConfigRepository couponActivityConfigRepository;

    @Autowired
    private CouponActivityRepository couponActivityRepository;

    /**
     * 新增积分兑换券表
     *
     * @author yang
     */
    @Transactional
    public PointsCoupon add(PointsCoupon entity) {
        // 验证参数
        checkParam(entity);
        // 添加活动
        String activityId = addCouponActivity(entity);
        entity.setActivityId(activityId);
        pointsCouponRepository.save(entity);
        return entity;
    }

    /**
     * 批量新增积分兑换券
     *
     * @param pointsCouponList
     * @return
     */
    @Transactional
    @GlobalTransactional
    public List<String>  batchAdd(List<PointsCoupon> pointsCouponList) {
        List<String> activityIdList = new ArrayList<>();
        for (PointsCoupon coupon : pointsCouponList){
            PointsCoupon pointsCoupon = this.add(coupon);
            activityIdList.add(pointsCoupon.getActivityId());
        }
        return activityIdList;
    }

    /**
     * 修改积分兑换券表
     *
     * @author yang
     */
    @Transactional
    public PointsCoupon modify(PointsCoupon entity) {
        PointsCouponStatus pointsCouponStatus = getPointsCouponStatus(entity);
        // 活动开始后无法修改
        if (!PointsCouponStatus.NOT_START.equals(pointsCouponStatus)) {
            throw new SbcRuntimeException(CouponErrorCode.MODIFY_ERROR);
        }
        checkParam(entity);
        // 修改活动
        modifyCouponActivity(entity);
        pointsCouponRepository.save(entity);
        return entity;
    }

    /**
     * 修改积分兑换券状态
     *
     * @param pointsCoupon
     */
    @Transactional
    public void modifyStatus(PointsCoupon pointsCoupon) {
        // 查询活动
        CouponActivity couponActivity = couponActivityService.getCouponActivityByPk(pointsCoupon.getActivityId());
        // 开启时才验证
        if (EnableStatus.ENABLE.equals(pointsCoupon.getStatus())) {
            // 验证库存
            if (pointsCoupon.getTotalCount() - pointsCoupon.getExchangeCount() == 0) {
                throw new SbcRuntimeException(CouponErrorCode.ENABLE_ERROR);
            }
            // 验证是否活动已结束
            PointsCouponStatus pointsCouponStatus = getPointsCouponStatus(pointsCoupon);
            if (PointsCouponStatus.ENDED.equals(pointsCouponStatus)) {
                throw new SbcRuntimeException(CouponErrorCode.START_ERROR);
            }
            // 修改活动状态
            couponActivity.setPauseFlag(DefaultFlag.NO);
        } else {
            couponActivity.setPauseFlag(DefaultFlag.YES);
        }
        couponActivityRepository.save(couponActivity);
        pointsCouponRepository.save(pointsCoupon);
    }

    /**
     * 单个删除积分兑换券表
     *
     * @author yang
     */
    @Transactional
    public void deleteById(Long id, String operatorId) {
        PointsCoupon pointsCoupon = pointsCouponRepository.findById(id).get();
        PointsCouponStatus pointsCouponStatus = getPointsCouponStatus(pointsCoupon);
        // 活动已开始无法删除
        if (!PointsCouponStatus.NOT_START.equals(pointsCouponStatus)) {
            throw new SbcRuntimeException(CouponErrorCode.DELETED_ERROR);
        }
        // 删除优惠券活动
        couponActivityRepository.deleteActivity(pointsCoupon.getActivityId(), operatorId);
        couponActivityConfigRepository.deleteByActivityId(pointsCoupon.getActivityId());
        // 删除积分兑换券
        pointsCouponRepository.deleteById(id);
    }

    /**
     * 单个查询积分兑换券表
     *
     * @author yang
     */
    public PointsCoupon getById(Long id) {
        return pointsCouponRepository.findById(id).get();
    }

    /**
     * 分页查询积分兑换券表
     *
     * @author yang
     */
    public Page<PointsCoupon> page(PointsCouponQueryRequest queryReq) {
        return pointsCouponRepository.findAll(
                PointsCouponWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询积分优惠券
     *
     * @author yang
     */
    public List<PointsCoupon> list(PointsCouponQueryRequest queryReq) {
        Sort sort = queryReq.getSort();
        if (Objects.nonNull(sort)) {
            return pointsCouponRepository.findAll(PointsCouponWhereCriteriaBuilder.build(queryReq), sort);
        } else {
            return pointsCouponRepository.findAll(PointsCouponWhereCriteriaBuilder.build(queryReq));
        }
    }

    /**
     * 查询过期的积分兑换券
     *
     * @return
     */
    public List<PointsCoupon> queryOverdueList() {
        return pointsCouponRepository.queryOverdueList();
    }

    /**
     * 发放积分兑换优惠券码
     *
     * @return
     */
    @GlobalTransactional
    @Transactional
    public PointsCouponSendCodeResponse sendCouponCode(PointsCouponFetchRequest pointsCouponFetchRequest) {
        // 1.根据id查询优惠券信息，数据校验
        PointsCoupon pointsCoupon = pointsCouponService.getById(pointsCouponFetchRequest.getPointsCouponId());
        if (pointsCoupon.getStatus() != EnableStatus.ENABLE) {
            throw new SbcRuntimeException("K-120007");// 很抱歉，积分优惠券兑换活动已停用
        }
        if (pointsCoupon.getTotalCount().compareTo(pointsCoupon.getExchangeCount()) != 1) {
            throw new SbcRuntimeException("K-120005");// 很抱歉，积分优惠券缺货
        }
        if (pointsCoupon.getEndTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-120006");// 很抱歉，积分优惠券兑换活动已过期
        }
        // 2.查询用户信息，校验积分数量
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(
                new CustomerGetByIdRequest(pointsCouponFetchRequest.getCustomerId())).getContext();
        if (customer.getPointsAvailable().compareTo(pointsCoupon.getPoints()) < 0) {
            throw new SbcRuntimeException("K-120004");// 很抱歉，您的积分余额不足无法兑换！
        }
        // 3.发放优惠券码
        CouponCode couponCode = new CouponCode();
        couponCode.setCouponCode(CodeGenUtil.toSerialCode(RandomUtils.nextInt(1, 10000)).toUpperCase());
        couponCode.setCouponId(pointsCoupon.getCouponId());
        couponCode.setActivityId(pointsCoupon.getActivityId());
        couponCode.setCustomerId(customer.getCustomerId());
        couponCode.setUseStatus(DefaultFlag.NO);
        couponCode.setAcquireTime(LocalDateTime.now());
        if (Objects.equals(RangeDayType.RANGE_DAY, pointsCoupon.getCouponInfo().getRangeDayType())) {//优惠券的起止时间
            couponCode.setStartTime(pointsCoupon.getCouponInfo().getStartTime());
            couponCode.setEndTime(pointsCoupon.getCouponInfo().getEndTime());
        } else {//领取生效
            couponCode.setStartTime(LocalDateTime.now());
            couponCode.setEndTime(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusDays(pointsCoupon.getCouponInfo().getEffectiveDays()).minusSeconds(1));
        }
        couponCode.setDelFlag(DeleteFlag.NO);
        couponCode.setCreateTime(LocalDateTime.now());
        couponCode.setCreatePerson(customer.getCustomerId());
        couponCodeRepository.save(couponCode);
        // 4.扣优惠券库存
        int record = pointsCouponRepository.deductStock(pointsCoupon.getPointsCouponId());
        if (record != 1) {
            throw new SbcRuntimeException("K-120005");
        }
        // 如果库存为0，修改售罄标识
        int state = pointsCouponRepository.updateSellOutFlag(pointsCoupon.getPointsCouponId());
        // 库存为0时，修改优惠券活动是否剩余
        if (state == 1) {
            CouponActivityConfig couponActivityConfig =
                    couponActivityConfigRepository.findByCouponId(pointsCoupon.getCouponId()).get(0);
            couponActivityConfig.setHasLeft(DefaultFlag.NO);
            couponActivityConfigRepository.save(couponActivityConfig);
        }
        CouponInfoVO couponInfoVO = couponInfoService.wrapperCouponDetailInfo(pointsCoupon.getCouponInfo());
        return PointsCouponSendCodeResponse.builder()
                .pointsCouponId(pointsCoupon.getPointsCouponId())
                .couponInfoVO(couponInfoVO)
                .couponCodeId(couponCode.getCouponCodeId())
                .couponCode(couponCode.getCouponCode())
                .points(pointsCoupon.getPoints())
                .customer(customer)
                .build();
    }

    /**
     * 验证添加参数
     *
     * @param pointsCoupon
     */
    private void checkParam(PointsCoupon pointsCoupon) {
        CouponInfo couponInfo = couponInfoService.getCouponInfoById(pointsCoupon.getCouponId());
        // 判断优惠券是否已删除
        if (couponInfo.getDelFlag().equals(DeleteFlag.YES)) {
            throw new SbcRuntimeException(CouponErrorCode.COUPON_INFO_DELETED,
                    new Object[]{couponInfo.getCouponName()});
        }
        // 判断优惠券有效时间(优惠券截止时间大于兑换结束时间)
        if (Objects.nonNull(couponInfo.getEndTime()) && couponInfo.getEndTime().isBefore(pointsCoupon.getEndTime())) {
            throw new SbcRuntimeException(CouponErrorCode.POINTS_COUPON_DATE_ERROR,
                    new Object[]{couponInfo.getCouponName()});
        }
        // 验证兑换时间
        checkTime(pointsCoupon);
    }

    /**
     * 验证兑换时间
     *
     * @param entity
     */
    private void checkTime(PointsCoupon entity) {
        List<PointsCoupon> pointsCouponList = list(PointsCouponQueryRequest.builder()
                .couponId(entity.getCouponId())
                .delFlag(DeleteFlag.NO)
                .build());
        // 修改时排除自己在比较
        if (Objects.nonNull(entity.getPointsCouponId())) {
            pointsCouponList = pointsCouponList.stream()
                    .filter(pointsCoupon -> !pointsCoupon.getPointsCouponId().equals(entity.getPointsCouponId()))
                    .collect(Collectors.toList());
        }
        // 验证结束时间是否在开始时间之前
        if (!entity.getBeginTime().isBefore(entity.getEndTime())) {
            throw new SbcRuntimeException(CouponErrorCode.END_DATE_ERROR);
        }
        if (Objects.nonNull(pointsCouponList) && pointsCouponList.size() > 0) {
            for (PointsCoupon pointsCoupon : pointsCouponList) {
                // 开始时间等于已绑定积分兑换券的开始时间
                if (pointsCoupon.getBeginTime().isEqual(entity.getBeginTime()) ||
                        // 开始时间等于已绑定积分兑换券的结束时间
                        pointsCoupon.getEndTime().isEqual(entity.getBeginTime()) ||
                        // 开始时间在已绑定积分兑换券的时间段内
                        (pointsCoupon.getBeginTime().isBefore(entity.getBeginTime())
                                && pointsCoupon.getEndTime().isAfter(entity.getBeginTime())) ||
                        // 结束时间等于已绑定积分兑换券的开始时间
                        pointsCoupon.getBeginTime().isEqual(entity.getEndTime()) ||
                        // 结束时间等于已绑定积分兑换券的结束时间
                        pointsCoupon.getEndTime().isEqual(entity.getEndTime()) ||
                        // 结束时间在已绑定积分兑换券的时间段内
                        (pointsCoupon.getBeginTime().isBefore(entity.getEndTime())
                                && pointsCoupon.getEndTime().isAfter(entity.getEndTime())) ||
                        // 该兑换券绑定积分兑换券的时间段在已绑定积分兑换券时间段内
                        (pointsCoupon.getBeginTime().isAfter(entity.getBeginTime())
                                && pointsCoupon.getEndTime().isBefore(entity.getEndTime()))
                ) {
                    throw new SbcRuntimeException(CouponErrorCode.TIME_ERROR,
                            new Object[]{pointsCoupon.getCouponInfo().getCouponName(),
                                    pointsCoupon.getBeginTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
                                    pointsCoupon.getEndTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))});
                }
                // 兑换开始时间应大于当前时间
                if (entity.getBeginTime().isBefore(LocalDateTime.now())) {
                    throw new SbcRuntimeException(CouponErrorCode.BEGIN_DATE_ERROR);
                }
            }
        }
    }

    /**
     * 添加活动
     *
     * @param entity
     * @return
     */
    private String addCouponActivity(PointsCoupon entity) {
        CouponActivityAddRequest activityAddRequest = CouponActivityAddRequest.builder()
                .activityName("积分优惠券")
                .startTime(entity.getBeginTime())
                .endTime(entity.getEndTime())
                .couponActivityType(CouponActivityType.POINTS_COUPON)
                .receiveType(DefaultFlag.NO)
                .joinLevel("-1")
                .pauseFlag(entity.getStatus().toValue() == 0 ? DefaultFlag.YES : DefaultFlag.NO)
                .platformFlag(DefaultFlag.YES)
                .storeId(Constant.BOSS_DEFAULT_STORE_ID)
                .createPerson(entity.getCreatePerson())
                .build();
        List<CouponActivityConfigSaveRequest> couponActivityConfigs = getCouponActivityConfigSaveRequests(entity);
        activityAddRequest.setCouponActivityConfigs(couponActivityConfigs);
        return couponActivityService.addCouponActivity(activityAddRequest).getCouponActivity().getActivityId();
    }

    /**
     * 编辑活动
     *
     * @param entity
     * @return
     */
    private void modifyCouponActivity(PointsCoupon entity) {
        CouponActivityModifyRequest activityModifyRequest = new CouponActivityModifyRequest();
        activityModifyRequest.setActivityId(entity.getActivityId());
        activityModifyRequest.setStartTime(entity.getBeginTime());
        activityModifyRequest.setEndTime(entity.getEndTime());
        activityModifyRequest.setUpdatePerson(entity.getCreatePerson());
        activityModifyRequest.setCouponActivityType(CouponActivityType.POINTS_COUPON);
        List<CouponActivityConfigSaveRequest> couponActivityConfigs = getCouponActivityConfigSaveRequests(entity);
        activityModifyRequest.setCouponActivityConfigs(couponActivityConfigs);
        couponActivityService.modifyCouponActivity(activityModifyRequest);
    }

    /**
     * 获取活动配置添加参数
     *
     * @param entity
     * @return
     */
    private List<CouponActivityConfigSaveRequest> getCouponActivityConfigSaveRequests(PointsCoupon entity) {
        List<CouponActivityConfigSaveRequest> couponActivityConfigs = new ArrayList<>();
        CouponActivityConfigSaveRequest couponActivityConfigSaveRequest = new CouponActivityConfigSaveRequest();
        couponActivityConfigSaveRequest.setCouponId(entity.getCouponId());
        couponActivityConfigSaveRequest.setTotalCount(entity.getTotalCount());
        couponActivityConfigs.add(couponActivityConfigSaveRequest);
        return couponActivityConfigs;
    }

    /**
     * 将实体包装成VO
     *
     * @author yang
     */
    public PointsCouponVO wrapperVo(PointsCoupon pointsCoupon) {
        if (pointsCoupon != null) {
            PointsCouponVO pointsCouponVO = new PointsCouponVO();
            KsBeanUtil.copyPropertiesThird(pointsCoupon, pointsCouponVO);
            CouponInfo couponInfo = pointsCoupon.getCouponInfo();
            // 优惠券信息
            if (Objects.nonNull(couponInfo)) {
                CouponInfoVO couponInfoVO = new CouponInfoVO();
                KsBeanUtil.copyPropertiesThird(couponInfo, couponInfoVO);
                pointsCouponVO.setCouponInfoVO(couponInfoVO);
            }
            // 活动状态
            PointsCouponStatus pointsCouponStatus = getPointsCouponStatus(pointsCoupon);
            pointsCouponVO.setPointsCouponStatus(pointsCouponStatus);
            return pointsCouponVO;
        }
        return null;
    }


    /**
     * 获取积分优惠券活动状态
     *
     * @param pointsCoupon
     * @return
     */
    public PointsCouponStatus getPointsCouponStatus(PointsCoupon pointsCoupon) {
        if (LocalDateTime.now().isBefore(pointsCoupon.getBeginTime())) {
            return PointsCouponStatus.NOT_START;
        } else if (LocalDateTime.now().isAfter(pointsCoupon.getEndTime())) {
            return PointsCouponStatus.ENDED;
        } else {
            if (pointsCoupon.getStatus().equals(EnableStatus.DISABLE)) {
                return PointsCouponStatus.PAUSED;
            } else {
                return PointsCouponStatus.STARTED;
            }
        }
    }
}
