package com.wanmi.sbc.bookmeta.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/10:18
 * @Description:
 */
@Data
public class MetaTradeBO implements Serializable {

    private int id;
    private int parentId;
    private String name;
    private String descr;
    private int orderNum;
    private String  path;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;
    private int delFlag;
    private List<MetaTradeBO> childrenList =new ArrayList<>();

    public void addChild(MetaTradeBO node) {
        childrenList.add(node);
    }
}
