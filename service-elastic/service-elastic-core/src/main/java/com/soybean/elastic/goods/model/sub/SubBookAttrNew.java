package com.soybean.elastic.goods.model.sub;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 图书属性
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/19 3:15 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class SubBookAttrNew {

    /**
     * 评分
     */
    @Field(type = FieldType.Double)
    private BigDecimal score;

    /**
     * 作者
     */
    @Field(type = FieldType.Object)
    private List<String> author;

    /**
     * isbn
     */
    @Field(type = FieldType.Keyword)
    private String ISBN;

    /**
     * 定价
     */
    @Field(type = FieldType.Double)
    private BigDecimal makePrice;

    /**
     * 出版社
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String publishHouse;
}
