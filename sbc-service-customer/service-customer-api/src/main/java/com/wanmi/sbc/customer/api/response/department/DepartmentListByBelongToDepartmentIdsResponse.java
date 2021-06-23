package com.wanmi.sbc.customer.api.response.department;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>部门管理列表结果</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentListByBelongToDepartmentIdsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID集合
     */
    @ApiModelProperty(value = "部门ID集合")
    private List<String> deparmentIds;
}
