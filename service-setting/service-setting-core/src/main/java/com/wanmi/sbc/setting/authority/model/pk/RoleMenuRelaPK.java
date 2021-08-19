package com.wanmi.sbc.setting.authority.model.pk;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleMenuRelaPK implements Serializable {

	private static final long serialVersionUID = -8349705525996917628L;

	/**
     * 角色标识
	 */
	private Long roleInfoId;

	/**
     * 菜单标识
	 */
	private String menuId;
	
}
