package com.wanmi.sbc.customer.api.response.department;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.DepartmentVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>部门管理分页结果</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部门管理分页结果
     */
    @ApiModelProperty(value = "部门管理分页结果")
    private MicroServicePage<DepartmentVO> departmentVOPage;
}
