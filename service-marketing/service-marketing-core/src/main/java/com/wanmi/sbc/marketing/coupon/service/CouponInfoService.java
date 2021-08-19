package com.wanmi.sbc.marketing.coupon.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.api.provider.brand.ContractBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.ContractBrandListRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandListRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdsRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateListByConditionRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateListCouponDetailRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewPageRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewPageResponse;
import com.wanmi.sbc.goods.bean.dto.CouponInfoForScopeNamesDTO;
import com.wanmi.sbc.goods.bean.dto.CouponMarketingScopeDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoAddRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoListByPageRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoModifyRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoQueryRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoPageResponse;
import com.wanmi.sbc.marketing.bean.constant.CouponErrorCode;
import com.wanmi.sbc.marketing.bean.enums.*;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.marketing.bean.vo.PointsCouponVO;
import com.wanmi.sbc.marketing.coupon.model.root.*;
import com.wanmi.sbc.marketing.coupon.repository.CouponActivityConfigRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponCateRelaRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponInfoRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponMarketingScopeRepository;
import com.wanmi.sbc.marketing.coupon.response.CouponCateResponse;
import com.wanmi.sbc.marketing.coupon.response.CouponInfoResponse;
import com.wanmi.sbc.marketing.pointscoupon.model.root.PointsCoupon;
import com.wanmi.sbc.marketing.pointscoupon.service.PointsCouponService;
import com.wanmi.sbc.marketing.util.XssUtils;
import com.wanmi.sbc.marketing.util.mapper.CouponInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: songhanlin
 * @Date: Created In 11:42 AM 2018/9/12
 * @Description: 优惠券信息Service
 */
@Slf4j
@Service
public class CouponInfoService {

    @Autowired
    private CouponInfoRepository couponInfoRepository;

    @Autowired
    private CouponCateRelaRepository couponCateRelaRepository;

    @Autowired
    private CouponMarketingScopeRepository couponMarketingScopeRepository;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private CouponActivityConfigRepository couponActivityConfigRepository;

    @Autowired
    private ContractBrandQueryProvider contractBrandQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private CouponCateService couponCateService;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private PointsCouponService pointsCouponService;

    @Autowired
    private CouponInfoMapper couponInfoMapper;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<CouponInfo> getWhereCriteria(CouponInfoQueryRequest request) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //店铺id
            if (Objects.nonNull(request.getStoreId())) {
                predicates.add(cbuild.equal(root.get("storeId"), request.getStoreId()));
            }

            if (Objects.equals(DefaultFlag.YES, request.getIsMarketingChose())) {
                Predicate p1 = cbuild.not(cbuild.lessThan(root.get("endTime"), LocalDateTime.now()));
                Predicate p2 = cbuild.equal(root.get("rangeDayType"), RangeDayType.DAYS);
                Predicate result = cbuild.or(p1, p2);
                predicates.add(result);
            }

            if (Objects.nonNull(request.getStartTime())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                //大于或等于传入时间
                Predicate p3 = cbuild.greaterThanOrEqualTo(root.get("startTime"), request.getStartTime());
                Predicate p4 = cbuild.equal(root.get("rangeDayType"), RangeDayType.RANGE_DAY);
                Predicate resultStartTime = cbuild.and(p3, p4);
                predicates.add(resultStartTime);
            }
            if (Objects.nonNull(request.getEndTime())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                Predicate p5 = cbuild.lessThanOrEqualTo(root.get("endTime"), request.getEndTime());
                Predicate p6 = cbuild.equal(root.get("rangeDayType"), RangeDayType.RANGE_DAY);
                Predicate resultEndTime = cbuild.and(p5, p6);
                predicates.add(resultEndTime);
            }

            //批量优惠券id
            if (CollectionUtils.isNotEmpty(request.getCouponIds())) {
                predicates.add(root.get("couponId").in(request.getCouponIds()));
            }

            //是否平台优惠券 1平台 0店铺
            if (Objects.nonNull(request.getPlatformFlag())) {
                predicates.add(cbuild.equal(root.get("platformFlag"), request.getPlatformFlag()));
            }


            //使用范围
            if (Objects.nonNull(request.getScopeType())) {
                predicates.add(cbuild.equal(root.get("scopeType"), request.getScopeType()));
            }

