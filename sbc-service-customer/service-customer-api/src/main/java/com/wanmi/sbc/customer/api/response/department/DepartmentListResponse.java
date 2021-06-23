package com.wanmi.sbc.customer.api.response.department;

import com.wanmi.sbc.customer.bean.vo.DepartmentTreeVO;
import com.wanmi.sbc.customer.bean.vo.DepartmentVO;
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
public class DepartmentListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部门管理列表结果(未处理数据)
     */
    @ApiModelProperty(value = "部门管理列表结果")
    private List<DepartmentVO> departmentVOS;

    /**
     * 部门管理列表结果（树形结构数据）
     */
    @ApiModelProperty(value = "部门管理列表结果")
    private List<DepartmentTreeVO> departmentVOList;

    /**
     * 管理部门id列表(数据隔离)
     */
    @ApiModelProperty(value = "管理部门列表")
    private List<String> manageDepartmentIdList;

    /**
     * 是否主账号 0：否，1：是
     */
    private Integer isMaster = 0 ;
}
