package com.wanmi.sbc.system;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.elastic.api.provider.operationlog.EsOperationLogQueryProvider;
import com.wanmi.sbc.elastic.api.request.operationlog.EsOperationLogListRequest;
import com.wanmi.sbc.elastic.api.response.operationlog.EsOperationLogPageResponse;
import com.wanmi.sbc.setting.api.provider.OperationLogQueryProvider;
import com.wanmi.sbc.setting.api.request.OperationLogListRequest;
import com.wanmi.sbc.setting.api.response.OperationLogListResponse;
import com.wanmi.sbc.setting.api.response.OperationLogPageResponse;
import com.wanmi.sbc.setting.bean.vo.OperationLogVO;
import com.wanmi.sbc.util.CommonUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 * 操作日志Controller
 * Created by CHENLI on 2017/5/12.
 */
@Api(tags = "OperationLogController", description = "操作日志")
@RestController
public class OperationLogController {

    @Autowired
    private OperationLogQueryProvider operationLogQueryProvider;

    @Autowired
    private EsOperationLogQueryProvider esOperationLogQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * 运营端和商家端查询最近10次登录信息
     *
     * @return
     */
    @ApiOperation(value = "运营端和商家端查询最近10次登录信息")
    @RequestMapping(value = "/system/operationLog", method = RequestMethod.GET)
    public BaseResponse<OperationLogListResponse> query(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        String employeeId = claims.get("employeeId").toString();
        String platform = Objects.toString(claims.get("platform"), StringUtils.EMPTY);//平台

        OperationLogListRequest queryRequest = new OperationLogListRequest();
        if (Platform.PLATFORM.toValue().equals(platform)) {
            queryRequest.setStoreId(0L);
            queryRequest.setCompanyInfoId(0L);
        }
        if (Platform.SUPPLIER.toValue().equals(platform)) {
            queryRequest.setStoreId(Long.valueOf(Objects.toString(claims.get("storeId"), "")));
            queryRequest.setCompanyInfoId(Long.valueOf(Objects.toString(claims.get("adminId"), "")));
        }
        if (Platform.PROVIDER.toValue().equals(platform)) {
            queryRequest.setStoreId(Long.valueOf(Objects.toString(claims.get("storeId"), "")));
            queryRequest.setCompanyInfoId(Long.valueOf(Objects.toString(claims.get("adminId"), "")));
        }

        queryRequest.setOpModule("登录");
        queryRequest.setEmployeeId(employeeId);
        return operationLogQueryProvider.list(queryRequest);
    }

    /**
     * 根据条件查询操作日志 运营端和商家端
     *
     * @param queryRequest
     * @param request
     * @return
     */
    @ApiOperation(value = "根据条件查询操作日志 运营端和商家端")
    @PostMapping("/system/operationLog/queryOpLogByCriteria")
    public BaseResponse<EsOperationLogPageResponse> queryByCriteria(@RequestBody EsOperationLogListRequest queryRequest,
                                                                    HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        String platform = Objects.toString(claims.get("platform"), StringUtils.EMPTY);//平台

        if (Platform.PLATFORM.toValue().equals(platform)) {
            queryRequest.setStoreId(0L);
            queryRequest.setCompanyInfoId(0L);
        }
        if (Platform.SUPPLIER.toValue().equals(platform) || Platform.PROVIDER.toValue().equals(platform)) {
            queryRequest.setStoreId(Long.valueOf(Objects.toString(claims.get("storeId"), "")));
            queryRequest.setCompanyInfoId(Long.valueOf(Objects.toString(claims.get("adminId"), "")));
        }
        this.validTimeRange(queryRequest.getBeginTime(), queryRequest.getEndTime());
        return esOperationLogQueryProvider.queryOpLogByCriteria(queryRequest);
    }

    /**
     * 校验时间范围
     *
     * @param beginTime
     * @param endTime
     */
    private void validTimeRange(String beginTime, String endTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_1);
        //页面传过来的时间是YYYY-MM-dd HH:mm:00 和 YYYY-MM-dd HH:mm:59 为了校验 所以改成 -59-1 = -1分钟
        LocalDateTime endTimeLocal = LocalDateTime.parse(endTime, dateTimeFormatter).plusMonths(-3).plusMinutes(-1);
        LocalDateTime beginTimeLocal = LocalDateTime.parse(beginTime, dateTimeFormatter);
        if (!endTimeLocal.isBefore(beginTimeLocal)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "开始时间和结束时间需在三个月之内");
        }
    }

    @ApiOperation(value = "导出操作日志")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @GetMapping(value = "/system/operationLog/exportOperationLogByParams/{encrypted}")
    public void exportOperationLogByParams(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        OperationLogListRequest operationLogListRequest = JSON.parseObject(decrypted, OperationLogListRequest.class);

        //校验时间范围
        this.validTimeRange(operationLogListRequest.getBeginTime(), operationLogListRequest.getEndTime());
        Operator operator = commonUtil.getOperator();
        if (Platform.PLATFORM.equals(operator.getPlatform())) {
            operationLogListRequest.setStoreId(0L);
            operationLogListRequest.setCompanyInfoId(0L);
        } else if (Platform.SUPPLIER.equals(operator.getPlatform())) {
            operationLogListRequest.setStoreId(Long.valueOf(operator.getStoreId()));
            operationLogListRequest.setCompanyInfoId(Long.valueOf(operator.getAdminId()));
        }

        BaseResponse<OperationLogListResponse> baseResponse = operationLogQueryProvider.exportOpLogByCriteria(operationLogListRequest);

        String headerKey = "Content-Disposition";
        LocalDateTime dateTime = LocalDateTime.now();
        String fileName = String.format("操作日志-%s.xls", dateTime.format(DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_3)));
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("/system/operationLog/exportOperationLogByParams, fileName={},err={}", fileName, e);
        }
        String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
        response.setHeader(headerKey, headerValue);

        try {
            this.export(baseResponse.getContext().getLogVOList(), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            LOGGER.error("/system/operationLog/exportOperationLogByParams 导出失败：{}", e.getMessage());
            throw new SbcRuntimeException(e);
        }
    }

    /**
     * @param opLogList
     * @param outputStream
     */
    private void export(List<OperationLogVO> opLogList, OutputStream outputStream) {
        ExcelHelper<OperationLogVO> excelHelper = new ExcelHelper<>();
        Column[] columns = {
                new Column("操作人账号", new SpelColumnRender<OperationLogVO>("opAccount")),
                new Column("操作人姓名", new SpelColumnRender<OperationLogVO>("opName")),
                new Column("操作人IP", new SpelColumnRender<OperationLogVO>("opIp")),
                new Column("操作时间", new SpelColumnRender<OperationLogVO>("opTime")),
                new Column("模块", new SpelColumnRender<OperationLogVO>("opModule")),
                new Column("操作类型", new SpelColumnRender<OperationLogVO>("opCode")),
                new Column("操作内容", new SpelColumnRender<OperationLogVO>("opContext"))
        };
        excelHelper.addSheet("操作日志", columns, opLogList);
        excelHelper.write(outputStream);
    }
}
