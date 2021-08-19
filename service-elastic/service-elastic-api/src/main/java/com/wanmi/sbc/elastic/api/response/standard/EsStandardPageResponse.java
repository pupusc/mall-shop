package com.wanmi.sbc.elastic.api.response.standard;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.bean.vo.goods.EsStandardGoodsPageVO;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandSimpleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品库分页查询结果
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsStandardPageResponse implements Serializable {

    /**
     * 商品库分页结果
     */
    private MicroServicePage<EsStandardGoodsPageVO> standardGoodsPage = new MicroServicePage<>();

    /**
     * 商品品牌所有数据
     */
    @ApiModelProperty(value = "商品品牌所有数据")
    private List<GoodsBrandSimpleVO> goodsBrandList = new ArrayList<>();

    /**
     * 商品分类所有数据
     */
    @ApiModelProperty(value = "商品分类所有数据")
    private List<GoodsCateSimpleVO> goodsCateList = new ArrayList<>();

    /**
     * 已被导入的商品库ID
     */
    @ApiModelProperty(value = "已被导入的商品库ID")
    private List<String> usedStandard = new ArrayList<>();

    /**
     * 需要同步的商品库ID
     */
    @ApiModelProperty(value = "需要同步的商品库ID")
    private List<String> needSynStandard = new ArrayList<>();
}
