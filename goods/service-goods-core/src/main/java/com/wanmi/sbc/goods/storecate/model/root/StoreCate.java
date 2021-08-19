package com.wanmi.sbc.goods.storecate.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类实体类
 * Created by bail on 2017/11/13.
 */
@Data
@Entity
@Table(name = "store_cate")
public class StoreCate implements Serializable {

    /**
     * 店铺分类标识
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_cate_id")
    private Long storeCateId;

    /**
     * 店铺标识
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 店铺分类名称
     */
    @NotBlank
    @Length(min = 1, max = 20)
    @Column(name = "cate_name")
    private String cateName;

    /**
     * 父分类标识
     */
    @Column(name = "cate_parent_id")
    private Long cateParentId;

    /**
     * 分类图片
     */
    @Column(name = "cate_img")
    private String cateImg;

    /**
     * 分类路径
     */
    @Column(name = "cate_path")
    private String catePath;

    /**
     * 分类层次
     */
    @Column(name = "cate_grade")
    private Integer cateGrade;

    /**
     * 拼音
     */
    @Column(name = "pin_yin")
    private String pinYin;

    /**
     * 简拼
     */
    @Column(name = "s_pin_yin")
    private String sPinYin;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 默认标记
     */
    @Column(name = "is_default")
    @Enumerated
    private DefaultFlag isDefault;

    /**
     * 一对多关系，子分类
     */
    @Transient
    private List<StoreCate> storeCateList;

    /**
     * 用户可编辑内容的对象转换,非null不复制
     * @param newStoreCate
     */
    public void convertBeforeEdit(StoreCate newStoreCate){
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

