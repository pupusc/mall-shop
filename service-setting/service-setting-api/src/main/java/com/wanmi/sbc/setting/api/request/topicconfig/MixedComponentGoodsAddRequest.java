package com.wanmi.sbc.setting.api.request.topicconfig;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.dto.KeyWordsDto;
import com.wanmi.sbc.setting.bean.dto.SelectDto;
import com.wanmi.sbc.setting.bean.enums.BookType;
import com.wanmi.sbc.setting.bean.enums.MixedComponentLevel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ApiModel
public class MixedComponentGoodsAddRequest implements Serializable {

    private Integer topicStoreyId = 194;

    private Integer level = MixedComponentLevel.FOUR.toValue();

    private String name;

    private Integer bookType;
    private String dropName;

    private Integer orderWay;
    private String image;

    private String titleImage;

    private String url;
    private String recommend;
    private Integer sorting;

    private List<Integer> labelId;

    private List<ColumnContentAddRequest> columnContent;

    public ColumnAddRequest getColumnAddRequest() {
        ColumnAddRequest columnAddRequest = new ColumnAddRequest();
        columnAddRequest.setTopicStoreyId(topicStoreyId);
        columnAddRequest.setName(name);
        columnAddRequest.setLevel(level);
        columnAddRequest.setBookType(bookType);
        columnAddRequest.setDropName(dropName);
        columnAddRequest.setOrderType(orderWay);
        columnAddRequest.setRecommend(recommend);
        columnAddRequest.setLabelId(JSON.toJSONString(labelId));
        columnAddRequest.setOrderNum(sorting);
        Map<String, Object> attributeInfo = new HashMap<>();
        if (BookType.VIDEO.toValue().equals(bookType)) {
            attributeInfo.put("image", image);
            attributeInfo.put("video", url);
            columnAddRequest.setAttributeInfo(JSON.toJSONString(attributeInfo));
        } else if(BookType.ASSIGN.toValue().equals(bookType))  {
            attributeInfo.put("titleImage", titleImage);
            attributeInfo.put("image", image);
            columnAddRequest.setAttributeInfo(JSON.toJSONString(attributeInfo));
        } else {}
        return columnAddRequest;
    }
}
