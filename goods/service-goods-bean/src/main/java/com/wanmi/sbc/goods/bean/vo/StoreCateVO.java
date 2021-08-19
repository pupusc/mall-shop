package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类实体类
 * Created by bail on 2017/11/13.
 */
@ApiModel
@Data
public class StoreCateVO implements Serializable {

    /**
     * 店铺分类标识
     */
    @ApiModelProperty(value = "店铺分类标识")
    private Long storeCateId;

    /**
     * 店铺标识
     */
    @ApiModelProperty(value = "店铺标识")
    private Long storeId;

    /**
     * 店铺分类名称
     */
    @ApiModelProperty(value = "店铺分类名称")
    private String cateName;

    /**
     * 父分类标识
     */
    @ApiModelProperty(value = "父分类标识")
    private Long cateParentId;

    /**
     * 分类图片
     */
    @ApiModelProperty(value = "分类图片")
    private String cateImg;

    /**
     * 分类路径
     */
    @ApiModelProperty(value = "分类路径")
    private String catePath;

    /**
     * 分类层次
     */
    @ApiModelProperty(value = "分类层次")
    private Integer cateGrade;

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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", notes = "0: 否, 1: 是")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 默认标记
     */
    @ApiModelProperty(value = "默认标记", notes = "0: 否, 1: 是")
    @Enumerated
    private DefaultFlag isDefault;

    /**
     * 一对多关系，子分类
     */
    @ApiModelProperty(value = "一对多关系，子分类")
    @Transient
    private List<StoreCateVO> storeCateList;

    /**
     * 用户可编辑内容的对象转换,非null不复制
     * @param newStoreCate
     */
    public void convertBeforeEdit(StoreCateVO newStoreCate){
        if(newStoreCate.getCateName()!=null){
            this.cateName = newStoreCate.getCateName();
        }
        if(newStoreCate.getCateParentId()!=null){
            this.cateParentId = newStoreCate.getCateParentId();
        }
        if(newStoreCate.getCatePath()!=null){
            this.catePath = newStoreCate.getCatePath();
        }
        if(newStoreCate.getCateGrade()!=null){
            this.cateGrade = newStoreCate.getCateGrade();
        }
        if(newStoreCate.getDelFlag()!=null){
            this.delFlag = newStoreCate.getDelFlag();
        }
        if(newStoreCate.getSort()!=null){
            this.sort = newStoreCate.getSort();
        }
        if(newStoreCate.getIsDefault()!=null){
            this.isDefault = newStoreCate.getIsDefault();
        }
    }
}

