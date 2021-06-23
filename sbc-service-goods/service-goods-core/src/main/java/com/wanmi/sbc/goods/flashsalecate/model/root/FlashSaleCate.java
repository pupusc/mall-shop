package com.wanmi.sbc.goods.flashsalecate.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>秒杀分类实体类</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@Data
@Entity
@Table(name = "flash_sale_cate")
public class FlashSaleCate implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 秒杀分类主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cate_id")
	private Long cateId;

	/**
	 * 分类名称
	 */
	@Column(name = "cate_name")
	private String cateName;

	/**
	 * 排序 默认0
	 */
	@Column(name = "sort")
	private Integer sort;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 修改时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 修改人
	 */
	@Column(name = "update_person")
	private String updatePerson;

	/**
	 * 删除标识,0: 未删除 1: 已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}