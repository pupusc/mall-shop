package com.wanmi.sbc.elastic.bean.vo.goods;

import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.goods.bean.vo.GoodsCateShenceBurialSiteVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品分类实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@ApiModel
public class GoodsCateNestVO implements Serializable {

    /**
     * 分类编号
     */
    @ApiModelProperty(value = "分类编号")
    private Long cateId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String cateName;

    /**
     * 拼音
     */
    @ApiModelProperty(value = "拼音")
    private String pinYin;

    /**
     * 简拼
     */
    @ApiModelProperty(value = "简拼")
    private String sPinYin;

    /**
     * 一二级分类信息
     */
    @ApiModelProperty(value = "一二级分类信息")
    private GoodsCateShenceBurialSiteVO goodsCateShenceBurialSite;
}
