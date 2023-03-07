package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/07/10:11
 * @Description:
 */
@Data
public class MetaTradePageQueryBO implements Serializable {

    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态：1启用；2停用；
     */
    private Integer status;
    /**
     * 父级id
     */
    private Integer parentId;
    /**
     * 场景：1适读对象；
     */
    private Integer scene;
    /**
     * 分页参数
     */
    private Integer pageNo;
    private Integer pageNum;

    private Integer isStatic;
}
