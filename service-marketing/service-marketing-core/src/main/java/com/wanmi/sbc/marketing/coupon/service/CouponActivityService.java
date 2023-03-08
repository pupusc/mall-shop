package com.wanmi.sbc.marketing.coupon.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerIdsListRequest;
import com.wanmi.sbc.customer.api.request.storelevel.CustomerLevelRequest;
import com.wanmi.sbc.customer.api.response.storelevel.CustomerLevelInfoResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.marketing.api.request.coupon.*;
import com.wanmi.sbc.marketing.api.response.coupon.*;
import com.wanmi.sbc.marketing.bean.constant.CouponErrorCode;
import com.wanmi.sbc.marketing.bean.dto.DistributionRewardCouponDTO;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.enums.RangeDayType;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityBaseVO;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityDisabledTimeVO;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityVO;
import com.wanmi.sbc.marketing.coupon.model.root.*;
import com.wanmi.sbc.marketing.coupon.repository.CouponActivityConfigRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponActivityRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponInfoRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponMarketingCustomerScopeRepository;
import com.wanmi.sbc.marketing.coupon.response.CouponActivityDetailResponse;
import com.wanmi.sbc.marketing.util.XssUtils;
import com.wanmi.sbc.marketing.util.mapper.CouponActivityMapper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: songhanlin
 * @Date: Created In 11:43 AM 2018/9/12
 * @Description: 优惠券活动Service
 */
@Service
@Slf4j
public class CouponActivityService {

    @Autowired
    private CouponActivityRepository couponActivityRepository;

    @Autowired
    private CouponActivityConfigRepository couponActivityConfigRepository;

    @Autowired
    private CouponInfoRepository couponInfoRepository;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CouponCacheService couponCacheService;

    @Autowired
    private CouponActivityConfigService couponActivityConfigService;

    @Autowired
    private CouponCodeService couponCodeService;

    @Autowired
    private CouponMarketingCustomerScopeRepository couponMarketingCustomerScopeRepository;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CouponActivityMapper couponActivityMapper;
    /**
     * 创建活动
     */
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public CouponActivityDetailResponse addCouponActivity(CouponActivityAddRequest couponActivityAddRequest) {
        // 校验 活动结束时间必须大于已选优惠券结束时间
        List<CouponActivityConfig> couponActivityConfigs = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(couponActivityAddRequest.getCouponActivityConfigs())) {
            couponActivityConfigs = KsBeanUtil.copyListProperties(couponActivityAddRequest.getCouponActivityConfigs(), CouponActivityConfig.class);
            List<String> errorIds = this.checkCoupon(couponActivityAddRequest.getEndTime(), couponActivityConfigs);
            if (!errorIds.isEmpty()) {
                throw new SbcRuntimeException(errorIds, CouponErrorCode.ACTIVITY_ERROR_COUPON);
            }
        }

        //保存活动
        CouponActivity couponActivity = new CouponActivity();
        KsBeanUtil.copyPropertiesThird(couponActivityAddRequest, couponActivity);
        couponActivity.setCreateTime(LocalDateTime.now());
        couponActivity.setDelFlag(DeleteFlag.NO);
        if (couponActivityAddRequest.getPauseFlag() == null) {
            couponActivity.setPauseFlag(DefaultFlag.NO);
        }
        if (CouponActivityType.REGISTERED_COUPON == couponActivityAddRequest.getCouponActivityType()
                || CouponActivityType.STORE_COUPONS == couponActivityAddRequest.getCouponActivityType()
                || CouponActivityType.ENTERPRISE_REGISTERED_COUPON == couponActivityAddRequest.getCouponActivityType()) {
            couponActivity.setLeftGroupNum(couponActivity.getReceiveCount());
        }
        /* 后期有并发问题，可以对这部分内容加分布式锁   start*/
        // 校验 进店赠券活动、注册赠券活动、企业注册赠券活动，同一时间段内只能有1个！
        if (this.checkActivity(couponActivityAddRequest.getStartTime(),
                couponActivityAddRequest.getEndTime(), couponActivityAddRequest.getCouponActivityType(),
                couponActivityAddRequest.getStoreId(), null)) {
            throw new SbcRuntimeException(CouponErrorCode.ACTIVITY_TIME_CHANGE);
        }
        couponActivity = couponActivityRepository.saveAndFlush(couponActivity);
        //保存活动关联的优惠券
        if (CollectionUtils.isNotEmpty(couponActivityConfigs)) {
            for (CouponActivityConfig item : couponActivityConfigs) {
                item.setActivityId(couponActivity.getActivityId());
            }
            couponActivityConfigs.forEach(item -> item.setHasLeft(DefaultFlag.YES));
            couponActivityConfigRepository.saveAll(couponActivityConfigs);
        }


