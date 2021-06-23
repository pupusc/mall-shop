package com.wanmi.sbc.goods.goodslabel.model.root;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>商品标签实体类</p>
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
@Data
@Entity
@Table(name = "goods_label")
public class GoodsLabel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签id
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "goods_label_id")
	private Long goodsLabelId;

	/**
	 * 标签名称
	 */
	@Column(name = "label_name")
	private String labelName;

	/**
	 * 前端是否展示 0: 关闭 1:开启
	 */
	@Column(name = "label_visible")
	private Boolean labelVisible;

	/**
	 * 排序
	 */
	@Column(name = "label_sort")
	private Integer labelSort;

	/**
	 * 店铺id
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 删除标识 0:未删除1:已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

}