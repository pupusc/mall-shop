package com.wanmi.sbc.livecompany;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.LiveErrCodeUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.livecompany.LiveCompanyProvider;
import com.wanmi.sbc.customer.api.provider.livecompany.LiveCompanyQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyPageRequest;
import com.wanmi.sbc.customer.api.request.livecompany.*;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoPageResponse;
import com.wanmi.sbc.customer.api.response.company.CompanyReponse;
import com.wanmi.sbc.customer.api.response.livecompany.*;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.EmployeeVO;
import com.wanmi.sbc.customer.bean.vo.LiveCompanyVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.bean.vo.LiveGoodsVO;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;


@Api(description = "直播商家管理API", tags = "LiveCompanyController")
@RestController
@RequestMapping(value = "/livecompany")
public class LiveCompanyController {

    @Autowired
    private LiveCompanyQueryProvider liveCompanyQueryProvider;
    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    @Autowired
    private LiveCompanyProvider liveCompanyProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @ApiOperation(value = "分页查询直播商家")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public BaseResponse<LiveCompanyPagePackResponse>  getPage(@RequestBody @Valid LiveCompanyQueryRequest pageReq) {

        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("id", "desc");
        return liveCompanyQueryProvider.pageNew(pageReq);
//        BaseResponse<LiveCompanyPageResponse> p = liveCompanyQueryProvider.page(pageReq);
//        //根据直播商家companyInfoId 去查询商家详细信息
//        Map<Long, String> collect = p.getContext().getLiveCompanyVOPage().getContent().stream().collect(Collectors.toMap(LiveCompanyVO::getCompanyInfoId, liveCompanyVO -> {
//            String auditReason = liveCompanyVO.getAuditReason();
//            if (auditReason == null) {
//                auditReason = "";
//            }
//            return auditReason;
//        }));
//        if(CollectionUtils.isNotEmpty(collect.keySet())) {
//            CompanyPageRequest request = new CompanyPageRequest();
//            request.setCompanyInfoIds(new ArrayList<>(collect.keySet()));
//            request.setDeleteFlag(DeleteFlag.NO);
//            request.setAccountName(pageReq.getAccountName());
//            request.setStoreName(pageReq.getStoreName());
//            request.putSort("createTime", SortType.DESC.toValue());
//            Page<CompanyInfoVO> page = companyInfoQueryProvider.pageCompanyInfo(request).getContext()
//                    .getCompanyInfoVOPage();
//
//            List<CompanyReponse> companyReponseList = new ArrayList<>();
//            page.getContent().forEach(info -> {
//                //组装返回结构
//                CompanyReponse companyReponse = new CompanyReponse();
//                companyReponse.setCompanyInfoId(info.getCompanyInfoId());
//                companyReponse.setCompanyCode(info.getCompanyCode());
//                companyReponse.setCompanyType(info.getCompanyType());
//                companyReponse.setSupplierName(info.getSupplierName());
//                companyReponse.setAuditReason(collect.get(info.getCompanyInfoId()));
//                if (CollectionUtils.isNotEmpty(info.getEmployeeVOList())) {
//                    EmployeeVO employee = info.getEmployeeVOList().get(0);
//                    companyReponse.setAccountName(employee.getAccountName());
//                    companyReponse.setAccountState(employee.getAccountState());
//                    companyReponse.setAccountDisableReason(employee.getAccountDisableReason());
//                }
//                if (nonNull(info.getStoreVOList()) && !info.getStoreVOList().isEmpty()) {
//                    StoreVO store = info.getStoreVOList().get(0);
//                    companyReponse.setStoreId(store.getStoreId());
//                    companyReponse.setStoreName(store.getStoreName());
//                    companyReponse.setContractStartDate(store.getContractStartDate());
//                    companyReponse.setContractEndDate(store.getContractEndDate());
//                    companyReponse.setAuditState(store.getAuditState());
//                   // companyReponse.setAuditReason(store.getAuditReason());
//                    companyReponse.setStoreState(store.getStoreState());
//                    companyReponse.setStoreClosedReason(store.getStoreClosedReason());
//                    companyReponse.setApplyEnterTime(store.getApplyEnterTime());
//                    companyReponse.setStoreType(store.getStoreType());
//                }
//                companyReponseList.add(companyReponse);
//            });
//            PageImpl<CompanyReponse> newPage = new PageImpl<>(companyReponseList, request.getPageable(), page.getTotalElements());
//            MicroServicePage<CompanyReponse> microPage = new MicroServicePage<>(newPage, pageReq.getPageable());
//            return BaseResponse.success(new LiveCompanyPagePackResponse(microPage));
//        }
//        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "列表查询直播商家")
    @PostMapping("/list")
    public BaseResponse<LiveCompanyListResponse> getList(@RequestBody @Valid LiveCompanyListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        return liveCompanyQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据storeId查询直播商家开通状态")
    @RequestMapping(value = "/{storeId}",method = RequestMethod.GET)
    public BaseResponse<LiveCompanyByIdResponse> getById(@PathVariable Long storeId) {
        if (storeId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        LiveCompanyByIdRequest idReq = new LiveCompanyByIdRequest();
        idReq.setStoreId(storeId);
        BaseResponse<LiveCompanyByIdResponse> byId = liveCompanyQueryProvider.getById(idReq);
        return  byId;
    }

    @ApiOperation(value = "supplier端申请开通直播")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public BaseResponse<LiveCompanyAddResponse> add(@RequestBody @Valid LiveCompanyAddRequest addReq) {
        //判断直播开关开关是否开启
        this.isOpen();
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setCreateTime(LocalDateTime.now());
        return liveCompanyProvider.add(addReq);
    }

    @ApiOperation(value = "直播商家审核")
    @RequestMapping(value = "/modify",method = RequestMethod.PUT)
    public BaseResponse<LiveCompanyModifyResponse> modify(@RequestBody @Valid LiveCompanyModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        modifyReq.setUpdateTime(LocalDateTime.now());
        return liveCompanyProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除直播商家")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        LiveCompanyDelByIdRequest delByIdReq = new LiveCompanyDelByIdRequest();
        delByIdReq.setId(id);
        return liveCompanyProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除直播商家")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid LiveCompanyDelByIdListRequest delByIdListReq) {
        return liveCompanyProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出直播商家列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        LiveCompanyListRequest listReq = JSON.parseObject(decrypted, LiveCompanyListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        List<LiveCompanyVO> dataRecords = liveCompanyQueryProvider.list(listReq).getContext().getLiveCompanyVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("直播商家列表_%s.xls", nowStr), "UTF-8");
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
    private void exportDataList(List<LiveCompanyVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
            new Column("提交审核时间", new SpelColumnRender<LiveCompanyVO>("submitTime")),
            new Column("直播状态 0未开通，1待审核，2已开通，3审核未通过，4禁用中", new SpelColumnRender<LiveCompanyVO>("liveBroadcastStatus")),
            new Column("直播审核原因", new SpelColumnRender<LiveCompanyVO>("auditReason")),
            new Column("创建人", new SpelColumnRender<LiveCompanyVO>("createPerson")),
            new Column("删除人", new SpelColumnRender<LiveCompanyVO>("deletePerson")),
            new Column("删除时间", new SpelColumnRender<LiveCompanyVO>("deleteTime")),
            new Column("公司信息ID", new SpelColumnRender<LiveCompanyVO>("companyInfoId")),
            new Column("店铺id", new SpelColumnRender<LiveCompanyVO>("storeId"))
        };
        excelHelper.addSheet("直播商家列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }
    /**
     * 判断直播开关是否开启
     */
    public void isOpen() {
        ConfigQueryRequest configQueryRequest = new ConfigQueryRequest();
        configQueryRequest.setDelFlag(0);
        configQueryRequest.setConfigKey("liveSwitch");
        configQueryRequest.setConfigType("liveSwitch");
        Integer status = systemConfigQueryProvider.findByConfigKeyAndDelFlag(configQueryRequest).getContext().getConfigVOList().get(0).getStatus();
        if (status == 0) {
            throw new SbcRuntimeException("10001", LiveErrCodeUtil.getErrCodeMessage(10001));
        }
    }
}
