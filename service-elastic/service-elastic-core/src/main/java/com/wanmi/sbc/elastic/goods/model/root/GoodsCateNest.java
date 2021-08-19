package com.wanmi.sbc.elastic.goods.model.root;

import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.goods.bean.vo.GoodsCateShenceBurialSiteVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * 商品分类实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@ApiModel
public class GoodsCateNest implements Serializable {

    /**
     * 分类编号
     */
    @ApiModelProperty(value = "分类编号")
    private Long cateId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    @Field(searchAnalyzer = EsConstants.DEF_ANALYZER, analyzer = EsConstants.DEF_ANALYZER, type = FieldType.Text)
    private String cateName;

    /**
     * 拼音
     */
    @ApiModelProperty(value = "拼音")
    @Field(searchAnalyzer = EsConstants.PINYIN_ANALYZER, analyzer = EsConstants.PINYIN_ANALYZER, type = FieldType.Text)
    private String pinYin;

    /**
     * 简拼
     */
    @ApiModelProperty(value = "简拼")
    @Field(index = false, type = FieldType.Text)
    private String sPinYin;

    /**
     * 一二级分类信息
     */
    @ApiModelProperty(value = "一二级分类信息")
    private GoodsCateShenceBurialSiteVO goodsCateShenceBurialSite;
}
