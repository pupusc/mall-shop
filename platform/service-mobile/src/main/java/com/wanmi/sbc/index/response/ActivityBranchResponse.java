package com.wanmi.sbc.index.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ActivityBranchResponse implements Serializable {


    private static final long serialVersionUID = -6502332422409885243L;
    /**
     * 主题色
     */
    private String themeColor;

    /**
     * 埋点键
     */
    private String buriedPoint;

    /**
     * 分会场名称
     */
    private String branchVenueName;


    /**
     * 分会场配置内容
     */
    private List<IndexConfigChild1Response> branchVenueConfigs;

    /**
     * 分会场模块内容
     */
    private List<ActivityBranchContentDetailResponse> branchVenueContents;

}