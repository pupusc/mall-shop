package com.wanmi.sbc.message.smssend;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.message.api.provider.smssend.SmsSendQueryProvider;
import com.wanmi.sbc.message.api.provider.smssend.SmsSendSaveProvider;
import com.wanmi.sbc.message.api.request.smssend.*;
import com.wanmi.sbc.message.api.response.smssend.*;
import com.wanmi.sbc.message.bean.vo.SmsSendVO;
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

@Deprecated
// @Api(description = "短信发送管理API", tags = "_SmsSendController")
@RestController
@RequestMapping(value = "/smssend")
public class SmsSendController_ {

    @Autowired
    private SmsSendQueryProvider smsSendQueryProvider;

    @Autowired
    private SmsSendSaveProvider smsSendSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

   // @ApiOperation(value = "分页查询短信发送")
    @PostMapping("/page")
    public BaseResponse<SmsSendPageResponse> getPage(@RequestBody @Valid SmsSendPageRequest pageReq) {
        pageReq.putSort("id", "desc");
        return smsSendQueryProvider.page(pageReq);
    }

   // @ApiOperation(value = "列表查询短信发送")
    @PostMapping("/list")
    public BaseResponse<SmsSendListResponse> getList(@RequestBody @Valid SmsSendListRequest listReq) {
        listReq.putSort("id", "desc");
        return smsSendQueryProvider.list(listReq);
    }

   // @ApiOperation(value = "根据id查询短信发送")
    @GetMapping("/{id}")
    public BaseResponse<SmsSendByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        SmsSendByIdRequest idReq = new SmsSendByIdRequest();
        idReq.setId(id);
        return smsSendQueryProvider.getById(idReq);
    }

   // @ApiOperation(value = "新增短信发送")
    @PostMapping("/add")
    public BaseResponse<SmsSendAddResponse> add(@RequestBody @Valid SmsSendAddRequest addReq) {
        addReq.setCreatePerson(commonUtil.getOperatorId());
        addReq.setCreateTime(LocalDateTime.now());
        return smsSendSaveProvider.add(addReq);
    }

   // @ApiOperation(value = "修改短信发送")
    @PutMapping("/modify")
    public BaseResponse<SmsSendModifyResponse> modify(@RequestBody @Valid SmsSendModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        modifyReq.setUpdateTime(LocalDateTime.now());
        return smsSendSaveProvider.modify(modifyReq);
    }

   // @ApiOperation(value = "根据id删除短信发送")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        SmsSendDelByIdRequest delByIdReq = new SmsSendDelByIdRequest();
        delByIdReq.setId(id);
        return smsSendSaveProvider.deleteById(delByIdReq);
    }

    //@ApiOperation(value = "根据idList批量删除短信发送")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid SmsSendDelByIdListRequest delByIdListReq) {
        return smsSendSaveProvider.deleteByIdList(delByIdListReq);
    }

    //@ApiOperation(value = "导出短信发送列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        SmsSendListRequest listReq = JSON.parseObject(decrypted, SmsSendListRequest.class);
        listReq.putSort("id", "desc");
        List<SmsSendVO> dataRecords = smsSendQueryProvider.list(listReq).getContext().getSmsSendVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("短信发送列表_%s.xls", nowStr), "UTF-8");
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
    private void exportDataList(List<SmsSendVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("短信内容", new SpelColumnRender<SmsSendVO>("context")),
            new Column("模板id", new SpelColumnRender<SmsSendVO>("templateCode")),
            new Column("签名id", new SpelColumnRender<SmsSendVO>("signId")),
            new Column("接收人描述", new SpelColumnRender<SmsSendVO>("receiveContext")),
            new Column("接收类型（0-全部，1-会员等级，2-会员人群，3-自定义）", new SpelColumnRender<SmsSendVO>("receiveType")),
            new Column("接收人明细", new SpelColumnRender<SmsSendVO>("receiveValue")),
            new Column("手工添加的号码", new SpelColumnRender<SmsSendVO>("manualAdd")),
            new Column("状态（0-未开始，1-进行中，2-已结束，3-任务失败）", new SpelColumnRender<SmsSendVO>("status")),
            new Column("任务执行信息", new SpelColumnRender<SmsSendVO>("message")),
            new Column("发送类型（0-立即发送，1-定时发送）", new SpelColumnRender<SmsSendVO>("sendType")),
            new Column("发送时间", new SpelColumnRender<SmsSendVO>("sendTime")),
            new Column("预计发送条数", new SpelColumnRender<SmsSendVO>("rowCount"))
        };
        excelHelper.addSheet("短信发送列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
