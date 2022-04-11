package com.wanmi.sbc.order.open.model;

import lombok.Data;

import java.io.*;

/**
 * @author Liang Jun
 * @date 2022-04-02 14:36:00
 */
@Data
public class FddsOrderCreateResult extends FddsBaseResult<FddsOrderCreateResultData> implements Serializable {
    /**
     * status：状态
     * 0000	操作成功
     * 2001	会期商品不存在
     * 2006	已领取不可重复领取
     * 2007	商品已下架
     * 2008	书籍包已下线或未发布
     * 2009	书籍包重复购买
     * 2010	结算方式未配置
     * 2007	商品已下架
     */
}
