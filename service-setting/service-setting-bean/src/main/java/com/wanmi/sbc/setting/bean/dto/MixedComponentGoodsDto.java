package com.wanmi.sbc.setting.bean.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.enums.BookType;
import com.wanmi.sbc.setting.bean.enums.MixedComponentLevel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@ApiModel
public class MixedComponentGoodsDto implements Serializable {
    private Integer id;

    private Integer topicStoreyId;

    private Integer level;

    private String name;

    private Integer bookType;
    private String dropName;

    private Integer orderWay;
    private String image;

    private String titleImage;

    private String url;
    private String recommend;
    private Integer sorting;

    private Integer publishState;

    private List<Integer> labelId;

    MicroServicePage<ColumnContentDTO> columnContentDTOS;

    public MixedComponentGoodsDto(ColumnDTO columnDTO) {
        this.id = columnDTO.getId();
        this.topicStoreyId = columnDTO.getTopicStoreyId();
        this.name = columnDTO.getName();
        this.dropName = columnDTO.getDropName();
        this.orderWay = columnDTO.getOrderWay();
        this.labelId = JSON.parseArray(columnDTO.getLabelId(), Integer.class);
        this.sorting = columnDTO.getOrderNum();
        this.publishState = columnDTO.getDeleted();
        this.level = columnDTO.getLevel();
        this.bookType = columnDTO.getBookType();
        this.recommend = columnDTO.getRecommend();
        JSONObject jsonObject = JSON.parseObject(columnDTO.getAttributeInfo());
        if (BookType.VIDEO.toValue().equals(this.bookType)) {
            this.image = jsonObject.get("image").toString();
            this.url = jsonObject.get("video").toString();
        } else if(BookType.ASSIGN.toValue().equals(this.bookType))  {
            this.image = jsonObject.get("image").toString();
            this.titleImage = jsonObject.get("titleImage").toString();
        } else {}
    }

    public MixedComponentGoodsDto() {
    }

}
