package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/04/10:55
 * @Description:
 */
@Data
public class GoodsLabelSpuReqBO implements Serializable {
    private int id;
    private String goodsId;
    private String goodsName;
    private int labelId;
    private String labelName;
    private Date createTime;
    private Date updateTime;
    private int firstId;
    private int secondId;
    private int orderNum;

}
