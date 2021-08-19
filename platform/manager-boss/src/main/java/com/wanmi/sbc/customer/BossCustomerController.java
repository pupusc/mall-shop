package com.wanmi.sbc.customer;

import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.aop.PageNumCheck;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.CustomerEditRequest;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoQueryRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerAddRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailPageForSupplierRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.NoDeleteCustomerGetByAccountRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValuePageRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoGetResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerDetailPageForSupplierResponse;
import com.wanmi.sbc.customer.api.response.customer.NoDeleteCustomerGetByAccountResponse;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailToEsVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerGrowthValueVO;
import com.wanmi.sbc.customer.validator.CustomerValidator;
import com.wanmi.sbc.elastic.api.provider.customer.EsCustomerDetailProvider;
import com.wanmi.sbc.elastic.api.provider.customer.EsCustomerDetailQueryProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerDetailAddRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerDetailModifyRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerDetailPageRequest;
import com.wanmi.sbc.elastic.bean.dto.customer.EsCustomerDetailDTO;
import com.wanmi.sbc.elastic.bean.dto.customer.EsStoreCustomerRelaDTO;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderModifyEmployeeIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeUpdateEmployeeIdRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 会员
 * Created by hht on 2017/4/19.
 */
@Api(description = "平台会员API", tags = "BossCustomerController")
@RestController
@RequestMapping(value = "/customer")
public class BossCustomerController {

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CustomerProvider customerProvider;

    @Autowired
    private CustomerValidator customerValidator;

    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private CustomerGrowthValueQueryProvider customerGrowthValueQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EsCustomerDetailProvider esCustomerDetailProvider;

    @Autowired
    private EsCustomerDetailQueryProvider esCustomerDetailQueryProvider;

    @InitBinder
    public void initBinder(DataBinder binder) {
        if (binder.getTarget() instanceof CustomerEditRequest) {
            binder.setValidator(customerValidator);
        }
    }


    /**
     * S2b-Boss端修改会员
     * 修改会员表，修改会员详细信息
     *
     * @return
     */
    @ApiOperation(value = "平台端修改会员")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> updateCustomerAll(@RequestBody CustomerModifyRequest customerModifyRequest) {
        String employeeId = commonUtil.getOperatorId();
        customerModifyRequest.setOperator(employeeId);
        //获取原业务员
        CustomerDetailVO detail = customerDetailQueryProvider.getCustomerDetailByCustomerId(
                CustomerDetailByCustomerIdRequest.builder().customerId(customerModifyRequest.getCustomerId()).build()
        ).getContext();
        String oldEmployeeId = Objects.nonNull(detail) ? detail.getEmployeeId() : null;
        CustomerDetailToEsVO customerDetailToEsVO = customerProvider.modifyCustomer(customerModifyRequest).getContext().getCustomerDetailToEsVO();
        //如更换业务员，将历史订单和历史退单的负责业务员更新为新业务员
        if(StringUtils.isNotBlank(customerModifyRequest.getEmployeeId()) && (!customerModifyRequest.getEmployeeId().equals(oldEmployeeId))){
            tradeProvider.updateEmployeeId(TradeUpdateEmployeeIdRequest.builder()
                    .employeeId(customerModifyRequest.getEmployeeId())
                    .customerId(customerModifyRequest.getCustomerId())
                    .build());
            returnOrderProvider.modifyEmployeeId(ReturnOrderModifyEmployeeIdRequest.builder()
                    .employeeId(customerModifyRequest.getEmployeeId())
                    .customerId(customerModifyRequest.getCustomerId()).build());
        }

        //操作日志记录
        operateLogMQUtil.convertAndSend("客户", "编辑客户",
                "编辑客户：" + customerModifyRequest.getCustomerAccount());

        EsCustomerDetailDTO esCustomerDetailDTO =  KsBeanUtil.convert(customerDetailToEsVO, EsCustomerDetailDTO.class);
        esCustomerDetailProvider.modify(new EsCustomerDetailModifyRequest(esCustomerDetailDTO));
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }


