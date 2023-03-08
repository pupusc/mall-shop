package com.wanmi.sbc.setting.api.request;

import io.swagger.models.auth.In;
import jdk.internal.dynalink.linker.LinkerServices;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class RankPageRequest implements Serializable {
    private static final long serialVersionUID = -5138062021719883495L;

    private Integer pageNum;

    private Integer pageSize=10;

    private Long total;

    private Long totalPages;

    private String imageUrl;

    private List<RankRequest> contentList;



}
