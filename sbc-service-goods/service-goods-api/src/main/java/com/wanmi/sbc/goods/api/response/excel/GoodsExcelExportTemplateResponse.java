package com.wanmi.sbc.goods.api.response.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 导出商品模板响应结构
 * @author daiyitian
 * @dateTime 2018/11/6 下午2:06
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsExcelExportTemplateResponse implements Serializable {

    private static final long serialVersionUID = 8195488364653694641L;

    /**
     * base64位字符串形式的文件流
     */
    @ApiModelProperty(value = "base64位字符串形式的文件流")
    private String file;
}
