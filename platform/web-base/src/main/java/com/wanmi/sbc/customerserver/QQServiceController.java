package com.wanmi.sbc.customerserver;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.TerminalType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.provider.onlineservice.OnlineServiceQueryProvider;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceListRequest;
import com.wanmi.sbc.setting.api.response.onlineservice.OnlineServiceListResponse;
import com.wanmi.sbc.setting.bean.vo.OnlineServiceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/customerService")
@Api(tags = "QQServiceController", description = "S2B web公用-QQ客户信息API")
public class QQServiceController {

    @Autowired
    private OnlineServiceQueryProvider onlineServiceQueryProvider;

    /**
     * 查询qq客服列表
     * @param storeId
     * @param type    0：PC, 1： H5, 2： APP;
     * @return
     */
    @ApiOperation(value = "查询qq客服列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId",
                    value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "type",
                    value = "生效终端，0：PC, 1： H5, 2： APP", required = true)
    })
    @RequestMapping(value = {"/qq/detail/{storeId}/{type}"}, method = RequestMethod.GET)
    public BaseResponse<OnlineServiceListResponse> qqDetail(@PathVariable Long storeId, @PathVariable Integer type) {
        if (storeId == null || type == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        OnlineServiceListResponse onlineServiceListResponse = onlineServiceQueryProvider.list(OnlineServiceListRequest.builder().storeId(storeId).build()).getContext();
        OnlineServiceVO onlineServiceVO = onlineServiceListResponse.getQqOnlineServerRop();
        if (Objects.isNull(onlineServiceVO) || onlineServiceListResponse.getQqOnlineServerItemRopList().isEmpty()
                || Objects.equals(DefaultFlag.NO, onlineServiceVO.getServerStatus())) {

            return BaseResponse.success(null);
        }
        if (TerminalType.APP.equals(type)) {
            if (Objects.equals(DefaultFlag.NO, onlineServiceVO.getEffectiveApp())) {
                return BaseResponse.success(null);
            }
        } else if (TerminalType.H5.equals(type)) {
            if (Objects.equals(DefaultFlag.NO, onlineServiceVO.getEffectiveMobile())) {
                return BaseResponse.success(null);
            }
        } else if (TerminalType.PC.equals(type)) {
            if (Objects.equals(DefaultFlag.NO, onlineServiceVO.getEffectivePc())) {
                return BaseResponse.success(null);
            }
        }

        return BaseResponse.success(onlineServiceListResponse);
    }

}
