package com.wanmi.sbc.setting.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TopicConfigVO implements Serializable {
    private static final long serialVersionUID = 5712546804426131799L;

    private  Integer id;

    private String topicName;
    private Integer topicType;
    private String linkUrl;
    private Integer status;
}
