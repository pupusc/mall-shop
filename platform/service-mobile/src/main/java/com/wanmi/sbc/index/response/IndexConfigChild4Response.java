package com.wanmi.sbc.index.response;

import lombok.Data;

import java.io.Serializable;


@Data
public class IndexConfigChild4Response implements Serializable {
    private static final long serialVersionUID = 6951120653774554222L;
    /**
     * 好书5折购
     */
    private IndexConfigChild1Response hs5zgUrl;
    /**
     * 童书2折起
     */
    private IndexConfigChild1Response ts2zqUrl;
    /**
     * 好书好课5.5折起
     */
    private IndexConfigChild1Response hshk55zqUrl;
    /**
     * 品牌联名专区
     */
    private IndexConfigChild1Response pplmzqUrl;
    /**
     * 大牌6折封顶
     */
    private IndexConfigChild1Response dp6zfdUrl;


}