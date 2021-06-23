package com.wanmi.sbc.account;

import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectProvider;
import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectQueryProvider;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectByIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectDeleteByIdRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectByIdResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 开票项目管理
 * Created by yuanlinling on 2017/4/25.
 */
@Api(tags = "InvoiceProjectBaseController", description = "开票项目管理 Api")
@RestController
@RequestMapping("/account")
public class InvoiceProjectBaseController {

    @Autowired
    private InvoiceProjectQueryProvider invoiceProjectQueryProvider;


    @Autowired
    private InvoiceProjectProvider invoiceProjectProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 根据id查询开票项目
     */
    @ApiOperation(value = "根据id查询开票项目")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "projectId", value = "开票项目ID", required = true)
    @RequestMapping(value = "/invoiceProject/{projectId}")
    public ResponseEntity<InvoiceProjectByIdResponse> findInvoiceProjectById(@PathVariable("projectId") String projectId) {
        InvoiceProjectByIdRequest request = new InvoiceProjectByIdRequest();
        request.setProjcetId(projectId);
        BaseResponse<InvoiceProjectByIdResponse> baseResponse = invoiceProjectQueryProvider.getById(request);
        InvoiceProjectByIdResponse response = baseResponse.getContext();
        if (Objects.isNull(response)) {
            throw new SbcRuntimeException("K-020004");
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 删除
     *
     * @param invoiceProjectRequest
     * @return
     */
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/invoiceProject", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponse> deleteInvoiceProject(@RequestBody InvoiceProjectDeleteByIdRequest invoiceProjectRequest) {
        InvoiceProjectByIdResponse invoice =
                invoiceProjectQueryProvider.getById(new InvoiceProjectByIdRequest(invoiceProjectRequest.getProjectId())).getContext();

        invoiceProjectProvider.delete(invoiceProjectRequest);
        // 记录日志
        operateLogMQUtil.convertAndSend("财务", "删除开票项目",
                "删除开票项目：" + (Objects.nonNull(invoice) ? invoice.getProjectName() : ""));

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }
}
