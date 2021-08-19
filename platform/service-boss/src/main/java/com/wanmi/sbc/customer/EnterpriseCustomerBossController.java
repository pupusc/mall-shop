package com.wanmi.sbc.customer;

import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.VASConstants;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueQueryProvider;
import com.wanmi.sbc.customer.api.request.CustomerEditRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerAddRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerEnterpriseCheckStateModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.NoDeleteCustomerGetByAccountRequest;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValuePageRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.customer.NoDeleteCustomerGetByAccountResponse;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailToEsVO;
import com.wanmi.sbc.customer.bean.vo.CustomerGrowthValueVO;
import com.wanmi.sbc.customer.validator.CustomerValidator;
import com.wanmi.sbc.elastic.api.provider.customer.EsCustomerDetailProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerDetailAddRequest;
import com.wanmi.sbc.elastic.bean.dto.customer.EsCustomerDetailDTO;
import com.wanmi.sbc.elastic.bean.dto.customer.EsStoreCustomerRelaDTO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

/**
 * 企业用户
 */
@Api(description = "企业用户API", tags = "enterpriseCustomer")
@RestController
@RequestMapping(value = "/enterpriseCustomer")
public class EnterpriseCustomerBossController {

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CustomerProvider customerProvider;

    @Autowired
    private CustomerValidator customerValidator;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private CustomerGrowthValueQueryProvider customerGrowthValueQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EsCustomerDetailProvider esCustomerDetailProvider;

    @InitBinder
    public void initBinder(DataBinder binder) {
        if (binder.getTarget() instanceof CustomerEditRequest) {
            binder.setValidator(customerValidator);
        }
    }

    /**
     * 保存企业会员
     *
     * @return
     */
    @ApiOperation(value = "保存企业会员")
    @EmployeeCheck
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> addEnterpriseCustomer(@Valid @RequestBody CustomerAddRequest request) {
        //账号已存在
        NoDeleteCustomerGetByAccountResponse customer = customerQueryProvider.getNoDeleteCustomerByAccount(new NoDeleteCustomerGetByAccountRequest
                (request.getCustomerAccount())).getContext();
        if (customer != null) {
            throw new SbcRuntimeException("K-010002");
        }
        request.setOperator(commonUtil.getOperatorId());
        request.setCustomerType(CustomerType.PLATFORM);
        request.setEnterpriseCustomer(true);
        CustomerDetailToEsVO customerDetailToEsVO =  customerProvider.saveCustomer(request).getContext().getCustomerDetailToEsVO();


        //操作日志记录
        operateLogMQUtil.convertAndSend("客户", "新增企业会员",
                "新增企业会员：" + request.getCustomerAccount());

        EsCustomerDetailDTO customerDetailDTO =  KsBeanUtil.convert(customerDetailToEsVO, EsCustomerDetailDTO.class);
        if (Objects.nonNull(customerDetailToEsVO.getStoreCustomerRela())) {
            List<EsStoreCustomerRelaDTO> esStoreCustomerRelaList = new ArrayList<>();
            esStoreCustomerRelaList.add(KsBeanUtil.convert(customerDetailToEsVO.getStoreCustomerRela(), EsStoreCustomerRelaDTO.class));
            customerDetailDTO.setEsStoreCustomerRelaList(esStoreCustomerRelaList);
        }

        esCustomerDetailProvider.add(new EsCustomerDetailAddRequest(customerDetailDTO));

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    @ApiOperation(value = "分页查询会员成长值")
    @EmployeeCheck
    @RequestMapping(value = "/queryToGrowthValue", method = RequestMethod.POST)
    public ResponseEntity<MicroServicePage<CustomerGrowthValueVO>> queryGrowthValue(@RequestBody CustomerGrowthValuePageRequest customerGrowthValuePageRequest) {
        return ResponseEntity.ok(customerGrowthValueQueryProvider.page(customerGrowthValuePageRequest).getContext()
                .getCustomerGrowthValueVOPage());
    }

    /**
     * 审核企业会员
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "审核企业会员")
    @RequestMapping(value = "/checkEnterpriseCustomer", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> checkEnterpriseCustomer(@RequestBody @Valid CustomerEnterpriseCheckStateModifyRequest request) {
        if(!commonUtil.findVASBuyOrNot(VASConstants.VAS_IEP_SETTING)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        customerProvider.checkEnterpriseCustomer(request);

        //获取企业会员
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (request.getCustomerId())).getContext();
        if (nonNull(customer)) {
            if (EnterpriseCheckState.CHECKED.equals(request.getEnterpriseCheckState())) {
                operateLogMQUtil.convertAndSend("企业会员", "审核企业会员", "审核企业会员：" + customer.getCustomerAccount());
            } else {
                operateLogMQUtil.convertAndSend("企业会员", "驳回企业会员", "驳回企业会员：" + customer.getCustomerAccount());
            }
        }
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

}
