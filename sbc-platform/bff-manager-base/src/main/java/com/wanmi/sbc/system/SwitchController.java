package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.setting.api.provider.SwitchProvider;
import com.wanmi.sbc.setting.api.provider.SwitchQueryProvider;
import com.wanmi.sbc.setting.api.request.SwitchGetByIdRequest;
import com.wanmi.sbc.setting.api.request.SwitchModifyRequest;
import com.wanmi.sbc.setting.api.response.SwitchGetByIdResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yuanlinling on 2017/4/26.
 */
@Api(tags = "SwitchController", description = "系统设置服务 Api")
@RestController
@RequestMapping("/system")
public class SwitchController {

    @Autowired
    private SwitchProvider switchProvider;

    @Autowired
    private SwitchQueryProvider switchQueryProvider;

    /**
     * 根据id查询
     */
    @ApiOperation(value = "根据id查询")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "switchId", value = "系统设置Id", required = true)
    @RequestMapping(value = "/switchId/{switchId}")
    public ResponseEntity<SwitchGetByIdResponse> findInvoiceProjectById(@PathVariable("switchId") String switchId) {
        SwitchGetByIdRequest request = new SwitchGetByIdRequest();
        request.setId(switchId);

        SwitchGetByIdResponse response = switchQueryProvider.getById(request).getContext();

        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            throw new SbcRuntimeException("K_090705");
        }
    }

    /**
     * 开关开启关闭
     * @param switchRequest
     * @return
     */
    @ApiOperation(value = "开关开启关闭")
    @RequestMapping(value = "/switch", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> updateSwitch(@RequestBody SwitchModifyRequest switchRequest){
        switchProvider.modify(switchRequest);

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

}
