package com.wanmi.sbc.goods.api.request.adjustprice;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>批量调价文件上传请求参数父类</p>
 * Created by of628-wenzhi on 2020-12-11-7:55 下午.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustFileUploadRequest extends GoodsBaseRequest {

    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    @NotBlank
    private String customerId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

    /**
     * 文件载体
     */
    @ApiModelProperty(value = "文件载体")
    @NotNull
    private MultipartFile uploadFile;

}
