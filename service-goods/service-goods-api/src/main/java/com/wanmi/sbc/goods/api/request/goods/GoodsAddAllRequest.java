package com.wanmi.sbc.goods.api.request.goods;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.goods.bean.dto.GoodsCustomerPriceDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsImageDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsIntervalPriceDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsLevelPriceDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsPropDetailRelDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsSpecDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsSpecDetailDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsTabRelaVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsAddALLRequest
 * 新增商品基本信息、基价请求对象
 * @author lipeng
 * @dateTime 2018/11/5 上午10:36
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsAddAllRequest extends BaseRequest {

    private static final long serialVersionUID = 4814498431750389445L;

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private GoodsDTO goods;

    /**
     * 商品相关图片
     */
    @ApiModelProperty(value = "商品相关图片")
    private List<GoodsImageDTO> images;

    /**
     * 商品属性列表
     */
    @ApiModelProperty(value = "商品属性列表")
    private List<GoodsPropDetailRelDTO> goodsPropDetailRels;

    /**
     * 商品规格列表
     */
    @ApiModelProperty(value = "商品规格列表")
    private List<GoodsSpecDTO> goodsSpecs;

    /**
     * 商品规格值列表
     */
    @ApiModelProperty(value = "商品规格值列表")
    private List<GoodsSpecDetailDTO> goodsSpecDetails;

    /**
     * 商品SKU列表
     */
    @ApiModelProperty(value = "商品SKU列表")
    private List<GoodsInfoDTO> goodsInfos;

    /**
     * 商品等级价格列表
     */
    @ApiModelProperty(value = "商品等级价格列表")
    private List<GoodsLevelPriceDTO> goodsLevelPrices;

    /**
     * 商品客户价格列表
     */
    @ApiModelProperty(value = "商品客户价格列表")
    private List<GoodsCustomerPriceDTO> goodsCustomerPrices;

    /**
     * 商品订货区间价格列表
     */
    @ApiModelProperty(value = "商品订货区间价格列表")
    private List<GoodsIntervalPriceDTO> goodsIntervalPrices;

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
    public String checkSensitiveWord(){
        StringBuilder sensitiveWord = new StringBuilder();
        if(goods != null) {
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
                    sensitiveWord.append(goodsSpecs.stream().map(GoodsSpecDTO::getSpecName).collect(Collectors.joining()));
                }
                if (CollectionUtils.isNotEmpty(goodsSpecDetails)) {
                    sensitiveWord.append(goodsSpecDetails.stream().map(GoodsSpecDetailDTO::getDetailName).collect(Collectors.joining()));
                }
            }
        }
        return sensitiveWord.toString();
    }

    /**
     * 编辑类型：1普通商品；2直充商品；3打包商品；
     */
    private Integer editType;
}
