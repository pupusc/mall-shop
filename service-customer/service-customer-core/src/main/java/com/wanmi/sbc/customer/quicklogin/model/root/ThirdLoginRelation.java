package com.wanmi.sbc.customer.quicklogin.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: songhanlin
 * @Date: Created In 10:01 AM 2018/8/8
 * @Description: 第三方关系表
 */
@Data
@Entity
@Table(name = "third_login_relation")
public class ThirdLoginRelation implements Serializable {

    /**
     * 第三方登录主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "third_login_id")
    private String thirdLoginId;

    /**
     * 第三方关系关联(union)Id
     */
    @Column(name = "third_login_uid")
    private String thirdLoginUid;

    /**
     * 店铺Id
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 用户Id
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 第三方类型 0:wechat
     */
    @Column(name = "third_login_type")
    @Enumerated
    private ThirdLoginType thirdLoginType;

    /**
     * 微信授权openId, 该字段只有微信才有, 由于微信登录使用的是unionId,
     * 但是微信模板消息发送需要使用openId, 所以需要union_id, 所以union_id和open_id单独存放
     */
    @Column(name = "third_login_open_id")
    private String thirdLoginOpenId;

    /**
     * 微信头像图片地址
     */
    @Column(name = "head_img_url")
    private String headimgurl;

    /**
     * 微信昵称
     */
    @Column(name = "nick_name")
    private String nickname;

    /**
     * 绑定时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "binding_time")
    private LocalDateTime bindingTime;


    /**
     * 是否解绑（1：已解绑 0：未解绑）
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag = DeleteFlag.NO;

    /**
     * 解绑时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "unbinding_time")
    private LocalDateTime unbindingTime;
}
