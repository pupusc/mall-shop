package com.wanmi.sbc.customer.api.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>根据店铺id查询任意（包含已删除）店铺基本信息request</p>
 * Created by of628-wenzhi on 2018-09-12-下午2:46.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreListByStoreIdsRequest implements Serializable{

    private static final long serialVersionUID = -125741706415554266L;
    /**
     * 店铺id集合
     */
    @ApiModelProperty(value = "店铺id集合")
    @NotNull
    private List<Long> storeIds;
}
