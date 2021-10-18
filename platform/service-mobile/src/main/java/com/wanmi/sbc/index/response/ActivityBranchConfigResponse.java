package com.wanmi.sbc.index.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ActivityBranchConfigResponse implements Serializable {


    private static final long serialVersionUID = -1497459452330608305L;
    /**
     * 活动会场ID
     */
    private Integer branchVenueId;

    /**
     * 主题色
     */
    private String themeColor;


    /**
     * 埋点键
     */
    private String buriedPoint;

    /**
     * 活动会场内容
     */
    private List<ActivityBranchContentResponse> branchVenueContents;

}