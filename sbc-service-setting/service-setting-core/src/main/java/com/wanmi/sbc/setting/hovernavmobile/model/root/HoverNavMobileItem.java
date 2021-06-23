package com.wanmi.sbc.setting.hovernavmobile.model.root;


import lombok.Data;
import java.io.Serializable;

/**
 * <p>移动端悬浮导航栏实体类</p>
 *
 * @author dyt
 * @date 2020-04-29 14:28:21
 */
@Data
public class HoverNavMobileItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 图片
     */
    private String imgSrc;

    /**
     * 导航名称
     */
    private String title;

    /**
     * 落地页的json字符串
     */
    private String linkInfoPage;
}