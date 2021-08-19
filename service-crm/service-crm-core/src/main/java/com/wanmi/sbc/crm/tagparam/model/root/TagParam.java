package com.wanmi.sbc.crm.tagparam.model.root;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>标签参数实体类</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@Data
@Entity
@Table(name = "tag_param")
public class TagParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 标签维度id
	 */
	@Column(name = "tag_dimension_id")
	private Long tagDimensionId;

	/**
	 * 维度配置名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 字段名称
	 */
	@Column(name = "column_name")
	private String columnName;

	/**
	 * 维度配置类型，0：top类型，1：聚合结果类型，2：查询条件类型
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 标签维度类型，0：top类型，1：count计数类型，2：sum求和，3：avg平均值，4：max最大值，5：min最小值，6：in包含类型，7：等于，8、区间类，9：多重期间or关系
	 */
	@Column(name = "tag_dimension_type")
	private Integer tagDimensionType;

    /**
     * 标签参数类型 1:指标值参数
     */
    @Column(name = "tag_type")
    private Integer tagType;

}