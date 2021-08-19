package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class EsBrandUpdateRequest implements Serializable {

    @ApiModelProperty(value = " 是否是删除品牌，false时表示更新品牌")
    private boolean isDelete;

    @ApiModelProperty(value = "操作品牌实体")
    private GoodsBrandVO goodsBrand;

}
