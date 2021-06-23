package com.wanmi.sbc.customer.api.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午2:06 2019/5/23
 * @Description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreNameListByStoreIdsResquest {

    /**
     * 店铺id列表
     */
    @ApiModelProperty(value = "店铺id列表")
    @NotEmpty
    private List<Long> storeIds;

}
