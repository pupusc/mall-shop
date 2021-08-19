package com.wanmi.sbc.page;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.page.MagicPageProvider;
import com.wanmi.sbc.setting.api.request.page.MagicPageMainSaveRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 魔方首页面缓存Controller
 */
@RestController
@RequestMapping("/magic-page")
@Api(tags = "BossMagicPageController", description = "boss 更新缓存的html页面信息bff")
@Validated
public class BossMagicPageController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private MagicPageProvider magicPageProvider;

    /**
     * 获取魔方首页缓存
     *
     * @return 返回html内容
     */
    @ApiOperation(value = "保存魔方首页缓存html")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResponse getMain(@Valid @RequestBody MagicPageMainSaveRequest request) {
        request.setOperatePerson(commonUtil.getOperatorId());
        return magicPageProvider.saveMain(request);
    }

}
