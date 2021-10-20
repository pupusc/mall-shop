package com.wanmi.sbc.index.requst;


import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


@Data
public class BranchVenueIdRequest extends BaseQueryRequest implements Serializable {

    private static final long serialVersionUID = 3228778527828317959L;

    /**
     * key
     */
    @NotEmpty(message = "branchVenueId不能为空！")
    private Integer branchVenueId;



}
