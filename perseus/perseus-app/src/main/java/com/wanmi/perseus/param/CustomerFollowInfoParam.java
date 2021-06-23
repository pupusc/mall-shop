package com.wanmi.perseus.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName CustomerFollowInfoParam
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/3/11 15:35
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFollowInfoParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 访问会员id
     */
    private String customer_id;

    /**
     * 访问页面url
     */
    private String url;

    /**
     * 访问终端类型："PC";//pc端，"H5";//h5端， "APP";//app端，"MINIPROGRAM";//小程序（小程序内嵌h5）
     */
    private int terminal_source;

    /**
     * 商品goods_info_id
     */
    private String goods_info_id;

    /**
     * 店铺id
     */
    private String company_info_id;

    /**
     * 访问浏览时间
     */
    private LocalDateTime create_time;

}
