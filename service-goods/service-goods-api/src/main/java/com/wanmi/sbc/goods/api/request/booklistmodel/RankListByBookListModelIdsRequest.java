package com.wanmi.sbc.goods.api.request.booklistmodel;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class RankListByBookListModelIdsRequest implements Serializable {

    private List<Integer> Ids;
}
