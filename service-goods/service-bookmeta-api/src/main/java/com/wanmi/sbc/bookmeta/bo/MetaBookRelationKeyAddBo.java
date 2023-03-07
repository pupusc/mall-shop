package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/06/17:35
 * @Description:
 */
@Data
public class MetaBookRelationKeyAddBo implements Serializable {

    private Integer id;

    private Integer relationId;

    private String name;

    private Integer orderNum;

    private Date createTime;

    private Date updateTime;

     private Integer delFlag;
}
