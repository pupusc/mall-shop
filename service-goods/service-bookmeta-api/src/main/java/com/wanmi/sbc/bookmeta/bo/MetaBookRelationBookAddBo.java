package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/06/17:34
 * @Description:
 */
@Data
public class MetaBookRelationBookAddBo implements Serializable {

    private int id;
    private int relationId;
    private String isbn;
    private String name;
    private int orderNum;
    private Date createTime;
    private Date  updateTime;
    private int delFlag;
}
