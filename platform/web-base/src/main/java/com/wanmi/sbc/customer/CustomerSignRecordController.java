package com.wanmi.sbc.customer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.QueryByIdListRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.customersignrecord.CustomerSignRecordQueryProvider;
import com.wanmi.sbc.customer.api.provider.customersignrecord.CustomerSignRecordSaveProvider;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;

import com.wanmi.sbc.customer.api.request.customersignrecord.*;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.customersignrecord.CustomerSignRecordAddResponse;
import com.wanmi.sbc.customer.api.response.customersignrecord.CustomerSignRecordInitInfoResponse;
import com.wanmi.sbc.customer.api.response.customersignrecord.CustomerSignRecordListResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerSignRecordVO;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Objects;


@Api(description = "用户签到记录管理API", tags = "CustomerSignRecordController")
@RestController
@RequestMapping(value = "/customer/signrecord")
public class CustomerSignRecordController {

    @Autowired
    private CustomerSignRecordQueryProvider customerSignRecordQueryProvider;

    @Autowired
    private CustomerSignRecordSaveProvider customerSignRecordSaveProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "查询用户信息")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public BaseResponse<CustomerSignRecordInitInfoResponse> getCustomerInfo() {
        String customerId = commonUtil.getOperatorId();
        BaseResponse<CustomerGetByIdResponse> customerById = customerQueryProvider.getCustomerById(
                new CustomerGetByIdRequest(customerId));
        CustomerSignRecordInitInfoResponse response = new CustomerSignRecordInitInfoResponse();
        response.setCustomerVO(customerById.getContext());
        //判断后台签到获取积分设置是否开启
        ConfigQueryRequest pointsRequest = new ConfigQueryRequest();
        pointsRequest.setConfigType(ConfigType.POINTS_BASIC_RULE_SIGN_IN.toValue());
        pointsRequest.setDelFlag(DeleteFlag.NO.toValue());
        ConfigVO pointsConfig = systemConfigQueryProvider.findByConfigTypeAndDelFlag(pointsRequest).getContext().getConfig();
        if(pointsConfig != null && pointsConfig.getStatus() == 1 ) {
            response.setPointFlag(true);
            response.setSignPoint(this.getValue(pointsConfig.getContext()));
        } else {
            response.setPointFlag(false);
        }
        //判断后台签到获取成长值设置是否开启
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setConfigType(ConfigType.GROWTH_VALUE_BASIC_RULE_SIGN_IN.toValue());
        request.setDelFlag(DeleteFlag.NO.toValue());
        ConfigVO growthConfig = systemConfigQueryProvider.findByConfigTypeAndDelFlag(request).getContext().getConfig();
        if(growthConfig != null && growthConfig.getStatus() == 1) {
            response.setGrowthFlag(true);
            response.setGrowthValue(this.getValue(growthConfig.getContext()));
        }else {
            response.setGrowthFlag(false);
        }
        //
        //判断今天是否签到
        CustomerSignRecordVO todayRecord = customerSignRecordQueryProvider.getRecordByDays(CustomerSignRecordGetByDaysRequest.builder().
                customerId(customerId).days(0L).build()).getContext().getCustomerSignRecordVO();
        //确认昨天是否签到
        CustomerSignRecordVO yesterdayRecord = customerSignRecordQueryProvider.getRecordByDays(CustomerSignRecordGetByDaysRequest.builder().
                customerId(customerId).days(1L).build()).getContext().getCustomerSignRecordVO();
        if(Objects.isNull(yesterdayRecord)) {
            if(Objects.isNull(todayRecord)) {
                //连续签到置为0
                response.getCustomerVO().setSignContinuousDays(0);
            }else {
                response.getCustomerVO().setSignContinuousDays(1);
            }
        }
        if(Objects.isNull(todayRecord)) {
            response.setSignFlag(false);
        } else {
            response.setSignFlag(true);
        }
        return BaseResponse.success(response);
    }

    @ApiOperation(value = "列表查询当月用户签到记录")
    @RequestMapping(value = "/listByMonth", method = RequestMethod.GET)
    public BaseResponse<CustomerSignRecordListResponse> getListByThisMonth() {
        CustomerSignRecordListRequest listReq = new CustomerSignRecordListRequest();
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.setCustomerId(commonUtil.getOperatorId());
        BaseResponse<CustomerSignRecordListResponse> response = customerSignRecordQueryProvider.listByMonth(listReq);
        return response;
    }

    @ApiOperation(value = "列表查询用户签到记录")
    @PostMapping("/list")
    public BaseResponse<CustomerSignRecordListResponse> getList(@RequestBody @Valid CustomerSignRecordListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.setCustomerId(commonUtil.getOperatorId());
        listReq.putSort("signRecord", "desc");
        return customerSignRecordQueryProvider.list(listReq);
    }

    @ApiOperation(value = "新增用户签到记录")
    @RequestMapping(value = "/add/{signTerminal}", method = RequestMethod.GET)
    public BaseResponse<CustomerSignRecordAddResponse> add(@PathVariable(value = "signTerminal") String signTerminal) {
        //判断今天是否已经签到完成
        CustomerSignRecordVO toDayRecord = customerSignRecordQueryProvider.getRecordByDays(CustomerSignRecordGetByDaysRequest.builder().
                customerId(commonUtil.getOperatorId()).days(0L).build()).getContext().getCustomerSignRecordVO();
        if(!Objects.isNull(toDayRecord)) {
            return BaseResponse.FAILED();

        }
        //添加用户签到记录
        CustomerSignRecordAddRequest addRecordReq = new CustomerSignRecordAddRequest();
        addRecordReq.setCustomerId(commonUtil.getOperatorId());
        addRecordReq.setSignRecord(LocalDateTime.now());
        addRecordReq.setDelFlag(DeleteFlag.NO);
        addRecordReq.setSignIp(commonUtil.getOperator().getIp());
        if(TerminalSource.MINIPROGRAM == commonUtil.getTerminal()){
            signTerminal = "minipro";
        }
        addRecordReq.setSignTerminal(signTerminal);
        return customerSignRecordSaveProvider.add(addRecordReq);
    }

    @ApiOperation(value = "根据id删除用户签到记录")
    @DeleteMapping("/{signRecordId}")
    public BaseResponse deleteById(@PathVariable String signRecordId) {
        if (signRecordId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        CustomerSignRecordDelByIdRequest delByIdReq = new CustomerSignRecordDelByIdRequest();
        delByIdReq.setSignRecordId(signRecordId);
        return customerSignRecordSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除用户签到记录")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid CustomerSignRecordDelByIdListRequest delByIdListReq) {
        return customerSignRecordSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出用户签到记录列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        CustomerSignRecordListRequest listReq = JSON.parseObject(decrypted, CustomerSignRecordListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("signRecordId", "desc");
        List<CustomerSignRecordVO> dataRecords = customerSignRecordQueryProvider.list(listReq).getContext().getCustomerSignRecordVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("用户签到记录列表_%s.xls", nowStr), "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            exportDataList(dataRecords, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 积分/成长值转换
     * @param value
     * @return
     */
    private Long getValue(String value){
        if(StringUtils.isNotBlank(value)){
            return JSONObject.parseObject(value).getLong("value");
        }else{
            return null;
        }

    }

    /**
     * 导出列表数据具体实现
     */
    private void exportDataList(List<CustomerSignRecordVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("用户id", new SpelColumnRender<CustomerSignRecordVO>("customerId")),
            new Column("签到日期记录", new SpelColumnRender<CustomerSignRecordVO>("signRecord"))
        };
        excelHelper.addSheet("用户签到记录列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
