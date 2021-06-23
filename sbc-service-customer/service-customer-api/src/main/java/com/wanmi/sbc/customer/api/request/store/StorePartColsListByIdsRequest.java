package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 根据店铺ids查询店铺自定义字段的请求
 * Created by daiyitian on 2020/12/22.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StorePartColsListByIdsRequest extends BaseQueryRequest implements Serializable {

    /**
     * 批量SPU编号
     */
    @NotEmpty
    @ApiModelProperty(value = "批量店铺编号")
    private List<Long> storeIds;

    /**
     * 定义局部字段
     */
    @NotEmpty
    @ApiModelProperty(value = "定义局部字段")
    private List<String> cols;

}
