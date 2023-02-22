package com.wanmi.sbc.setting.api.request.topicconfig;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.dto.KeyWordsDto;
import com.wanmi.sbc.setting.bean.dto.SelectDto;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel
public class MixedComponentGoodsAddRequest implements Serializable {

    private String name;
    private Integer level;

    private Integer bookType;
    private String dropName;

    private Integer orderWay;
    private SelectDto image;

    private String recommend;
    private Integer sorting;

    private List<Integer> labelId;

    public ColumnAddRequest getColumnAddRequest() {
        ColumnAddRequest columnAddRequest = new ColumnAddRequest();
        columnAddRequest.setName(name);
        columnAddRequest.setLevel(level);
        columnAddRequest.setBookType(bookType);
        columnAddRequest.setDropName(dropName);
        columnAddRequest.setOrderType(orderWay);
        columnAddRequest.setImage(image);
        columnAddRequest.setRecommend(recommend);
        columnAddRequest.setOrderNum(sorting);
        return columnAddRequest;
    }
}
