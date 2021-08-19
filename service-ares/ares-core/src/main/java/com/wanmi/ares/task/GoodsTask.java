package com.wanmi.ares.task;

import com.wanmi.ares.report.goods.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDate;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-9-24
 * \* Time: 10:11
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Component
@Slf4j
public class GoodsTask {

    @Resource
    private SkuGenerateService skuGenerateService;

    @Resource
    private GoodsCateGenerateService goodsCateGenerateService;

    @Resource
    private GoodsBrandGenerateService goodsBrandGenerateService;

    @Resource
    private GoodsStoreCateGenerateService goodsStoreCateGenerateService;

    @Resource
    private GoodsTotalGenerateService goodsTotalGenerateService;

    @Resource
    private GoodsTotalRatioGenerateService goodsTotalRatioGenerateService;


    @Transactional
    public void generate(int type) throws Exception{
        LocalDate date = LocalDate.now();

        //商品概况统计
        try {
            goodsTotalGenerateService.generate(type);
        } catch (Exception e) {
            log.error("goodsTotal report generation failed. date--->" + LocalDate.now(), e);
            throw e;
        }

        //商品概况比率
        try {

            goodsTotalRatioGenerateService.generate(type);

        } catch (Exception e) {
            log.error("goodsTotalRatio report generation failed. date--->" + date, e);
            throw e;
        }

        //商品SKU
        try {
            skuGenerateService.generate(type);
        } catch (Exception e) {
            log.error("sku report generation failed. date--->" + date, e);
            throw e;
        }
        //商品分类
        try {
            goodsCateGenerateService.generate(type);
        } catch (Exception e) {
            log.error("goodsCate report generation failed. date--->" + date, e);
            throw e;
        }
        //商品品牌
        try {
            goodsBrandGenerateService.generate(type);

        } catch (Exception e) {
            log.error("goodsBrand report generation failed. date--->" + date, e);
            throw e;
        }

        //店铺端商品分类
        try {
            goodsStoreCateGenerateService.generate(type);

        } catch (Exception e) {
            log.error("goodsStoreCate report generation failed. date--->" + date, e);
            throw e;
        }
    }
}
