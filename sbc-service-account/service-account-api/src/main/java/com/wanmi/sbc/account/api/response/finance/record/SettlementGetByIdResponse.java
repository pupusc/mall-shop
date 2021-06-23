package com.wanmi.sbc.account.api.response.finance.record;

import com.wanmi.sbc.account.bean.vo.SettlementVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 结算单响应请求
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class SettlementGetByIdResponse extends SettlementVO implements Serializable {

    private static final long serialVersionUID = -8841546927742638134L;
}
