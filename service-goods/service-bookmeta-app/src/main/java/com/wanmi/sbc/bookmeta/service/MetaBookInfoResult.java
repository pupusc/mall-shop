package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookClump;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.entity.MetaProducer;
import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import lombok.Data;

import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-07-25 19:17:00
 */
@Data
public class MetaBookInfoResult extends MetaBook {
    /**
     * 丛书信息
     */
    private MetaBookClump metaBookClump;
    /**
     * 图书作者
     */
    private List<MetaFigure> authors;
    /**
     * 出版社
     */
    private MetaPublisher publisher;
    /**
     * 出品方
     */
    private MetaProducer producer;
}
