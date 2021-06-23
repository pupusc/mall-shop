package com.wanmi.sbc.setting.authority.model.pk;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleFunctionRelaPK implements Serializable {

	private static final long serialVersionUID = -8349705525996917628L;
	/**
	 * 角色标识
	 */
	private Long roleInfoId;
	/**
	 * 功能标识
	 */
	private String functionId;

}
