package com.wanmi.sbc.goods.api.request.bookingsale;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * <p>单个删除预售信息请求参数</p>
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleDelByIdRequest extends GoodsBaseRequest {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户id")
    private Long storeId;
}
