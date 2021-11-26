package com.wanmi.sbc.crm.customerplansms.model.root;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>运营计划与短信关联实体类</p>
 * @author dyt
 * @date 2020-01-10 11:12:50
 */
@Data
@Entity
@Table(name = "customer_plan_sms_rel")
public class CustomerPlanSmsRel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 标识
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 签名id
	 */
	@Column(name = "sign_id")
	private Long signId;

    /**
     * 签名名称
     */
    @Column(name = "sign_name")
    private String signName;

	/**
	 * 模板id
	 */
	@Column(name = "template_code")
	private String templateCode;

	/**
	 * 模板内容
	 */
	@Column(name = "template_content")
	private String templateContent;

	/**
	 * 计划id
	 */
	@Column(name = "plan_id")
	private Long planId;

}