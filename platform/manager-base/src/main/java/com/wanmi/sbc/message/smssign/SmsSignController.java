package com.wanmi.sbc.message.smssign;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.crm.api.provider.customerplan.CustomerPlanQueryProvider;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanListRequest;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanVO;
import com.wanmi.sbc.message.api.provider.smssetting.SmsSettingQueryProvider;
import com.wanmi.sbc.message.api.provider.smssign.SmsSignQueryProvider;
import com.wanmi.sbc.message.api.provider.smssign.SmsSignSaveProvider;
import com.wanmi.sbc.message.api.request.smssign.*;
import com.wanmi.sbc.message.api.response.smssign.*;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.bean.vo.SmsSignVO;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
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


@Api(description = "短信签名管理API", tags = "SmsSignController")
@RestController
@RequestMapping(value = "/smssign")
public class SmsSignController {

    @Autowired
    private SmsSignQueryProvider smsSignQueryProvider;

    @Autowired
    private SmsSignSaveProvider smsSignSaveProvider;

    @Autowired
    private SmsSettingQueryProvider smsSettingQueryProvider;

    @Autowired
    private CustomerPlanQueryProvider customerPlanQueryProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @ApiOperation(value = "分页查询短信签名")
    @PostMapping("/page")
    public BaseResponse<SmsSignPageResponse> getPage(@RequestBody @Valid SmsSignPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("id", "desc");
        return smsSignQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询短信签名")
    @PostMapping("/list")
    public BaseResponse<SmsSignListResponse> getList(@RequestBody @Valid SmsSignListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        return smsSignQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询短信签名")
    @GetMapping("/{id}")
    public BaseResponse<SmsSignByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        SmsSignByIdRequest idReq = new SmsSignByIdRequest();
        idReq.setId(id);
        return smsSignQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增短信签名")
    @PostMapping("/add")
    public BaseResponse<SmsSignAddResponse> add(@RequestBody @Valid SmsSignAddRequest addReq) {
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setCreateTime(LocalDateTime.now());
        addReq.setReviewStatus(ReviewStatus.PENDINGREVIEW);
        //校验签名名称是否存在
        checkSignName(SmsSignQueryRequest.builder().smsSignName(addReq.getSmsSignName()).delFlag(DeleteFlag.NO).build());
        return smsSignSaveProvider.add(addReq);
    }

    @ApiOperation(value = "修改短信签名")
    @PutMapping("/modify")
    public BaseResponse<SmsSignModifyResponse> modify(@RequestBody @Valid SmsSignModifyRequest modifyReq) {
        //校验签名名称是否存在
        checkSignName(SmsSignQueryRequest.builder().id(modifyReq.getId()).smsSignName(modifyReq.getSmsSignName()).delFlag(DeleteFlag.NO).build());
        return smsSignSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除短信签名")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        this.checkCustomerPlan(id);
        SmsSignDelByIdRequest delByIdReq = new SmsSignDelByIdRequest();
        delByIdReq.setId(id);
        return smsSignSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据id删除前验证")
    @GetMapping("/check/{id}")
    public BaseResponse checkById(@PathVariable Long id) {
        return smsSignQueryProvider.check(SmsSignCheckByIdRequest.builder().id(id).build());
    }

    @ApiOperation(value = "根据idList批量删除短信签名")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid SmsSignDelByIdListRequest delByIdListReq) {
        return smsSignSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "短信平台短信签名同步API")
    @PostMapping("/synchronize-platform-smsSign")
    public BaseResponse synchronizePlatformSmsSign() {
        return smsSignSaveProvider.synchronizePlatformSmsSign();
    }

    @ApiOperation(value = "同步短信平台历史短信签名数据")
    @PostMapping("/sync-platform-history-smsSign-by-names")
    public BaseResponse syncPlatformHistorySmsSignByNames(@RequestBody @Valid SyncSmsSignByNamesRequest syncSmsSignByNamesRequest) {
        return smsSignSaveProvider.syncPlatformHistorySmsSignByNames(syncSmsSignByNamesRequest);
    }

    @ApiOperation(value = "导出短信签名列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        SmsSignListRequest listReq = JSON.parseObject(decrypted, SmsSignListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        List<SmsSignVO> dataRecords = smsSignQueryProvider.list(listReq).getContext().getSmsSignVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("短信签名列表_%s.xls", nowStr), "UTF-8");
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
    private void exportDataList(List<SmsSignVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("签名名称", new SpelColumnRender<SmsSignVO>("smsSignName")),
                new Column("签名来源,0：企事业单位的全称或简称,1：工信部备案网站的全称或简称,2：APP应用的全称或简称,3：公众号或小程序的全称或简称,4：电商平台店铺名的全称或简称,5：商标名的全称或简称", new SpelColumnRender<SmsSignVO>("signSource")),
                new Column("短信签名申请说明", new SpelColumnRender<SmsSignVO>("remark")),
                new Column("是否涉及第三方利益：0：否，1：是", new SpelColumnRender<SmsSignVO>("involveThirdInterest")),
                new Column("审核状态：0:待审核，1:审核通过，2:审核未通过", new SpelColumnRender<SmsSignVO>("reviewStatus")),
                new Column("审核原因", new SpelColumnRender<SmsSignVO>("reviewReason")),
                new Column("短信配置id", new SpelColumnRender<SmsSignVO>("smsSettingId"))
        };
        excelHelper.addSheet("短信签名列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

    /**
     * @return boolean
     * @Author lvzhenwei
     * @Description 校验签名是否存在
     * @Date 16:40 2019/12/9
     * @Param [signListRequest]
     **/
    private void checkSignName(SmsSignQueryRequest signQueryRequest) {
        //判断签名名称是否已经存在
        List<SmsSignVO> smsSignVOList = smsSignQueryProvider.getBySmsSignNameAndAndDelFlag(signQueryRequest).getContext().getSmsSignVOList();
        if (CollectionUtils.isNotEmpty(smsSignVOList) && smsSignVOList.size() > 0) {
            if (Objects.nonNull(signQueryRequest.getId()) && smsSignVOList.size() == 1 && !smsSignVOList.get(0).getId().equals(signQueryRequest.getId())) {
                //抛出异常
                throw new SbcRuntimeException("K-300202");
            }else if(Objects.isNull(signQueryRequest.getId())) {
                //抛出异常
                throw new SbcRuntimeException("K-300202");
            }
        }
    }

    /***
     * 验测是否被计划引用
     * @param signId 签名id
     */
    private void checkCustomerPlan(Long signId){
        //验证是否购买CRM
        TradeConfigGetByTypeRequest typeRequest = new TradeConfigGetByTypeRequest();
        typeRequest.setConfigType(ConfigType.CRM_FLAG);
        Integer status = auditQueryProvider.getTradeConfigByType(typeRequest).getContext().getStatus();
        if(Constants.yes.equals(status)) {
            //判断计划名称是否已经存在
            List<CustomerPlanVO> customerPlanList = customerPlanQueryProvider
                    .list(CustomerPlanListRequest.builder().signId(signId).notEndStatus(Boolean.TRUE).delFlag(DeleteFlag.NO).build())
                    .getContext().getCustomerPlanList();
            if (CollectionUtils.isNotEmpty(customerPlanList)) {
                throw new SbcRuntimeException("K-200403");
            }
        }
    }
}
