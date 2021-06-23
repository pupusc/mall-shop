package com.wanmi.sbc.goods.api.response.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商家根据店铺编号导出模板响应结构
 * @author daiyitian
 * @dateTime 2018/11/6 下午2:06
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSupplierExcelExportTemplateResponse implements Serializable {

    private static final long serialVersionUID = -6039756679784698582L;

    /**
     * base64位字符串形式的文件流
     */
    @ApiModelProperty(value = "base64位字符串形式的文件流")
    private String file;
}
