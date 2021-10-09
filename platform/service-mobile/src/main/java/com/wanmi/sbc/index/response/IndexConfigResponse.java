package com.wanmi.sbc.index.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class IndexConfigResponse implements Serializable {

    private static final long serialVersionUID = -54783098328642901L;
    /**
     * 阅读狂欢记
     */
    private IndexConfigChild1Response ydkhjConfig;
    /**
     * 会员限量福利抢
     */
    private IndexConfigChild1Response hyxlflqConfig;

    /**
     * 好物提前购
     */
    private List<IndexConfigChild1Response> hwtqgConfig;
    /**
     * 樊登推荐
     */
    private IndexConfigChild3Response fdtjConfig;

    /**
     * 好书5折封顶
     */
    private List<IndexConfigChild1Response> hs5zfdConfig;

    /**
     * 童书2折起
     */
    private List<IndexConfigChild1Response> ts2zqConfig;
    /**
     * 好书+好课5.5折起
     */
    private List<IndexConfigChild1Response> hshk55zqConfig;
    /**
     * 品牌联名专区-横版
     */
    private List<IndexConfigChild1Response> pplmzqhbConfig;
    /**
     * 品牌联名专区-坚版
     */
    private List<IndexConfigChild1Response> pplmzqsbConfig;

    /**
     * 大牌6折封顶
     */
    private List<IndexConfigChild1Response> dp6zfdConfig;

    /**
     * 更多地址
     */
    private IndexConfigChild4Response moreJumpUrl;

    /**
     * 搜索文案
     */
    private String searchText;


}