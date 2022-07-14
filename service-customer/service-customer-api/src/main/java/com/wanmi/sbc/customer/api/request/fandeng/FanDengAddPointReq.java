package com.wanmi.sbc.customer.api.request.fandeng;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Description: 新增积分
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/14 3:17 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class FanDengAddPointReq {


    /**
     * userNo
     */
    @NotBlank
    private String userNo;

    /**
     * 数量
     */
    @NotNull
    @JSONField(name = "increment")
    private Long num;

    /**
     * 变更类型 1加 2 减
     */
    @NotNull
    private Integer type;

    /**
     * 变更来源 12 表示积分商城
     */
    private Integer changeSource = 12;

    @NotBlank
    private String sourceId;

    @NotBlank
    private String description;
}
