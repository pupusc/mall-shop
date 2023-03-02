package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/10:22
 * @Description:
 */
@Data
public class MetaTradeTreeRespVO {

    private int id;
    private int parentId;
    private String name;
    private String descr;
    private int orderNum;
    private String  path;
    private String createTime;
    private String updateTime;
    private int delFlag;
    private List<MetaTradeTreeRespVO> childrenList;
}
