package com.wanmi.sbc.setting.storeresourcecate.model.root;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.bean.enums.AuditStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>店铺资源资源分类表实体类</p>
 * @author lq
 * @date 2019-11-05 16:13:19
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store_resource_cate")
public class StoreResourceCate implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 素材分类id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cate_id")
	private Long cateId;

	/**
	 * 店铺标识
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 商家标识
	 */
	@Column(name = "company_info_id")
	private Long companyInfoId;

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
	 * 分类图片
	 */
	@Column(name = "cate_img")
	private String cateImg;

	/**
	 * 分类层次路径,例1|01|001
	 */
	@Column(name = "cate_path")
	private String catePath;

	/**
	 * 分类层级
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
	private String spinYin;

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
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;

	/**
	 * 是否默认,0:否1:是
	 */
	@Column(name = "is_default")
	 private DefaultFlag isDefault;

}