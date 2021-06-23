package com.wanmi.sbc.goods.thirdgoodscate.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;

/**
 * <p>第三方平台类目实体类</p>
 * @author 
 * @date 2020-08-29 13:35:42
 */
@Data
@Entity
@Table(name = "third_goods_cate")
public class ThirdGoodsCate {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 三方商品分类主键
	 */
	@Column(name = "cate_id")
	private Long cateId;

	/**
	 * 分类名称
	 */
	@Column(name = "cate_name")
	private String cateName;

	/**
	 * 父分类ID
	 */
	@Column(name = "cate_parent_id")
	private Long cateParentId;

	/**
	 * 分类层次路径,例0|01|001
	 */
	@Column(name = "cate_path")
	private String catePath;

	/**
	 * 分类层级
	 */
	@Column(name = "cate_grade")
	private Integer cateGrade;

	/**
	 * 创建时间
	 */
	@CreatedDate
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@LastModifiedDate
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 第三方平台来源(0,linkedmall)
	 */
	@Column(name = "third_platform_type")
	@Enumerated
	private ThirdPlatformType thirdPlatformType;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}