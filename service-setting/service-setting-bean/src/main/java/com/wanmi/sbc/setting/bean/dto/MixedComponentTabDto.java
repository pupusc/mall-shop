package com.wanmi.sbc.setting.bean.dto;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.enums.BookType;
import com.wanmi.sbc.setting.bean.enums.MixedComponentLevel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author zh
 * @Date 2023/2/10 10:29
 */
@Data
@ApiModel
public class MixedComponentTabDto implements Serializable {
    private static final long serialVersionUID = 1817873869725056066L;

    private Integer id;

    private String name;

    private Integer level;

    private String labelId;

    private String subName;

    private String dropName;

    private SelectDto color;

    private SelectDto image;

    private Integer sorting;
    private Integer pId;

    private Integer bookType;

    private String recommend;

    private String attributeInfo;

    /**
     * 1-未启用 0-启用
     */
    public Integer publishState;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    private List<KeyWordsDto> keywords;

    public MixedComponentTabDto(ColumnDTO columnDTO) {
        this.id = columnDTO.getId();
        this.name = columnDTO.getName();
        this.subName = columnDTO.getSubName();
        this.dropName = columnDTO.getDropName();
        this.labelId = columnDTO.getLabelId();
        this.sorting = columnDTO.getOrderNum();
        this.startTime = columnDTO.getCreateTime();
        this.endTime = columnDTO.getEndTime();
        this.publishState = columnDTO.getDeleted();
        this.level = columnDTO.getLevel();
        this.pId = columnDTO.getPId();
        this.bookType = columnDTO.getBookType();
        this.recommend = columnDTO.getRecommend();
        this.attributeInfo = columnDTO.getAttributeInfo();
        this.color = JSON.parseObject(columnDTO.getColor(), SelectDto.class);
        this.image = JSON.parseObject(columnDTO.getImage(), SelectDto.class);
        if (!MixedComponentLevel.FOUR.toValue().equals(level)) {
            List<KeyWordsDto> keyWordsDtos = JSON.parseArray(columnDTO.getAttributeInfo(), KeyWordsDto.class);
            this.keywords = keyWordsDtos != null ? keyWordsDtos.stream().sorted(Comparator.comparing(KeyWordsDto::getSort))
                    .collect(Collectors.toList()) : null;
        }
    }

    public MixedComponentTabDto() {
    }
}
