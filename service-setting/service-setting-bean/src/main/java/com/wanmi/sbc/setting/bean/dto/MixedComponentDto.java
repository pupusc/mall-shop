package com.wanmi.sbc.setting.bean.dto;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
public class MixedComponentDto implements Serializable {

    private Integer id;

    private String name;

    private String subName;

    private SelectDto color;

    private SelectDto image;

    private List<KeyWordDto> keywords;

    public MixedComponentDto() {
    }

    public MixedComponentDto(MixedComponentTabDto columnDTO) {
        this.id = columnDTO.getId();
        this.name = columnDTO.getName();
        this.subName = columnDTO.getSubName();
        this.color = columnDTO.getColor();
        this.image = columnDTO.getImage();
    }
}
