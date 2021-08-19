package com.wanmi.sbc.goods.log.service;

import com.wanmi.sbc.goods.log.GoodsCheckLog;
import com.wanmi.sbc.goods.log.GoodsCheckLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-04-08 17:27
 */
@Service
public class GoodsCheckLogService {

    @Autowired
    private GoodsCheckLogRepository goodsCheckLogRepository;

    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param goodsCheckLog
     */
    @Transactional
    public void addGoodsCheckLog(GoodsCheckLog goodsCheckLog) {
        goodsCheckLogRepository.save(goodsCheckLog);
    }

}
