package com.wanmi.sbc.goods.fandeng;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-03-17 21:19:00
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SiteSearchNotifyModel implements Serializable {
    /**
     * 书籍单本
     */
    public static final String NOTIFY_TYPE_BOOK_RES = "BOOK_RES";
    /**
     * 书籍书单
     */
    public static final String NOTIFY_TYPE_BOOK_PKG = "BOOK_PKG";

    /**
     * 类型：单本BOOK_RES;书单BOOK_PKG;
     */
    private String type;
    /**
     * 商品id
     */
    private List<String> ids;

    public static SiteSearchNotifyModel buildBookResModel(List<String> ids) {
        return new SiteSearchNotifyModel(NOTIFY_TYPE_BOOK_RES, ids);
    }

    public static SiteSearchNotifyModel buildBookPkgModel(List<String> ids) {
        return new SiteSearchNotifyModel(NOTIFY_TYPE_BOOK_PKG, ids);
    }
}