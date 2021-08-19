package com.wanmi.sbc.paidcardbuyrecord;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.paidcardbuyrecord.PaidCardBuyRecordQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardbuyrecord.PaidCardBuyRecordSaveProvider;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.*;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.wanmi.sbc.customer.bean.vo.PaidCardBuyRecordVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Api(description = "付费会员管理API", tags = "PaidCardBuyRecordController")
@RestController
@RequestMapping(value = "/paidcardbuyrecord")
public class PaidCardBuyRecordController {

    @Autowired
    private PaidCardBuyRecordQueryProvider paidCardBuyRecordQueryProvider;

    @Autowired
    private PaidCardBuyRecordSaveProvider paidCardBuyRecordSaveProvider;

    private AtomicInteger exportCount = new AtomicInteger(0);

    private final Integer exportPageSize = 2000;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "分页查询付费会员")
    @PostMapping("/page")
    public BaseResponse<PaidCardBuyRecordPageResponse> getPage(@RequestBody @Valid PaidCardBuyRecordPageRequest pageReq) {
        pageReq.putSort("payCode", "desc");
        return paidCardBuyRecordQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询付费会员")
    @PostMapping("/list")
    public BaseResponse<PaidCardBuyRecordListResponse> getList(@RequestBody @Valid PaidCardBuyRecordListRequest listReq) {
        listReq.putSort("payCode", "desc");
        return paidCardBuyRecordQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询付费会员")
    @GetMapping("/{payCode}")
    public BaseResponse<PaidCardBuyRecordByIdResponse> getById(@PathVariable String payCode) {
        if (payCode == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PaidCardBuyRecordByIdRequest idReq = new PaidCardBuyRecordByIdRequest();
        idReq.setPayCode(payCode);
        return paidCardBuyRecordQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增付费会员")
    @PostMapping("/add")
    public BaseResponse<PaidCardBuyRecordAddResponse> add(@RequestBody @Valid PaidCardBuyRecordAddRequest addReq) {
        addReq.setCreateTime(LocalDateTime.now());
        return paidCardBuyRecordSaveProvider.add(addReq);
    }

    @ApiOperation(value = "修改付费会员")
    @PutMapping("/modify")
    public BaseResponse<PaidCardBuyRecordModifyResponse> modify(@RequestBody @Valid PaidCardBuyRecordModifyRequest modifyReq) {
        return paidCardBuyRecordSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除付费会员")
    @DeleteMapping("/{payCode}")
    public BaseResponse deleteById(@PathVariable String payCode) {
        if (payCode == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PaidCardBuyRecordDelByIdRequest delByIdReq = new PaidCardBuyRecordDelByIdRequest();
        delByIdReq.setPayCode(payCode);
        return paidCardBuyRecordSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除付费会员")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid PaidCardBuyRecordDelByIdListRequest delByIdListReq) {
        return paidCardBuyRecordSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出付费会员列表")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        if (exportCount.incrementAndGet() > 1) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
        }
        Operator operator = commonUtil.getOperator();
        log.debug(String.format("/goods/export/params, employeeId=%s", operator.getUserId()));
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        PaidCardBuyRecordPageRequest pageReq = JSON.parseObject(decrypted, PaidCardBuyRecordPageRequest.class);
        pageReq.setPageNum(NumberUtils.INTEGER_ZERO);
        pageReq.setPageSize(exportPageSize);
        pageReq.putSort("payCode", "desc");
        List<PaidCardBuyRecordVO> dataRecords = paidCardBuyRecordQueryProvider.page(pageReq).getContext().getPaidCardBuyRecordVOPage().getContent();
        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("付费会员列表_%s.xls", nowStr), "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            exportDataList(dataRecords, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            log.error("/paidcardbuyrecord/export error: ", e);
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }finally {
            exportCount.set(0);
        }
    }

    /**
     * 导出列表数据具体实现
     */
    private void exportDataList(List<PaidCardBuyRecordVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("流水号", new SpelColumnRender<PaidCardBuyRecordVO>("payCode")),
            new Column("客户", new SpelColumnRender<PaidCardBuyRecordVO>("customerName")),
            new Column("购买时间", new SpelColumnRender<PaidCardBuyRecordVO>("createTime")),
            new Column("卡号", new SpelColumnRender<PaidCardBuyRecordVO>("cardNo")),
            new Column("付费周期", new SpelColumnRender<PaidCardBuyRecordVO>("ruleName")),
            new Column("付费价（实付）", new SpelColumnRender<PaidCardBuyRecordVO>("price")),
        };
        excelHelper.addSheet("付费会员列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }



}
