package com.wanmi.sbc.elastic.api.response.storeInformation;

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
 * @Author yangzhen
 * @Description //查询账期内有效店铺，自动关联5条信息response
 * @Date 10:40 2020/12/17
 * @Param
 * @return
 **/
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsListStoreByNameForAutoCompleteResponse implements Serializable{

    private static final long serialVersionUID = -8576096693233067335L;

    /**
     * 店铺列表
     */
    @ApiModelProperty(value = "店铺列表")
    private List<StoreVO> storeVOList;
}
