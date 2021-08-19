package com.wanmi.sbc.paidcardrightsrel;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.paidcardrightsrel.PaidCardRightsRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardrightsrel.PaidCardRightsRelSaveProvider;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.*;
import com.wanmi.sbc.customer.api.response.paidcardrightsrel.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import com.wanmi.sbc.customer.bean.vo.PaidCardRightsRelVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "付费会员管理API", tags = "PaidCardRightsRelController")
@RestController
@RequestMapping(value = "/paidcardrightsrel")
public class PaidCardRightsRelController {

    @Autowired
    private PaidCardRightsRelQueryProvider paidCardRightsRelQueryProvider;

    @Autowired
    private PaidCardRightsRelSaveProvider paidCardRightsRelSaveProvider;

    @ApiOperation(value = "分页查询付费会员")
    @PostMapping("/page")
    public BaseResponse<PaidCardRightsRelPageResponse> getPage(@RequestBody @Valid PaidCardRightsRelPageRequest pageReq) {
        pageReq.putSort("id", "desc");
        return paidCardRightsRelQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询付费会员")
    @PostMapping("/list")
    public BaseResponse<PaidCardRightsRelListResponse> getList(@RequestBody @Valid PaidCardRightsRelListRequest listReq) {
        listReq.putSort("id", "desc");
        return paidCardRightsRelQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询付费会员")
    @GetMapping("/{id}")
    public BaseResponse<PaidCardRightsRelByIdResponse> getById(@PathVariable String id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PaidCardRightsRelByIdRequest idReq = new PaidCardRightsRelByIdRequest();
        idReq.setId(id);
        return paidCardRightsRelQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增付费会员")
    @PostMapping("/add")
    public BaseResponse<PaidCardRightsRelAddResponse> add(@RequestBody @Valid PaidCardRightsRelAddRequest addReq) {
        return paidCardRightsRelSaveProvider.add(addReq);
    }

    @ApiOperation(value = "修改付费会员")
    @PutMapping("/modify")
    public BaseResponse<PaidCardRightsRelModifyResponse> modify(@RequestBody @Valid PaidCardRightsRelModifyRequest modifyReq) {
        return paidCardRightsRelSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除付费会员")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable String id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PaidCardRightsRelDelByIdRequest delByIdReq = new PaidCardRightsRelDelByIdRequest();
        delByIdReq.setId(id);
        return paidCardRightsRelSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除付费会员")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid PaidCardRightsRelDelByIdListRequest delByIdListReq) {
        return paidCardRightsRelSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出付费会员列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        PaidCardRightsRelListRequest listReq = JSON.parseObject(decrypted, PaidCardRightsRelListRequest.class);
        listReq.putSort("id", "desc");
        List<PaidCardRightsRelVO> dataRecords = paidCardRightsRelQueryProvider.list(listReq).getContext().getPaidCardRightsRelVOList();

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
    private void exportDataList(List<PaidCardRightsRelVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("所属会员权益id", new SpelColumnRender<PaidCardRightsRelVO>("paidCardId")),
            new Column("权益id", new SpelColumnRender<PaidCardRightsRelVO>("rightsId"))
        };
        excelHelper.addSheet("付费会员列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
