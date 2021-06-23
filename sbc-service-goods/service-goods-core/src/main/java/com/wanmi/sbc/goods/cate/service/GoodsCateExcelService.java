package com.wanmi.sbc.goods.cate.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.cate.request.GoodsCateQueryRequest;
import com.wanmi.sbc.goods.cate.request.GoodsCateSaveRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @Author: songhanlin
 * @Date: Created In 14:11 2018-12-12
 * @Description: 商品类目导入
 */
@Slf4j
@Service
public class GoodsCateExcelService {

    @Autowired
    private GoodsCateRepository goodsCateRepository;

    @Autowired
    private GoodsCateService goodsCateService;

    @Transactional
    public Boolean importGoodsCate(List<GoodsCate> goodsCateList) {
        //处理层级结构
        List<GoodsCate> list = goodsCateList.stream().filter(cate -> cate.getCateParentId().intValue() == 0)
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(c -> c.getCateName()))), ArrayList::new)).stream().map(cate -> {
                    cate.setGoodsCateList(goodsCateList.stream().filter(c -> c.getCateParentId().equals(cate.getCateId()))
                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(c -> c.getCateName()))), ArrayList::new)).stream().map(c -> {
                                c.setGoodsCateList(goodsCateList.stream().filter(c1 -> c1.getCateParentId().equals(c.getCateId())).collect(Collectors.toList()));
                                return c;
                            }).collect(Collectors.toList()));
                    return cate;
                }).collect(Collectors.toList());
        //保存类目
        list.forEach(cate -> {
            GoodsCateSaveRequest saveRequest = new GoodsCateSaveRequest();
            saveRequest.setGoodsCate(cate);
            GoodsCateQueryRequest goodsCateQueryRequest = GoodsCateQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).cateParentId(0L).cateName(cate.getCateName()).build();
            GoodsCate newGoodsCate = goodsCateRepository.count(goodsCateQueryRequest.getWhereCriteria()) > 0
                    ? goodsCateRepository.findOne(goodsCateQueryRequest.getWhereCriteria()).orElse(null)
                    : goodsCateService.add(saveRequest);
            if (CollectionUtils.isNotEmpty(cate.getGoodsCateList())) {
                cate.getGoodsCateList().forEach(cate1 -> {
                    cate1.setCateParentId(newGoodsCate.getCateId());
                    GoodsCateSaveRequest saveRequest1 = new GoodsCateSaveRequest();
                    saveRequest1.setGoodsCate(cate1);
                    GoodsCateQueryRequest goodsCateQueryRequest1 = GoodsCateQueryRequest.builder().delFlag(DeleteFlag.NO.toValue())
                            .cateParentId(newGoodsCate.getCateId()).cateName(cate1.getCateName()).build();
                    GoodsCate goodsCate1 = goodsCateRepository.count(goodsCateQueryRequest1.getWhereCriteria()) > 0
                            ? goodsCateRepository.findOne(goodsCateQueryRequest1.getWhereCriteria()).orElse(null)
                            : goodsCateService.add(saveRequest1);
                    if (CollectionUtils.isNotEmpty(cate1.getGoodsCateList())) {
                        cate1.getGoodsCateList().forEach(cate2 -> {
                            cate2.setCateParentId(goodsCate1.getCateId());
                            GoodsCateSaveRequest saveRequest2 = new GoodsCateSaveRequest();
                            saveRequest2.setGoodsCate(cate2);
                            goodsCateService.add(saveRequest2);
                        });
                    }
                });
            }

        });

        return true;
    }
}
