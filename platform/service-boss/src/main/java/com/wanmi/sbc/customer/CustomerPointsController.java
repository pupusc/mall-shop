package com.wanmi.sbc.customer;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailPageRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailQueryRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsStatisticsQueryRequest;
import com.wanmi.sbc.customer.api.response.points.CustomerPointsDetailPageResponse;
import com.wanmi.sbc.customer.api.response.points.CustomerPointsStatisticsResponse;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailForPageVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "会员积分API", tags = "BossCustomerPointsController")
@Slf4j
@RestController
@RequestMapping(value = "/customer/points")
public class CustomerPointsController {

    @Autowired
    private CustomerPointsDetailQueryProvider customerPointsDetailQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 导出文件名后的时间后缀
     */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    /**
     * 分页查询会员积分列表
     *
     * @param customerDetailQueryRequest
     * @return 会员信息
     */
    @ApiOperation(value = "分页查询会员积分列表")
    @EmployeeCheck
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> page(@RequestBody CustomerDetailPageRequest customerDetailQueryRequest) {
        customerDetailQueryRequest.putSort("customer.pointsAvailable", SortType.DESC.toValue());
        return ResponseEntity.ok(customerQueryProvider.page(customerDetailQueryRequest));
    }

    @ApiOperation(value = "分页查询会员积分增减记录")
    @EmployeeCheck
    @RequestMapping(value = "/pageDetail", method = RequestMethod.POST)
    public BaseResponse<CustomerPointsDetailPageResponse> pageDetail(@RequestBody @Valid CustomerPointsDetailQueryRequest request) {
        if (CollectionUtils.isNotEmpty(request.getEmployeeIds())) {
            List<String> customerIdList = customerQueryProvider.page(CustomerDetailPageRequest.builder().employeeIds(request.getEmployeeIds()).build())
                    .getContext()
                    .getDetailResponseList()
                    .stream()
                    .map(item -> item.getCustomerId())
                    .collect(Collectors.toList());
            request.setCustomerIdList(customerIdList);
        }
        return customerPointsDetailQueryProvider.page(request);
    }

    @ApiOperation(value = "查询积分历史累计发放及使用")
    @EmployeeCheck
    @RequestMapping(value = "/queryIssueStatistics", method = RequestMethod.POST)
    public BaseResponse<CustomerPointsStatisticsResponse> queryIssueStatistics(@RequestBody CustomerPointsStatisticsQueryRequest request) {
        if (CollectionUtils.isNotEmpty(request.getEmployeeIds())) {
            List<String> customerIdList = customerQueryProvider.page(CustomerDetailPageRequest.builder().employeeIds(request.getEmployeeIds()).build())
                    .getContext()
                    .getDetailResponseList()
                    .stream()
                    .map(item -> item.getCustomerId())
                    .collect(Collectors.toList());
            request.setCustomerIdList(customerIdList);
        }
        return customerPointsDetailQueryProvider.queryIssueStatistics(request);
    }

    /**
     * 会员积分列表导出
     *
     * @return
     */
    @ApiOperation(value = "会员积分列表导出")
    @EmployeeCheck
    @RequestMapping(value = "/export/{encrypted}", method = RequestMethod.GET)
    public void exportDataStatictics(CustomerDetailPageRequest req,  @PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        List<CustomerDetailForPageVO> dataRecords = new ArrayList<>();
        // 分批次查询，避免出现hibernate事物超时
        for (int i = 0; i < 5; i++) {
            CustomerDetailPageRequest customerDetailQueryRequest = JSON.parseObject(decrypted, CustomerDetailPageRequest.class);
            customerDetailQueryRequest.putSort("customer.pointsAvailable", SortType.DESC.toValue());
            customerDetailQueryRequest.setPageNum(i);
            customerDetailQueryRequest.setPageSize(100);
            customerDetailQueryRequest.setEmployeeIds(req.getEmployeeIds());
            List<CustomerDetailForPageVO> data = customerQueryProvider.page(customerDetailQueryRequest)
                    .getContext()
                    .getDetailResponseList();
            dataRecords.addAll(data);
            if (data.size() < 100) {
                break;
            }
        }


        String headerKey = "Content-Disposition";
        LocalDateTime dateTime = LocalDateTime.now();
        String fileName = String.format("积分列表_%s.xls", dateTime.format(formatter));
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("/dataAnalysis/dataStatistics/export/{encrypted}, fileName={}, error={}", fileName, e);
        }
        String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
        response.setHeader(headerKey, headerValue);

        try {
            export(dataRecords, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new SbcRuntimeException(e);
        }
    }

    public void export(List<CustomerDetailForPageVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("会员名称", new SpelColumnRender<CustomerDetailForPageVO>("customerName")),
                new Column("账号", new SpelColumnRender<CustomerDetailForPageVO>("customerAccount")),
                new Column("账号状态", (cell, object) -> {
                    CustomerDetailForPageVO d = (CustomerDetailForPageVO) object;
                    if (d.getCustomerStatus().equals(CustomerStatus.ENABLE)) {
                        cell.setCellValue("启用");
                    } else {
                        cell.setCellValue("禁用");
                    }
                }),
                new Column("积分余额", new SpelColumnRender<CustomerDetailForPageVO>("pointsAvailable")),
        };
        excelHelper.addSheet(
                "积分列表",
                columns,
                dataRecords
        );
        excelHelper.write(outputStream);
    }

}
