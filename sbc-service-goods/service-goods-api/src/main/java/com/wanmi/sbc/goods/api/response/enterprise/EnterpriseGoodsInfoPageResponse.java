package com.wanmi.sbc.goods.api.response.enterprise;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品SKU视图分页响应
 * Created by baijianzhong on 2019/2/20.
 * @author baijianzhong
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseGoodsInfoPageResponse implements Serializable {

    private static final long serialVersionUID = -4370164109574914820L;

    /**
     * 分页商品SKU信息
     */
    @ApiModelProperty(value = "分页商品SKU信息")
    private MicroServicePage<GoodsInfoVO> goodsInfoPage;

    /**
     * 品牌列表
     */
    @ApiModelProperty(value = "品牌列表")
    private List<GoodsBrandVO> brands = new ArrayList<>();

    /**
     * 分类列表
     */
    @ApiModelProperty(value = "分类列表")
    private List<GoodsCateVO> cates = new ArrayList<>();

    /**
     * 商品SKU的规格值全部数据
     */
    @ApiModelProperty(value = "商品SKU的规格值全部数据")
    private List<GoodsInfoSpecDetailRelVO> goodsInfoSpecDetails;

    /**
     * 商家信息数据
     */
    @ApiModelProperty(value = "商家信息数据")
    private List<CompanyInfoVO> companyInfoList = new ArrayList<>();
}
