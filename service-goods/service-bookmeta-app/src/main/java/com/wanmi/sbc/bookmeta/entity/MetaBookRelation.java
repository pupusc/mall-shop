package com.wanmi.sbc.bookmeta.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.GeneratedValue;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/06/16:33
 * @Description:
 */
@Data
public class MetaBookRelation implements Serializable {

    @GeneratedValue
    private Integer id;
    private Integer bookId;
    private String name;
    private String subName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT-8")
    private String startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT-8")
    private String endTime;
    private Integer orderNum;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT-8")
    private String createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT-8")
    private String updateTime;
    private Integer delFlag;


}
