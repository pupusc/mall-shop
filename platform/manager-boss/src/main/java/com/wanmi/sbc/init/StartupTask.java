package com.wanmi.sbc.init;

import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 项目启动初始加载任务
 * Created by dyt on 2017/6/20.
 */
@Component
@Order(value=1)
@Slf4j
public class StartupTask implements CommandLineRunner {

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Value("${goods.es.data.init}")
    private Boolean goodsEsDataInit;

    @Override
    public void run(String... strings) throws Exception {
        if(Objects.nonNull(goodsEsDataInit) && goodsEsDataInit) {
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().build());
        }
    }

}
