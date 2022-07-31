package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 图书分区(MetaZone)实体类
 *
 * @author Liang Jun
 * @since 2022-07-27 10:45:10
 */
@Data
public class MetaZoneEnableReqVO implements Serializable {
    /**
     * 主键
     */
    @NotNull
    private Integer id;
    /**
     * 启用区域：true开启；false关闭（默认）；
     */
    @NotNull
    private Boolean flag;
}

