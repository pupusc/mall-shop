package com.wanmi.sbc.paidcardcustomerrel;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.BusinessCodeGenUtils;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelSaveProvider;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.*;
import com.wanmi.sbc.customer.api.response.paidcardcustomerrel.*;
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
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.elastic.api.provider.customer.EsCustomerDetailProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerDetailInitRequest;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "付费会员管理API", tags = "PaidCardCustomerRelController")
@RestController
@RequestMapping(value = "/paidcardcustomerrel")
public class PaidCardCustomerRelController {

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private PaidCardCustomerRelSaveProvider paidCardCustomerRelSaveProvider;

    @Autowired
    private EsCustomerDetailProvider esCustomerDetailProvider;

    @ApiOperation(value = "分页查询付费会员")
    @PostMapping("/page")
    public BaseResponse<PaidCardCustomerRelPageResponse> getPage(@RequestBody @Valid PaidCardCustomerRelPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("id", "desc");
        return paidCardCustomerRelQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询付费会员")
    @PostMapping("/list")
    public BaseResponse<PaidCardCustomerRelListResponse> getList(@RequestBody @Valid PaidCardCustomerRelListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        return paidCardCustomerRelQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询付费会员")
    @GetMapping("/{id}")
    public BaseResponse<PaidCardCustomerRelByIdResponse> getById(@PathVariable String id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PaidCardCustomerRelByIdRequest idReq = new PaidCardCustomerRelByIdRequest();
        idReq.setId(id);
        return paidCardCustomerRelQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增付费会员")
    @PostMapping("/add")
    @GlobalTransactional
    public BaseResponse<PaidCardCustomerRelAddResponse> add(@RequestBody @Valid PaidCardCustomerRelAddRequest addReq) {
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setCardNo(BusinessCodeGenUtils.genPaidCardCode());
        addReq.setPaidSource(1);
        addReq.setSendExpireMsgFlag(Boolean.FALSE);
        addReq.setSendMsgFlag(Boolean.FALSE);
        paidCardCustomerRelSaveProvider.add(addReq);
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "修改付费会员")
    @PutMapping("/modify")
    public BaseResponse<PaidCardCustomerRelModifyResponse> modify(@RequestBody @Valid PaidCardCustomerRelModifyRequest modifyReq) {
        return paidCardCustomerRelSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除付费会员")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable String id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PaidCardCustomerRelDelByIdRequest delByIdReq = new PaidCardCustomerRelDelByIdRequest();
        delByIdReq.setId(id);
        return paidCardCustomerRelSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除付费会员")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid PaidCardCustomerRelDelByIdListRequest delByIdListReq) {
        return paidCardCustomerRelSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出付费会员列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        PaidCardCustomerRelListRequest listReq = JSON.parseObject(decrypted, PaidCardCustomerRelListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        List<PaidCardCustomerRelVO> dataRecords = paidCardCustomerRelQueryProvider.list(listReq).getContext().getPaidCardCustomerRelVOList();

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
    private void exportDataList(List<PaidCardCustomerRelVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("会员id", new SpelColumnRender<PaidCardCustomerRelVO>("customerId")),
            new Column("付费会员类型ID", new SpelColumnRender<PaidCardCustomerRelVO>("paidCardId")),
            new Column("开始时间", new SpelColumnRender<PaidCardCustomerRelVO>("beginTime")),
            new Column("结束时间", new SpelColumnRender<PaidCardCustomerRelVO>("endTime")),
            new Column("卡号", new SpelColumnRender<PaidCardCustomerRelVO>("cardNo"))
        };
        excelHelper.addSheet("付费会员列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
