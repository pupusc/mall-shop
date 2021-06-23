package com.wanmi.sbc.elastic.goods.model.root;

import com.wanmi.sbc.common.util.EsConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 商品品牌实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@ApiModel
public class GoodsBrandNest implements Serializable {

    /**
     * 品牌编号
     */
    @Id
    @ApiModelProperty(value = "品牌编号")
    private Long brandId;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    @Field(searchAnalyzer = EsConstants.DEF_ANALYZER, analyzer = EsConstants.DEF_ANALYZER, type = FieldType.Text)
    private String brandName;

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
}
