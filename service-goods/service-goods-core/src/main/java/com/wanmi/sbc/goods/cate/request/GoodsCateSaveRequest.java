package com.wanmi.sbc.goods.cate.request;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import lombok.Data;

/**
 * 分类更新请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
public class GoodsCateSaveRequest extends BaseRequest {

    /**
     * 商品分类信息
     */
    private GoodsCate goodsCate;
}
