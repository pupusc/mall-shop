package com.wanmi.sbc.customer.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentSortDTO implements Serializable {

    private static final long serialVersionUID = -9015124894584238109L;

    /**
     * 部门Id
     */
    private String departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 公司ID
     */
    private Long companyInfoId;

    /**
     * 层级
     */
    private Integer departmentGrade;

    /**
     * 主管ID
     */
    private String employeeId;

    /**
     * 主管名称
     */
    private String employeeName;


    /**
     * 排序
     */
    private Integer departmentSort;

    /**
     * 父部门id（上一级）
     */
    private String parentDepartmentId;

    /**
     * 员工数
     */
    private Integer employeeNum;

    /**
     * 父部门id集合（多级）
     */
    private String parentDepartmentIds;

    /**
     * 删除标志 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标志")
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    private String createPerson;


    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    private String updatePerson;
}
