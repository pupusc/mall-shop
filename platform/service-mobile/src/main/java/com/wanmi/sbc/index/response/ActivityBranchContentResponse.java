package com.wanmi.sbc.index.response;

import lombok.Data;

import java.io.Serializable;


@Data
public class ActivityBranchContentResponse implements Serializable {


    private static final long serialVersionUID = 5840690096149862208L;
    /**
     * 模块类型
     */
    private Integer type;

    /**
     * 模块名称
     */
    private String title;

}