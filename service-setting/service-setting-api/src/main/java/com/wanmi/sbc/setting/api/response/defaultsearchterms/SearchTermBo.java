package com.wanmi.sbc.setting.api.response.defaultsearchterms;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/16/14:05
 * @Description:
 */
@Data
public class SearchTermBo {

    private Long id;
    private Integer defaultChannel;
    private String defaultSearchKeyword;
    private String name;
    private String relatedLandingPage;
    private Long parentId;
    private Boolean isParent;
    private String imgBefore;
    private String imgAfter;
    private Long sortNumber;
    private DeleteFlag delFlag;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
    private String createPerson;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;
    private String updatePerson;
    private List<SearchTermBo> childrenList;
    private Integer pageNum = 0;
    private Integer pageSize = 10;
}
