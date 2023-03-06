package com.wanmi.sbc.setting.api.request.topicconfig;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.setting.bean.dto.SelectDto;
import com.wanmi.sbc.setting.bean.enums.BookType;
import com.wanmi.sbc.setting.bean.enums.MixedComponentLevel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ApiModel
public class MixedComponentVideoAddRequest implements Serializable {
    private Integer level = MixedComponentLevel.FOUR.toValue();

    private Integer bookType;
    private String dropName;

    private String image;

    private String titleImage;

    private String url;
    private String recommend;
    private Integer sorting;

    private List<Integer> labelId;

    private List<ColumnContentAddRequest> columnContent;

    public ColumnAddRequest getColumnAddRequest() {
        ColumnAddRequest columnAddRequest = new ColumnAddRequest();
        columnAddRequest.setLevel(level);
        columnAddRequest.setBookType(bookType);
        columnAddRequest.setDropName(dropName);
        columnAddRequest.setRecommend(recommend);
        columnAddRequest.setOrderNum(sorting);
        columnAddRequest.setLabelId(JSON.toJSONString(labelId));
        Map<String, Object> attributeInfo = new HashMap<>();
        if (BookType.VIDEO.toValue().equals(bookType)) {
            attributeInfo.put("image", image);
            attributeInfo.put("video", url);
        } else if(BookType.ASSIGN.toValue().equals(bookType))  {
            attributeInfo.put("titleImage", titleImage);
            attributeInfo.put("image", image);
        } else {

        }
        columnAddRequest.setAttributeInfo(JSON.toJSONString(attributeInfo));
        return columnAddRequest;
    }
}
