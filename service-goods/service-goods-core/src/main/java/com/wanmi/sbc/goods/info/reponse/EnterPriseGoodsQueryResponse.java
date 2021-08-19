package com.wanmi.sbc.goods.info.reponse;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品查询请求
 * Created by CHENLI on 2019/2/19.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterPriseGoodsQueryResponse {

    /**
     * 分销商品分页数据
     */
    private MicroServicePage<GoodsInfo> goodsInfoPage = new MicroServicePage<>(new ArrayList<>());

    /**
     * 商品SKU的规格值全部数据
     */
    private List<GoodsInfoSpecDetailRel> goodsInfoSpecDetails = new ArrayList<>();

    /**
     * 商品品牌所有数据
     */
    private List<GoodsBrand> goodsBrandList = new ArrayList<>();

    /**
     * 商品分类所有数据
     */
    private List<GoodsCate> goodsCateList = new ArrayList<>();

    /**
     * 商家所有数据
     */
    private List<CompanyInfoVO> companyInfoList = new ArrayList<>();

}
