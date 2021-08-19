package com.wanmi.sbc.account.api.response.finance.record;

import com.wanmi.sbc.account.bean.vo.SettlementViewVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 结算单响应请求
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class SettlementGetViewResponse extends SettlementViewVO implements Serializable {

    private static final long serialVersionUID = 4287955217503178411L;
}
