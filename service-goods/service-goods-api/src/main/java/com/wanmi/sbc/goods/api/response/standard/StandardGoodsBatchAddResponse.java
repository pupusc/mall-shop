package com.wanmi.sbc.goods.api.response.standard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 标准库导出模板响应结构
 * @author daiyitian
 * @dateTime 2018/11/6 下午2:06
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardGoodsBatchAddResponse implements Serializable {

    private static final long serialVersionUID = -6039756679784698582L;

    @ApiModelProperty(value = "商品库id")
    private List<String> standardIds;
}
