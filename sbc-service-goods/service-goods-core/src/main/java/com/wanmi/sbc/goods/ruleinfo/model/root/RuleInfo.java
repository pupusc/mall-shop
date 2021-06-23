package com.wanmi.sbc.goods.ruleinfo.model.root;

import com.wanmi.sbc.common.base.BaseEntity;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.RuleType;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>规则说明实体类</p>
 *
 * @author zxd
 * @date 2020-05-25 18:55:56
 */
@Data
@Entity
@Table(name = "rule_info")
public class RuleInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 规则名称
     */
    @Column(name = "rule_name")
    private String ruleName;

    /**
     * 规则说明
     */
    @Column(name = "rule_content")
    private String ruleContent;

    /**
     * 规则类型 0:预约 1:预售
     */
    @Column(name = "rule_type")
    private RuleType ruleType;

    /**
     * 是否删除标志 0：否，1：是
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

}