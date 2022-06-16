package com.wanmi.sbc.bookmeta.bo.collect;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 图书信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/17 1:52 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CollectMetaBookResp {

    private Integer bookId;

    private String isbn;

    private String bookName;

    /**
     * 原作名
     */
    private String bookOriginName;

    /**
     * 简介
     */
    private String bookDesc;

    /**
     * 作者名
     */
    private List<String> authorNames;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 定价
     */
    private Double fixPrice;

    /**
     * 出品方
     */
    private String producer;

    /**
     * 丛书名称
     */
    private String clumpName;

    /**
     * 奖项名称
     */
    private List<Award> awards;

    /**
     * 书组名称
     */
    private String groupName;

    /**
     * 套系列
     */
    private String seriesName;

    /**
     * 装帧
     */
    private String bindingName;

    /**
     * 标签
     */
    private List<Tag> tags;

    /**
     * 标签
     */
    @Data
    public static class Tag {

        private Integer sTagId;

        private String sTagName;

        private Integer tagId;

        private String tagName;
    }

    /**
     * 奖项
     */
    @Data
    public static class Award{

        private Integer awardCategory;

        /**
         * 奖项名称
         */
        private String awardName;
    }
}
