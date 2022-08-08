package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 图书分区(MetaZone)实体类
 *
 * @author Liang Jun
 * @since 2022-07-27 10:45:10
 */
@Data
public class MetaZoneEditReqBO implements Serializable {
    @NotNull
    private Integer id;
    /**
     * 类型：1榜单；2书单；3套系；4版本；
     */
    @NotNull
    private Integer type;
    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * 简介
     */
    private String descr;
    /**
     * 关联图书ids
     */
    private List<Integer> books;
}

