package com.soybean.elastic.spu.model.sub;

import com.soybean.elastic.collect.constant.ConstantUtil;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * Description: 图书属性
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/19 3:15 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SubBookNew {

    /**
     * isbn
     */
    @Field(type = FieldType.Keyword)
    private String isbn;

    /**
     * 书名
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String bookName;


    /**
     * 原作名
     */
    @Field(type = FieldType.Keyword)
    private String bookOriginName;

    /**
     * 简介
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String bookDesc;

    /**
     * 作者
     */
    @Field(type = FieldType.Keyword)
    private List<String> authorNames;

    /**
     * 评分
     */
    @Field(type = FieldType.Double)
    private Double score;

    /**
     * 出版社
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String publisher;

    /**
     * 定价
     */
    @Field(type = FieldType.Double)
    private Double fixPrice;

    /**
     * 出品方
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String producer;

    /**
     * 丛书名称
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String clumpName;

    /**
     * 奖项名称
     */
    @Field(type = FieldType.Object)
    private List<SubAwardNew> awards;

    /**
     * 书组名称
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String groupName;

//    /**
//     * 套系列
//     */
//    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
//    private String seriesName;

    /**
     * 装帧
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String bindingName;

    /**
     * 标签
     */
    @Field(type = FieldType.Object)
    private List<SubBookLabelNew> tags;

}