    /**
     * 分页查询会员
     *
     * @param customerDetailQueryRequest
     * @return 会员信息
     */
    @ApiOperation(value = "分页查询会员")
    @EmployeeCheck
    @PageNumCheck
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> page(@RequestBody EsCustomerDetailPageRequest customerDetailQueryRequest) {
        return ResponseEntity.ok(esCustomerDetailQueryProvider.page(customerDetailQueryRequest));
    }

    /**
     * 分页查询会员
     *
     * @param customerDetailQueryRequest
     * @return 会员信息
     */
    @ApiOperation(value = "分页查询会员")
    @EmployeeCheck
    @RequestMapping(value = "/page/supplierLevel", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> page(@RequestBody CustomerDetailPageForSupplierRequest customerDetailQueryRequest) {
        customerDetailQueryRequest.putSort("createTime", SortType.DESC.toValue());
        customerDetailQueryRequest.setShowAreaFlag(true);
        BaseResponse<CustomerDetailPageForSupplierResponse> customerDetailPageForSupplierResponseBaseResponse =
                customerQueryProvider.pageForS2bSupplier(customerDetailQueryRequest);
        return ResponseEntity.ok(customerDetailPageForSupplierResponseBaseResponse);
    }

    /**
     * Boss端保存会员
     *
     * @return
     */
    @ApiOperation(value = "平台端保存会员")
    @EmployeeCheck
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> addCustomerAll(@Valid @RequestBody CustomerAddRequest customerAddRequest,
                                                       HttpServletRequest request) {
        //账号已存在
        NoDeleteCustomerGetByAccountResponse customer = customerQueryProvider.getNoDeleteCustomerByAccount(new NoDeleteCustomerGetByAccountRequest
                (customerAddRequest.getCustomerAccount())).getContext();
        if (customer != null) {
            throw new SbcRuntimeException("K-010002");
        }
        String employeeId = ((Claims) request.getAttribute("claims")).get("employeeId").toString();
        customerAddRequest.setOperator(employeeId);
        customerAddRequest.setCustomerType(CustomerType.PLATFORM);
        CustomerDetailToEsVO customerDetailToEsVO = customerProvider.saveCustomer(customerAddRequest).getContext().getCustomerDetailToEsVO();
        //操作日志记录
        operateLogMQUtil.convertAndSend("客户", "新增客户",
                "新增客户：" + customerAddRequest.getCustomerAccount());

        EsCustomerDetailDTO esCustomerDetailDTO =  KsBeanUtil.convert(customerDetailToEsVO, EsCustomerDetailDTO.class);
        if (Objects.nonNull(customerDetailToEsVO.getStoreCustomerRela())) {
            List<EsStoreCustomerRelaDTO> esStoreCustomerRelaList = new ArrayList<>();
            esStoreCustomerRelaList.add(KsBeanUtil.convert(customerDetailToEsVO.getStoreCustomerRela(), EsStoreCustomerRelaDTO.class));
            esCustomerDetailDTO.setEsStoreCustomerRelaList(esStoreCustomerRelaList);
        }
        esCustomerDetailProvider.add(new EsCustomerDetailAddRequest(esCustomerDetailDTO));

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }


    /**
     * 获取客户归属商家的商家名称
     *
     * @param customerId
     * @return
     */
    @ApiOperation(value = "获取客户归属商家的商家名称")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "客户id", required = true)
    @RequestMapping(value = "/supplier/name/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse<String>> getBelongSupplier(@PathVariable("customerId")String customerId){
        CompanyInfoQueryRequest request = new CompanyInfoQueryRequest();
        request.setCustomerId(customerId);
        CompanyInfoGetResponse companyInfo = storeCustomerQueryProvider.getCompanyInfoBelongByCustomerId(request).getContext();
        return ResponseEntity.ok(BaseResponse.success(companyInfo.getSupplierName()));
    }

    @ApiOperation(value = "分页查询会员成长值")
    @RequestMapping(value = "/queryToGrowthValue", method = RequestMethod.POST)
    public ResponseEntity<MicroServicePage<CustomerGrowthValueVO>> queryGrowthValue(@RequestBody CustomerGrowthValuePageRequest customerGrowthValuePageRequest) {
        return ResponseEntity.ok(customerGrowthValueQueryProvider.page(customerGrowthValuePageRequest).getContext()
                .getCustomerGrowthValueVOPage());
    }

}
