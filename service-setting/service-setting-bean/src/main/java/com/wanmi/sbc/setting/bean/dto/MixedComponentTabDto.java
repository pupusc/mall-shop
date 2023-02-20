package com.wanmi.sbc.setting.bean.dto;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    private String subName;

    private String dropName;

    private SelectDto color;

    private SelectDto image;

    private Integer sorting;

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

    public MixedComponentTabDto(ColumnDTO columnDTO) {
        this.id = columnDTO.getId();
        this.name = columnDTO.getName();
        this.subName = columnDTO.getSubName();
        this.dropName = columnDTO.getDropName();
        this.sorting = columnDTO.getOrderNum();
        this.startTime = columnDTO.getCreateTime();
        this.endTime = columnDTO.getEndTime();
        this.publishState = columnDTO.getDeleted();
        this.color = JSON.parseObject(columnDTO.getColor(), SelectDto.class);
        this.image = JSON.parseObject(columnDTO.getImage(), SelectDto.class);
    }

    public MixedComponentTabDto() {
    }
}