            //模糊查询名称
            if (StringUtils.isNotEmpty(request.getLikeCouponName())) {
                predicates.add(cbuild.like(root.get("couponName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(request.getLikeCouponName().trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }

            //删除标记
            if (Objects.nonNull(request.getDelFlag())) {
                predicates.add(cbuild.equal(root.get("delFlag"), request.getDelFlag()));
            }

            if (Objects.nonNull(request.getCouponStatus())) {

                switch (request.getCouponStatus()) {
//                " AND now() >= t.begin_time AND now() <= t.end_time";
                    case STARTED://进行中
//                  predicates.add(cbuild.between(root.get("endTime"),LocalDateTime.parse(endTime,formatter),LocalDateTime.parse(endTime,formatter));
                        predicates.add(cbuild.lessThan(root.get("startTime"), LocalDateTime.now()));
                        predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"), LocalDateTime.now()));
                        predicates.add(cbuild.equal(root.get("rangeDayType"), RangeDayType.RANGE_DAY));
                        break;
                    case NOT_START://未生效
                        predicates.add(cbuild.greaterThanOrEqualTo(root.get("startTime"), LocalDateTime.now()));
                        predicates.add(cbuild.equal(root.get("rangeDayType"), RangeDayType.RANGE_DAY));
                        break;
                    case DAYS://领取生效
                        predicates.add(cbuild.equal(root.get("rangeDayType"), RangeDayType.DAYS));
                        break;
                    case ENDED://已结束
                        predicates.add(cbuild.lessThan(root.get("endTime"), LocalDateTime.now()));
                        predicates.add(cbuild.equal(root.get("rangeDayType"), RangeDayType.RANGE_DAY));
                        break;
                    default:
                        break;
                }
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }


    /**
     * 条件查询优惠券列表
     *
     * @param request
     * @return
     */
    public List<CouponInfo> queryCouponInfos(CouponInfoQueryRequest request) {
        Sort sort = request.getSort();
        if(Objects.nonNull(sort)) {
            return couponInfoRepository.findAll(this.getWhereCriteria(request), sort);
        }else {
            return couponInfoRepository.findAll(this.getWhereCriteria(request));
        }
    }

    /**
     * 分页查询优惠券列表
     *
     * @param request
     * @return
     */
    public CouponInfoPageResponse getCouponInfoPage(CouponInfoQueryRequest request) {
        //分页查询SKU信息列表
        Page<CouponInfo> couponInfos = couponInfoRepository.findAll(this.getWhereCriteria(request), request.getPageRequest());

        if (CollectionUtils.isEmpty(couponInfos.getContent())) {
            return CouponInfoPageResponse.builder().couponInfos(new MicroServicePage<>(Collections.emptyList())).build();
        }

        // 获得所有的优惠券id
        List<String> couponIds = couponInfos.getContent().stream().map(coupon -> coupon.getCouponId()).collect(Collectors.toList());
        // 查询所有优惠券的优惠券分类关系
        List<CouponCateRela> couponCateRelaList = couponCateRelaRepository.findByCouponIdIn(couponIds);



        // 查询查询所有优惠券的优惠券分类
        List<CouponCate> cateList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(couponCateRelaList)) {


            cateList = couponCateService.queryByIds(
                    couponCateRelaList.stream().map(item -> item.getCateId()).distinct().collect(Collectors.toList()));

        }
        // 查询查询所有优惠券的商品信息


        List<CouponMarketingScope> scopeList = couponMarketingScopeRepository.findByCouponIdIn(couponIds);

        List<CouponInfoVO> couponInfoVos = this.copyToCouponInfoVoNew(couponInfos.getContent(), couponCateRelaList, cateList, scopeList);

        List<CouponActivityConfig> configs = this.checkOpt(couponIds);
        //优惠券是否关联活动，0否 1是
        couponInfoVos.forEach(coupon -> {
            if (configs.stream().anyMatch(config -> config.getCouponId().equals(coupon.getCouponId()))) {
                coupon.setIsFree(DefaultFlag.NO);
            } else {
                coupon.setIsFree(DefaultFlag.YES);
            }
        });
        MicroServicePage<CouponInfoVO> page = new MicroServicePage<>(couponInfoVos, request.getPageable(), couponInfos.getTotalElements());
        return CouponInfoPageResponse.builder().couponInfos(page).build();
    }

    /**
     * 查询积分商城中积分兑换优惠券列表
     *
     * @param pointsCouponPage
     * @return
     */
    public Page<PointsCouponVO> getPointsCouponInfoPage(Page<PointsCoupon> pointsCouponPage) {
        Page<PointsCouponVO> pointsCouponVOPage = pointsCouponPage.map(entity -> {
            if (entity != null) {
                PointsCouponVO pointsCouponVO = new PointsCouponVO();
                KsBeanUtil.copyPropertiesThird(entity, pointsCouponVO);
                // 活动状态
                PointsCouponStatus pointsCouponStatus = pointsCouponService.getPointsCouponStatus(entity);
                pointsCouponVO.setPointsCouponStatus(pointsCouponStatus);
                return pointsCouponVO;
            }
            return null;
        });
        // 分页查询SKU信息列表
        List<CouponInfo> couponInfos = pointsCouponPage.getContent().stream()
                .map(PointsCoupon::getCouponInfo)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(couponInfos)) {
            return pointsCouponVOPage;
        }
        // 获得所有的优惠券id
        List<String> couponIds = couponInfos.stream().map(CouponInfo::getCouponId).collect(Collectors.toList());
        // 查询所有优惠券的优惠券分类关系
        List<CouponCateRela> couponCateRelaList = couponCateRelaRepository.findByCouponIdIn(couponIds);
        // 查询所有优惠券的优惠券分类
        List<CouponCate> cateList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(couponCateRelaList)) {
            cateList = couponCateService.queryByIds(
                    couponCateRelaList.stream().map(item -> item.getCateId()).distinct().collect(Collectors.toList()));
        }
        // 查询查询所有优惠券的商品信息
        List<CouponMarketingScope> scopeList = couponMarketingScopeRepository.findByCouponIdIn(couponIds);
        List<CouponInfoVO> couponInfoVos = this.copyToCouponInfoVo(couponInfos, couponCateRelaList, cateList, scopeList);

        List<CouponActivityConfig> configs = this.checkOpt(couponIds);
        //优惠券是否关联活动，0否 1是
        couponInfoVos.forEach(coupon -> {
            if (configs.stream().anyMatch(config -> config.getCouponId().equals(coupon.getCouponId()))) {
                coupon.setIsFree(DefaultFlag.NO);
            } else {
                coupon.setIsFree(DefaultFlag.YES);
            }
        });
        pointsCouponVOPage.forEach(pointsCouponVO -> {
            couponInfoVos.forEach(couponInfoVO -> {
                if (couponInfoVO.getCouponId().equals(pointsCouponVO.getCouponId())) {
                    pointsCouponVO.setCouponInfoVO(couponInfoVO);
                }
            });
        });
        return pointsCouponVOPage;
    }

    /**
     * 组装优惠券信息
     *
     * @param couponInfo
     * @return
     */
    public CouponInfoVO wrapperCouponDetailInfo(CouponInfo couponInfo) {
        // 获得优惠券id
        String couponId = couponInfo.getCouponId();
        // 查询优惠券的优惠券分类关系
        List<CouponCateRela> couponCateRelaList = couponCateRelaRepository.findByCouponId(couponId);
        // 查询优惠券的优惠券分类
        List<CouponCate> cateList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(couponCateRelaList)) {
            cateList = couponCateService.queryByIds(
                    couponCateRelaList.stream().map(CouponCateRela::getCateId).distinct().collect(Collectors.toList()));
        }
        // 查询所有优惠券的商品信息
        List<CouponMarketingScope> scopeList = couponMarketingScopeRepository.findByCouponId(couponId);
        List<CouponInfoVO> couponInfoVos = this.copyToCouponInfoVo(Collections.singletonList(couponInfo), couponCateRelaList, cateList, scopeList);

        List<CouponActivityConfig> configs = this.checkOpt(Collections.singletonList(couponId));
        //优惠券是否关联活动，0否 1是
        couponInfoVos.forEach(coupon -> {
            if (configs.stream().anyMatch(config -> config.getCouponId().equals(coupon.getCouponId()))) {
                coupon.setIsFree(DefaultFlag.NO);
            } else {
                coupon.setIsFree(DefaultFlag.YES);
            }
        });
        return couponInfoVos.get(0);
    }

