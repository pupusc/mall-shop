package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/10:20
 * @Description:
 */
@Data
public class MetaTradeTreeResVO implements Serializable {
    private static final long serialVersionUID = 609911887943575185L;

    private Integer id;
    /**
     * 类型：1榜单；2书单；3套系；4版本；
     */
    private Integer type;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态：1启用；2停用；
     */
    private Integer status;
    /**
     * 简介
     */
    private String descr;
    /**
     * 关联图书ids
     */
    private List<MetaZoneQueryByIdResVO$Book> books;
}
