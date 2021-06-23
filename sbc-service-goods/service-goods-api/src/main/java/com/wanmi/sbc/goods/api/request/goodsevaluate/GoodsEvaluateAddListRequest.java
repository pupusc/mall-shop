package com.wanmi.sbc.goods.api.request.goodsevaluate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author lvzhenwei
 * @Description 批量保存商品评论参数
 * @Date 15:35 2019/4/10
 * @Param
 * @return
 **/
@Data
public class GoodsEvaluateAddListRequest implements Serializable {

    private static final long serialVersionUID = 7105187507608553558L;

    private List<GoodsEvaluateAddRequest> goodsEvaluateAddList;
}
