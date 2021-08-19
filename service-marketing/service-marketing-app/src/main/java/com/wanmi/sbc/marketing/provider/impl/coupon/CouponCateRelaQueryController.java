package com.wanmi.sbc.marketing.provider.impl.coupon;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCateRelaQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCateRelaListByCouponIdsRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateRelaListByCouponIdsResponse;
import com.wanmi.sbc.marketing.bean.vo.CouponCateRelaVO;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivityConfig;
import com.wanmi.sbc.marketing.coupon.model.root.CouponCate;
import com.wanmi.sbc.marketing.coupon.service.CouponCateService;
import com.wanmi.sbc.marketing.coupon.service.CouponInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-24
 */
@Validated
@RestController
public class CouponCateRelaQueryController implements CouponCateRelaQueryProvider {

    @Autowired
    private CouponCateService couponCateService;

    @Autowired
    private CouponInfoService couponInfoService;

    @Override
    public BaseResponse<CouponCateRelaListByCouponIdsResponse> listByCateIdsMap(@RequestBody  @Valid CouponCateRelaListByCouponIdsRequest request) {
        Map<String,List<String>> cateIdsMap = request.getCateIdsMap();
        List<String> couponIds = cateIdsMap.keySet().stream().collect(Collectors.toList());
        List<CouponActivityConfig> configs = couponInfoService.checkOpt(couponIds);
        List<CouponCateRelaVO> couponCateRelaVOList = cateIdsMap.entrySet().stream().map(e -> {
            List<CouponCate> cateList = CollectionUtils.isNotEmpty(e.getValue()) ? couponCateService.queryByIds(e.getValue()) : Lists.newArrayList();
            CouponCateRelaVO vo = new CouponCateRelaVO();
            vo.setCouponId(e.getKey());
            vo.setCouponCateName(cateList.stream().map(CouponCate::getCouponCateName).collect(Collectors.toList()));
            if (configs.stream().anyMatch(config -> config.getCouponId().equals(e.getKey()))) {
                vo.setIsFree(DefaultFlag.NO);
            } else {
                vo.setIsFree(DefaultFlag.YES);
            }
            return vo;
        }).collect(Collectors.toList());
        return BaseResponse.success(new CouponCateRelaListByCouponIdsResponse(couponCateRelaVOList));
    }
}
