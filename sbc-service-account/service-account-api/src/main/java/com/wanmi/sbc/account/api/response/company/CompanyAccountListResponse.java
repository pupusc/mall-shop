package com.wanmi.sbc.account.api.response.company;

import com.wanmi.sbc.account.bean.vo.CompanyAccountVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 商家收款账户列表响应
 * Created by daiyitian on 2017/11/30.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAccountListResponse implements Serializable {

    private static final long serialVersionUID = -2629974480473851524L;

    /**
     * 收款账户列表数据 {@link CompanyAccountVO}
     */
    @ApiModelProperty(value = "收款账户列表数据")
    private List<CompanyAccountVO> companyAccountVOList;
}
