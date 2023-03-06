package com.wanmi.sbc.setting.api.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RankRedisListResponse implements Serializable {
    private static final long serialVersionUID = -5138062021719883495L;

    private List<RankRequest> rankRequestList;
}
