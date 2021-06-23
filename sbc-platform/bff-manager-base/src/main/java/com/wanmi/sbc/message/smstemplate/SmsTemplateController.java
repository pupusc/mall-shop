package com.wanmi.sbc.message.smstemplate;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.node.*;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.crm.api.provider.customerplan.CustomerPlanQueryProvider;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanListRequest;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanVO;
import com.wanmi.sbc.customer.bean.enums.SmsTemplate;
import com.wanmi.sbc.message.api.provider.smstemplate.SmsTemplateQueryProvider;
import com.wanmi.sbc.message.api.provider.smstemplate.SmsTemplateSaveProvider;
import com.wanmi.sbc.message.api.request.smstemplate.*;
import com.wanmi.sbc.message.api.response.smstemplate.*;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.bean.enums.SmsType;
import com.wanmi.sbc.message.bean.enums.node.CustomerManageType;
import com.wanmi.sbc.message.bean.vo.SmsPurposeVO;
import com.wanmi.sbc.message.bean.vo.SmsTemplateVO;
import com.wanmi.sbc.message.smstemplate.response.SmsPurposeListResponse;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Api(description = "短信模板管理API", tags = "SmsTemplateController")
@RestController
@RequestMapping(value = "/smstemplate")
public class SmsTemplateController {

    @Autowired
    private SmsTemplateQueryProvider smsTemplateQueryProvider;

    @Autowired
    private SmsTemplateSaveProvider smsTemplateSaveProvider;