    /**
     * CouponInfo  包装成vo
     *
     * @param couponInfoList
     * @param couponCateRelaList
     * @param cateList
     * @param scopeList
     * @return
     */
    private List<CouponInfoVO> copyToCouponInfoVo(List<CouponInfo> couponInfoList, List<CouponCateRela> couponCateRelaList, List<CouponCate> cateList, List<CouponMarketingScope> scopeList) {
        List<CouponInfoVO> couponInfoVoList = couponInfoList.stream().map(
                couponInfo -> {
                    CouponInfoResponse couponInfoResponse = new CouponInfoResponse();
                    CouponInfoVO couponInfoVo = new CouponInfoVO();
                    KsBeanUtil.copyPropertiesThird(couponInfo, couponInfoVo);
                    couponInfoResponse.setCouponInfo(couponInfoVo);
                    //设置优惠券分类id
                    couponInfoResponse.getCouponInfo().setCateIds(
                            couponCateRelaList.stream().filter(item -> item.getCouponId().equals(couponInfo.getCouponId()))
                                    .map(item -> item.getCateId()).collect(Collectors.toList())
                    );
                    //设置优惠券分类名称
                    List<String> nameList = couponInfoResponse.getCouponInfo().getCateIds().stream().map(
                            id -> {
                                String name = "";
                                for (CouponCate cate : cateList) {
                                    if (cate.getCouponCateId().equals(id)) {
                                        name = cate.getCouponCateName();
                                        return name;
                                    }
                                }
                                return name;
                            }
                    ).collect(Collectors.toList());
                    couponInfoResponse.getCouponInfo().setCateNames(nameList);
                    if (ScopeType.SKU != couponInfo.getScopeType()) {
                        this.couponDetail(couponInfoResponse, couponInfo,
                                scopeList.stream().filter(item -> item.getCouponId().equals(couponInfo.getCouponId())).collect(Collectors.toList()));
                        couponInfoVo.setScopeNames(couponInfoResponse.getCouponInfo().getScopeNames());//关联商品
                    }
                    couponInfoVo.setCateNames(couponInfoResponse.getCouponInfo().getCateNames());//关联分类
                    couponInfoVo.setCouponStatus(getCouponStatus(couponInfo));//获取优惠券状态
                    return couponInfoVo;
                }).collect(Collectors.toList());
        return couponInfoVoList;
    }

