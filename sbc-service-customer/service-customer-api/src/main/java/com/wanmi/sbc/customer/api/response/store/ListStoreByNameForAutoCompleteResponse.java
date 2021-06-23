package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.customer.bean.vo.StoreVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>查询账期内有效店铺，自动关联5条信息response</p>
 * Created by of628-wenzhi on 2018-09-12-下午7:18.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListStoreByNameForAutoCompleteResponse implements Serializable{

    private static final long serialVersionUID = -8576096693233067335L;

    /**
     * 店铺列表
     */
    @ApiModelProperty(value = "店铺列表")
    private List<StoreVO> storeVOList;
}
