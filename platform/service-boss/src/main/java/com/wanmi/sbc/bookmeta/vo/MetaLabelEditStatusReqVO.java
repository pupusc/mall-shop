package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 标签(MetaLabel)实体类
 *
 * @author Liang Jun
 * @since 2022-05-17 11:38:03
 */
@Data
public class MetaLabelEditStatusReqVO implements Serializable {
    @NotNull
    private Integer id;
    /**
     * 状态：1启用；2停用；
     */
    @Min(value = 1, message = "状态值必须是1或2")
    @Max(value = 2, message = "状态值必须是1或2")
    @NotNull
    private Integer status;
}

