package com.wanmi.sbc.bookmeta.vo;

import com.wanmi.sbc.bookmeta.bo.MetaTradeBO;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/07/10:29
 * @Description:
 */
@Data
public class MetaTradePageQueryRespVO {
    private List<MetaTradeBO> metaTrades;
    private int total;
    private int totalPage;
}
