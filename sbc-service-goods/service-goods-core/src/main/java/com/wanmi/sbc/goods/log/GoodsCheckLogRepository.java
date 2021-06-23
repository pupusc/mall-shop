package com.wanmi.sbc.goods.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 商品审核日志repository
 * Created by daiyitian on 16/11/2017.
 */
public interface GoodsCheckLogRepository
        extends JpaRepository<GoodsCheckLog, String>,
        JpaSpecificationExecutor<GoodsCheckLog> {

}
