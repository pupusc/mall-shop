package com.wanmi.sbc.marketing.coupon.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.coupon.model.root.CouponCode;
import com.wanmi.sbc.marketing.coupon.model.root.CouponCodeCopy;
import com.wanmi.sbc.marketing.coupon.repository.CouponCodeCopyRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 主要用做历史券码表CouponCode-历史数据迁移
 */
@Slf4j
@Service
public class CouponCodeCopyService {

    @Autowired
    private CouponCodeCopyRepository couponCodeCopyRepository;

    @Autowired
    private CouponCodeRepository couponCodeRepository;

    /**
     * 数据迁移：旧coupon_code按照新的分表规则进行拆分保存至新表中
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int dataMigrationFromCouponCode(Integer pageNum,Integer pageSize ){
        List<CouponCodeCopy> codeList = couponCodeCopyRepository.pageCouponCode(PageRequest.of(pageNum,pageSize));
        if (CollectionUtils.isEmpty(codeList)){
           return NumberUtils.INTEGER_ZERO;
        }else{
            List<String> couponCodeIds = codeList.stream().map(CouponCodeCopy::getCouponCodeId).collect(Collectors.toList());
            List<CouponCode> list = KsBeanUtil.convert(codeList,CouponCode.class);
            list =  list.stream().peek(couponCode -> couponCode.setCouponCodeId(null)).collect(Collectors.toList());
            int size = couponCodeRepository.saveAll(list).size();
            if (size == couponCodeIds.size()){
                couponCodeCopyRepository.deleteByCouponCodeIdsIn(couponCodeIds);
            }
            return size;
        }
    }
}
