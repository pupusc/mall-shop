package com.wanmi.sbc.index.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class IndexConfigChild3Response implements Serializable {
    private static final long serialVersionUID = 6951120653774554222L;
    /**
     * 同步樊登直播折扣好书URL
     */
    private String tbfdzbzkhsUrl;
    /**
     * 同步樊登直播折扣好书
     */
    private List<IndexConfigChild2Response> tbfdzbzkhsConfig;

    /**
     * 樊登周六解读好书URL
     */
    private String fdzljdhsUrl;
    /**
     * 樊登周六解读好书
     */
    private List<IndexConfigChild2Response> fdzljdhsConfig;
}