package com.wanmi.sbc.index.response;

import com.wanmi.sbc.booklistmodel.response.SortGoodsCustomResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ActivityBranchContentDetailResponse extends ActivityBranchContentResponse implements Serializable {


    private static final long serialVersionUID = 2582391775848060303L;
    /**
     * 模块内容
     */
    private List<SortGoodsCustomResponse> activityBranchContentResponses;

}