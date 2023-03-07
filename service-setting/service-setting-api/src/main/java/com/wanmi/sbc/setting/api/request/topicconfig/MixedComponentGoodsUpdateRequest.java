package com.wanmi.sbc.setting.api.request.topicconfig;

import com.alibaba.fastjson.JSON;
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
public class MixedComponentGoodsUpdateRequest implements Serializable {

    private Integer id;

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


    public ColumnAddRequest getColumnAddRequest() {
        ColumnUpdateRequest columnAddRequest = new ColumnUpdateRequest();
        columnAddRequest.setId(id);
        columnAddRequest.setName(name);
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
