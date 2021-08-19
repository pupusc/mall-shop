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
 * <p>根据店铺ids查询店铺自定义字段response</p>
 * Created by daiyitian on 2020/12/22.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorePartColsListByIdsResponse implements Serializable{

    private static final long serialVersionUID = 4645900776045500019L;

    /**
     * 店铺列表
     */
    @ApiModelProperty(value = "店铺列表")
    private List<StoreVO> storeVOList;
}
