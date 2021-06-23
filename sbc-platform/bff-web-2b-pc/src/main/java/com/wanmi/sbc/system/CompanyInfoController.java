package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.companyinfo.CompanyInfoQueryProvider;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoRopResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公司信息服务
 * Created by CHENLI on 2017/5/12.
 */
@Api(tags = "CompanyInfoController", description = "公司信息服务Api")
@RestController
public class CompanyInfoController {
    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    /**
     * 查询公司信息
     * @return
     */
    @ApiOperation(value = "查询公司信息")
    @RequestMapping(value = "/companyInfo", method = RequestMethod.GET)
    public BaseResponse<CompanyInfoRopResponse> findCompanyInfo() {
        return companyInfoQueryProvider.findCompanyInfos();
//        CompositeResponse<CompanyInfoRopResponse> ropResponse = sdkClient.buildClientRequest()
//                .get(CompanyInfoRopResponse.class, "companyInfo.find", "1.0.0");
//        return BaseResponse.success( ropResponse.getSuccessResponse());
    }

}
