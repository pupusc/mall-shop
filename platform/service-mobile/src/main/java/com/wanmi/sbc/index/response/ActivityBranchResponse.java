package com.wanmi.sbc.index.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ActivityBranchResponse implements Serializable {


    private static final long serialVersionUID = -6502332422409885243L;
    /**
     * 分会场配置内容
     */
    private List<Object> branchVenueConfigs;

    /**
     * 分会场模块内容
     */
    private List<ActivityBranchContentDetailResponse> branchVenueContents;

}