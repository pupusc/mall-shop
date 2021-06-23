package com.wanmi.sbc.goods.api.request.freight;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 店铺运费模板分页请求数据结构
 * Created by daiyitian on 2018/5/3.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class FreightTemplateStorePageRequest extends BaseQueryRequest {

    private static final long serialVersionUID = -8107078231538944644L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

}
