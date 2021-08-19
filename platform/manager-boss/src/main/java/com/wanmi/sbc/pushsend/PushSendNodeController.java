package com.wanmi.sbc.pushsend;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.message.api.provider.pushsendnode.PushSendNodeProvider;
import com.wanmi.sbc.message.api.provider.pushsendnode.PushSendNodeQueryProvider;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeByIdRequest;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeModifyRequest;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodePageRequest;
import com.wanmi.sbc.message.api.response.pushsendnode.PushSendNodeByIdResponse;
import com.wanmi.sbc.message.api.response.pushsendnode.PushSendNodeModifyResponse;
import com.wanmi.sbc.message.api.response.pushsendnode.PushSendNodePageResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @program: sbc-micro-service
 * @description: 推送通知节点
 * @create: 2020-01-13 10:40
 **/
@Api(description = "推送通知节点管理API", tags = "PushSendNodeController")
@RestController
@RequestMapping(value = "/pushsendnode")
public class PushSendNodeController {

    @Autowired
    private PushSendNodeProvider pushSendNodeProvider;

    @Autowired
    private PushSendNodeQueryProvider pushSendNodeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "修改通知节点")
    @PostMapping("/modify")
    public BaseResponse<PushSendNodeModifyResponse> modify(@RequestBody @Valid PushSendNodeModifyRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        return pushSendNodeProvider.modify(request);
    }

    @ApiOperation(value = "根据id查询通知节点")
    @GetMapping("/{id}")
    public BaseResponse<PushSendNodeByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PushSendNodeByIdRequest idReq = new PushSendNodeByIdRequest();
        idReq.setId(id);
        return pushSendNodeQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "分页查询通知节点")
    @PostMapping("/page")
    public BaseResponse<PushSendNodePageResponse> getPage(@RequestBody @Valid PushSendNodePageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("id", "desc");
        return pushSendNodeQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "通知节点开关设置")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "开关标示", required = true)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "开关标示", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "status", value = "状态", required = true)
    })
    @PutMapping("/enabled/{id}/{status}")
    public BaseResponse enabled(@PathVariable Long id, @PathVariable Integer status) {
        return pushSendNodeProvider.enabled(PushSendNodeModifyRequest.builder().id(id).status(status).build());
    }


}