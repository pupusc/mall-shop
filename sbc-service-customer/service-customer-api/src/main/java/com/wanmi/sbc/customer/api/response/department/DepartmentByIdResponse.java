package com.wanmi.sbc.customer.api.response.department;

import com.wanmi.sbc.customer.bean.vo.DepartmentVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）部门管理信息response</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部门管理信息
     */
    @ApiModelProperty(value = "部门管理信息")
    private DepartmentVO departmentVO;
}
