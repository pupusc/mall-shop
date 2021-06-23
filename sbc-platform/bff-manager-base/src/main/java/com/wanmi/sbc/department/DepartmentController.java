package com.wanmi.sbc.department;

import com.wanmi.sbc.aop.DepartmentIsolation;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.customer.api.provider.department.DepartmentProvider;
import com.wanmi.sbc.customer.api.provider.department.DepartmentQueryProvider;
import com.wanmi.sbc.customer.api.request.department.*;
import com.wanmi.sbc.customer.api.response.department.DepartmentAddResponse;
import com.wanmi.sbc.customer.api.response.department.DepartmentListResponse;
import com.wanmi.sbc.customer.api.response.department.DepartmentModifyResponse;
import com.wanmi.sbc.department.request.DepartmentExcelImportRequest;
import com.wanmi.sbc.department.service.DepartmentExcelService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Objects;


@Api(description = "部门管理管理API", tags = "DepartmentController")
@RestController
@RequestMapping(value = "/department")
public class DepartmentController {

    @Autowired
    private DepartmentQueryProvider departmentQueryProvider;

    @Autowired
    private DepartmentProvider departmentSaveProvider;

    @Autowired
    private DepartmentExcelService departmentExcelService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @ApiOperation(value = "列表查询部门管理-数据隔离")
    @DepartmentIsolation
    @PostMapping("/get-department-tree")
    public BaseResponse<DepartmentListResponse> getDepartmentTree(@RequestBody @Valid DepartmentListRequest listReq) {
        listReq.setCompanyInfoId(Objects.nonNull(commonUtil.getCompanyInfoId()) ? commonUtil.getCompanyInfoId() : 0L);
        listReq.setDelFlag(DeleteFlag.NO.toValue());
        listReq.putSort("departmentGrade", SortType.ASC.toValue());
        listReq.putSort("departmentSort", SortType.ASC.toValue());
        return listReq.getBelongToDepartment() ? departmentQueryProvider.listDepartmentTree(listReq) : BaseResponse.success(new DepartmentListResponse(Collections.EMPTY_LIST,Collections.EMPTY_LIST,Collections.EMPTY_LIST,listReq.getIsMaster()));
    }

    @ApiOperation(value = "列表查询部门管理")
    @PostMapping("/list-department-tree")
    public BaseResponse<DepartmentListResponse> listDepartmentTree(@RequestBody @Valid DepartmentListRequest listReq) {
        listReq.setCompanyInfoId(Objects.nonNull(commonUtil.getCompanyInfoId()) ? commonUtil.getCompanyInfoId() : 0L);
        listReq.setDelFlag(DeleteFlag.NO.toValue());
        listReq.putSort("departmentGrade", SortType.ASC.toValue());
        listReq.putSort("departmentSort", SortType.ASC.toValue());
        return departmentQueryProvider.listDepartmentTree(listReq);
    }

    @ApiOperation(value = "新增部门管理")
    @PostMapping("/add")
    public BaseResponse<DepartmentAddResponse> add(@RequestBody @Valid DepartmentAddRequest addReq) {
        addReq.setCompanyInfoId(Objects.nonNull(commonUtil.getCompanyInfoId()) ? commonUtil.getCompanyInfoId() : 0L);
        addReq.setCreatePerson(commonUtil.getOperatorId());
        BaseResponse<DepartmentAddResponse> baseResponse = departmentSaveProvider.add(addReq);
        operateLogMQUtil.convertAndSend("部门", "新增部门", "新增部门：" + addReq.getDepartmentName());
        return baseResponse;
    }

    @ApiOperation(value = "修改部门管理")
    @PutMapping("/modifyDepartmentName")
    public BaseResponse<DepartmentModifyResponse> modifyDepartmentName(@RequestBody @Valid DepartmentModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        BaseResponse<DepartmentModifyResponse> baseResponse = departmentSaveProvider.modifyDepartmentName(modifyReq);
        operateLogMQUtil.convertAndSend("部门", "修改部门", "新修改部门：" + modifyReq.getDepartmentName());
        return baseResponse;
    }

