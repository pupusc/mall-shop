package com.wanmi.sbc.customer.api.request.storeevaluate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author lvzhenwei
 * @Description 批量保存商品评论参数
 * @Date 15:32 2019/4/10
 * @Param
 * @return
 **/
@Data
public class StoreEvaluateAddListRequest implements Serializable {
    private static final long serialVersionUID = 5236879364611811765L;

    private List<StoreEvaluateAddRequest> storeEvaluateAddRequestList;
}
