package com.wanmi.sbc.bookmeta.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/16:58
 * @Description:
 */
@Data
public class MetaTradeAddReq implements Serializable {

    private int id;
    private int parentId;
    private String name;
    private String descr;
    private int orderNum;
    private String path;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;
    private int delFlag;


}
