package com.soybean.elastic.api.resp;

import com.soybean.elastic.api.utils.ConstantUtil;
import com.wanmi.sbc.goods.bean.dto.MarketingLabelNewDTO;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
    private SubClassify classify;

    /**
     * 图书信息
     */
    private Book book;

    /**
     * 标签信息
     */
    private List<SubLabel> labels;

    /**
     * 商品标签
     */
    private List<SubSpuLabelNew> spuLabels;

    /**
     * 营销标签
     */
    private List<SubSkuMarketingLabelNew> marketingLabel;

    @Data
    public class SubSkuMarketingLabelNew {

        @Field(type = FieldType.Text)
        private String name;

        @Field(type = FieldType.Nested)
        private List<MarketingLabelNewDTO.Labels> labels;
    }

    @Data
    public static class SubSpuLabelNew {
        @Field(type = FieldType.Text)
        private String showName;

        @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
        private String name;

        @Field(type = FieldType.Integer)
        private Integer id;

        @Field(type = FieldType.Integer)
        private Integer orderType;

        @Field(type = FieldType.Integer)
        private Integer type = 1;
    }

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

        private Integer fClassifyId;

        /**
         * 一级店铺分类名称
         */
        private String fClassifyName;

        private Integer classifyId;

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
            private Integer stagId;

            private String stagName;

            /**
             * 3级标签
             */
            private Integer tagId;

            private String tagName;
        }
    }

    /**
     * 同一标签
     */
    @Data
    public static class SubLabel {

        /**
         * 标签名
         */
        private String labelName;

        /**
         * 1、49包邮标签 {@link com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum}
         */
        private Integer category;
    }

}
