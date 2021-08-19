package com.wanmi.sbc.crm.rfmsetting;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.crm.api.provider.rfmsetting.RfmSettingQueryProvider;
import com.wanmi.sbc.crm.api.provider.rfmsetting.RfmSettingSaveProvider;
import com.wanmi.sbc.crm.api.request.rfmsetting.*;
import com.wanmi.sbc.crm.api.response.rfmsetting.*;
import com.wanmi.sbc.crm.bean.vo.RfmSettingVO;
import com.wanmi.sbc.util.CommonUtil;
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


@Api(description = "rfm参数配置管理API", tags = "RfmSettingController")
@RestController
@RequestMapping(value = "/rfmsetting")
public class RfmSettingController {

    @Autowired
    private RfmSettingQueryProvider rfmSettingQueryProvider;

    @Autowired
    private RfmSettingSaveProvider rfmSettingSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "分页查询rfm参数配置")
    @PostMapping("/page")
    public BaseResponse<RfmSettingPageResponse> getPage(@RequestBody @Valid RfmSettingPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("id", "desc");
        return rfmSettingQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询rfm参数配置")
    @PostMapping("/list")
    public BaseResponse<RfmSettingListResponse> getList(@RequestBody @Valid RfmSettingListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        return rfmSettingQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询rfm参数配置")
    @GetMapping("/{id}")
    public BaseResponse<RfmSettingByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        RfmSettingByIdRequest idReq = new RfmSettingByIdRequest();
        idReq.setId(id);
        return rfmSettingQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增rfm参数配置")
    @PostMapping("/add")
    public BaseResponse<RfmSettingAddResponse> add(@RequestBody @Valid RfmSettingAddRequest addReq) {
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setCreatePerson(commonUtil.getOperatorId());
        addReq.setCreateTime(LocalDateTime.now());
        return rfmSettingSaveProvider.add(addReq);
    }

    @ApiOperation(value = "修改rfm参数配置")
    @PutMapping("/modify")
    public BaseResponse<RfmSettingModifyResponse> modify(@RequestBody @Valid RfmSettingModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        modifyReq.setUpdateTime(LocalDateTime.now());
        return rfmSettingSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除rfm参数配置")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        RfmSettingDelByIdRequest delByIdReq = new RfmSettingDelByIdRequest();
        delByIdReq.setId(id);
        return rfmSettingSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除rfm参数配置")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid RfmSettingDelByIdListRequest delByIdListReq) {
        return rfmSettingSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出rfm参数配置列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        RfmSettingListRequest listReq = JSON.parseObject(decrypted, RfmSettingListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        List<RfmSettingVO> dataRecords = rfmSettingQueryProvider.list(listReq).getContext().getRfmSettingVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("rfm参数配置列表_%s.xls", nowStr), "UTF-8");
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
    private void exportDataList(List<RfmSettingVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("参数", new SpelColumnRender<RfmSettingVO>("param")),
            new Column("得分", new SpelColumnRender<RfmSettingVO>("score")),
            new Column("参数类型：0:R,1:F,2:M", new SpelColumnRender<RfmSettingVO>("type")),
            new Column("统计周期：0:近一个月，1:近3个月，2:近6个月，3:近一年", new SpelColumnRender<RfmSettingVO>("period"))
        };
        excelHelper.addSheet("rfm参数配置列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

    @ApiOperation(value = "查询rfm参数配置")
    @GetMapping("/detail")
    public BaseResponse<RfmSettingResponse> getRfmSetting(){
        return rfmSettingQueryProvider.getRfmSetting();
    }

    @ApiOperation(value = "保存rfm参数配置")
    @PostMapping("/allocation")
    public BaseResponse allocation(@RequestBody @Valid RfmSettingRequest rfmSettingRequest){
        rfmSettingRequest.setCreatePerson(commonUtil.getOperatorId());
        rfmSettingRequest.setCreateTime(LocalDateTime.now());
        return rfmSettingSaveProvider.allocation(rfmSettingRequest);
    }

}
