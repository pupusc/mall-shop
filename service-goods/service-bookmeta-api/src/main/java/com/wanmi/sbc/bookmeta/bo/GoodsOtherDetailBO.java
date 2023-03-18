package com.wanmi.sbc.bookmeta.bo;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/18/14:40
 * @Description:
 */
@Data
public class GoodsOtherDetailBO implements Serializable {
    private int id;
    private String goodsId;
    private String goodsName;
    private int labelId;
    private int firstId;
    private int secondId;
    private int orderNum;
    private Page page = new Page(1,10);
}
