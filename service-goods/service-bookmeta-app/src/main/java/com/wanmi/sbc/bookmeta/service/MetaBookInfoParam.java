package com.wanmi.sbc.bookmeta.service;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Liang Jun
 * @date 2022-07-25 19:17:00
 */
@Data
public class MetaBookInfoParam {
    /**
     * 主键
     */
    @NotNull
    private Integer id;
    /**
     * 是否查询关联丛书
     */
    private boolean queryBookClump;
    /**
     * 是否查询关联作者
     */
    private boolean queryAuthor;
    /**
     * 是否查询出版社
     */
    private boolean queryPublisher;
    /**
     * 是否查询出品方
     */
    private boolean queryProducer;
}