    /**
     * 复制一个优惠券信息
     *
     * @param couponId
     * @param operatorId
     * @throws SbcRuntimeException
     */
    @Transactional
    public CouponInfoVO copyCouponInfo(String couponId, String operatorId) throws SbcRuntimeException {
        //业务校验
        Optional<CouponInfo> couponInfoOptional = couponInfoRepository.findById(couponId);
        List<String> cateIds = null;
        List<String> scopeIds = null;
        //业务数据校验
        if (!couponInfoOptional.isPresent()) {
            //优惠券不存在
            throw new SbcRuntimeException(CouponErrorCode.COUPON_INFO_NOT_EXIST);
        }
        CouponInfo couponInfo = couponInfoOptional.get();
        //优惠券分类信息
        List<CouponCateRela> couponCateRelaList = couponCateRelaRepository.findByCouponId(couponInfo.getCouponId());
        if (CollectionUtils.isNotEmpty(couponCateRelaList)) {
            cateIds = couponCateRelaList.stream().map(CouponCateRela::getCateId).collect(Collectors.toList());
        }
        //商品信息：
        if (CouponType.FREIGHT_VOUCHER != couponInfo.getCouponType()) {
            List<CouponMarketingScope> scopeList = couponMarketingScopeRepository.findByCouponId(couponInfo.getCouponId());
            if (CollectionUtils.isNotEmpty(scopeList)) {
                scopeIds = scopeList.stream().map(CouponMarketingScope::getScopeId).collect(Collectors.toList());
            }
        }

        //优惠券分类
        if (CollectionUtils.isNotEmpty(cateIds) && cateIds.size() > 3) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //优惠券商品
        if (!Objects.equals(ScopeType.ALL, couponInfo.getScopeType())) {
            if (CollectionUtils.isEmpty(scopeIds)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }
        CouponInfo couponInfoNew = new CouponInfo();
        KsBeanUtil.copyPropertiesThird(couponInfo, couponInfoNew);
        couponInfoNew.setCouponId(null);
        couponInfoNew.setCreateTime(LocalDateTime.now());
        couponInfoNew.setCreatePerson(operatorId);
        //保存优惠券
        CouponInfo couponInfoCopy = couponInfoRepository.save(couponInfoNew);
        //保存优惠券关联分类
        if (CollectionUtils.isNotEmpty(cateIds)) {
            couponCateRelaRepository.saveAll(generateCouponCateList(couponInfoCopy, cateIds));
        }
        //保存优惠券关联商品
        if (!Objects.equals(ScopeType.ALL, couponInfoCopy.getScopeType())) {
            couponMarketingScopeRepository.saveAll(generateCouponScopeList(couponInfoCopy, scopeIds));
        }
        CouponInfoVO couponInfoVO = couponInfoMapper.couponInfoToCouponInfoVO(couponInfoCopy);
        couponInfoVO.setCateIds(cateIds);
        couponInfoVO.setScopeIds(scopeIds);
        return couponInfoVO;
    }

    /**
     * 新增优惠券信息
     *
     * @param request
     * @throws SbcRuntimeException
     */
    @Transactional
    public CouponInfoVO addCouponInfo(CouponInfoAddRequest request) throws SbcRuntimeException {
        // 校验优惠券分类
        this.checkCouponCate(request.getCateIds(), request.getPlatformFlag());
        //保存优惠券
        CouponInfo couponInfo = couponInfoRepository.save(generateCoupon(request));
        //保存优惠券关联分类
        List<String> cateIds = request.getCateIds();
        if (CollectionUtils.isNotEmpty(cateIds)) {
           couponCateRelaRepository.saveAll(generateCouponCateList(couponInfo, cateIds));
        }
        //保存优惠券关联商品
        List<String> scopeIds = request.getScopeIds();
        if (CouponType.FREIGHT_VOUCHER != request.getCouponType()) {
            if (!Objects.equals(ScopeType.ALL, request.getScopeType())) {
                couponMarketingScopeRepository.saveAll(generateCouponScopeList(couponInfo, request.getScopeIds()));
            }
        }
        CouponInfoVO couponInfoVO = couponInfoMapper.couponInfoToCouponInfoVO(couponInfo);
        couponInfoVO.setCateIds(cateIds);
        couponInfoVO.setScopeIds(scopeIds);
        return couponInfoVO;
    }

    private void checkCouponCate(List<String> cateList, DefaultFlag platformFlag) {
        if (CollectionUtils.isEmpty(cateList)) {
            return;
        }
        List<CouponCate> couponCateList = couponCateService.queryByIds(cateList);
        if (cateList.size() == couponCateList.size()) {
            //如果是商家端的，要排除掉平台专用分类
            if (DefaultFlag.NO.equals(platformFlag)) {
                couponCateList = couponCateList.stream().filter(i ->
                        DefaultFlag.NO.equals(i.getOnlyPlatformFlag())
                ).collect(Collectors.toList());
            }
        } else {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<CouponCate> newCouponCateList = couponCateList.stream().filter(i -> DeleteFlag.NO == i.getDelFlag()).collect(Collectors.toList());
        //筛选出已经被删除的id
        List<String> errorCateIds = cateList.stream().filter(id ->
                newCouponCateList.stream().noneMatch(cate -> cate.getCouponCateId().equals(id))
        ).collect(Collectors.toList());
        if (!errorCateIds.isEmpty()) {
            throw new SbcRuntimeException(errorCateIds, CouponErrorCode.COUPON_CATE_NOT_EXIST);
        }
    }

    /**
     * 编辑优惠券信息
     *
     * @param request
     * @throws SbcRuntimeException
     */
    @Transactional
    public CouponInfoVO modifyCouponInfo(CouponInfoModifyRequest request) throws SbcRuntimeException {

        //业务校验 1.未被关联至活动的优惠券支持编辑
        CouponInfo couponInfo = couponInfoRepository.findById(request.getCouponId()).orElseThrow(() ->
                new SbcRuntimeException(CouponErrorCode.COUPON_INFO_NOT_EXIST));

        List<String> ids = new ArrayList<>();
        ids.add(couponInfo.getCouponId());
        List<CouponActivityConfig> configs = checkOpt(ids);
        if (!configs.isEmpty()) {
            //优惠券已经绑定活动
            throw new SbcRuntimeException(CouponErrorCode.COUPON_INFO_IN_USE);
        }
        // 校验优惠券分类
        this.checkCouponCate(request.getCateIds(), couponInfo.getPlatformFlag());
        //全部删除关联优惠券商品
        if (CouponType.FREIGHT_VOUCHER != request.getCouponType()) {
            couponMarketingScopeRepository.deleteByCouponId(couponInfo.getCouponId());
        }
        //保存优惠券
        couponInfo = couponInfoRepository.save(generateCouponByModify(request, couponInfo));

        //删除全部关联优惠券分类
        couponCateRelaRepository.deleteByCouponId(couponInfo.getCouponId());
        //保存优惠券关联分类
        List<String> cateIds = request.getCateIds();
        if (CollectionUtils.isNotEmpty(cateIds)) {
            //保存优惠券关联分类
            couponCateRelaRepository.saveAll(generateCouponCateList(couponInfo, cateIds));
        }
        //保存优惠券关联商品
        List<String> scopeIds = request.getScopeIds();
        if (CouponType.FREIGHT_VOUCHER != request.getCouponType()) {
            if (!Objects.equals(ScopeType.ALL, request.getScopeType())) {
                couponMarketingScopeRepository.saveAll(generateCouponScopeList(couponInfo, scopeIds));
            }
        }

        CouponInfoVO couponInfoVO = couponInfoMapper.couponInfoToCouponInfoVO(couponInfo);
        couponInfoVO.setCateIds(cateIds);
        couponInfoVO.setScopeIds(scopeIds);
        return couponInfoVO;
    }

    /**
     * 优惠券编辑删除验证
     *
     * @param couponIds
     * @return
     */
    public List<CouponActivityConfig> checkOpt(List<String> couponIds) {
        //只有未关联活动的优惠券支持删除
        return couponActivityConfigRepository.findByCouponIds(couponIds);
    }


    /**
     * 删除优惠券
     *
     * @param couponId
     * @param operatorId
     */
    @Transactional
    public void deleteCoupon(String couponId, String operatorId) {
        List<String> ids = new ArrayList<>();
        ids.add(couponId);
        List<CouponActivityConfig> configList = checkOpt(ids);
        if (!configList.isEmpty()) {
            //优惠券已经绑定活动
            throw new SbcRuntimeException(CouponErrorCode.COUPON_INFO_IN_USE);
        }
        //保存优惠券
        couponInfoRepository.deleteCoupon(couponId, operatorId);
    }

    /**
     * 查询优惠券详情（包含优惠券+优惠券分类+优惠券商品信息）
     *
     * @param couponId
     * @return
     */
    public CouponInfoResponse queryCouponInfoDetail(String couponId) {
        CouponInfoResponse couponInfoResponse = new CouponInfoResponse();
        CouponInfo couponInfo = couponInfoRepository.findById(couponId).get();
        CouponInfoVO couponInfoVo = new CouponInfoVO();
        KsBeanUtil.copyPropertiesThird(couponInfo, couponInfoVo);
        couponInfoResponse.setCouponInfo(couponInfoVo);
        List<CouponCateRela> couponCateRelaList = couponCateRelaRepository.findByCouponId(couponInfo.getCouponId());
        //优惠券分类信息
        couponCate(couponInfoResponse, couponCateRelaList);
        //商品信息：
        List<CouponMarketingScope> scopeList = couponMarketingScopeRepository.findByCouponId(couponInfo.getCouponId());
        //组装商品信息
        couponDetail(couponInfoResponse, couponInfo, scopeList);
        return couponInfoResponse;
    }

    /**
     * 查询优惠券信息（优惠券单表信息）
     *
     * @param couponId
     * @return
     */
    public CouponInfoVO queryCouponInfo(String couponId) {
        CouponInfo couponInfo = couponInfoRepository.findById(couponId).orElse(null);
        if (Objects.isNull(couponInfo)) {
            return null;
        }
        CouponInfoVO couponInfoVo = new CouponInfoVO();
        KsBeanUtil.copyPropertiesThird(couponInfo, couponInfoVo);
        return couponInfoVo;
    }


    /**
     * 优惠券详情
     * 优惠券分类信息
     * 商品信息：全部、指定品牌、指定分类、指定商品
     *
     * @param couponInfoResponse
     * @param couponInfo
     * @param scopeList
     */
    private void couponDetail(CouponInfoResponse couponInfoResponse, CouponInfo couponInfo, List<CouponMarketingScope> scopeList) {
        // 运费券没有商品信息
        if (CouponType.FREIGHT_VOUCHER == couponInfo.getCouponType()) {
            return;
        }
        //品牌分类
        if (ScopeType.BRAND.equals(couponInfo.getScopeType())) {
            //营销活动包含的所有品牌Id
            List<Long> brandsIds = scopeList.stream().map(scope -> Long.valueOf(scope.getScopeId())).collect(Collectors.toList());
            couponBrandDetail(couponInfoResponse, couponInfo, brandsIds);
        }
        //店铺分类
        if (ScopeType.STORE_CATE.equals(couponInfo.getScopeType())) {
            //营销活动包含的所有商品Id
            List<Long> cateIds = scopeList.stream().map(scope -> Long.valueOf(scope.getScopeId())).collect(Collectors.toList());
            couponStoreCateDetail(couponInfoResponse, couponInfo, cateIds);
        }
        //平台分类
        if (ScopeType.BOSS_CATE.equals(couponInfo.getScopeType())) {
            //营销活动包含的所有商品Id
            List<Long> cateIds = scopeList.stream().map(scope -> Long.valueOf(scope.getScopeId())).collect(Collectors.toList());
            couponBossCateDetail(couponInfoResponse, couponInfo, cateIds);
        }
        //店铺可用
        if (ScopeType.SKU.equals(couponInfo.getScopeType())) {
            //营销活动包含的所有商品Id
            List<String> goodsInfoIds = scopeList.stream().map(CouponMarketingScope::getScopeId).collect(Collectors.toList());
            couponGoodsDetail(couponInfoResponse, couponInfo, goodsInfoIds);
        }
    }

    /**
     * 优惠券分类
     *
     * @param couponInfoResponse
     * @param couponCateRelaList
     */
    private void couponCate(CouponInfoResponse couponInfoResponse, List<CouponCateRela> couponCateRelaList) {
        //组装商品信息
        if (CollectionUtils.isNotEmpty(couponCateRelaList)) {
            //营销活动包含的所有商品Id
            List<String> couponCateRelas = couponCateRelaList.stream().map(CouponCateRela::getCateId).collect(Collectors.toList());
            List<CouponCateResponse> couponCateList = couponCateService.listCouponCateLimitThreeByCouponCateIds(couponCateRelas);
            couponInfoResponse.getCouponInfo().setCateNames(couponCateList.stream().map(couponCate -> couponCate.getCouponCateName()).collect(Collectors.toList()));
            couponInfoResponse.getCouponInfo().setCateIds(couponCateList.stream().map(couponCate -> couponCate.getCouponCateId()).collect(Collectors.toList()));

        }
    }

    /**
     * 优惠券商品信息
     *
     * @param couponInfoResponse
     * @param couponInfo
     * @param goodsInfoIds
     */
    private void couponGoodsDetail(CouponInfoResponse couponInfoResponse, CouponInfo couponInfo, List<String> goodsInfoIds) {
        //组装商品信息
        if (CollectionUtils.isNotEmpty(goodsInfoIds)) {
            GoodsInfoViewPageRequest queryRequest = new GoodsInfoViewPageRequest();
            //FIXME 营销是平铺展示，但是数量达到一定层级，还是需要分页，先暂时这么控制
            queryRequest.setPageSize(10000);
            queryRequest.setStoreId(couponInfo.getStoreId());
            queryRequest.setAddedFlag(AddedFlag.YES.toValue());//上架
            queryRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
            queryRequest.setAuditStatus(CheckStatus.CHECKED);//已审核
            queryRequest.setGoodsInfoIds(goodsInfoIds);
            GoodsInfoViewPageResponse goodsInfoResponse = goodsInfoQueryProvider.pageView(queryRequest).getContext();
            couponInfoResponse.setGoodsList(GoodsInfoResponse.builder()
                    .goodsInfoPage(goodsInfoResponse.getGoodsInfoPage())
                    .brands(goodsInfoResponse.getBrands())
                    .cates(goodsInfoResponse.getCates())
                    .goodses(goodsInfoResponse.getGoodses())
                    .build());
            couponInfoResponse.getCouponInfo().setScopeNames(goodsInfoResponse.getGoodsInfoPage().getContent().stream().map(goodsInfo -> goodsInfo.getGoodsInfoName()).collect(Collectors.toList()));
            couponInfoResponse.getCouponInfo().setScopeIds(goodsInfoIds);
        }
    }

    /**
     * 优惠券商品分类信息
     *
     * @param couponInfoResponse
     * @param couponInfo
     * @param cateIds
     */
    private void couponStoreCateDetail(CouponInfoResponse couponInfoResponse, CouponInfo couponInfo, List<Long> cateIds) {

        //组装商品信息
        if (CollectionUtils.isNotEmpty(cateIds)) {
            //店铺分类
            if (Objects.equals(couponInfo.getPlatformFlag(), DefaultFlag.NO)) {
                List<StoreCateVO> storeCateList = storeCateQueryProvider.listByIds(new StoreCateListByIdsRequest(cateIds)).getContext().getStoreCateVOList();
                storeCateList = storeCateList.stream().filter(cate -> cate.getDelFlag() == DeleteFlag.NO).collect(Collectors.toList());
                List<StoreCateVO> newCateList = storeCateList;
                //只显示父级的节点的名称
                List<StoreCateVO> nameGoodsCateList = storeCateList.stream().filter(item -> newCateList.stream().noneMatch(i -> i.getStoreCateId().equals(item.getCateParentId()))).collect(Collectors.toList());

                couponInfoResponse.getCouponInfo().setScopeNames(nameGoodsCateList.stream().map(cate -> cate.getCateName()).collect(Collectors.toList()));
                couponInfoResponse.getCouponInfo().setScopeIds(cateIds.stream().map(id -> String.valueOf(id)).collect(Collectors.toList()));
            }
        }
    }

    /**
     * 优惠券商品分类信息
     *
     * @param couponInfoResponse
     * @param couponInfo
     * @param cateIds
     */
    private void couponBossCateDetail(CouponInfoResponse couponInfoResponse, CouponInfo couponInfo, List<Long> cateIds) {

        //组装商品信息
        if (CollectionUtils.isNotEmpty(cateIds)) {
            //平台分类
            if (Objects.equals(couponInfo.getPlatformFlag(), DefaultFlag.YES)) {
                GoodsCateListByConditionRequest request = new GoodsCateListByConditionRequest();
                request.setCateIds(cateIds);
                request.setDelFlag(DeleteFlag.NO.toValue());
                final List<GoodsCateVO> cateList = goodsCateQueryProvider.listByCondition(request).getContext().getGoodsCateVOList();
                //只显示父级的节点的名称
                List<GoodsCateVO> nameGoodsCateList = cateList.stream().filter(item -> cateList.stream().noneMatch(i -> i.getCateId().equals(item.getCateParentId()))).collect(Collectors.toList());
                couponInfoResponse.getCouponInfo().setScopeNames(nameGoodsCateList.stream().map(GoodsCateVO::getCateName).collect(Collectors.toList()));
                couponInfoResponse.getCouponInfo().setScopeIds(cateIds.stream().map(id -> String.valueOf(id)).collect(Collectors.toList()));
            }
        }
    }

    /**
     * 优惠券商品品牌信息
     *
     * @param couponInfoResponse
     * @param couponInfo
     * @param brandsIds
     */
    private void couponBrandDetail(CouponInfoResponse couponInfoResponse, CouponInfo couponInfo, List<Long> brandsIds) {
        //优惠券品牌信息
        if (CollectionUtils.isNotEmpty(brandsIds)) {
            if (DefaultFlag.NO.equals(couponInfo.getPlatformFlag())) {
                //获取店铺签约的品牌
                ContractBrandListRequest brandRequest = new ContractBrandListRequest();
                brandRequest.setGoodsBrandIds(brandsIds);
                brandRequest.setStoreId(couponInfo.getStoreId());
                //获取店铺签约的品牌
                List<ContractBrandVO> brandList = contractBrandQueryProvider.list(brandRequest).getContext().getContractBrandVOList();
                //筛选出店铺签约的品牌信息
                brandList = brandList.stream().filter(item ->
                        brandsIds.stream().anyMatch(i ->
                                i.equals(item.getGoodsBrand().getBrandId())
                        )
                ).collect(Collectors.toList());
                couponInfoResponse.getCouponInfo().setScopeNames(brandList.stream().map(item -> item.getGoodsBrand().getBrandName()).collect(Collectors.toList()));
            } else {
                //获取平台的品牌
                GoodsBrandListRequest brandRequest = new GoodsBrandListRequest();
                brandRequest.setDelFlag(DeleteFlag.NO.toValue());
                brandRequest.setBrandIds(brandsIds);
                List<GoodsBrandVO> brandList = goodsBrandQueryProvider.list(brandRequest).getContext().getGoodsBrandVOList();
                couponInfoResponse.getCouponInfo().setScopeNames(brandList.stream().map(GoodsBrandVO::getBrandName).collect(Collectors.toList()));
            }
            couponInfoResponse.getCouponInfo().setScopeIds(brandsIds.stream().map(id -> String.valueOf(id)).collect(Collectors.toList()));
        }

    }

    /**
     * 促销范围Ids转List<CouponMarketingScope>
     *
     * @param couponInfo
     * @param scopeIds
     * @return
     */
    public List<CouponMarketingScope> generateCouponScopeList(CouponInfo couponInfo, List<String> scopeIds) {
        if (Objects.equals(ScopeType.BOSS_CATE, couponInfo.getScopeType()) || Objects.equals(ScopeType.STORE_CATE, couponInfo.getScopeType())) {
            Map<String, Integer> cateGrades = getCouponCateGrade(couponInfo, scopeIds);
            return generateCouponScopeListForCate(couponInfo, scopeIds, cateGrades);
        }
        return scopeIds.stream().map((scopeId) -> {
            CouponMarketingScope scope = new CouponMarketingScope();
            scope.setCouponId(couponInfo.getCouponId());
            scope.setScopeType(couponInfo.getScopeType());
            scope.setScopeId(scopeId);
            return scope;
        }).collect(Collectors.toList());
    }

    /**
     * 促销范围Ids转List如果为分类填充分类层级
     *
     * @param couponInfo
     * @param scopeIds
     * @param cateGrades
     * @return
     */
    public List<CouponMarketingScope> generateCouponScopeListForCate(CouponInfo couponInfo, List<String> scopeIds, Map<String, Integer> cateGrades) {
        return scopeIds.stream().map((scopeId) -> {
            CouponMarketingScope scope = new CouponMarketingScope();
            scope.setCouponId(couponInfo.getCouponId());
            scope.setScopeId(scopeId);
            scope.setScopeType(couponInfo.getScopeType());
            scope.setCateGrade(cateGrades.get(scopeId));
            return scope;
        }).collect(Collectors.toList());
    }

    /**
     * 优惠券分类Ids转List<CouponMarketingScope>
     *
     * @param couponInfo
     * @param cateIds
     * @return
     */
    public List<CouponCateRela> generateCouponCateList(CouponInfo couponInfo, List<String> cateIds) {
        return cateIds.stream().map((cateId) -> {
            CouponCateRela cateRela = new CouponCateRela();
            cateRela.setPlatformFlag(couponInfo.getPlatformFlag());
            cateRela.setCouponId(couponInfo.getCouponId());
            cateRela.setCateId(cateId);
            return cateRela;
        }).collect(Collectors.toList());
    }


    /**
     * 获取优惠券状态
     *
     * @param couponInfo
     * @return
     */
    public CouponStatus getCouponStatus(CouponInfo couponInfo) {
        if (Objects.equals(RangeDayType.DAYS, couponInfo.getRangeDayType())) {
            return CouponStatus.DAYS;
        } else {
            if (couponInfo.getStartTime() != null && couponInfo.getEndTime() != null) {
                if (LocalDateTime.now().isBefore(couponInfo.getStartTime())) {
                    return CouponStatus.NOT_START;
                } else if (LocalDateTime.now().isAfter(couponInfo.getEndTime())) {
                    return CouponStatus.ENDED;
                } else {
                    return CouponStatus.STARTED;
                }
            }
        }
        return null;
    }

    /**
     * 根据分类id获取分类层级
     *
     * @param couponInfo
     * @param scopeIds
     * @return
     */
    public Map<String, Integer> getCouponCateGrade(CouponInfo couponInfo, List<String> scopeIds) {
        Map<String, Integer> cateGrades = new HashMap<>();
        //处理分类层级
        if (Objects.equals(ScopeType.BOSS_CATE, couponInfo.getScopeType())) {
            List<Long> bossCateIds = scopeIds.stream().map(scope -> Long.valueOf(scope)).collect(Collectors.toList());
            List<GoodsCateVO> cateList = goodsCateQueryProvider.getByIds(new GoodsCateByIdsRequest(bossCateIds)).getContext().getGoodsCateVOList();
            cateList.forEach(goodsCate -> cateGrades.put(goodsCate.getCateId().toString(), goodsCate.getCateGrade()));

        }
        if (Objects.equals(ScopeType.STORE_CATE, couponInfo.getScopeType())) {
            //营销活动包含的所有商品Id
            List<Long> storeCateIds = scopeIds.stream().map(scope -> Long.valueOf(scope)).collect(Collectors.toList());
            //店铺分类
            if (Objects.equals(couponInfo.getPlatformFlag(), DefaultFlag.NO)) {
                List<StoreCateVO> storeCateList = storeCateQueryProvider.listByIds(new StoreCateListByIdsRequest(storeCateIds)).getContext().getStoreCateVOList();
                storeCateList.forEach(goodsCate -> cateGrades.put(goodsCate.getStoreCateId().toString(), goodsCate.getCateGrade()));
            }
        }
        return cateGrades;
    }

    /**
     * 根据优惠券id获取优惠券实体
     *
     * @param couponInfoId
     * @return
     */
    public CouponInfo getCouponInfoById(String couponInfoId) {
        return couponInfoRepository.findById(couponInfoId).get();
    }

    public CouponInfo findByCouponIdAndStoreIdAndDelFlag(String couponInfoId, Long storeId) {
        return couponInfoRepository.findByCouponIdAndStoreIdAndDelFlag(couponInfoId,storeId,DeleteFlag.NO).get();
    }


    /**
     * 优惠券对象
     *
     * @return
     */
    private CouponInfo generateCoupon(CouponInfoAddRequest request) {
        CouponInfo couponInfo = new CouponInfo();
        couponInfo.setCouponName(request.getCouponName());
        couponInfo.setRangeDayType(request.getRangeDayType());
        if (request.getRangeDayType().equals(RangeDayType.RANGE_DAY)) {
            couponInfo.setStartTime(request.getStartTime());
            couponInfo.setEndTime(request.getEndTime());
        } else {
            couponInfo.setEffectiveDays(request.getEffectiveDays());
        }
        couponInfo.setFullBuyType(request.getFullBuyType());
        couponInfo.setFullBuyPrice(request.getFullBuyPrice());
        couponInfo.setDenomination(request.getDenomination());
        couponInfo.setPlatformFlag(request.getPlatformFlag());
        couponInfo.setStoreId(request.getStoreId());
        couponInfo.setScopeType(request.getScopeType());
        couponInfo.setCouponDesc(request.getCouponDesc());
        couponInfo.setCouponType(request.getCouponType());
        couponInfo.setCreatePerson(request.getCreatePerson());
        couponInfo.setCreateTime(LocalDateTime.now());
        couponInfo.setDelFlag(DeleteFlag.NO);
        return couponInfo;
    }

    /**
     * 优惠券对象
     *
     * @return
     */
    public CouponInfo generateCouponByModify(CouponInfoModifyRequest request, CouponInfo couponInfo) {
        couponInfo.setCouponName(request.getCouponName());
        couponInfo.setRangeDayType(request.getRangeDayType());
        if (request.getRangeDayType().equals(RangeDayType.RANGE_DAY)) {
            couponInfo.setStartTime(request.getStartTime());
            couponInfo.setEndTime(request.getEndTime());
            couponInfo.setEffectiveDays(null);
        } else {
            couponInfo.setEffectiveDays(request.getEffectiveDays());
            couponInfo.setStartTime(null);
            couponInfo.setEndTime(null);
        }
        couponInfo.setFullBuyType(request.getFullBuyType());
        couponInfo.setFullBuyPrice(request.getFullBuyPrice());
        couponInfo.setDenomination(request.getDenomination());
        couponInfo.setScopeType(request.getScopeType());
        couponInfo.setCouponDesc(request.getCouponDesc());
        couponInfo.setUpdatePerson(request.getUpdatePerson());
        couponInfo.setUpdateTime(LocalDateTime.now());
        return couponInfo;
    }

    public List<CouponInfo> queryByIds(List<String> couponIds) {
        return couponInfoRepository.queryByIds(couponIds);
    }

    /**
     * CouponInfo  包装成vo
     *
     * @param couponInfoList
     * @param couponCateRelaList
     * @param cateList
     * @param marketingScopeList
     * @return
     */
    private List<CouponInfoVO> copyToCouponInfoVoNew(List<CouponInfo> couponInfoList, List<CouponCateRela> couponCateRelaList, List<CouponCate> cateList, List<CouponMarketingScope> marketingScopeList) {
        Map<String, List<String>> scopeNamesMap = this.couponDetail(couponInfoList,marketingScopeList);
        List<CouponInfoVO> couponInfoVoList = couponInfoList.stream().map(
                couponInfo -> {
                    CouponInfoResponse couponInfoResponse = new CouponInfoResponse();
                    CouponInfoVO couponInfoVo = new CouponInfoVO();
                    KsBeanUtil.copyPropertiesThird(couponInfo, couponInfoVo);
                    couponInfoResponse.setCouponInfo(couponInfoVo);
                    //设置优惠券分类id
                    List<String> cateIds =  couponCateRelaList.stream().filter(item -> item.getCouponId().equals(couponInfo.getCouponId()))
                            .map(item -> item.getCateId()).collect(Collectors.toList());
                    couponInfoResponse.getCouponInfo().setCateIds(cateIds);
                    //设置优惠券分类名称
                    List<String> nameList = cateIds.stream().map(
                            id -> {
                                String name = "";
                                for (CouponCate cate : cateList) {
                                    if (cate.getCouponCateId().equals(id)) {
                                        name = cate.getCouponCateName();
                                        return name;
                                    }
                                }
                                return name;
                            }
                    ).collect(Collectors.toList());
                    couponInfoResponse.getCouponInfo().setCateNames(nameList);
                    if (ScopeType.SKU != couponInfo.getScopeType() && CouponType.FREIGHT_VOUCHER != couponInfo.getCouponType()) {
////                        this.couponDetail(couponInfoResponse, couponInfo,couponMarketingScopeMap.get(couponInfo.getCouponId()));
//                        List<CouponMarketingScopeDTO> scopeList = couponMarketingScopeMap.get(couponInfo.getCouponId());
////                        if (CouponType.FREIGHT_VOUCHER == couponInfo.getCouponType()) {
//                           // return;
////                        }
//                        //品牌分类
//                        if (ScopeType.BRAND.equals(couponInfo.getScopeType())) {
//                            //营销活动包含的所有品牌Id
//                            List<Long> brandsIds = scopeList.stream().map(scope -> Long.valueOf(scope.getScopeId())).collect(Collectors.toList());
//                            couponBrandDetail(couponInfoResponse, couponInfo, brandsIds);
//                        }
//                        //店铺分类
//                        if (ScopeType.STORE_CATE.equals(couponInfo.getScopeType())) {
//                            //营销活动包含的所有商品Id
//                            List<Long> cateIdsFromStore = scopeList.stream().map(scope -> Long.valueOf(scope.getScopeId())).collect(Collectors.toList());
//                            couponStoreCateDetail(couponInfoResponse, couponInfo, cateIdsFromStore);
//                        }
//                        //平台分类
//                        if (ScopeType.BOSS_CATE.equals(couponInfo.getScopeType())) {
//                            //营销活动包含的所有商品Id
//                            List<Long> cateIdsFromBoss = scopeList.stream().map(scope -> Long.valueOf(scope.getScopeId())).collect(Collectors.toList());
//                            couponBossCateDetail(couponInfoResponse, couponInfo, cateIdsFromBoss);
//                        }
//                        //店铺可用
//                        if (ScopeType.SKU.equals(couponInfo.getScopeType())) {
//                            //营销活动包含的所有商品Id
//                            List<String> goodsInfoIds = scopeList.stream().map(CouponMarketingScopeDTO::getScopeId).collect(Collectors.toList());
//                            couponGoodsDetail(couponInfoResponse, couponInfo, goodsInfoIds);
//                        }

                        couponInfoVo.setScopeNames(scopeNamesMap.get(couponInfo.getCouponId()));//关联商品
                    }
                    couponInfoVo.setCateNames(nameList);//关联分类
                    couponInfoVo.setCouponStatus(getCouponStatus(couponInfo));//获取优惠券状态
                    return couponInfoVo;
                }).collect(Collectors.toList());
        return couponInfoVoList;
    }

    public Map<String, List<String>> couponDetail(List<CouponInfo> couponInfoList, List<CouponMarketingScope> marketingScopeList){
        Map<String, List<CouponMarketingScopeDTO>> couponMarketingScopeMap = marketingScopeList.stream().map(m -> {
            CouponMarketingScopeDTO couponMarketingScopeDTO = new CouponMarketingScopeDTO();
            couponMarketingScopeDTO.setCouponId(m.getCouponId());
            couponMarketingScopeDTO.setScopeId(m.getScopeId());
            return couponMarketingScopeDTO;
        }).collect(Collectors.groupingBy(CouponMarketingScopeDTO::getCouponId));
        List<CouponInfoForScopeNamesDTO> dtoList =  couponInfoList.stream().map(couponInfo -> {
            CouponInfoForScopeNamesDTO couponInfoForScopeNamesDTO = new CouponInfoForScopeNamesDTO();
            couponInfoForScopeNamesDTO.setCouponId(couponInfo.getCouponId());
            couponInfoForScopeNamesDTO.setCouponType(com.wanmi.sbc.goods.bean.enums.CouponType.fromValue(couponInfo.getCouponType().toValue()));
            couponInfoForScopeNamesDTO.setScopeType(com.wanmi.sbc.goods.bean.enums.ScopeType.fromValue(couponInfo.getScopeType().toValue()));
            couponInfoForScopeNamesDTO.setPlatformFlag(couponInfo.getPlatformFlag());
            couponInfoForScopeNamesDTO.setStoreId(couponInfo.getStoreId());
            return couponInfoForScopeNamesDTO;
        }).collect(Collectors.toList());

        List<CouponInfoForScopeNamesVO> couponInfoForScopeNamesVOS = goodsCateQueryProvider.couponDetail(new GoodsCateListCouponDetailRequest(dtoList,couponMarketingScopeMap)).getContext().getVoList();
       return couponInfoForScopeNamesVOS.stream().collect(Collectors.toMap(CouponInfoForScopeNamesVO::getCouponId,CouponInfoForScopeNamesVO::getScopeNames));

    }

    /**
     * 分页查询优惠券列表
     *
     * @param request
     * @return
     */
    public List<CouponInfoVO> page(CouponInfoListByPageRequest request) {

        List<String> couponIds = CollectionUtils.isNotEmpty(request.getCouponIds()) ? request.getCouponIds() : couponInfoRepository.listByPage(request.getPageRequest());

        List<CouponInfo> couponInfoList = couponInfoRepository.findAllById(couponIds);

        if (CollectionUtils.isEmpty(couponInfoList)) {
            return Lists.newArrayList();
        }

        List<CouponCateRela> couponCateRelaList = couponCateRelaRepository.findByCouponIdIn(couponIds);
        Map<String, List<CouponCateRela>> cateIdsMap = couponCateRelaList.stream().collect(Collectors.groupingBy(CouponCateRela::getCouponId));

        List<CouponMarketingScope> scopeList = couponMarketingScopeRepository.findByCouponIdIn(couponIds);
        Map<String, List<CouponMarketingScope>> scopeIdsMap = scopeList.stream().collect(Collectors.groupingBy(CouponMarketingScope::getCouponId));

        List<CouponInfoVO> couponInfoVOS = couponInfoMapper.couponInfoToCouponInfoVO(couponInfoList);
        couponInfoVOS.stream().forEach(c -> {
            List<CouponCateRela> cateRelaList = cateIdsMap.get(c.getCouponId());
            if (CollectionUtils.isNotEmpty(cateRelaList)){
                c.setCateIds(cateRelaList.stream().map(CouponCateRela::getCateId).collect(Collectors.toList()));
            }
            List<CouponMarketingScope> scopes = scopeIdsMap.get(c.getCouponId());
            if (CollectionUtils.isNotEmpty(scopes)){
                c.setScopeIds(scopes.stream().map(CouponMarketingScope::getScopeId).collect(Collectors.toList()));
            }
        });
        return couponInfoVOS;
    }

}
