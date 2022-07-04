package com.wanmi.sbc.goods.api.request.spec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfoSpecDetailRelBySpuIdsRequest implements Serializable {
    @NotEmpty
    private List<String> spuIds;
}
