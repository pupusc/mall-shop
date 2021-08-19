package com.wanmi.sbc.goods.api.response.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: songhanlin
 * @Date: Created In 11:04 2018-12-18
 * @Description: 商品分类excel导入返回Response
 */
@ApiModel
@Data
public class GoodsCateExcelImportResponse implements Serializable {
    private static final long serialVersionUID = -7812692785967306817L;

    /**
     * 是否正确上传成功
     */
    @ApiModelProperty(value = "是否正确上传成功")
    private Boolean flag;
}
