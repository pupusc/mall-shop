package com.wanmi.sbc.index.requst;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


@Data
public class BranchVenueIdRequest implements Serializable {

    private static final long serialVersionUID = 3228778527828317959L;

    /**
     * key
     */
    @NotEmpty(message = "branchVenueId不能为空！")
    private Integer branchVenueId;



}
