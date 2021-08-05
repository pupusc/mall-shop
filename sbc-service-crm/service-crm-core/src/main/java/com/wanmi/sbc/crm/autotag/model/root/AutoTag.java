package com.wanmi.sbc.crm.autotag.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;

import com.wanmi.sbc.crm.bean.enums.RelationType;
import com.wanmi.sbc.crm.bean.enums.TagType;
import com.wanmi.sbc.crm.bean.vo.TagRuleVO;
import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;

/**
 * <p>自动标签实体类</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@Data
@Entity
@Table(name = "auto_tag")
public class AutoTag extends BaseEntity {
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
	 * 会员人数
	 */
	@Column(name = "customer_count")
	private Long customerCount;

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
	 * 删除标识，0：未删除，1：已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

    /**
     * 规则信息JSON字符串，参照TagRuleVO类
     * @see TagRuleVO List<TagRuleVO>的json内容
     */
    @Column(name = "rule_json")
	private String ruleJson;

    /**
     * 规则信息JSON字符串，参照TagRuleVO类
     * 为了供SQL分析使用，里面
     * @see TagRuleVO List<TagRuleVO>的json内容
     */
    @Column(name = "rule_json_sql")
    private String ruleJsonSql;

    /**
     * 系统标识，0：非系统，1：系统
     */
    @Column(name = "system_flag")
    private Boolean systemFlag;

	/**
	 * 系统标签id
	 */
	@Column(name = "system_tag_id")
	private Long systemTagId;

}