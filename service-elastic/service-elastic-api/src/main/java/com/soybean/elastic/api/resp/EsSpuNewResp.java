package com.soybean.elastic.api.resp;

import lombok.Data;

import java.awt.print.Book;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 商品对象信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 11:39 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsSpuNewResp implements Serializable {

    private String spuId;

    /**
     * 商品名称
     */
    private String spuName;

    /**
     *  商品副标题
     */
    private String spuSubName;

    /**
     * 商品分类 1 图书 2 商品
     */
    private Integer spuCategory;

    /**
     * 销售价格(最小sku价格)
     */
    private BigDecimal salesPrice;

    /**
     * 商品主图
     */
    private String pic;

    /**
     * 商品无背景图
     */
    private String unBackgroundPic;

    /**
     * 主播推荐
     */
    private List<SubAnchorRecom> anchorRecoms;


    /**
     * 店铺分类列表
     */
    private List<SubClassify> classifys;

    /**
     * 图书信息
     */
    private Book book;


    /**
     * 主播推荐
     */
    @Data
    public static class SubAnchorRecom {

        /**
         * 主播推荐id
         */
        private Integer recomId;

        /**
         * 主播推荐名称  1、樊登讲书 2非凡精读 3樊登直播 4李蕾慢读
         */
        private String recomName;
    }


    @Data
    public static class SubClassify {

        private Long fClassifyId;

        /**
         * 一级店铺分类名称
         */
        private String fClassifyName;

        private Long classifyId;

        /**
         * 店铺分类名称
         */
        private String classifyName;
    }


    /**
     * 图书基础信息
     */
    @Data
    public static class Book {

        private String isbn;

        /**
         * 作者列表
         */
        private List<String> authorNames;

        /**
         * 评分
         */
        private Double score;

        /**
         * 出版社
         */
        private String publisher;

        /**
         * 定价
         */
        private Double fixPrice;


        /**
         * 标签
         */
        private List<SubBookLabel> tags;


        @Data
        public static class SubBookLabel{
            /**
             * 2级标签
             */
            private Integer sTagId;

            private String sTagName;

            /**
             * 3级标签
             */
            private Integer tagId;

            private String tagName;
        }
    }

}
