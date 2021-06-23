package com.wanmi.sbc.goods.storegoodstab.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品模板配置实体类
 * Created by xiemengnan on 2018/10/12.
 */
@Data
@Entity
@Table(name = "store_goods_tab")
public class StoreGoodsTab implements Serializable {

    /**
     * 模板标识
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tab_id")
    private Long tabId;


    /**
     * 模板名称
     */
    @NotBlank
    @Column(name = "tab_name")
    private String tabName;

    /**
     * 店铺标识
     */
    @Column(name = "store_id")
    private Long storeId;

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
     * 删除时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "del_time")
    private LocalDateTime delTime;

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
     * 创建人
     */
    @Column(name = "create_person")
    private String createPerson;
    /**
     * 修改人
     */
    @Column(name = "update_person")
    private String updatePerson;

    /**
     * 删除人
     */
    @Column(name = "del_person")
    private String delPerson;

    /**
     * 用户可编辑内容的对象转换,非null不复制
     *
     * @param newStoreGoodsTab
     */
    public void convertBeforeEdit(StoreGoodsTab newStoreGoodsTab) {
        if (newStoreGoodsTab.getTabName() != null) {
            this.tabName = newStoreGoodsTab.getTabName();
        }
        if (newStoreGoodsTab.getCreatePerson() != null) {
            this.createPerson = newStoreGoodsTab.getCreatePerson();
        }
        if (newStoreGoodsTab.getUpdatePerson() != null) {
            this.updatePerson = newStoreGoodsTab.getUpdatePerson();
        }
        if (newStoreGoodsTab.getDelPerson() != null) {
            this.delPerson = newStoreGoodsTab.getDelPerson();
        }

        if (newStoreGoodsTab.getDelFlag() != null) {
            this.delFlag = newStoreGoodsTab.getDelFlag();
        }
        if (newStoreGoodsTab.getSort() != null) {
            this.sort = newStoreGoodsTab.getSort();
        }

    }

}

