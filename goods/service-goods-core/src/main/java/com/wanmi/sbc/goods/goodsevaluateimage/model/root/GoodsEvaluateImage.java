package com.wanmi.sbc.goods.goodsevaluateimage.model.root;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wanmi.sbc.goods.goodsevaluate.model.root.GoodsEvaluate;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>商品评价图片实体类</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@Data
@Entity
@Table(name = "goods_evaluate_image")
public class GoodsEvaluateImage implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 图片Id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "image_id")
	private String imageId;

	/**
	 * 评价id
	 */
	@Column(name = "evaluate_id")
	private String evaluateId;

	/**
	 * 商品ID
	 */
	@Column(name = "goods_id")
	private String goodsId;

	/**
	 * 图片KEY
	 */
	@Column(name = "image_key")
	private String imageKey;

	/**
	 * 图片名称
	 */
	@Column(name = "image_name")
	private String imageName;

	/**
	 * 原图地址
	 */
	@Column(name = "artwork_url")
	private String artworkUrl;

	/**
	 * 是否展示 0：否，1：是
	 */
	@Column(name = "is_show")
	private Integer isShow;

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

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	@Column(name = "del_flag")
	private Integer delFlag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evaluate_id", insertable = false, updatable = false)
	@org.hibernate.annotations.OrderBy(clause = "create_time")
	@JsonManagedReference
	private GoodsEvaluate goodsEvaluate;

}