    @ApiOperation(value = "拖拽排序")
    @PutMapping("/sort")
    public BaseResponse sort(@RequestBody @Valid DepartmentSortRequest sortRequest) {
        BaseResponse baseResponse = departmentSaveProvider.sort(sortRequest);
        operateLogMQUtil.convertAndSend("部门", "修改部门排序", "拖拽排序");
        return baseResponse;
    }


    @ApiOperation(value = "根据id删除部门管理")
    @DeleteMapping("/{departmentId}")
    public BaseResponse deleteById(@PathVariable String departmentId) {
        if (departmentId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        DepartmentDelByIdRequest delByIdReq = new DepartmentDelByIdRequest();
        delByIdReq.setDepartmentId(departmentId);
        BaseResponse baseResponse = departmentSaveProvider.deleteById(delByIdReq);
        operateLogMQUtil.convertAndSend("部门", "删除部门", "根据ID:" + departmentId + "删除部门管理");

        return baseResponse;
    }


    @ApiOperation(value = "设置部门主管")
    @PutMapping("/modify-leader")
    public BaseResponse modifyLeader(@RequestBody @Valid DepartmentModifyLeaderRequest request) {
        if(StringUtils.equals(request.getOldEmployeeId(), request.getNewEmployeeId())) {
            operateLogMQUtil.convertAndSend("部门", "设置部门主管", "部门主管修改无变化, ID:" + request.getNewEmployeeId());
            return BaseResponse.SUCCESSFUL();
        }
        BaseResponse baseResponse = departmentSaveProvider.modifyLeader(request);
        operateLogMQUtil.convertAndSend("部门", "设置部门主管", "新主管ID:" + request.getNewEmployeeId());

        return baseResponse;
    }



    /**
     * 下载部门导入模板
     */
    @ApiOperation(value = "下载部门导入模板")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted",
            value = "加密", required = true)
    @RequestMapping(value = "/excel/template/{encrypted}", method = RequestMethod.GET)
    public void template(@PathVariable String encrypted) {
        String file = departmentQueryProvider.exportTemplate().getContext().getFile();
        if (StringUtils.isNotBlank(file)) {
            try {
                String fileName = URLEncoder.encode("部门导入模板.xls", "UTF-8");
                HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";" +
                        "filename*=\"utf-8''%s\"", fileName, fileName));
                HttpUtil.getResponse().getOutputStream().write(new BASE64Decoder().decodeBuffer(file));
            } catch (Exception e) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
        }
    }

    /**
     * 上传文件
     */
    @ApiOperation(value = "上传文件")
    @RequestMapping(value = "/excel/upload", method = RequestMethod.POST)
    public BaseResponse<String> upload(@RequestParam("uploadFile") MultipartFile uploadFile) {
        return BaseResponse.success(departmentExcelService.upload(uploadFile, commonUtil.getOperatorId()));
    }

    /**
     * 确认导入部门
     *
     * @param ext 文件格式 {@link String}
     * @return
     */
    @ApiOperation(value = "确认导入部门")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "ext", value = "文件名后缀", required = true)
    @RequestMapping(value = "/import/{ext}", method = RequestMethod.GET)
    public BaseResponse<Boolean> implGoods(@PathVariable String ext) {
        if (!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        DepartmentExcelImportRequest standardExcelImplGoodsRequest = new DepartmentExcelImportRequest();
        standardExcelImplGoodsRequest.setExt(ext);
        standardExcelImplGoodsRequest.setCompanyInfoId(Objects.nonNull(commonUtil.getCompanyInfoId()) ? commonUtil.getCompanyInfoId() : 0L);
        standardExcelImplGoodsRequest.setUserId(commonUtil.getOperatorId());
        departmentExcelService.importDepartment(standardExcelImplGoodsRequest);
        //操作日志记录
        operateLogMQUtil.convertAndSend("部门", "批量导入", "批量导入");
        return BaseResponse.success(Boolean.TRUE);
    }


    /**
     * 下载错误文档
     */
    @ApiOperation(value = "下载错误文档")
    @RequestMapping(value = "/excel/err/{ext}/{decrypted}", method = RequestMethod.GET)
    public void downErrExcel(@PathVariable String ext, @PathVariable String decrypted) {
        if (!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        departmentExcelService.downErrExcel(commonUtil.getOperatorId(), ext);
    }
}
