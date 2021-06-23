package com.wanmi.sbc.providerreturnorder;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.request.refund.RefundOrderByReturnOrderNoRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByConditionRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderPageRequest;
import com.wanmi.sbc.order.api.response.refund.RefundOrderByReturnOrderNoResponse;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.providerreturnorder.service.ProviderReturnExportService;
import com.wanmi.sbc.returnorder.request.ReturnExportRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 供应商退单处理
 * @Autho qiaokang
 * @Date：2020-03-29 14:39
 */
@Api(tags = "ProviderReturnOrderController", description = "退货服务API")
@RestController
@RequestMapping("/providerReturn")
@Validated
@Slf4j
public class ProviderReturnOrderController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private RefundOrderQueryProvider refundOrderQueryProvider;

    @Autowired
    private ProviderReturnExportService providerReturnExportService;

    /**
     * 导出文件名后的时间后缀
     */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    /**
     * 供应商分页查询退单信息
     * @param request
     * @return
     */
    @ApiOperation(value = "供应商分页查询退单信息")
    @EmployeeCheck
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<ReturnOrderVO>> page(@RequestBody ReturnOrderPageRequest request) {
        request.setProviderCompanyInfoId(commonUtil.getCompanyInfoId());
        MicroServicePage<ReturnOrderVO> page = returnOrderQueryProvider.page(request).getContext().getReturnOrderPage();
        page.getContent().forEach(returnOrder -> {
            RefundOrderByReturnOrderNoResponse refundOrderByReturnCodeResponse = refundOrderQueryProvider.getByReturnOrderNo(new RefundOrderByReturnOrderNoRequest(returnOrder.getId())).getContext();
            if (Objects.nonNull(refundOrderByReturnCodeResponse)) {
                returnOrder.setRefundStatus(refundOrderByReturnCodeResponse.getRefundStatus());
            }
        });
        return BaseResponse.success(page);
    }

    /**
     * 导出退单
     *
     * @param encrypted
     * @param response
     */
    @ApiOperation(value = "导出退单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}", method = RequestMethod.GET)
    public void exportByParams(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted));
        ReturnExportRequest returnExportRequest = JSON.parseObject(decrypted, ReturnExportRequest.class);

        Operator operator = commonUtil.getOperator();
        log.debug("/providerReturn/export/params, employeeId=" + operator.getUserId());
        returnExportRequest.setProviderCompanyInfoId(Long.parseLong(operator.getAdminId()));

        ReturnOrderByConditionRequest conditionRequest = new ReturnOrderByConditionRequest();
        KsBeanUtil.copyPropertiesThird(returnExportRequest, conditionRequest);
        List<ReturnOrderVO> returnOrders =
                returnOrderQueryProvider.listByCondition(conditionRequest).getContext().getReturnOrderList();

        String headerKey = "Content-Disposition";
        LocalDateTime dateTime = LocalDateTime.now();
        String fileName = String.format("批量导出退单_%s.xls", dateTime.format(formatter));
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("/providerReturn/export/params, fileName={}, error={}", fileName, e);
        }
        String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
        response.setHeader(headerKey, headerValue);
        try {
            providerReturnExportService.export(returnOrders, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new SbcRuntimeException(e);
        }
    }


}
