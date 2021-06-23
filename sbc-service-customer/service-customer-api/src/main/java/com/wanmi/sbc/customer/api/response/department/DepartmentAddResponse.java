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
 * <p>部门管理新增结果</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的部门管理信息
     */
    @ApiModelProperty(value = "已新增的部门管理信息")
    private DepartmentVO departmentVO;
}
