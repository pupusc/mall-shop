package com.soybean.mall.miniapp.url;


import com.soybean.mall.order.bean.vo.MiniProgramOrderReportVO;
import com.soybean.mall.wx.mini.common.bean.request.UrlschemeRequest;
import com.soybean.mall.wx.mini.common.controller.CommonController;
import com.wanmi.sbc.common.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "MiniProgramController", description = "小程序")
@RequestMapping("/wx/scheme")
@RestController
public class MiniSchemeController {

    @Autowired
    private CommonController commonController;

    /**
     * @description 小程序实时报表
     * @param
     * @menu 小程序/直播助手
     * @status done
     */
    @ApiOperation("小程序实时报表")
    @GetMapping("/generate")
    public BaseResponse<String> createUrl(@Validated @RequestBody UrlschemeRequest request) {
        return commonController.urlschemeGenerate(request);
    }
}
