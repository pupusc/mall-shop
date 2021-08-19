package com.wanmi.sbc.order.bean.dto.yzorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单买家信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyerInfoDTO implements Serializable {

    private static final long serialVersionUID = -3948125301181399663L;

    /**
     * 买家id
     */
    private Long buyer_id;

    /**
     * 粉丝昵称
     */
    private String fans_nickname;

    /**
     * 粉丝id
     */
    private Long fans_id;

    /**
     * 粉丝类型
     */
    private Long fans_type;

    /**
     * 买家手机号
     */
    private String buyer_phone;

    /**
     * 微信H5和微信小程序（有赞小程序和小程序插件）的订单会返回微信weixin_openid，三方App（有赞APP开店）的订单会返回open_user_id，2019年1月30号后的订单支持返回该参数
     */
    private String outer_user_id;

    /**
     * 有赞对外统一openId
     */
    private String yz_open_id;
}
