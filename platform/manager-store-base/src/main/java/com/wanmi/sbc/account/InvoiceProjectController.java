package com.wanmi.sbc.account;

import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectProvider;
import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectQueryProvider;
import com.wanmi.sbc.account.api.request.invoice.*;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectByIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectListByCompanyInfoIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectPageByCompanyInfoIdResponse;
import com.wanmi.sbc.account.bean.vo.InvoiceProjectListVO;
import com.wanmi.sbc.account.bean.vo.InvoiceProjectVO;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 开票项目管理
 * Created by chenli on 2017/12/12.
 */
@Api(tags = "InvoiceProjectController", description = "开票项目管理 API")
@RestController("supplierInvoiceProject")
@RequestMapping("/account")
public class InvoiceProjectController {

    @Autowired
    private InvoiceProjectQueryProvider invoiceProjectQueryProvider;

    @Autowired
    private InvoiceProjectProvider invoiceProjectProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 分页查询开票项目
     *
     * @param baseQueryRequest
     * @return
     */
    @ApiOperation(value = "分页查询开票项目")
    @RequestMapping(value = "/invoiceProjects", method = RequestMethod.POST)
    public ResponseEntity<MicroServicePage<InvoiceProjectVO>> findInvoiceProjects(@RequestBody BaseQueryRequest baseQueryRequest) {
        InvoiceProjectPageByCompanyInfoIdRequest invoiceProjectPageByCompanyInfoIdRequest = new InvoiceProjectPageByCompanyInfoIdRequest();
        invoiceProjectPageByCompanyInfoIdRequest.setBaseQueryRequest(baseQueryRequest);
        invoiceProjectPageByCompanyInfoIdRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        BaseResponse<InvoiceProjectPageByCompanyInfoIdResponse> baseResponse = invoiceProjectQueryProvider.pageByCompanyInfoId(invoiceProjectPageByCompanyInfoIdRequest);
        InvoiceProjectPageByCompanyInfoIdResponse response = baseResponse.getContext();
        if (Objects.isNull(response)){
            return null;
        }
        return ResponseEntity.ok(response.getInvoiceProjectVOPage());
    }

    /**
     * 新增开票项目
     *
     * @param invoiceProjectSaveRequest
     * @return
     */
    @ApiOperation(value = "新增开票项目")
    @RequestMapping(value = "/invoiceProject", method = RequestMethod.POST)
    public BaseResponse saveInvoiceProject(@RequestBody InvoiceProjectAddRequest invoiceProjectSaveRequest) {
        invoiceProjectSaveRequest.setOperatePerson(commonUtil.getOperatorId());
        invoiceProjectSaveRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        invoiceProjectProvider.add(invoiceProjectSaveRequest);

        //操作日志记录
        operateLogMQUtil.convertAndSend("财务", "新增开票项目",
                "新增开票项目：" + invoiceProjectSaveRequest.getProjectName());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 编辑开票项目
     *
     * @param invoiceProjectSaveRequest
     * @return
     */
    @ApiOperation(value = "编辑开票项目")
    @RequestMapping(value = "/invoiceProject", method = RequestMethod.PUT)
    public BaseResponse editInvoiceProject(@RequestBody InvoiceProjectModifyRequest invoiceProjectSaveRequest) {
        InvoiceProjectByIdResponse response =
                invoiceProjectQueryProvider.getById(new InvoiceProjectByIdRequest(invoiceProjectSaveRequest.getProjectId())).getContext();

        invoiceProjectSaveRequest.setOperatePerson(commonUtil.getOperatorId());
        invoiceProjectSaveRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        invoiceProjectProvider.modify(invoiceProjectSaveRequest);

        // 操作日志记录
        operateLogMQUtil.convertAndSend("财务", "编辑开票项目",
                "编辑开票项目：'" + (Objects.nonNull(response) ? response.getProjectName() : "") + "' 改为 '" + invoiceProjectSaveRequest.getProjectName() + "'");

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询所有开票项
     *
     * @return List<InvoiceResponse>
     */
    @ApiOperation(value = "查询所有开票项")
    @RequestMapping(value = "/invoiceProjects", method = RequestMethod.GET)
    public BaseResponse<List<InvoiceProjectListVO>> findAllInvoiceProject() {
        InvoiceProjectListByCompanyInfoIdRequest request = new InvoiceProjectListByCompanyInfoIdRequest();
        request.setCompanyInfoId(commonUtil.getCompanyInfoId());
        BaseResponse<InvoiceProjectListByCompanyInfoIdResponse> baseResponse = invoiceProjectQueryProvider.listByCompanyInfoId(request);
        InvoiceProjectListByCompanyInfoIdResponse response = baseResponse.getContext();
        if (Objects.isNull(response)){
            return null;
        }
        return BaseResponse.success(response.getInvoiceProjectListDTOList());
    }
}
