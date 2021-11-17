package com.wanmi.sbc.setting.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel
public class TopicConfigVO implements Serializable {
    private static final long serialVersionUID = 5712546804426131799L;

    @ApiModelProperty("主键")
    private  Integer id;
    @ApiModelProperty("专题id")
    private String topicKey;
    @ApiModelProperty("专题名称")
    private String topicName;

    private String linkUrl;
    @ApiModelProperty("主题色")
    private String topicColor;

    @ApiModelProperty("导航色")
    private String navigationColor;
}
