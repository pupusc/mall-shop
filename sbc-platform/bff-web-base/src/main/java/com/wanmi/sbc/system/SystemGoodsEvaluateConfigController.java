package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.response.BossGoodsEvaluateResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lvzhenwei
 * @Description 商品评价开关设置
 * @Date 14:19 2019/4/3
 * @Param 
 * @return 
 **/
@Api(tags = "SystemPointsConfigController", description = "WEB端-商品评价开关设置API")
@RestController
@RequestMapping("/systemGoodsEvaluateConfig")
public class SystemGoodsEvaluateConfigController {

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    /**
     * 商品评价开关设置是否开启
     * @return
     */
    @RequestMapping(value = "/isGoodsEvaluate", method = RequestMethod.GET)
    public BaseResponse<BossGoodsEvaluateResponse> isGoodsEvaluate() {
        return auditQueryProvider.isGoodsEvaluate();
    }
}
