package com.wanmi.sbc.customer.api.response.quicklogin;

import com.wanmi.sbc.customer.bean.vo.WeChatQuickLoginVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-05-22 16:26
 **/
@Data
public class WeChatQuickLoginResp implements Serializable {

    private static final long serialVersionUID = -794926359841715439L;

    private WeChatQuickLoginVo weChatQuickLoginVo;

}