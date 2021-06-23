package com.wanmi.sbc.fdpaidcast;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.fdpaidcast.FdPaidCastQueryProvider;
import com.wanmi.sbc.customer.api.provider.fdpaidcast.FdPaidCastProvider;
import com.wanmi.sbc.customer.api.request.fdpaidcast.*;
import com.wanmi.sbc.customer.api.response.fdpaidcast.*;
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
import com.wanmi.sbc.customer.bean.vo.FdPaidCastVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "樊登付费类型 映射商城付费类型管理API", tags = "FdPaidCastController")
@RestController
@RequestMapping(value = "/fdpaidcast")
public class FdPaidCastController {

    @Autowired
    private FdPaidCastQueryProvider fdPaidCastQueryProvider;

    @Autowired
    private FdPaidCastProvider fdPaidCastProvider;

    @ApiOperation(value = "分页查询樊登付费类型 映射商城付费类型")
    @PostMapping("/page")
    public BaseResponse<FdPaidCastPageResponse> getPage(@RequestBody @Valid FdPaidCastPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("id", "desc");
        return fdPaidCastQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询樊登付费类型 映射商城付费类型")
    @PostMapping("/list")
    public BaseResponse<FdPaidCastListResponse> getList(@RequestBody @Valid FdPaidCastListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        return fdPaidCastQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询樊登付费类型 映射商城付费类型")
    @GetMapping("/{id}")
    public BaseResponse<FdPaidCastByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        FdPaidCastByIdRequest idReq = new FdPaidCastByIdRequest();
        idReq.setId(id);
        return fdPaidCastQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增樊登付费类型 映射商城付费类型")
    @PostMapping("/add")
    public BaseResponse<FdPaidCastAddResponse> add(@RequestBody @Valid FdPaidCastAddRequest addReq) {
        addReq.setDelFlag(DeleteFlag.NO);
        return fdPaidCastProvider.add(addReq);
    }

    @ApiOperation(value = "修改樊登付费类型 映射商城付费类型")
    @PutMapping("/modify")
    public BaseResponse<FdPaidCastModifyResponse> modify(@RequestBody @Valid FdPaidCastModifyRequest modifyReq) {
        return fdPaidCastProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除樊登付费类型 映射商城付费类型")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        FdPaidCastDelByIdRequest delByIdReq = new FdPaidCastDelByIdRequest();
        delByIdReq.setId(id);
        return fdPaidCastProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除樊登付费类型 映射商城付费类型")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid FdPaidCastDelByIdListRequest delByIdListReq) {
        return fdPaidCastProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出樊登付费类型 映射商城付费类型列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        FdPaidCastListRequest listReq = JSON.parseObject(decrypted, FdPaidCastListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        List<FdPaidCastVO> dataRecords = fdPaidCastQueryProvider.list(listReq).getContext().getFdPaidCastVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("樊登付费类型 映射商城付费类型列表_%s.xls", nowStr), "UTF-8");
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
    private void exportDataList(List<FdPaidCastVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("樊登付费会员类型", new SpelColumnRender<FdPaidCastVO>("fdPayType")),
            new Column("商城付费会员类型id", new SpelColumnRender<FdPaidCastVO>("paidMemberId")),
            new Column("删除时间", new SpelColumnRender<FdPaidCastVO>("deleteTime"))
        };
        excelHelper.addSheet("樊登付费类型 映射商城付费类型列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
