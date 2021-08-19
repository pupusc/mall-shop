package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.goodscate.GoodsCateDeleteByIdRequest
 * 根据分类编号删除分类信息请求对象
 * @author lipeng
 * @dateTime 2018/11/1 下午4:58
 */
@ApiModel
@Data
public class GoodsCateDeleteByIdRequest implements Serializable {

    private static final long serialVersionUID = 7438265527612290337L;

    @ApiModelProperty(value = "分类Id")
    private Long cateId;
}
