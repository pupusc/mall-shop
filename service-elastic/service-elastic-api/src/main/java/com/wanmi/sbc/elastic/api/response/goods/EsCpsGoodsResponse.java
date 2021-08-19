package com.wanmi.sbc.elastic.api.response.goods;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLabelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsListBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSpecDetailVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSpecVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品索引SPU查询结果
 * Created by daiyitian on 2017/3/24.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class EsCpsGoodsResponse {

    /**
     * 索引SKU
     */
    @ApiModelProperty(value = "索引SKU")
    private MicroServicePage<EsGoodsVO> esGoods = new MicroServicePage<>(new ArrayList<>());
    /**
     * 索引知识顾问专属SKU
     */
    @ApiModelProperty(value = "索引知识顾问专属SKU")
    private MicroServicePage<EsGoodsVO> esCpsGoods = new MicroServicePage<>(new ArrayList<>());

    /**
     * SPU
     */
    @ApiModelProperty(value = "SPU")
    private List<GoodsVO> goodsList = new ArrayList<>();

    /**
     * 商品区间价格列表
     */
    @ApiModelProperty(value = "商品区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices = new ArrayList<>();

    /**
     * 商品级别价格列表
     */
    @ApiModelProperty(value = "商品级别价格列表")
    private List<GoodsLevelPriceVO> goodsLevelPrices = new ArrayList<>();

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private List<GoodsListBrandVO> brands = new ArrayList<>();

    /**
     * 分类
     */
    @ApiModelProperty(value = "分类")
    private List<GoodsCateVO> cateList = new ArrayList<>();

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private List<GoodsSpecVO> goodsSpecs = new ArrayList<>();

    /**
     * 规格值
     */
    @ApiModelProperty(value = "规格值")
    private List<GoodsSpecDetailVO> goodsSpecDetails = new ArrayList<>();

    /**
     * 预约抢购信息
     */
    @ApiModelProperty(value = "预约抢购信息列表")
    private List<AppointmentSaleVO> appointmentSaleVOList;

    /**
     * 预售信息列表结果
     */
    @ApiModelProperty(value = "预售信息列表结果")
    private List<BookingSaleVO> bookingSaleVOList;

    /**
     * 商品标签
     */
    @ApiModelProperty(value = "商品标签")
    private List<GoodsLabelVO> goodsLabelVOList;

}
