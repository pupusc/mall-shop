package com.wanmi.sbc.setting.hovernavmobile.model.root;


import com.wanmi.sbc.setting.bean.enums.UsePageType;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * <p>移动端悬浮导航栏实体类</p>
 * @author dyt
 * @date 2020-04-29 14:28:21
 */
@Data
public class HoverNavMobile implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@Id
	private Long storeId;

	/**
	 * 应用页面
	 */
	private List<UsePageType> usePages;

	/**
	 * 导航项
	 */
	private List<HoverNavMobileItem> navItems;
}