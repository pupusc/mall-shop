package com.wanmi.sbc.bookmeta.bo;

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
public class MetaZoneEnableReqBO implements Serializable {
    @NotNull
    private Integer id;
    @NotNull
    private boolean flag;
}

