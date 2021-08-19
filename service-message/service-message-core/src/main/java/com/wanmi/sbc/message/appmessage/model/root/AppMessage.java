package com.wanmi.sbc.message.appmessage.model.root;

import com.wanmi.sbc.message.bean.enums.MessageType;
import com.wanmi.sbc.message.bean.enums.NoticeType;
import com.wanmi.sbc.message.bean.enums.ReadFlag;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

/**
 * <p>App站内信消息发送表实体类</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@Data
@Entity
@Table(name = "app_message")
public class AppMessage extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	private String appMessageId;

	/**
	 * 消息一级类型：0优惠促销、1服务通知
	 */
	@Column(name = "message_type")
	private MessageType messageType;

	/**
	 * 消息二级类型
	 */
	@Column(name = "message_type_detail")
	private NoticeType messageTypeDetail;

	/**
	 * 封面图
	 */
	@Column(name = "img_url")
	private String imgUrl;

	/**
	 * 消息标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 消息内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 跳转路由
	 */
	@Column(name = "route_name")
	private String routeName;

	/**
	 * 移动端路由参数
	 */
	@Column(name = "route_param")
	private String routeParam;

	/**
	 * PC端路由参数
	 */
	@Column(name = "pc_route_param")
	private String pcRouteParam;

	/**
	 * 发送时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "send_time")
	private LocalDateTime sendTime;

	/**
	 * 是否已读 0：未读、1：已读
	 */
	@Column(name = "is_read")
	private ReadFlag isRead;

	/**
	 * 删除标识 0：未删除、1：删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 会员id
	 */
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 关联的任务或节点id
	 */
	@Column(name = "join_id")
	private Long joinId;


}