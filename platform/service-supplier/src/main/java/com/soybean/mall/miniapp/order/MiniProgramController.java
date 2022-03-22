package com.soybean.mall.miniapp.order;

import com.soybean.mall.order.api.provider.order.MiniAppOrderProvider;
import com.soybean.mall.order.bean.vo.MiniProgramOrderReportVO;
import com.wanmi.sbc.common.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "MiniProgramController", description = "小程序")
@RequestMapping("/wx/order")
@RestController
public class MiniProgramController {

    @Autowired
    private MiniAppOrderProvider miniAppOrderProvider;
    /**
     * @description 小程序实时报表
     * @param
     * @menu 小程序/直播助手
     * @status done
     */
    @ApiOperation("小程序实时报表")
    @GetMapping("/report")
    public BaseResponse<MiniProgramOrderReportVO> getOrderReportCache() {
        return miniAppOrderProvider.getOrderReportCache();
    }
}
