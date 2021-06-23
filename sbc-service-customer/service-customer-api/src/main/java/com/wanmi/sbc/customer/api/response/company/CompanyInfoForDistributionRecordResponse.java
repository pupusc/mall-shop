package com.wanmi.sbc.customer.api.response.company;

import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class CompanyInfoForDistributionRecordResponse {

   @ApiModelProperty(value = "公司信息列表")
   private List<CompanyInfoVO> companyInfoList;
}
