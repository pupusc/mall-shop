package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 标签(MetaLabel)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 13:54:10
 */
@Data
public class MetaLabelUpdateStatusReqBO implements Serializable {
    @NotNull
    private Integer id;

    @NotNull
    private Boolean enable;
}

