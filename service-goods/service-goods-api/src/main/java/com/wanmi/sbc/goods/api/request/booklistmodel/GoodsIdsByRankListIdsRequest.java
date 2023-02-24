package com.wanmi.sbc.goods.api.request.booklistmodel;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class GoodsIdsByRankListIdsRequest implements Serializable {

    private List<Integer> ids;
}
