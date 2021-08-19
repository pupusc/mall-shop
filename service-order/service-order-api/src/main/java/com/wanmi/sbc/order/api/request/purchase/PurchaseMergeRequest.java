package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.order.bean.dto.PurchaseMergeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-09-25
 */
@Data
@ApiModel
public class PurchaseMergeRequest implements Serializable {

    private static final long serialVersionUID = -4440773479042823980L;

    @ApiModelProperty(value = "采购单信息")
    @NotNull
    @Size(min = 1,max = 50)
    @Valid
    private List<PurchaseMergeDTO> purchaseMergeDTOList;

    @ApiModelProperty(value = "客户信息")
    private CustomerDTO customer;

    /**
     * 邀请人id-会员id
     */
    @ApiModelProperty(value = "邀请人id")
    String inviteeId;

    /**
     * 终端来源
     */
    @ApiModelProperty(value = "终端来源", hidden = true)
    private TerminalSource terminalSource;
}
