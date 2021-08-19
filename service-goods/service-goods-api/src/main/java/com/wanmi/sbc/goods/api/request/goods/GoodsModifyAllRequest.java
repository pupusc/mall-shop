package com.wanmi.sbc.goods.api.request.goods;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.goods.bean.dto.GoodsSpecDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsSpecDetailDTO;
import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsModifyAllResponse
 * 修改商品基本信息、基价请求对象
 * @author lipeng
 * @dateTime 2018/11/5 上午10:41
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsModifyAllRequest extends BaseRequest {

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private GoodsVO goods;

    /**
     * 商品相关图片
     */
    @ApiModelProperty(value = "商品相关图片")
    private List<GoodsImageVO> images;

    /**
     * 商品属性列表
     */
    @ApiModelProperty(value = "商品属性列表")
    private List<GoodsPropDetailRelVO> goodsPropDetailRels;

    /**
     * 商品规格列表
     */
    @ApiModelProperty(value = "商品规格列表")
    private List<GoodsSpecVO> goodsSpecs;

    /**
     * 商品规格值列表
     */
    @ApiModelProperty(value = "商品规格值列表")
    private List<GoodsSpecDetailVO> goodsSpecDetails;

    /**
     * 商品SKU列表
     */
    @ApiModelProperty(value = "商品SKU列表")
    private List<GoodsInfoVO> goodsInfos;

    /**
     * 商品等级价格列表
     */
    @ApiModelProperty(value = "商品等级价格列表")
    private List<GoodsLevelPriceVO> goodsLevelPrices;

    /**
     * 商品客户价格列表
     */
    @ApiModelProperty(value = "商品客户价格列表")
    private List<GoodsCustomerPriceVO> goodsCustomerPrices;

    /**
     * 商品订货区间价格列表
     */
    @ApiModelProperty(value = "商品订货区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices;

    /**
     * 是否修改价格及订货量设置
     */
    @ApiModelProperty(value = "是否修改价格及订货量设置", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isUpdatePrice;

    /**
     * 商品详情模板关联
     */
    @ApiModelProperty(value = "商品详情模板关联")
    private List<GoodsTabRelaVO> goodsTabRelas;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updatePerson;
    /**
     * 重写敏感词，用于验证
     * @return 拼凑关键内容
     */
    @Override
    public String checkSensitiveWord() {
        StringBuilder sensitiveWord = new StringBuilder();
        if (goods != null) {
            if (Objects.nonNull(goods.getGoodsName())) {
                sensitiveWord.append(goods.getGoodsName());
            }
            if (Objects.nonNull(goods.getGoodsSubtitle())) {
                sensitiveWord.append(goods.getGoodsSubtitle());
            }
            if (Objects.nonNull(goods.getGoodsDetail())) {
                sensitiveWord.append(goods.getGoodsDetail());
            }
            if (Objects.nonNull(goods.getLabelName())) {
                sensitiveWord.append(goods.getLabelName());
            }
            if (Constants.yes.equals(goods.getMoreSpecFlag())) {
                if (CollectionUtils.isNotEmpty(goodsSpecs)) {
                    sensitiveWord.append(goodsSpecs.stream().map(GoodsSpecVO::getSpecName).collect(Collectors.joining()));
                }
                if (CollectionUtils.isNotEmpty(goodsSpecDetails)) {
                    sensitiveWord.append(goodsSpecDetails.stream().map(GoodsSpecDetailVO::getDetailName).collect(Collectors.joining()));
                }
            }
        }
        return sensitiveWord.toString();
    }
}
