package com.wanmi.sbc.customer.api.response.employee;

import com.wanmi.sbc.customer.bean.vo.RoleInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/11 9:26
 * @Version: 1.0
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoListResponse implements Serializable {

    private static final long serialVersionUID = 5559608328484054281L;

    @ApiModelProperty(value = "角色列表")
    private List<RoleInfoVO> roleInfoVOList;
}
