package com.soybean.mall.goods.controller;

import com.soybean.mall.goods.response.sputopic.HomeSpuConfigResp;
import com.soybean.mall.goods.service.NormalModelService;
import com.wanmi.sbc.common.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 7:38 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/home")
@RestController
@Slf4j
public class HomeController {

    @Autowired
    private NormalModelService normalModelService;

    /**
     * 获取首页 商品栏目模块信息
     * @menu 返积分活动
     * @return
     */
    @GetMapping("/homeSpuTopic")
    public BaseResponse homeSpuTopic() {
        HomeSpuConfigResp homeSpuConfigResp = new HomeSpuConfigResp();
        homeSpuConfigResp.setSpuTopic(normalModelService.homeSpuTopic());
        return BaseResponse.success(homeSpuConfigResp);
    }
}
