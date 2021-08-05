package com.wanmi.sbc.crm.tagdimension.model.root;


import com.wanmi.sbc.crm.bean.enums.DimensionName;
import com.wanmi.sbc.crm.bean.enums.TagDimensionFirstLastType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>标签维度实体类</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@Data
@Entity
@Table(name = "tag_dimension")
public class TagDimension implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 维度名
	 */
	@Column(name = "name")
	private String name;

	@Column(name = "dimension_enum")
	private DimensionName dimensionName;

	/**
	 * 维度对应表明
	 */
	@Column(name = "table_name")
	private String tableName;

	/**
	 * 首次末次类型
	 */
	@Column(name = "first_last_type")
	private TagDimensionFirstLastType firstLastType;

}