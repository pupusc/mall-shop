package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/15:43
 * @Description:
 */
@Data
public class GoodsSearchKeyAddBo {
    private int id;
    private String name;
    private String spuId;
    private int delFlag;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
