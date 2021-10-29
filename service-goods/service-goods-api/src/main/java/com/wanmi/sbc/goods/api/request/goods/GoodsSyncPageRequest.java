package com.wanmi.sbc.goods.api.request.goods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@ApiModel
@Data
public class GoodsSyncPageRequest extends BaseQueryRequest implements Serializable {


    private static final long serialVersionUID = -2561942390993537043L;

    @ApiModelProperty("isbn")
    private List<String> isbn;

    private String goodsName;

    private Integer cateId;

    private String author;

    private String publishName;

}

