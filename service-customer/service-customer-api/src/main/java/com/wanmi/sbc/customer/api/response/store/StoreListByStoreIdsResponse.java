package com.wanmi.sbc.customer.api.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>根据id查询任意（包含已删除）店铺信息response</p>
 * Created by of628-wenzhi on 2018-09-12-下午2:56.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreListByStoreIdsResponse implements Serializable {

    private static final long serialVersionUID = -4646813086428737944L;

    /**
     * 店铺ID集合
     */
    @ApiModelProperty(value = "店铺ID集合")
    private List<Long> longList;
}
