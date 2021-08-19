package com.wanmi.sbc.crm.customertag.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>会员标签实体类</p>
 * @author zhanglingke
 * @date 2019-10-14 11:19:11
 */
@Data
@Entity
@Table(name = "customer_tag")
public class CustomerTag implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 标签名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 会员人数
	 */
	@Column(name = "customer_num")
	private Integer customerNum;

	/**
	 * 删除标志位，0:未删除，1:已删除
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
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

}