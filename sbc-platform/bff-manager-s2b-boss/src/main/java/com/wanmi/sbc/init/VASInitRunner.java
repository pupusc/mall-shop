package com.wanmi.sbc.init;

import com.wanmi.sbc.init.service.VASCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 增值服务_初始化数据
 *
 * @version: 1.0
 */
@Slf4j
@Order(7)
@Component
public class VASInitRunner implements CommandLineRunner {


    @Autowired
    private VASCommonService vasCommonService;

    @Override
    public void run(String... args) throws Exception {
        // saas初始化, 后期如果有迭代, 考虑移入增值服务内
       // saasService.saasInit();
        // 增值服务初始化
        vasCommonService.init();
    }

}
