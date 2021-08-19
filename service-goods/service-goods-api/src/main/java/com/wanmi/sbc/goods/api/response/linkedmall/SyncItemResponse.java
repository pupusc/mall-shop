package com.wanmi.sbc.goods.api.response.linkedmall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class SyncItemResponse implements Serializable {

    private static final long serialVersionUID = -2456148279347178129L;

    @ApiModelProperty("商品库id")
    private List<String> standardIds;
}
