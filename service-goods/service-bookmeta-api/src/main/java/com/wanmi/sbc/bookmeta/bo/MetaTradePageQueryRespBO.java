package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/07/10:23
 * @Description:
 */
@Data
public class MetaTradePageQueryRespBO implements Serializable {
    private List<MetaTradeBO> metaTrades;

    private int totalPage;
    private int total;
}
