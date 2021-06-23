package com.wanmi.sbc.distribute.dto;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.bean.enums.DistributionLimitType;
import lombok.Data;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午2:46 2019/3/15
 * @Description: 邀请注册信息
 */
@Data
public class InviteRegisterDTO {

    /**
     * 邀请注册可用状态
     */
    private DefaultFlag enableFlag;

    /**
     * 限制条件
     */
    private DistributionLimitType limitType;


    /**
     * 邀请人数
     */
    private Integer inviteCount;

}
