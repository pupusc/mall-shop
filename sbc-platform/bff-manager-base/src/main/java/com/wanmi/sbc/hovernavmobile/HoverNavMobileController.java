package com.wanmi.sbc.hovernavmobile;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.setting.api.provider.hovernavmobile.HoverNavMobileProvider;
import com.wanmi.sbc.setting.api.provider.hovernavmobile.HoverNavMobileQueryProvider;
import com.wanmi.sbc.setting.api.request.hovernavmobile.HoverNavMobileByIdRequest;
import com.wanmi.sbc.setting.api.request.hovernavmobile.HoverNavMobileModifyRequest;
import com.wanmi.sbc.setting.api.response.hovernavmobile.HoverNavMobileByIdResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(description = "移动端悬浮导航栏管理API", tags = "HoverNavMobileController")
@RestController
@RequestMapping(value = "/hoverNavMobile")
public class HoverNavMobileController {

    @Autowired
    private HoverNavMobileQueryProvider hoverNavMobileQueryProvider;

    @Autowired
    private HoverNavMobileProvider hoverNavMobileProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "查询移动端悬浮导航栏")
    @GetMapping()
    public BaseResponse<HoverNavMobileByIdResponse> get() {
        Long storeId = commonUtil.getStoreId();
        if (storeId == null) {
            storeId = Constants.BOSS_DEFAULT_STORE_ID;
        }
        HoverNavMobileByIdRequest idReq = new HoverNavMobileByIdRequest();
        idReq.setStoreId(storeId);
        return hoverNavMobileQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "修改移动端悬浮导航栏")
    @PutMapping("/modify")
    public BaseResponse modify(@RequestBody @Valid HoverNavMobileModifyRequest modifyReq) {
        Long storeId = commonUtil.getStoreId();
        if (storeId == null) {
            storeId = Constants.BOSS_DEFAULT_STORE_ID;
        }
        modifyReq.setStoreId(storeId);
        return hoverNavMobileProvider.modify(modifyReq);
    }
}
