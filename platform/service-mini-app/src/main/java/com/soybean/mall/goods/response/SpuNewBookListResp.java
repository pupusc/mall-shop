package com.soybean.mall.goods.response;

import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.elastic.api.utils.ConstantUtil;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 商品书单结果信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 12:17 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SpuNewBookListResp {

    private String spuId;

    private String skuId;

    /**
     * 商品标题
     */
    private String spuName;

    /**
     * 商品副标题
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
     * 市场价
     */
    private BigDecimal marketPrice;

    /**
     * 是否是会员 0 否 1 是
     */
    private Integer hasVip;

    /**
     * 是否有更多规格
     */
    private Boolean specMore = false;


    /**
     * 主播推荐
     */
    private String anchorRecomName;

    /**
     * 显示标签(当前只有栏目中有设置)
     */
    private String spuTag;

    /**
     * 商品主图
     */
    private String pic;

    /**
     * 商品无背景图
     */
    private String unBackgroundPic;

    /**
     * 书单/榜单信息
     */
    private BookList bookList;


    /**
     * 商品库存
     */
    private Long stock;

    /**
     * 标签信息
     */
    private List<Label> labels;

    /**
     * 氛围图
     */
    private Atmosphere atmosphere;

    /**
     * 图书信息
     */
    private Book book;

    /**
     * 优惠券标签
     */
    private List<CouponLabel> couponLabels = new ArrayList<>();

    /**
     * 促销标签
     */
    private List<MarketingLabel> marketingLabels = new ArrayList<>();

    /**
     * 活动信息
     */
    private List<NormalActivity> activities;

    /**
     * spu关联的所有sku
     */
    private List<GoodsInfoVO> skus = new ArrayList<>();

    private Integer buyCount;

    /**
     * 商品标签
     */
    private List<SubSpuLabelNew> spuLabels;

    /**
     * 营销标签
     */
    private List<SubSkuMarketingLabelNew> marketingLabel;

    @Data
    public static class SubSpuLabelNew {

        private String showName;

        private String name;

        private Integer id;

        private Integer orderType;

        private Integer type = 1;
    }

    @Data
    public static class SubSkuMarketingLabelNew {

        @Field(type = FieldType.Text)
        private String name;

        @Field(type = FieldType.Nested)
        private List<Labels> labels;

        @Data
        public class Labels {

            @Field(type = FieldType.Text)
            private String sku_no;

            @Field(type = FieldType.Text)
            private String spu_no;

            @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
            private String show_name;

            @Field(type = FieldType.Text)
            private String name;

            @Field(type = FieldType.Text)
            private String goods_info_id;

            @Field(type = FieldType.Text)
            private String goods_info_name;

            @Field(type = FieldType.Integer)
            private Integer id;

            @Field(type = FieldType.Integer)
            private Integer order_num;

            @Field(type = FieldType.Integer)
            private Integer order_type;

            @Field(type = FieldType.Integer)
            private Integer freight_temp_id;
        }
    }

    /**
     * 书单或者榜单信息
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookList {

        /**
         * 书单展示内容【拼接好的】
         */
        private String bookListNameShow;
        /**
         * 书单名称
         */
        private String bookListName;

        /**
         * 排序
         */
        private Integer sortNum;


        private Integer bookListBusinessType;
    }

    /**
     * 商品信息
     */
    @Data
    public static class Book {

        /**
         * 作者信息
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

        /**    private String elementThree;

         * 图书标签
         */
        private List<BookTag> tags;

        /**
         * 图书标签
         */
        @Data
        public static class BookTag {
            /**
             * 标签id
             */
            private Integer tageId;

            /**
             * 标签名
             */
            private String tagName;
        }
    }

    /**
     * 氛围图信息
     */
    @Data
    public static class Atmosphere {
        /**
         * 图片地址
         */
        private String imageUrl;

        /**
         * 氛围类型1通用2积分
         */
        private Integer atmosType;

        /**
         * 元素1,左侧第一行文字
         */
        private String elementOne;

        /**
         * 元素2：左侧第二行文字
         */
        private String elementTwo;

        /**
         * 元素3：右侧第一行文字
         */
        private String elementThree;

        /**
         * 元素4：右侧第二行文字
         */
        private String elementFour;

    }

    /**
     * 优惠券标签
     */
    @Data
    public static class  CouponLabel {
        /**
         * 优惠券Id
         */
        private String couponInfoId;

        /**
         * 优惠券活动Id
         */
        private String couponActivityId;

        /**
         * 促销描述
         */
        private String couponDesc;
        /**
         * 使用场景，1专题2商详3领券中心，可多选，用，分隔
         */
        private String couponScene;
    }

    /**
     * 营销标签
     */
    @Data
    public static class MarketingLabel {
        /**
         * 营销编号
         */
        private Long marketingId;

        /**
         * 促销类型 0：满减 1:满折 2:满赠
         * 与Marketing.marketingType保持一致
         */
        private Integer marketingType;

        /**
         * 促销描述
         */
        private String marketingDesc;

        /**
         * 活动状态
         */
        private Integer marketingStatus;
    }

    /**
     * 活动信息
     */
    @Data
    public static class NormalActivity {
        /**
         * 份数
         */
        private Integer num;

        /**
         * 活动展示
         */
        private String activityShow;

    }

    /**
     * 标签信息
     */
    @Data
    public static class Label {

        /**
         * 标签名
         */
        private String labelName;

        /**
         * 标签类别 {@link com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum}
         */
        private Integer labelCategory;
    }
}
