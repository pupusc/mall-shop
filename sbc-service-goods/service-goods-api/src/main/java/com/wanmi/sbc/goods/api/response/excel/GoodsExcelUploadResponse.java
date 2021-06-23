package com.wanmi.sbc.goods.api.response.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.response.goodsexcel.GoodsExcelUploadResponse
 * 上传商品excel文件响应对象
 * @author lipeng
 * @dateTime 2018/11/6 下午2:06
 */
@ApiModel
@Data
public class GoodsExcelUploadResponse implements Serializable {

    private static final long serialVersionUID = 8195488364653694641L;

    @ApiModelProperty(value = "上传商品excel文件响应对象")
    private String fileExt;
}
