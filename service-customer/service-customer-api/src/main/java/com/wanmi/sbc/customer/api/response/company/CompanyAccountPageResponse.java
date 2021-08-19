package com.wanmi.sbc.customer.api.response.company;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.CompanyAccountVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商家账号分页响应
 * Created by sunkun on 2017/12/4.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAccountPageResponse implements Serializable {

    private static final long serialVersionUID = -499117397630725286L;

    /**
     * 公司信息列表
     */
    @ApiModelProperty(value = "公司信息列表")
    private MicroServicePage<CompanyAccountVO> companyAccountVOPage;

}
