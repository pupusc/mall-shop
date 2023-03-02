package com.wanmi.sbc.setting.api.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RankRequestListResponse implements Serializable {
    private static final long serialVersionUID = -5138062021719883495L;

    private List<RankRequest> rankRequestList;

    private List<String> idList;

    private List<Integer> rankIds;
}
