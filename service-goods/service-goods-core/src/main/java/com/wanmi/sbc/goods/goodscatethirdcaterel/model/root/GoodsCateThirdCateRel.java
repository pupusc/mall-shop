package com.wanmi.sbc.goods.goodscatethirdcaterel.model.root;

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
 * <p>平台类目和第三方平台类目映射实体类</p>
 * @author
 * @date 2020-08-18 19:51:55
 */
@Data
@Entity
@Table(name = "goods_cate_third_cate_rel")
public class GoodsCateThirdCateRel {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 平台类目主键
	 */
	@Column(name = "cate_id")
	private Long cateId;

	/**
	 * 第三方平台类目主键
	 */
	@Column(name = "third_cate_id")
	private Long thirdCateId;

	/**
	 * 第三方渠道(0，linkedmall)
	 */
	@Column(name = "third_platform_type")
	@Enumerated
	private ThirdPlatformType thirdPlatformType;
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
	 * 删除标识,0:未删除1:已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}