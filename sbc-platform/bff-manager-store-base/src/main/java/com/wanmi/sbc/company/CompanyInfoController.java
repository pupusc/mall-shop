package com.wanmi.sbc.company;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoProvider;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.api.request.company.CompanyInformationSaveRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoResponse;
import com.wanmi.sbc.customer.api.response.company.CompanyInformationModifyResponse;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @Author: songhanlin
 * @Date: Created In 下午7:03 2017/11/1
 * @Description: 公司信息Controller
 */
@Api(tags = "CompanyInfoController", description = "公司信息 API")
@RestController("supplierCompanyInfoController")
@RequestMapping("/company")
public class CompanyInfoController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyInfoController.class);

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private CompanyInfoProvider companyInfoProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 工商信息修改
     * @param request
     * @return
     */
    @ApiOperation(value = "工商信息修改")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse<CompanyInformationModifyResponse> update(@Valid @RequestBody CompanyInformationSaveRequest request) {
        if(!Objects.equals(commonUtil.getCompanyInfoId(), request.getCompanyInfoId())) {
            throw new SbcRuntimeException(CommonErrorCode.PERMISSION_DENIED);
        }
        operateLogMQUtil.convertAndSend("设置","店铺信息","编辑店铺信息");
        return companyInfoProvider.modifyCompanyInformation(request);
    }

    /**
     * 查询公司信息
     * @return
     */
    @ApiOperation(value = "查询公司信息")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<CompanyInfoResponse> findOne() {
        CompanyInfoResponse response = new CompanyInfoResponse();
        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(commonUtil.getCompanyInfoId()).build()
        ).getContext();
        BeanUtils.copyProperties(companyInfo, response);
        return BaseResponse.success(response);
    }

}