        //保存活动关联的目标客户作用范围
        List<CouponMarketingCustomerScope> couponMarketingCustomerScope = saveMarketingCustomerScope
                (couponActivityAddRequest.getCustomerScopeIds(),
                        couponActivity);

        CouponActivityDetailResponse response = new CouponActivityDetailResponse();
        response.setCouponActivity(couponActivity);
        response.setCouponActivityConfigList(couponActivityConfigs);
        response.setCouponMarketingCustomerScope(couponMarketingCustomerScope);
        return response;
    }

    /**
     * 编辑活动
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public CouponActivityModifyResponse modifyCouponActivity(CouponActivityModifyRequest couponActivityModifyRequest) {
        // 校验，如果活动未开始才可以编辑
        CouponActivity couponActivity = couponActivityRepository.findById(couponActivityModifyRequest.getActivityId()).get();
        if (!couponActivityModifyRequest.getCouponActivityType().equals(CouponActivityType.POINTS_COUPON)) {
            if (couponActivity.getStartTime() != null && couponActivity.getStartTime().isBefore(LocalDateTime.now())) {
                // 活动已经开始，不可以编辑，删除
                throw new SbcRuntimeException(CouponErrorCode.ACTIVITY_GOING);
            }
        }

        List<CouponActivityConfig> couponActivityConfigs = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(couponActivityModifyRequest.getCouponActivityConfigs())) {
            couponActivityConfigs = KsBeanUtil.copyListProperties(couponActivityModifyRequest
                    .getCouponActivityConfigs(), CouponActivityConfig.class);
            // 校验 活动结束时间必须大于已选优惠券结束时间
            List<String> errorIds = this.checkCoupon(couponActivityModifyRequest.getEndTime(), couponActivityConfigs);
            if (!errorIds.isEmpty()) {
                throw new SbcRuntimeException(errorIds, CouponErrorCode.ACTIVITY_ERROR_COUPON);
            }
        }

        //全部删除活动关联的优惠券
        couponActivityConfigRepository.deleteByActivityId(couponActivityModifyRequest.getActivityId());
        //保存活动
        KsBeanUtil.copyProperties(couponActivityModifyRequest, couponActivity);
        couponActivity.setUpdateTime(LocalDateTime.now());
        couponActivityRepository.save(couponActivity);
        if (CouponActivityType.REGISTERED_COUPON == couponActivityModifyRequest.getCouponActivityType()
                || CouponActivityType.STORE_COUPONS == couponActivityModifyRequest.getCouponActivityType()) {
            couponActivity.setLeftGroupNum(couponActivity.getReceiveCount());
        }
        /* 后期有并发问题，可以对这部分内容加分布式锁   start*/
        // 校验 进店赠券活动、注册赠券活动，同一时间段内只能各有1个！
        if (this.checkActivity(couponActivityModifyRequest.getStartTime(),
                couponActivityModifyRequest.getEndTime(), couponActivityModifyRequest.getCouponActivityType(),
                couponActivityModifyRequest.getStoreId(), couponActivityModifyRequest.getActivityId())) {
            throw new SbcRuntimeException(CouponErrorCode.ACTIVITY_TIME_CHANGE);
        }
        //保存活动
        couponActivity = couponActivityRepository.save(couponActivity);
        /* 后期有并发问题，可以对这部分内容加分布式锁   end*/
        //全部删除活动关联的优惠券
        couponActivityConfigRepository.deleteByActivityId(couponActivityModifyRequest.getActivityId());
        //保存活动关联的优惠券
        if (CollectionUtils.isNotEmpty(couponActivityConfigs)) {
            for (CouponActivityConfig item : couponActivityConfigs) {
                item.setActivityId(couponActivity.getActivityId());
            }
            couponActivityConfigs.forEach(item -> item.setHasLeft(DefaultFlag.YES));
            couponActivityConfigRepository.saveAll(couponActivityConfigs);
        }

        //保存活动关联的目标客户作用范围
        saveMarketingCustomerScope(couponActivityModifyRequest.getCustomerScopeIds(), couponActivity);

        CouponActivityVO couponActivityVO = couponActivityMapper.couponActivityToCouponActivityVO(couponActivity);

        return new CouponActivityModifyResponse(couponActivityVO);
    }


    /**
     * 开始活动
     * @param id
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void startActivity(String id) {
        //当活动暂停时才可以开始
        CouponActivity couponActivity = couponActivityRepository.findById(id).get();
        if (CouponActivityType.SPECIFY_COUPON == couponActivity.getCouponActivityType()) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (couponActivity.getStartTime().isAfter(LocalDateTime.now()) && couponActivity.getEndTime().isBefore
                (LocalDateTime.now())) {
            //非进行中的活动
            throw new SbcRuntimeException(CouponErrorCode.ACTIVITY_NOT_START);
        }
        couponActivityRepository.startActivity(id);
        couponCacheService.refreshCachePart(Arrays.asList(couponActivity.getActivityId()));
    }

    /**
     * 暂停活动
     * @param id
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void pauseActivity(String id) {
        // 只有进行中的活动才可以暂停
        CouponActivity couponActivity = couponActivityRepository.findById(id).get();
        if (CouponActivityType.SPECIFY_COUPON == couponActivity.getCouponActivityType()) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (couponActivity.getStartTime().isAfter(LocalDateTime.now())) {
            //活动还未开始不能暂停
            throw new SbcRuntimeException(CouponErrorCode.ACTIVITY_NOT_START);
        }
        if (couponActivity.getEndTime().isBefore(LocalDateTime.now())) {
            //活动已经结束
            throw new SbcRuntimeException(CouponErrorCode.ACTIVITY_FINISH);
        }
        couponActivityRepository.pauseActivity(id);
        couponCacheService.refreshCachePart(Arrays.asList(couponActivity.getActivityId()));
    }

    /**
     * 删除活动
     * @param id
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void deleteActivity(String id, String operatorId) {
        //只有未开始的活动才可以删除
        CouponActivity couponActivity = couponActivityRepository.findById(id).get();
        if (CouponActivityType.RIGHTS_COUPON != couponActivity.getCouponActivityType() && couponActivity.getStartTime().isBefore(LocalDateTime.now())) {
            //活动已开始不可以删除
            throw new SbcRuntimeException(CouponErrorCode.ACTIVITY_GOING);
        }
        couponActivityRepository.deleteActivity(id, operatorId);
        couponActivityConfigRepository.deleteByActivityId(id);
    }

    /**
     * 查询活动详情
     * @param id
     */
    public CouponActivityDetailResponse getActivityDetail(String id, Long storeId) {
        // 1、查询活动基本信息
        Optional<CouponActivity> couponActivityOptional = couponActivityRepository.findById(id);
        if (!couponActivityOptional.isPresent()) {
            throw new SbcRuntimeException(CouponErrorCode.ACTIVITY_NOT_EXIST);
        }
        CouponActivity couponActivity = couponActivityOptional.get();
        CouponActivityDetailResponse response = new CouponActivityDetailResponse();
        //  2、查询关联优惠券信息
        List<CouponActivityConfig> couponActivityConfigs = couponActivityConfigRepository.findByActivityId(id);
        List<String> ids = new ArrayList<>();
        couponActivityConfigs.forEach(item -> ids.add(item.getCouponId()));
        List<CouponInfo> couponInfos = couponInfoRepository.queryByIds(ids);
        //  3、查询客户等级信息
        List<CustomerLevelVO> customerLevels = customerLevels(couponActivity);

        //4.查询指定用户
        List<CouponMarketingCustomerScope> couponMarketingCustomerScope = couponMarketingCustomerScopeRepository
                .findByActivityId(couponActivity.getActivityId());
        if (CollectionUtils.isNotEmpty(couponMarketingCustomerScope)) {
            List<CustomerVO> detailResponseList = getCouponMarketingCustomers(couponMarketingCustomerScope,
                    couponActivity);
            response.setCustomerDetailVOS(detailResponseList);
        }
        response.setCouponMarketingCustomerScope(couponMarketingCustomerScope);
        response.setCouponActivity(couponActivity);
        response.setCouponActivityConfigList(couponActivityConfigs);
        response.setCouponInfoList(couponInfos);
        response.setCustomerLevelList(customerLevels);

        return response;
    }

    /**
     * 通过主键获取优惠券活动
     * @param id
     * @return
     */
    public CouponActivity getCouponActivityByPk(String id) {
        return couponActivityRepository.findById(id).orElse(null);
    }

    /**
     * 查询活动列表
     * @param request
     */
    public Page<CouponActivity> pageActivityInfo(CouponActivityPageRequest request, Long storeId) {
        request.setStoreId(storeId);
        return couponActivityRepository.findAll(getWhereCriteria(request),request.getPageRequest());
//        //查询列表
//        String sql = "SELECT t.* FROM coupon_activity t ";
//        //条件查询
//        StringBuilder whereSql = new StringBuilder("WHERE t.del_flag = 0");
//
//        if (storeId != null) {
//            whereSql.append(" AND t.store_id = " + storeId).append(" AND t.platform_flag = 0");
//        } else {
//            whereSql.append(" AND t.platform_flag = 1");
//        }
//        //活动名称查找
//        if (StringUtils.isNotBlank(request.getActivityName())) {
//            whereSql.append(" AND t.activity_name LIKE '%" + request.getActivityName() + "%'");
//        }
//        //活动类型筛选
//        if (CouponActivityType.ALL_COUPONS.equals(request.getCouponActivityType())) {
//            whereSql.append(" AND t.activity_type = " + CouponActivityType.ALL_COUPONS.toValue());
//        } else if (CouponActivityType.SPECIFY_COUPON.equals(request.getCouponActivityType())) {
//            whereSql.append(" AND t.activity_type = " + CouponActivityType.SPECIFY_COUPON.toValue());
//        } else if (CouponActivityType.STORE_COUPONS.equals(request.getCouponActivityType())) {
//            whereSql.append(" AND t.activity_type = " + CouponActivityType.STORE_COUPONS.toValue());
//        } else if (CouponActivityType.REGISTERED_COUPON.equals(request.getCouponActivityType())) {
//            whereSql.append(" AND t.activity_type = " + CouponActivityType.REGISTERED_COUPON.toValue());
//        } else if (CouponActivityType.RIGHTS_COUPON.equals(request.getCouponActivityType())) {
//            whereSql.append(" AND t.activity_type = " + CouponActivityType.RIGHTS_COUPON.toValue());
//        } else if (CouponActivityType.DISTRIBUTE_COUPON.equals(request.getCouponActivityType())) {
//            whereSql.append(" AND t.activity_type = " + CouponActivityType.DISTRIBUTE_COUPON.toValue());
//        } else if (CouponActivityType.POINTS_COUPON.equals(request.getCouponActivityType())) {
//            whereSql.append(" AND t.activity_type = " + CouponActivityType.POINTS_COUPON.toValue());
//        } else if (CouponActivityType.ENTERPRISE_REGISTERED_COUPON.equals(request.getCouponActivityType())) {
//            whereSql.append(" AND t.activity_type = " + CouponActivityType.ENTERPRISE_REGISTERED_COUPON.toValue());
//        }
//
//        //时间筛选
//        if (null != request.getStartTime()) {
//            whereSql.append(" AND '" + DateUtil.format(request.getStartTime(), DateUtil.FMT_TIME_1) + "' <= t" +
//                    ".start_time");
//        }
//        if (null != request.getEndTime()) {
//            whereSql.append(" AND '" + DateUtil.format(request.getEndTime(), DateUtil.FMT_TIME_1) + "' >= t.end_time");
//        }
//
//        switch (request.getQueryTab()) {
//            case STARTED://进行中
//                whereSql.append(" AND ((now() >= t.start_time AND now() <= t.end_time) or t.activity_type=4) AND t" +
//                        ".pause_flag = 0 AND t.activity_type != 1");
//                break;
//            case PAUSED://暂停中
//                whereSql.append(" AND ((now() >= t.start_time AND now() <= t.end_time) or t.activity_type=4) AND t" +
//                        ".pause_flag = 1");
//                break;
//            case NOT_START://未开始
//                whereSql.append(" AND now() < t.start_time");
//                break;
//            case ENDED://已结束
//                whereSql.append(" AND now() > t.end_time");
//                break;
//            default:
//                break;
//        }
//
//        if (StringUtils.isNotBlank(request.getJoinLevel())) {
//            whereSql.append(" AND  find_in_set( '" + request.getJoinLevel() + "' , t.join_level)");
//        }
//        whereSql.append(" order by t.create_time desc");
//        Query query = entityManager.createNativeQuery(sql.concat(whereSql.toString()));
//        query.setFirstResult(request.getPageNum() * request.getPageSize());
//        query.setMaxResults(request.getPageSize());
//        query.unwrap(SQLQuery.class).addEntity("t", CouponActivity.class);
//        List<CouponActivity> responsesList = (List<CouponActivity>) query.getResultList();
//        //查询记录的总数量
//        String countSql = "SELECT count(1) count FROM coupon_activity t ";
//        long count = 0;
//        if (CollectionUtils.isNotEmpty(responsesList)) {
//            Query queryCount = entityManager.createNativeQuery(countSql.concat(whereSql.toString()));
//            count = Long.parseLong(queryCount.getSingleResult().toString());
//        }
//        return new PageImpl<>(responsesList, request.getPageable(), count);
    }

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<CouponActivity> getWhereCriteria(CouponActivityPageRequest request) {
        CouponActivityType couponActivityType = request.getCouponActivityType();
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //店铺id
            if (Objects.nonNull(request.getStoreId())) {
                predicates.add(cbuild.equal(root.get("storeId"), request.getStoreId()));
                predicates.add(cbuild.equal(root.get("platformFlag"), 0));
            }else{
                predicates.add(cbuild.equal(root.get("platformFlag"), 1));
            }

            //活动名称查找
            if (StringUtils.isNotBlank(request.getActivityName())) {
                predicates.add(cbuild.like(root.get("activityName"),
                        StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(request.getActivityName().trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //活动类型筛选
            if (CouponActivityType.ALL_COUPONS.equals(couponActivityType)
                    || CouponActivityType.SPECIFY_COUPON.equals(couponActivityType)
                    || CouponActivityType.STORE_COUPONS.equals(couponActivityType)
                    || CouponActivityType.REGISTERED_COUPON.equals(couponActivityType)
                    || CouponActivityType.RIGHTS_COUPON.equals(couponActivityType)
                    || CouponActivityType.DISTRIBUTE_COUPON.equals(couponActivityType)
                    || CouponActivityType.POINTS_COUPON.equals(couponActivityType)
                    || CouponActivityType.ENTERPRISE_REGISTERED_COUPON.equals(couponActivityType)) {
                predicates.add(cbuild.equal(root.get("couponActivityType"),  couponActivityType.toValue()));
            }

            if (Objects.nonNull(request.getStartTime())) {
                Predicate p1 = cbuild.greaterThanOrEqualTo(root.get("startTime"), request.getStartTime());
                predicates.add(p1);
            }
            if (Objects.nonNull(request.getEndTime())) {
                Predicate p1 = cbuild.lessThanOrEqualTo(root.get("endTime"), request.getEndTime());
                predicates.add(p1);
            }

            switch (request.getQueryTab()) {
                case STARTED://进行中
                    Predicate p1 = cbuild.lessThan(root.get("startTime"), LocalDateTime.now());
                    Predicate p2 = cbuild.greaterThanOrEqualTo(root.get("endTime"), LocalDateTime.now());
                    Predicate p3 = cbuild.and(p1, p2);
                    Predicate p4 = cbuild.equal(root.get("couponActivityType"), 4);
                    Predicate p5 = cbuild.or(p3,p4);
                    predicates.add(p5);
                    predicates.add(cbuild.equal(root.get("pauseFlag"), 0));
                    predicates.add(cbuild.notEqual(root.get("couponActivityType"), 1));
                    break;
                case PAUSED://暂停中
                    Predicate p6 = cbuild.lessThanOrEqualTo(root.get("startTime"), LocalDateTime.now());
                    Predicate p7 = cbuild.greaterThanOrEqualTo(root.get("endTime"), LocalDateTime.now());
                    Predicate p8 = cbuild.and(p6, p7);
                    Predicate p9 = cbuild.equal(root.get("couponActivityType"), 4);
                    Predicate p10 = cbuild.or(p8,p9);
                    predicates.add(p10);
                    predicates.add(cbuild.equal(root.get("pauseFlag"), 1));
                    break;
                case NOT_START://未开始
                    predicates.add(cbuild.greaterThan(root.get("startTime"),  LocalDateTime.now()));
                    break;
                case ENDED://已结束
                    predicates.add(cbuild.lessThan(root.get("endTime"),  LocalDateTime.now()));
                    break;
                default:
                    break;
            }

            if (StringUtils.isNotBlank(request.getJoinLevel())) {
                Expression<Integer> expression = cbuild.function("FIND_IN_SET", Integer.class, cbuild.literal(request.getJoinLevel()), root.get("joinLevel"));
                predicates.add(cbuild.greaterThan(expression, 0));
            }

            predicates.add(cbuild.equal(root.get("delFlag"), 0));

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }


    /**
     * 保存活动时，1 校验优惠券是否存在 2校验优惠券的结束时间是否都在活动结束时间内
     * 将不符合的优惠券id返回
     * @return
     */
    private List<String> checkCoupon(LocalDateTime activityEndTime, List<CouponActivityConfig> couponActivityConfigs) {
        List<String> ids = new ArrayList<>();
        couponActivityConfigs.forEach(item -> ids.add(item.getCouponId()));
        List<CouponInfo> couponInfos = couponInfoRepository.queryByIds(ids);
        List<String> errorIds = new ArrayList<>();
        if (ids.size() > couponInfos.size()) {
            errorIds = ids.stream().filter(id ->
                    couponInfos.stream().noneMatch(couponInfo -> couponInfo.getCouponId().equals(id))).collect
                    (Collectors.toList());
            throw new SbcRuntimeException(errorIds, CouponErrorCode.COUPON_INFO_NOT_EXIST);
        }

        // 会员权益赠券的活动没有结束时间，直接return
        if (Objects.isNull(activityEndTime)) {
            return errorIds;
        }

        for (CouponInfo item : couponInfos) {
            if (RangeDayType.RANGE_DAY == item.getRangeDayType() && item.getEndTime().isBefore(activityEndTime)) {
                errorIds.add(item.getCouponId());
            }
            continue;
        }
        return errorIds;
    }

    /**
     * 获取目前最后一个开始的优惠券活动
     * @return
     */
    public CouponActivity getLastActivity() {
        List<CouponActivity> activityList = couponActivityRepository.getLastActivity(PageRequest.of(0, 1, Sort
                .Direction.DESC, "startTime"));
        return CollectionUtils.isNotEmpty(activityList) ? activityList.get(0) : null;
    }

    /**
     * 校验 进店赠券活动、注册赠券活动、企业注册赠券活动，同一时间段内只能有1个！
     * true 表示 校验失败
     * @param statTime
     * @param endTime
     * @param type
     * @param storeId
     * @param activityId
     * @return
     */
    private Boolean checkActivity(LocalDateTime statTime, LocalDateTime endTime, CouponActivityType type, Long
            storeId, String activityId) {
        Boolean flag = Boolean.FALSE;
        if (CouponActivityType.REGISTERED_COUPON == type || CouponActivityType.STORE_COUPONS == type
                || CouponActivityType.ENTERPRISE_REGISTERED_COUPON == type) {
            List<CouponActivity> activityList = couponActivityRepository.queryActivityByTime(statTime, endTime, type, storeId);
            //过滤当前活动id
            if (StringUtils.isNotBlank(activityId)) {
                activityList = activityList.stream().filter(
                        item -> !item.getActivityId().equals(activityId)).collect(Collectors.toList());
            }
            if (!activityList.isEmpty()) {
                flag = Boolean.TRUE;
            }
        }
        return flag;
    }

    /**
     * 领取一组优惠券 （注册活动或者进店活动）
     * 用户注册成功或者进店后，发放赠券
     * @param customerId
     * @param type
     * @param storeId
     * @return
     */
    @Transactional
    @GlobalTransactional
    public GetRegisterOrStoreCouponResponse getCouponGroup(String customerId, CouponActivityType type, Long storeId) {
        //参数校验
        if (CouponActivityType.REGISTERED_COUPON != type && CouponActivityType.STORE_COUPONS != type && CouponActivityType.ENTERPRISE_REGISTERED_COUPON != type) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (customerId == null || storeId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        // 1、查询是否该类型的活动在进行中 并且活动剩余优惠券组数>0；
        List<CouponActivity> couponActivityList = couponActivityRepository.queryGoingActivityByType(type, storeId);
        //企业会员注册-企业会员的注册赠券活动，优先级高于全平台客户。
        //企业会员注册赠券活动不存在或者优惠券组数没有了，继续注册赠券的逻辑
        if(CouponActivityType.ENTERPRISE_REGISTERED_COUPON == type && CollectionUtils.isEmpty(couponActivityList)){
            couponActivityList=couponActivityRepository.queryGoingActivityByType(CouponActivityType.REGISTERED_COUPON , storeId);
        }

        if (couponActivityList.size() == 0) {
            return null;
        } else if (couponActivityList.size() != 1) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "查询活动时：活动类型为：" + type.toString() + "的活动数据重复！！！");
        }
        CouponActivity activity = couponActivityList.get(0);
        GetRegisterOrStoreCouponResponse response = new GetRegisterOrStoreCouponResponse();
        boolean flag = this.checkCustomerQualify(customerId, type, activity.getActivityId());
        if (!flag) {
            return null;
        }
        //如果剩下的数量<0,返回空
        if (activity.getLeftGroupNum() == 0) {
            return null;
        }
        response.setDesc(activity.getActivityDesc());
        response.setTitle(activity.getActivityTitle());
        // 2、未领完则 先领取（并发如果很大，可对这部分加锁）
        int num = couponActivityRepository.getCouponGroup(activity.getActivityId());
        if (num == 0) {
            return null;
        }
        if (num != 1) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "更新剩余数量时：活动类型为：" + type.toString() + "的活动数据重复！！！");
        }
        // 3、再生成用户优惠券数据
        List<CouponActivityConfig> couponActivityConfigList = couponActivityConfigService.queryByActivityId(activity
                .getActivityId());
        List<CouponInfo> couponInfoList = couponInfoRepository.queryByIds(couponActivityConfigList.stream().map(
                CouponActivityConfig::getCouponId).collect(Collectors.toList()));
        List<GetCouponGroupResponse> getCouponGroupResponse = KsBeanUtil.copyListProperties(couponInfoList,
                GetCouponGroupResponse.class);
        getCouponGroupResponse = getCouponGroupResponse.stream().peek(item -> couponActivityConfigList.forEach(config
                -> {
            if (item.getCouponId().equals(config.getCouponId())) {
                item.setTotalCount(config.getTotalCount());
            }
        })).collect(Collectors.toList());
        couponCodeService.sendBatchCouponCodeByCustomer(getCouponGroupResponse, customerId, activity.getActivityId(), null);
        //4. 按金额大小 从大到小排序
        getCouponGroupResponse.sort(Comparator.comparing(GetCouponGroupResponse::getDenomination).reversed());
        response.setCouponList(getCouponGroupResponse);
        return response;
    }

    /**
     * 查询活动（注册赠券活动、进店赠券活动）不可用的时间范围
     * @param request
     * @return
     */
    public CouponActivityDisabledTimeResponse queryActivityEnableTime(CouponActivityDisabledTimeRequest request) {
        if (CouponActivityType.ALL_COUPONS == request.getCouponActivityType()
                || CouponActivityType.SPECIFY_COUPON == request.getCouponActivityType()) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<CouponActivity> couponActivityList = couponActivityRepository.queryActivityDisableTime(request
                .getCouponActivityType(), request.getStoreId());
        if (StringUtils.isNotBlank(request.getActivityId()) && !couponActivityList.isEmpty()) {
            couponActivityList = couponActivityList.stream().filter(
                    item -> !item.getActivityId().equals(request.getActivityId())).collect(Collectors.toList());
        }
        CouponActivityDisabledTimeResponse disabledTimeResponses = new CouponActivityDisabledTimeResponse();
        disabledTimeResponses.setCouponActivityDisabledTimeVOList(
                couponActivityList.stream().map(item -> {
                    CouponActivityDisabledTimeVO disabledTime = new CouponActivityDisabledTimeVO();
                    disabledTime.setStartTime(item.getStartTime());
                    disabledTime.setEndTime(item.getEndTime());
                    return disabledTime;
                }).collect(Collectors.toList()));
        return disabledTimeResponses;
    }

    /**
     * 判断用户领券资格
     * @param customerId
     * @param type
     * @param activityId
     * @return
     */
    private boolean checkCustomerQualify(String customerId, CouponActivityType type, String activityId) {
        boolean flag = Boolean.FALSE;
        if (CouponActivityType.REGISTERED_COUPON == type ||CouponActivityType.ENTERPRISE_REGISTERED_COUPON== type) {
            //注册赠券(注册赠券、企业会员注册赠券)，校验当前用户是否有券，如果有券了就不可用领注册券
            Integer num = couponCodeService.countByCustomerId(customerId);
            if (num == 0) {
                flag = true;
            }
        } else if (CouponActivityType.STORE_COUPONS == type) {
            //进店赠券，根据customerId和activityId判断当前用户在本次活动中是否已经有券
            Integer num = couponCodeService.countByCustomerIdAndActivityId(customerId, activityId);
            if (num == 0) {
                flag = true;
            }
        }
        return flag;
    }


    /**
     * 指定活动赠券
     * @param request
     * @return
     */
    @Transactional
    public SendCouponResponse sendCouponGroup(SendCouponGroupRequest request) {
        SendCouponResponse response = new SendCouponResponse();
        couponCodeService.sendBatchCouponCodeByCustomer(request.getCouponInfos(), request.getCustomerId(), request
                .getActivityId(), null);
        response.setCouponList(request.getCouponInfos());
        return response;
    }

    /**
     * 邀新注册-发放优惠券
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean sendCouponGroup(CouponGroupAddRequest request) {

        String requestCustomerId = request.getRequestCustomerId();

        List<DistributionRewardCouponDTO> list = request.getDistributionRewardCouponDTOList();

        if (StringUtils.isBlank(requestCustomerId) || CollectionUtils.isEmpty(list)) {
            return Boolean.FALSE;
        }

        List<String> couponIdList = list.stream().map(DistributionRewardCouponDTO::getCouponId).collect(Collectors
                .toList());

        Map<String, Integer> map = list.stream().collect(Collectors.toMap(DistributionRewardCouponDTO::getCouponId,
                DistributionRewardCouponDTO::getCount));

        Integer sum = list.stream().map(DistributionRewardCouponDTO::getCount).reduce(Integer::sum).orElse
                (NumberUtils.INTEGER_ZERO);

        List<CouponInfo> couponInfoList = couponInfoRepository.queryByIds(couponIdList);

        List<GetCouponGroupResponse> couponGroupResponseList = couponInfoList.stream().map(couponInfo -> {
            GetCouponGroupResponse getCouponGroupResponse = KsBeanUtil.convert(couponInfo, GetCouponGroupResponse
                    .class);
            getCouponGroupResponse.setTotalCount(map.get(couponInfo.getCouponId()).longValue());
            return getCouponGroupResponse;
        }).collect(Collectors.toList());

        CouponActivity couponActivity = findDistributeCouponActivity();

        List<CouponCode> codeList = couponCodeService.sendBatchCouponCodeByCustomer(couponGroupResponseList,
                requestCustomerId, couponActivity.getActivityId(), null);

        return sum == codeList.size() ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 查询分销邀新赠券活动
     * @return
     */
    public CouponActivity findDistributeCouponActivity() {
        return couponActivityRepository.findDistributeCouponActivity();
    }

    /**
     * 保存优惠券活动目标客户作用范围
     * @param customerScopeIds
     * @param couponActivity
     */
    private List<CouponMarketingCustomerScope> saveMarketingCustomerScope(List<String> customerScopeIds, CouponActivity
            couponActivity) {
        //全部删除活动关联的目标客户作用范围
        couponMarketingCustomerScopeRepository.deleteByActivityId(couponActivity.getActivityId());
        //保存优惠券活动目标客户作用范围
        if (CollectionUtils.isNotEmpty(customerScopeIds)) {
            List<CouponMarketingCustomerScope> couponMarketingCustomerScopes = new ArrayList<>();
            for (String item : customerScopeIds) {
                CouponMarketingCustomerScope temp = new CouponMarketingCustomerScope();
                temp.setActivityId(couponActivity.getActivityId());
                temp.setCustomerId(item);
                couponMarketingCustomerScopes.add(temp);
            }
            couponMarketingCustomerScopeRepository.saveAll(couponMarketingCustomerScopes);
            return couponMarketingCustomerScopes;
        }
        return null;
    }

    /**
     * 查询客户等级
     * @param couponActivity
     * @return
     */
    private List<CustomerLevelVO> customerLevels(CouponActivity couponActivity) {
        List<CustomerLevelVO> customerLevels = null;
        MarketingJoinLevel marketingJoinLevel = getMarketingJoinLevel(couponActivity.getJoinLevel());
        //其他等级
        if (Objects.equals(MarketingJoinLevel.LEVEL_LIST, marketingJoinLevel)) {

            CustomerLevelRequest customerLevelRequest = CustomerLevelRequest.builder().storeId(couponActivity
                    .getStoreId())
                    .levelType(couponActivity.getJoinLevelType()).build();
            BaseResponse<CustomerLevelInfoResponse> customerLevelInfoResponse = storeLevelQueryProvider
                    .queryCustomerLevelInfo(customerLevelRequest);
            customerLevels = customerLevelInfoResponse.getContext().getCustomerLevelVOList();
        }
        return customerLevels;
    }

    /**
     * 活动关联客户信息
     * @param couponMarketingCustomerScope
     * @param couponActivity
     * @return
     */
    private List<CustomerVO> getCouponMarketingCustomers(List<CouponMarketingCustomerScope> couponMarketingCustomerScope,
                                                         CouponActivity
                                                                 couponActivity) {
        if (CollectionUtils.isNotEmpty(couponMarketingCustomerScope)) {
            List<String> customerIds = couponMarketingCustomerScope.stream().map
                    (CouponMarketingCustomerScope::getCustomerId).collect(Collectors.toList());
            CustomerIdsListRequest request = new CustomerIdsListRequest();
            request.setCustomerIds(customerIds);
            List<CustomerVO> detailResponseList = customerQueryProvider.getCustomerListByIds(request)
                    .getContext().getCustomerVOList();
            return detailResponseList;

        }
        return null;
    }

    public MarketingJoinLevel getMarketingJoinLevel(String joinLevel) {
        if (joinLevel.equals("0")) {
            return MarketingJoinLevel.ALL_LEVEL;
        } else if (joinLevel.equals("-1")) {
            return MarketingJoinLevel.ALL_CUSTOMER;
        } else if (joinLevel.equals("-2")) {
            return MarketingJoinLevel.SPECIFY_CUSTOMER;
        } else {
            return MarketingJoinLevel.LEVEL_LIST;
        }
    }

    /**
     * 分页查询优惠券活动
     * @param request
     * @return
     */
    public List<CouponActivityBaseVO> page(CouponActivityListPageRequest request) {

        List<String> activityIds = CollectionUtils.isNotEmpty(request.getActivityIds()) ? request.getActivityIds() : couponActivityRepository.listByPage(request.getPageRequest());

        List<CouponActivity> couponActivityList = couponActivityRepository.findAllById(activityIds);

        if (CollectionUtils.isEmpty(couponActivityList)) {
            return Lists.newArrayList();
        }

        return couponActivityMapper.couponActivityToCouponActivityBaseVO(couponActivityList);
    }



}