    @Autowired
    private CustomerPlanQueryProvider customerPlanQueryProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @ApiOperation(value = "分页查询短信模板")
    @PostMapping("/page")
    public BaseResponse<SmsTemplatePageResponse> getPage(@RequestBody @Valid SmsTemplatePageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("id", "desc");
        return smsTemplateQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询短信模板")
    @PostMapping("/list")
    public BaseResponse<SmsTemplateListResponse> getList(@RequestBody @Valid SmsTemplateListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        return smsTemplateQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询短信模板")
    @GetMapping("/{id}")
    public BaseResponse<SmsTemplateByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        SmsTemplateByIdRequest idReq = new SmsTemplateByIdRequest();
        idReq.setId(id);
        return smsTemplateQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增短信模板")
    @PostMapping("/add")
    public BaseResponse<SmsTemplateAddResponse> add(@RequestBody @Valid SmsTemplateAddRequest addReq) {
        //填充用途
        addReq.setPurpose(chgBusinessTypeToName(addReq.getBusinessType(), addReq.getTemplateType()));
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setCreateTime(LocalDateTime.now());
        addReq.setReviewStatus(ReviewStatus.PENDINGREVIEW);
        //如果是验证码类短信校验签名id、业务类型和用途是否为空
        if ((addReq.getTemplateType() == SmsType.VERIFICATIONCODE || addReq.getTemplateType() == SmsType.NOTICE)
                &&(Objects.isNull(addReq.getSignId()) || StringUtils.isBlank(addReq.getBusinessType()) || StringUtils.isBlank(addReq.getPurpose()))
        ) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        checkTemplateName(SmsTemplateListRequest.builder().templateName(addReq.getTemplateName()).delFlag(DeleteFlag.NO).build());
        checkTemplateCode(SmsTemplateListRequest.builder().businessType(addReq.getBusinessType()).build());
        return smsTemplateSaveProvider.add(addReq);
    }

    @ApiOperation(value = "修改短信模板")
    @PutMapping("/modify")
    public BaseResponse<SmsTemplateModifyResponse> modify(@RequestBody @Valid SmsTemplateModifyRequest modifyReq) {
        //填充用途
        modifyReq.setPurpose(chgBusinessTypeToName(modifyReq.getBusinessType(), modifyReq.getTemplateType()));
        //如果是验证码类短信校验签名id、业务类型和用途是否为空
        if ((modifyReq.getTemplateType() == SmsType.VERIFICATIONCODE  || modifyReq.getTemplateType() == SmsType.NOTICE)
                &&(Objects.isNull(modifyReq.getSignId()) || StringUtils.isBlank(modifyReq.getBusinessType()) || StringUtils.isBlank(modifyReq.getPurpose()))
        ) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        checkTemplateName(SmsTemplateListRequest.builder().id(modifyReq.getId()).templateName(modifyReq.getTemplateName()).delFlag(DeleteFlag.NO).build());
        checkTemplateCode(SmsTemplateListRequest.builder().id(modifyReq.getId()).businessType(modifyReq.getBusinessType()).build());
        return smsTemplateSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除短信模板")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        SmsTemplateVO templateVO = smsTemplateQueryProvider.getById(SmsTemplateByIdRequest.builder().id(id).build()).getContext().getSmsTemplateVO();
        if(Objects.nonNull(templateVO)) {
            this.checkCustomerPlan(templateVO.getTemplateCode());
        }
        SmsTemplateDelByIdRequest delByIdReq = new SmsTemplateDelByIdRequest();
        delByIdReq.setId(id);
        return smsTemplateSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除短信模板")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid SmsTemplateDelByIdListRequest delByIdListReq) {
        return smsTemplateSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "短信平台短信模板同步")
    @PostMapping("/synchronize-platform-sms-template")
    public BaseResponse synchronizePlatformSmsTemplate() {
        return smsTemplateSaveProvider.synchronizePlatformSmsTemplate();
    }

    @ApiOperation(value = "同步短信平台短信模板数据")
    @PostMapping("/syn-platform-history-sms-template")
    public BaseResponse synPlatformHistorySmsTemplate(@RequestBody @Valid SyncPlatformHistorySmsTemplateRequest request) {
        return smsTemplateSaveProvider.synPlatformHistorySmsTemplate(request);
    }

    @ApiOperation(value = "导出短信模板列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        SmsTemplateListRequest listReq = JSON.parseObject(decrypted, SmsTemplateListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        List<SmsTemplateVO> dataRecords = smsTemplateQueryProvider.list(listReq).getContext().getSmsTemplateVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("短信模板列表_%s.xls", nowStr), "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            exportDataList(dataRecords, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    @ApiOperation(value = "提交备案")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "id", value = "模板Id", required = true)
    @PutMapping("/upload/{id}")
    public BaseResponse upload(@PathVariable Long id) {
        return smsTemplateSaveProvider.upload(SmsTemplateUploadByIdRequest.builder().id(id).build());
    }

    @ApiOperation(value = "用途列表")
    @PostMapping("/purposes")
    public BaseResponse<SmsPurposeListResponse> purposes() {
        return BaseResponse.success(SmsPurposeListResponse.builder().smsPurposeList(listPurpose()).build());
    }

    @ApiOperation(value = "通知节点列表")
    @PostMapping("/notices")
    public BaseResponse<SmsPurposeListResponse> notices() {
        return BaseResponse.success(SmsPurposeListResponse.builder().smsPurposeList(listNode()).build());
    }

    @ApiOperation(value = "修改开关标识")
    @PutMapping("/open-flag")
    public BaseResponse modifyOpenFlag(@RequestBody @Valid SmsTemplateModifyOpenFlagByIdRequest request) {
        return smsTemplateSaveProvider.modifyOpenFlagById(request);
    }

    /**
     * 导出列表数据具体实现
     */
    private void exportDataList(List<SmsTemplateVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("模板名称", new SpelColumnRender<SmsTemplateVO>("templateName")),
                new Column("模板内容", new SpelColumnRender<SmsTemplateVO>("templateContent")),
                new Column("短信模板申请说明", new SpelColumnRender<SmsTemplateVO>("remark")),
                new Column("短信类型。其中： 0：验证码。 1：短信通知。 2：推广短信。 短信类型,0：验证码,1：短信通知,2：推广短信,3：国际/港澳台消息。", new SpelColumnRender<SmsTemplateVO>("templateType")),
                new Column("模板状态，0：待审核，1：审核通过，2：审核未通过", new SpelColumnRender<SmsTemplateVO>("reviewStatus")),
                new Column("模板code,模板审核通过返回的模板id，发送短信时使用", new SpelColumnRender<SmsTemplateVO>("templateCode")),
                new Column("审核原因", new SpelColumnRender<SmsTemplateVO>("reviewReason")),
                new Column("短信配置id", new SpelColumnRender<SmsTemplateVO>("smsSettingId"))
        };
        excelHelper.addSheet("短信模板列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

    /**
     * @return boolean
     * @Author lvzhenwei
     * @Description 校验短信模板名称是否存在--编辑短信模板
     * @Date 17:03 2019/12/9
     * @Param [smsTemplateListReq]
     **/
    private void checkTemplateName(SmsTemplateListRequest smsTemplateListReq) {
        List<SmsTemplateVO> smsTemplateVOList = smsTemplateQueryProvider.list(
                SmsTemplateListRequest.builder().equalTemplateName(smsTemplateListReq.getTemplateName()).delFlag(DeleteFlag.NO).build())
                .getContext().getSmsTemplateVOList();
        if(CollectionUtils.isNotEmpty(smsTemplateVOList)){
            if (Objects.nonNull(smsTemplateListReq.getId()) && !smsTemplateVOList.get(0).getId().equals(smsTemplateListReq.getId())) {
                throw new SbcRuntimeException("K-300302");
            }else if(Objects.isNull(smsTemplateListReq.getId())){
                throw new SbcRuntimeException("K-300302");
            }
        }
    }

    private void checkTemplateCode(SmsTemplateListRequest smsTemplateListReq) {
        if(StringUtils.isNotBlank(smsTemplateListReq.getBusinessType())) {
            List<SmsTemplateVO> smsTemplateVOList = smsTemplateQueryProvider.list(
                    SmsTemplateListRequest.builder().businessType(smsTemplateListReq.getBusinessType()).delFlag(DeleteFlag.NO).build())
                    .getContext().getSmsTemplateVOList();
            if(CollectionUtils.isNotEmpty(smsTemplateVOList)){
                if (Objects.nonNull(smsTemplateListReq.getId()) && !smsTemplateVOList.get(0).getId().equals(smsTemplateListReq.getId())) {
                    throw new SbcRuntimeException("K-300304");
                }else if(Objects.isNull(smsTemplateListReq.getId())){
                    throw new SbcRuntimeException("K-300304");
                }
            }
        }
    }

    /***
     * 验测是否被计划引用
     * @param templateCode 模板id
     */
    private void checkCustomerPlan(String templateCode){
        //验证是否购买CRM
        TradeConfigGetByTypeRequest typeRequest = new TradeConfigGetByTypeRequest();
        typeRequest.setConfigType(ConfigType.CRM_FLAG);
        Integer status = auditQueryProvider.getTradeConfigByType(typeRequest).getContext().getStatus();
        if(Constants.yes.equals(status)) {
            //判断计划名称是否已经存在
            List<CustomerPlanVO> customerPlanList = customerPlanQueryProvider
                    .list(CustomerPlanListRequest.builder().templateCode(templateCode).notEndStatus(Boolean.TRUE).delFlag(DeleteFlag.NO).build())
                    .getContext().getCustomerPlanList();
            if (CollectionUtils.isNotEmpty(customerPlanList)) {
                throw new SbcRuntimeException("K-200403");
            }
        }
    }

    private List<SmsPurposeVO> listPurpose() {
        Map<String, String> fieldMap = mapPurpose();
        return Arrays.stream(SmsTemplate.values())
                .filter(t -> !(t.equals(SmsTemplate.CUSTOMER_IMPORT_SUCCESS) || t.equals(SmsTemplate.CUSTOMER_PASSWORD)|| t.equals(SmsTemplate.EMPLOYEE_PASSWORD))).map(t -> {
            SmsPurposeVO vo = new SmsPurposeVO();
            vo.setBusinessType(t.name());
            vo.setPurpose(fieldMap.get(t.name()));
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 对用途枚举进行反射获取名称
     * @return map<key,获取名称>
     */
    private Map<String, String> mapPurpose() {
        return Arrays.stream(SmsTemplate.class.getFields())
                .collect(Collectors.toMap(Field::getName, c -> c.getAnnotation(ApiEnumProperty.class).value()));
    }

    /**
     * 在验证码类、通知类下，根据业务类型转换相应的名称
     * @param businessType
     * @param templateType
     * @return
     */
    private String chgBusinessTypeToName(String businessType, SmsType templateType) {
        if (StringUtils.isBlank(businessType)) {
            return null;
        }
        if (SmsType.NOTICE.equals(templateType)) {
            return listNode().stream().filter(t -> t.getBusinessType().equalsIgnoreCase(businessType)).findFirst().map(SmsPurposeVO::getPurpose).orElse(null);
        } else if (SmsType.VERIFICATIONCODE.equals(templateType)) {
            return mapPurpose().get(businessType);
        }
        return null;
    }

    /**
     * 根据枚举列表通知节点
     * @return
     */
    private List<SmsPurposeVO> listNode() {
        //账号安全
        List<SmsPurposeVO> voList = Arrays.stream(AccountSecurityType.values()).map(t -> {
            SmsPurposeVO vo = new SmsPurposeVO();
            vo.setBusinessType(t.getType());
            vo.setPurpose(t.getDescription());
            return vo;
        }).collect(Collectors.toList());
        //账号导入
        voList.addAll(Arrays.stream(CustomerManageType.values()).map(t -> {
            SmsPurposeVO vo = new SmsPurposeVO();
            vo.setBusinessType(t.getType());
            vo.setPurpose(t.getDescription());
            return vo;
        }).collect(Collectors.toList()));
        //账户资产
        voList.addAll(Arrays.stream(AccoutAssetsType.values()).map(t -> {
            SmsPurposeVO vo = new SmsPurposeVO();
            vo.setBusinessType(t.getType());
            vo.setPurpose(t.getDescription());
            return vo;
        }).collect(Collectors.toList()));
        //订单进度
        voList.addAll(Arrays.stream(OrderProcessType.values()).map(t -> {
            SmsPurposeVO vo = new SmsPurposeVO();
            vo.setBusinessType(t.getType());
            vo.setPurpose(t.getDescription());
            return vo;
        }).collect(Collectors.toList()));
        //退单进度
        voList.addAll(Arrays.stream(ReturnOrderProcessType.values()).map(t -> {
            SmsPurposeVO vo = new SmsPurposeVO();
            vo.setBusinessType(t.getType());
            vo.setPurpose(t.getDescription());
            return vo;
        }).collect(Collectors.toList()));
        //分销业务
        voList.addAll(Arrays.stream(DistributionType.values()).map(t -> {
            SmsPurposeVO vo = new SmsPurposeVO();
            vo.setBusinessType(t.getType());
            vo.setPurpose(t.getDescription());
            return vo;
        }).collect(Collectors.toList()));
        return voList;
    }
}
