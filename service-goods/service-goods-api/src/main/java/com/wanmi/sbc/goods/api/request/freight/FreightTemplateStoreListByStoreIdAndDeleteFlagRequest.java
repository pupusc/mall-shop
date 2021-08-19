package com.wanmi.sbc.goods.api.request.freight;

import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 根据店铺id和删除状态查询店铺运费模板请求数据结构
 * Created by daiyitian on 2018/5/3.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreightTemplateStoreListByStoreIdAndDeleteFlagRequest implements Serializable {

    private static final long serialVersionUID = -6415341912353434056L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

    /**
     * 删除状态
     */
    @ApiModelProperty(value = "删除状态", notes = "0: 否, 1: 是")
    @NotNull
    private DeleteFlag deleteFlag;
}
