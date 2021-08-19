package com.wanmi.sbc.hovernavmobile;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.setting.api.provider.hovernavmobile.HoverNavMobileQueryProvider;
import com.wanmi.sbc.setting.api.request.hovernavmobile.HoverNavMobileByIdRequest;
import com.wanmi.sbc.setting.api.response.hovernavmobile.HoverNavMobileByIdResponse;
import com.wanmi.sbc.setting.bean.enums.UsePageType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@Api(description = "移动端悬浮导航栏管理API", tags = "HoverNavMobileController")
@RestController
@RequestMapping(value = "/hoverNavMobile")
public class HoverNavMobileController {

    @Autowired
    private HoverNavMobileQueryProvider hoverNavMobileQueryProvider;

    @ApiOperation(value = "平台移动端悬浮导航栏")
    @GetMapping("/{usePageType}")
    public BaseResponse<HoverNavMobileByIdResponse> getByMain(@PathVariable Integer usePageType) {
        HoverNavMobileByIdRequest idReq = new HoverNavMobileByIdRequest();
        idReq.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        HoverNavMobileByIdResponse response = hoverNavMobileQueryProvider.getById(idReq).getContext();
        if(Objects.nonNull(response.getHoverNavMobileVO())
                && CollectionUtils.isNotEmpty(response.getHoverNavMobileVO().getUsePages())
                && response.getHoverNavMobileVO().getUsePages().contains(UsePageType.fromValue(usePageType))){
            return BaseResponse.success(response);
        }
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "店铺移动端悬浮导航栏")
    @GetMapping("/{storeId}/{usePageType}")
    public BaseResponse<HoverNavMobileByIdResponse> get(@PathVariable Long storeId, @PathVariable Integer usePageType) {
        HoverNavMobileByIdRequest idReq = new HoverNavMobileByIdRequest();
        idReq.setStoreId(storeId);
        HoverNavMobileByIdResponse response = hoverNavMobileQueryProvider.getById(idReq).getContext();
        if(Objects.nonNull(response.getHoverNavMobileVO())
                && CollectionUtils.isNotEmpty(response.getHoverNavMobileVO().getUsePages())
                && response.getHoverNavMobileVO().getUsePages().contains(UsePageType.fromValue(usePageType))){
            return BaseResponse.success(response);
        }
        return BaseResponse.SUCCESSFUL();
    }
}
