package com.wanmi.sbc.message.smssetting;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.message.api.provider.smssetting.SmsSettingQueryProvider;
import com.wanmi.sbc.message.api.provider.smssetting.SmsSettingSaveProvider;
import com.wanmi.sbc.message.api.request.smssetting.*;
import com.wanmi.sbc.message.api.response.smssetting.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import com.wanmi.sbc.message.bean.vo.SmsSettingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "短信配置管理API", tags = "SmsSettingController")
@RestController
@RequestMapping(value = "/smssetting")
public class SmsSettingController {

    @Autowired
    private SmsSettingQueryProvider smsSettingQueryProvider;

    @Autowired
    private SmsSettingSaveProvider smsSettingSaveProvider;

    @ApiOperation(value = "分页查询短信配置")
    @PostMapping("/page")
    public BaseResponse<SmsSettingPageResponse> getPage(@RequestBody @Valid SmsSettingPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("id", "desc");
        return smsSettingQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询短信配置")
    @PostMapping("/list")
    public BaseResponse<SmsSettingListResponse> getList(@RequestBody @Valid SmsSettingListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        return smsSettingQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询短信配置")
    @GetMapping("/{id}")
    public BaseResponse<SmsSettingByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        SmsSettingByIdRequest idReq = new SmsSettingByIdRequest();
        idReq.setId(id);
        return smsSettingQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增短信配置")
    @PostMapping("/add")
    public BaseResponse<SmsSettingAddResponse> add(@RequestBody @Valid SmsSettingAddRequest addReq) {
        addReq.setDelFlag(DeleteFlag.NO);
        return smsSettingSaveProvider.add(addReq);
    }

    @ApiOperation(value = "修改短信配置")
    @PutMapping("/modify")
    public BaseResponse<SmsSettingModifyResponse> modify(@RequestBody @Valid SmsSettingModifyRequest modifyReq) {
        return smsSettingSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除短信配置")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        SmsSettingDelByIdRequest delByIdReq = new SmsSettingDelByIdRequest();
        delByIdReq.setId(id);
        return smsSettingSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除短信配置")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid SmsSettingDelByIdListRequest delByIdListReq) {
        return smsSettingSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出短信配置列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        SmsSettingListRequest listReq = JSON.parseObject(decrypted, SmsSettingListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        List<SmsSettingVO> dataRecords = smsSettingQueryProvider.list(listReq).getContext().getSmsSettingVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("短信配置列表_%s.xls", nowStr), "UTF-8");
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
    private void exportDataList(List<SmsSettingVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("调用api参数key", new SpelColumnRender<SmsSettingVO>("accessKeyId")),
            new Column("调用api参数secret", new SpelColumnRender<SmsSettingVO>("accessKeySecret")),
            new Column("短信平台类型：0：阿里云短信平台", new SpelColumnRender<SmsSettingVO>("type")),
            new Column("是否启用：0：未启用；1：启用", new SpelColumnRender<SmsSettingVO>("status")),
            new Column("创建时间", new SpelColumnRender<SmsSettingVO>("creatTime"))
        };
        excelHelper.addSheet("短信配置列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
