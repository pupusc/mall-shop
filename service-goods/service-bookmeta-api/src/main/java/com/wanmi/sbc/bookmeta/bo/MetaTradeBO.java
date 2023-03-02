package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/10:18
 * @Description:
 */
@Data
public class MetaTradeBO {

    private int id;
    private int parentId;
    private String name;
    private String descr;
    private int orderNum;
    private String  path;
    private String createTime;
    private String updateTime;
    private int delFlag;
    private List<MetaTradeBO> childrenList;
}
