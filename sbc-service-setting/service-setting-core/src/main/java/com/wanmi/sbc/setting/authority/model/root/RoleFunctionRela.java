package com.wanmi.sbc.setting.authority.model.root;

import com.wanmi.sbc.setting.authority.model.pk.RoleFunctionRelaPK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 角色权限关联信息
 * Created by bail on 2017-01-04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(RoleFunctionRelaPK.class)
@Table(name = "role_function_rela")
public class RoleFunctionRela implements Serializable {

	private static final long serialVersionUID = -8349705525996917628L;

	/**
	 * 角色标识
	 */
	@Id
	@Column(name = "role_info_id")
	private Long roleInfoId;

	/**
	 * 功能标识
	 */
	@Id
	@Column(name = "function_id")
	private String functionId;
	
}
