package com.wanmi.sbc.paidcardrule;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.paidcardrule.PaidCardRuleQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardrule.PaidCardRuleSaveProvider;
import com.wanmi.sbc.customer.api.request.paidcardrule.*;
import com.wanmi.sbc.customer.api.response.paidcardrule.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import com.wanmi.sbc.customer.bean.vo.PaidCardRuleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "付费会员管理API", tags = "PaidCardRuleController")
@RestController
@RequestMapping(value = "/paidcardrule")
public class PaidCardRuleController {

    @Autowired
    private PaidCardRuleQueryProvider paidCardRuleQueryProvider;

    @Autowired
    private PaidCardRuleSaveProvider paidCardRuleSaveProvider;

    @ApiOperation(value = "分页查询付费会员")
    @PostMapping("/page")
    public BaseResponse<PaidCardRulePageResponse> getPage(@RequestBody @Valid PaidCardRulePageRequest pageReq) {
        pageReq.putSort("id", "desc");
        return paidCardRuleQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询付费会员")
    @PostMapping("/list")
    public BaseResponse<PaidCardRuleListResponse> getList(@RequestBody @Valid PaidCardRuleListRequest listReq) {
        listReq.putSort("id", "desc");
        return paidCardRuleQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询付费会员")
    @GetMapping("/{id}")
    public BaseResponse<PaidCardRuleByIdResponse> getById(@PathVariable String id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PaidCardRuleByIdRequest idReq = new PaidCardRuleByIdRequest();
        idReq.setId(id);
        return paidCardRuleQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增付费会员")
    @PostMapping("/add")
    public BaseResponse<PaidCardRuleAddResponse> add(@RequestBody @Valid PaidCardRuleAddRequest addReq) {
        return paidCardRuleSaveProvider.add(addReq);
    }

    @ApiOperation(value = "修改付费会员")
    @PutMapping("/modify")
    public BaseResponse<PaidCardRuleModifyResponse> modify(@RequestBody @Valid PaidCardRuleModifyRequest modifyReq) {
        return paidCardRuleSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除付费会员")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable String id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PaidCardRuleDelByIdRequest delByIdReq = new PaidCardRuleDelByIdRequest();
        delByIdReq.setId(id);
        return paidCardRuleSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除付费会员")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid PaidCardRuleDelByIdListRequest delByIdListReq) {
        return paidCardRuleSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出付费会员列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        PaidCardRuleListRequest listReq = JSON.parseObject(decrypted, PaidCardRuleListRequest.class);
        listReq.putSort("id", "desc");
        List<PaidCardRuleVO> dataRecords = paidCardRuleQueryProvider.list(listReq).getContext().getPaidCardRuleVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("付费会员列表_%s.xls", nowStr), "UTF-8");
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
    private void exportDataList(List<PaidCardRuleVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("配置类型 0：付费配置；1：续费配置", new SpelColumnRender<PaidCardRuleVO>("type")),
            new Column("名称", new SpelColumnRender<PaidCardRuleVO>("name")),
            new Column("价格", new SpelColumnRender<PaidCardRuleVO>("price")),
            new Column("0:禁用；1：启用", new SpelColumnRender<PaidCardRuleVO>("status")),
            new Column("付费会员类型id", new SpelColumnRender<PaidCardRuleVO>("paidCardId")),
            new Column("时间单位：0天，1月，2年", new SpelColumnRender<PaidCardRuleVO>("timeUnit")),
            new Column("时间（数值）", new SpelColumnRender<PaidCardRuleVO>("timeVal"))
        };
        excelHelper.addSheet("付费会员列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
