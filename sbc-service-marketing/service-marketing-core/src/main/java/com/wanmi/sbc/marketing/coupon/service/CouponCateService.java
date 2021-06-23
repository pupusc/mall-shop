package com.wanmi.sbc.marketing.coupon.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCateAddRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCateDeleteRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCateIsOnlyPlatformRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCateModifyRequest;
import com.wanmi.sbc.marketing.bean.constant.Constant;
import com.wanmi.sbc.marketing.bean.constant.CouponErrorCode;
import com.wanmi.sbc.marketing.bean.vo.CouponCateSortVO;
import com.wanmi.sbc.marketing.coupon.model.root.CouponCate;
import com.wanmi.sbc.marketing.coupon.model.root.CouponCateRela;
import com.wanmi.sbc.marketing.coupon.repository.CouponCateRelaRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponCateRepository;
import com.wanmi.sbc.marketing.coupon.request.CouponCateSortRequest;
import com.wanmi.sbc.marketing.coupon.response.CouponCateResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CouponCateService {

    @Autowired
    private CouponCateRepository couponCateRepository;

    @Autowired
    private CouponCateRelaRepository couponCateRelaRepository;

    @Autowired
    private CouponCacheService couponCacheService;

    /**
     * 查询优惠券分类列表提供给优惠券使用, 最多可以批量查询3个
     *
     * @return
     */
    public List<CouponCateResponse> listCouponCateLimitThreeByCouponCateIds(List<String> couponCateIds) {
        // 优惠券分类Id不能为空
        if (CollectionUtils.isEmpty(couponCateIds)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        } else if (CollectionUtils.isNotEmpty(couponCateIds) &&
                couponCateIds.size() > Constant.MAX_CATE_COUNT_FOR_COUPON) {
            // 查询的优惠券分类Id数量不能超过3个
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<CouponCate> source = couponCateRepository.listCouponCateLimitThreeByCouponCateIds(couponCateIds);
        List<CouponCateResponse> target = source.stream().map(cate -> {
            CouponCateResponse response = new CouponCateResponse();
            KsBeanUtil.copyPropertiesThird(cate, response);
            return response;
        }).collect(Collectors.toList());
        return target;
    }

    /**
     *
     *
     * @return
     */
    public List<CouponCateResponse> listCouponCate() {
        List<CouponCate> source = couponCateRepository.listCouponCate();
        if (CollectionUtils.isEmpty(source)) {
            return new ArrayList<>();
        }
        List<CouponCateResponse> target = source.stream().map(cate -> {
            CouponCateResponse response = new CouponCateResponse();
            KsBeanUtil.copyPropertiesThird(cate, response);
            return response;
        }).collect(Collectors.toList());
        return target;
    }

    /**
     * 根据优惠券分类Id查询单个优惠券分类
     *
     * @param couponCateId
     * @return
     */
    public CouponCateResponse getCouponCateByCouponCateId(String couponCateId) {
        if (StringUtils.isBlank(couponCateId)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        CouponCate cate = couponCateRepository.getCouponCateByCouponCateId(couponCateId);
        if (Objects.isNull(cate)) {
            throw new SbcRuntimeException(CouponErrorCode.COUPON_CATE_NOT_EXIST);
        }
        CouponCateResponse response = new CouponCateResponse();
        KsBeanUtil.copyPropertiesThird(cate, response);
        return response;
    }

    /**
     * 删除优惠券分类
     *
     * @param request
     */
    @Transactional
    public void deleteCouponCateByCouponCateId(CouponCateDeleteRequest request) {
        //1、删除分类
        if (StringUtils.isBlank(request.getCouponCateId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        couponCateRepository.deleteCouponCateByCouponCateId(request.getCouponCateId(),
                request.getDelPerson(),
                request.getDelTime());
        //2、删除分类关系表
        couponCateRelaRepository.deleteByCateId(request.getCouponCateId());
        //刷新优惠券缓存
        couponCacheService.updateCateRelaCache(request.getCouponCateId(), true);
    }

    /**
     * 新增优惠券分类
     *
     * @param request
     */
    @Transactional
    public void addCouponCate(CouponCateAddRequest request) {
        List<CouponCate> cates = couponCateRepository.listCouponCate();
        if (CollectionUtils.isNotEmpty(cates)) {
            // 优惠券分类数量不能超过30
            if (cates.size() >= Constant.MAX_CATE_COUNT) {
                throw new SbcRuntimeException(CouponErrorCode.COUPON_CATE_ALREADY_MAX);
            }
            // 优惠券名称不能已经存在
            if (cates.stream().anyMatch(cate ->
                    StringUtils.equals(request.getCouponCateName(), cate.getCouponCateName()))) {
                throw new SbcRuntimeException(CouponErrorCode.COUPON_CATE_NAME_EXIST);
            }
        }
        CouponCate cate = new CouponCate();
        KsBeanUtil.copyProperties(request, cate);
        cate.setDelFlag(DeleteFlag.NO);
        cate.setCateSort(Constant.MIN_CATE_SORT);
        cate.setOnlyPlatformFlag(DefaultFlag.NO);
        couponCateRepository.save(cate);
    }

    /**
     * 修改优惠券分类
     *
     * @param request
     * @return
     */
    @Transactional
    public CouponCateResponse modifyCouponCate(CouponCateModifyRequest request) {
        if (StringUtils.isBlank(request.getCouponCateId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        CouponCate cate = couponCateRepository.getCouponCateByCouponCateId(request.getCouponCateId());
        // 优惠券分类不存在
        if (Objects.isNull(cate)) {
            throw new SbcRuntimeException(CouponErrorCode.COUPON_CATE_NOT_EXIST);
        }
        // 除自身外, 优惠券名分类称不能重复
        if (Objects.nonNull(couponCateRepository.findByCouponCateNameNotSelf(
                request.getCouponCateId(), request.getCouponCateName()))) {
            throw new SbcRuntimeException(CouponErrorCode.COUPON_CATE_NAME_EXIST);
        }
        KsBeanUtil.copyProperties(request, cate);
        CouponCate source = couponCateRepository.saveAndFlush(cate);
        CouponCateResponse response = new CouponCateResponse();
        KsBeanUtil.copyPropertiesThird(source, response);
        return response;
    }

    /**
     * 优惠券分类排序
     *
     * @param list 排序集合
     */
    @Transactional
    public List<CouponCateSortVO> sortCouponCate(List<CouponCateSortRequest> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (CollectionUtils.isNotEmpty(list) && list.size() > Constant.MAX_CATE_COUNT) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (list.stream().map(CouponCateSortRequest::getCouponCateId).distinct().count() != list.size()) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (list.stream().anyMatch(sort -> Objects.isNull(sort.getCouponCateId()))
                || list.stream().anyMatch(sort -> Objects.isNull(sort.getCateSort()))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        list.forEach(cate ->
                couponCateRepository.sortCouponCate(cate.getCouponCateId(),
                        cate.getCateSort()));
        return KsBeanUtil.convertList(list,CouponCateSortVO.class);
    }

    /**
     * 优惠券分类设置只供平台可用
     *
     * @param request
     */
    @Transactional
    public void isOnlyPlatform(CouponCateIsOnlyPlatformRequest request) {
        if (StringUtils.isBlank(request.getCouponCateId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        CouponCate cate = couponCateRepository.getCouponCateByCouponCateId(request.getCouponCateId());
        // 优惠券分类不存在
        if (Objects.isNull(cate)) {
            throw new SbcRuntimeException(CouponErrorCode.COUPON_CATE_NOT_EXIST);
        }
        KsBeanUtil.copyProperties(request, cate);
        if (Objects.equals(cate.getOnlyPlatformFlag(), DefaultFlag.YES)) {
            cate.setOnlyPlatformFlag(DefaultFlag.NO);
        } else {
            cate.setOnlyPlatformFlag(DefaultFlag.YES);
        }
        couponCateRepository.save(cate);
        //删除分类关系表中 该分类对应下的店铺关联的信息
        couponCateRelaRepository.deleteByCateIdAndPlatformFlag(request.getCouponCateId(),DefaultFlag.NO);
        //刷新优惠券缓存
        couponCacheService.updateCateRelaCache(request.getCouponCateId(), false);
    }


    /**
     * 查询优惠券分类列表提供给商家使用
     *
     * @return
     */
    public List<CouponCateResponse> listCouponCateForSupplier() {
        List<CouponCate> source = couponCateRepository.listCouponCateForSupplier();
        if (CollectionUtils.isEmpty(source)) {
            return new ArrayList<>();
        }
        List<CouponCateResponse> target = source.stream().map(cate -> {
            CouponCateResponse response = new CouponCateResponse();
            KsBeanUtil.copyPropertiesThird(cate, response);
            return response;
        }).collect(Collectors.toList());
        return target;
    }

    /**
     * 根据优惠券id集合查询优惠券分类
     * @param couponInfoIds
     * @return
     */
    public Map<String, List<CouponCateRela>> mapCateByCouponIds(List<String> couponInfoIds){
        List<CouponCateRela> relaList = couponCateRelaRepository.findByCouponIdIn(couponInfoIds);
        return relaList.stream().collect(Collectors.groupingBy(CouponCateRela::getCouponId));
    }

    /**
     *
     * @param ids
     * @return
     */
    public List<CouponCate> queryByIds(List<String> ids){
       return couponCateRepository.queryByIds(ids);
    }
}
