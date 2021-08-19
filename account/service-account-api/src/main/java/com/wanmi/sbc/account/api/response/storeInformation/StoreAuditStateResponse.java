package com.wanmi.sbc.account.api.response.storeInformation;


import com.wanmi.sbc.account.bean.enums.CheckState;
import com.wanmi.sbc.account.bean.vo.CompanyAccountVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@ApiModel
@Data
public class StoreAuditStateResponse implements Serializable {

    private static final long serialVersionUID = -2629974480473851524L;

    /**
     * 审核状态 0、待审核 1、已审核 2、审核未通过
     */
    @ApiModelProperty(value = "审核状态")
    private CheckState auditState;


    /**
     * 是否确认打款 (-1:全部,0:否,1:是)
     */
    @ApiModelProperty(value = "是否确认打款(-1:全部,0:否,1:是)")
    private Integer remitAffirm;
}
