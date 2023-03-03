package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/15:32
 * @Description:
 */
@Data
public class GoodsSearchKeyAddReqVO implements Serializable {

    private int id;
    private String name;
    private String spuId;
    private int delFlag;
    private Integer status;
    private String createTime;
    private String updateTime;
}
