package com.wanmi.ares.report.goods.service;

import com.wanmi.ares.report.goods.dao.GoodsStoreCateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品分类报表服务
 * Created by zgl on 2019/8/27.
 */
@Service
@Slf4j
public class GoodsStoreCateGenerateService {


    @Autowired
    private GoodsStoreCateMapper goodsStoreCateMapper;
    @Autowired
    private GoodsGenerateService generateService;

    /**
     * 生成分类报表
     * @param type 0:今日 1:昨日 2:近7天 3:近30天 4:上个月 5:今年
     */
    public void generate(int type){
        this.generateService.generate(this.goodsStoreCateMapper,type);
    }

}
