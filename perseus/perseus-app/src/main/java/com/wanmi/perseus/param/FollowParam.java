package com.wanmi.perseus.param;

import lombok.Data;

/**
 * @ClassName FollowParam
 * @Description TODO
 * @Author zhanggaolei
 * @Date 2021/2/2 14:26
 * @Version 1.0
 **/
@Data
public class FollowParam {
    /**
     * 会员id
     */
    private String id;

    /**
     * skuId
     */
    private String skuId;

    /**
     * 访问路径
     */
    private String url;

    /**
     * 终端类型
     */
    private String clientType;

    /**
     *公司id
     */
    private String shopId;

}
