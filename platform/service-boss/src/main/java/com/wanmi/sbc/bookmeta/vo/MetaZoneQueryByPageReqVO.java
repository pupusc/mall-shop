package com.wanmi.sbc.bookmeta.vo;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * 图书分区(MetaZone)实体类
 *
 * @author Liang Jun
 * @since 2022-07-27 10:45:10
 */
@Data
public class MetaZoneQueryByPageReqVO implements Serializable {

    private static final long serialVersionUID = 566709444912667834L;
    /**
     * 类型：1榜单；2书单；3套系；4版本；
     */
    private Integer type;

    private Integer scType;
    /**
     * 名称
      */
    private String name;
    /**
     * 状态：1启用；2停用；
     */
    private Integer status;
    /**
     * 分页参数
     */
    private Page page = new Page(1, 10);
}

