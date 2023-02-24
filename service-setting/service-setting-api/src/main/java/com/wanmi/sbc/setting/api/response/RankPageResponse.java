package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.api.request.RankPageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RankPageResponse implements Serializable {
    private static final long serialVersionUID = -5138062021719883495L;

    private RankPageRequest pageRequest;

    private List<String> idList;

    private List<Integer> rankIdList;

    private String imgUrl;

}
