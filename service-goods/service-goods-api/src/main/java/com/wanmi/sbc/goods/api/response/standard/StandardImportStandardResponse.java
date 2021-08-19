package com.wanmi.sbc.goods.api.response.standard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.standard.StandardImportGoodsResponse
 * 商品导入商品库请求对象
 * @author lipeng
 * @dateTime 2018/11/9 下午2:48
 */
@ApiModel
@Data
public class StandardImportStandardResponse implements Serializable {

    private static final long serialVersionUID = -2406224019106354126L;

    @ApiModelProperty(value = "商品库ids")
    private List<String> standardIds;
}
