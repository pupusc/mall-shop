package com.wanmi.sbc.customer.api.response.employee;

import com.wanmi.sbc.customer.bean.vo.RoleInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: songhanlin
 * @Date: Created In 17:15 2018-12-19
 * @Description: 角色Response
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoQueryResponse implements Serializable {
    private static final long serialVersionUID = 1261280854938790287L;

    @ApiModelProperty(value = "角色信息")
    private RoleInfoVO roleInfoVO;
}
