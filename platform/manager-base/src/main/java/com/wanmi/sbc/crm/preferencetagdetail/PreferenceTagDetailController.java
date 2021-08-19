package com.wanmi.sbc.crm.preferencetagdetail;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.crm.api.provider.autotagpreference.AutotagPreferenceQueryProvider;
import com.wanmi.sbc.crm.api.provider.preferencetagdetail.PreferenceTagDetailProvider;
import com.wanmi.sbc.crm.api.provider.preferencetagdetail.PreferenceTagDetailQueryProvider;
import com.wanmi.sbc.crm.api.request.autotagpreference.AutoPreferencePageRequest;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.*;
import com.wanmi.sbc.crm.api.response.autotagpreference.AutotagPreferencePageResponse;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailAddResponse;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailByIdResponse;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailListResponse;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailModifyResponse;
import com.wanmi.sbc.crm.bean.vo.PreferenceTagDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;


@Api(description = "偏好标签明细管理API", tags = "PreferenceTagDetailController")
@RestController
@RequestMapping(value = "/preferencetagdetail")
public class PreferenceTagDetailController {

    @Autowired
    private PreferenceTagDetailQueryProvider preferenceTagDetailQueryProvider;

    @Autowired
    private PreferenceTagDetailProvider preferenceTagDetailProvider;

    @Autowired
    private AutotagPreferenceQueryProvider autotagPreferenceQueryProvider;

    @ApiOperation(value = "分页查询偏好标签明细")
    @PostMapping("/page")
    public BaseResponse<AutotagPreferencePageResponse> getPage(@RequestBody @Valid AutoPreferencePageRequest pageReq) {
        return autotagPreferenceQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询偏好标签明细")
    @PostMapping("/list")
    public BaseResponse<PreferenceTagDetailListResponse> getList(@RequestBody @Valid PreferenceTagDetailListRequest listReq) {
        return preferenceTagDetailQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询偏好标签明细")
    @GetMapping("/{id}")
    public BaseResponse<PreferenceTagDetailByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PreferenceTagDetailByIdRequest idReq = new PreferenceTagDetailByIdRequest();
        idReq.setId(id);
        return preferenceTagDetailQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增偏好标签明细")
    @PostMapping("/add")
    public BaseResponse<PreferenceTagDetailAddResponse> add(@RequestBody @Valid PreferenceTagDetailAddRequest addReq) {
        return preferenceTagDetailProvider.add(addReq);
    }

    @ApiOperation(value = "修改偏好标签明细")
    @PutMapping("/modify")
    public BaseResponse<PreferenceTagDetailModifyResponse> modify(@RequestBody @Valid PreferenceTagDetailModifyRequest modifyReq) {
        return preferenceTagDetailProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除偏好标签明细")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PreferenceTagDetailDelByIdRequest delByIdReq = new PreferenceTagDetailDelByIdRequest();
        delByIdReq.setId(id);
        return preferenceTagDetailProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除偏好标签明细")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid PreferenceTagDetailDelByIdListRequest delByIdListReq) {
        return preferenceTagDetailProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出偏好标签明细列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        PreferenceTagDetailListRequest listReq = JSON.parseObject(decrypted, PreferenceTagDetailListRequest.class);
        List<PreferenceTagDetailVO> dataRecords = preferenceTagDetailQueryProvider.list(listReq).getContext().getPreferenceTagDetailVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("偏好标签明细列表_%s.xls", nowStr), "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            exportDataList(dataRecords, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 导出列表数据具体实现
     */
    private void exportDataList(List<PreferenceTagDetailVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("标签id", new SpelColumnRender<PreferenceTagDetailVO>("tagId")),
            new Column("偏好类标签名称", new SpelColumnRender<PreferenceTagDetailVO>("detailName")),
            new Column("会员人数", new SpelColumnRender<PreferenceTagDetailVO>("customerCount"))
        };
        excelHelper.addSheet("偏好标签明细列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
