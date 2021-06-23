package com.wanmi.sbc.customer.api.response.company;

import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 公司信息查询
 * @Author: daiyitian
 * @Date: Created In 上午11:38 2017/11/14
 * @Description: 公司信息Response
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyTypeModifyResponse extends CompanyInfoVO implements Serializable {


    private static final long serialVersionUID = -2426273139099738867L;
}
