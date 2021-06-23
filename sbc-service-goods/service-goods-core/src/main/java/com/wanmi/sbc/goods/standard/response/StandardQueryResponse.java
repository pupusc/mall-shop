package com.wanmi.sbc.goods.standard.response;

import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSkuSpecDetailRel;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 商品库查询请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
public class StandardQueryResponse {

    /**
     * 商品分页数据
     */
    private Page<StandardGoods> standardGoodsPage ;

    /**
     * 商品SKU全部数据
     */
    private List<StandardSku> standardSkuList ;

    /**
     * 商品SKU的规格值全部数据
     */
    private List<StandardSkuSpecDetailRel> standardSkuSpecDetails ;

    /**
     * 商品品牌所有数据
     */
    private List<GoodsBrand> goodsBrandList ;

    /**
     * 商品分类所有数据
     */
    private List<GoodsCate> goodsCateList ;

    /**
     * 已被导入的商品库ID
     */
    private List<String> usedStandard ;
}
