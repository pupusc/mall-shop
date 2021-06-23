package com.wanmi.sbc.goods.api.response.adjustprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>批量调价模板下载返回结构</p>
 * Created by of628-wenzhi on 2020-12-11-7:01 下午.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ExportAdjustmentPriceTemplateResponse implements Serializable {

    private static final long serialVersionUID = -2715424236088947616L;

    /**
     * base64位字符串形式的文件流
     */
    @ApiModelProperty(value = "base64位字符串形式的文件输出流")
    private String fileOutputStream;
}
