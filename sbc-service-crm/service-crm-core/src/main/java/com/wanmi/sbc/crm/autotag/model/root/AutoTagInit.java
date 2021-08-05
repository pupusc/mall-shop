package com.wanmi.sbc.crm.autotag.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.crm.bean.enums.RelationType;
import com.wanmi.sbc.crm.bean.enums.TagType;
import com.wanmi.sbc.crm.bean.vo.TagRuleVO;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>自动标签实体类</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@Data
@Entity
@Table(name = "auto_tag_init")
public class AutoTagInit implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 自动标签名称
	 */
	@Column(name = "tag_name")
	private String tagName;

	/**
	 * 标签类型，0：偏好标签组，1：指标值标签，2：指标值范围标签，3、综合类标签
	 */
	@Column(name = "type")
	private TagType type;

    /**
     * 规则天数
     */
    @Column(name = "day")
    private Integer day;

	/**
	 * 一级维度且或关系，0：且，1：或
	 */
	@Column(name = "relation_type")
	private RelationType relationType;

    /**
     * 规则信息JSON字符串
     * @see TagRuleVO
     */
    @Column(name = "rule_json")
	private String ruleJson;

    /**
     * 规则信息JSON字符串
     * @see TagRuleVO
     */
    @Column(name = "rule_json_sql")
    private String ruleJsonSql;

}