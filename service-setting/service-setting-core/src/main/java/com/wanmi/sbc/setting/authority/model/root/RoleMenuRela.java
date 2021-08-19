package com.wanmi.sbc.setting.authority.model.root;

import com.wanmi.sbc.setting.authority.model.pk.RoleMenuRelaPK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 角色菜单关联信息
 * Created by bail on 2017-12-28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(RoleMenuRelaPK.class)
@Table(name = "role_menu_rela")
public class RoleMenuRela implements Serializable {

	private static final long serialVersionUID = -8349705525996917628L;

	/**
     * 角色标识
	 */
	@Id
	@Column(name = "role_info_id")
	private Long roleInfoId;

	/**
     * 菜单标识
	 */
	@Id
	@Column(name = "menu_id")
	private String menuId;
	
}
