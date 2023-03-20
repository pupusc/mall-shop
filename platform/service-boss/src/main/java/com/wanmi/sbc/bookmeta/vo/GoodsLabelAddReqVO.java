package com.wanmi.sbc.bookmeta.vo;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/04/10:57
 * @Description:
 */
@Data
public class GoodsLabelAddReqVO implements Serializable {

    private int id;
    private String goodsId;
    private String goodsName;
    private int labelId;
    private int firstId;
    private int secondId;
    private int orderNum;
    private Page page = new Page(1,10);
}
