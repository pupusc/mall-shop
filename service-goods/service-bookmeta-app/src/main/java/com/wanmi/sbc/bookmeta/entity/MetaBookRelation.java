package com.wanmi.sbc.bookmeta.entity;

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
    private int id;
    private int bookId;
    private String name;
    private String subName;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT-8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT-8")
    private Date endTime;
    private int orderNum;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT-8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT-8")
    private Date updateTime;
    private int delFlag;


}
