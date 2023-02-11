package com.wanmi.sbc.topic.response;

import com.wanmi.sbc.setting.api.request.RankRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RankPageRespones implements Serializable {
    private static final long serialVersionUID = -5138062021719883495L;

    private Integer pageNum;

    private Integer pageSize=10;

    private Long total;

    private Long totalPages;

    private List<RankRequest> contentList;
}
