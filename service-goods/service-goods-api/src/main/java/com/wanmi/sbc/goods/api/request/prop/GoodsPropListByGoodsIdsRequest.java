package com.wanmi.sbc.goods.api.request.prop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsPropListByGoodsIdsRequest implements Serializable {
    @NotEmpty
    private List<String> goodsIds;
}
