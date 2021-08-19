package com.wanmi.ares.report.goods.service;

import com.wanmi.ares.report.goods.dao.GoodsTotalRatioMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 商品分类报表服务
 * Created by zgl on 2019/8/27.
 */
@Service
@Slf4j
public class GoodsTotalRatioGenerateService {


    @Autowired
    private GoodsTotalRatioMapper goodsTotalRatioMapper;
    @Autowired
    private GoodsGenerateService generateService;

    private LocalDate month;

    public void setMonth(LocalDate month){
        this.month = month;
    }

    /**
     * 生成分类报表
     * @param type 0:今日 1:昨日 2:近7天 3:近30天 4:上个月 5:今年
     */
    public void generate(int type){
        this.generateService.generate(this.goodsTotalRatioMapper,type);
    }

}
