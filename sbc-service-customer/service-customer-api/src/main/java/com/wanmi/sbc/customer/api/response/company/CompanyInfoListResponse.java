package com.wanmi.sbc.customer.api.response.company;

import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 公司信息查询
 * @Author: daiyitian
 * @Date: Created In 上午11:38 2017/11/14
 * @Description: 公司信息Response
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyInfoListResponse implements Serializable {

    private static final long serialVersionUID = 4063335105824358925L;
    /**
     * 公司信息列表
     */
    @ApiModelProperty(value = "公司信息列表")
    private List<CompanyInfoVO> companyInfoVOList;

}
