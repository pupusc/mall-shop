package com.soybean.elastic.booklistmodel.model.sub;

import com.soybean.elastic.collect.constant.ConstantUtil;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/31 5:30 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsBookListSubSpuNew {

    @Field(type = FieldType.Keyword)
    private String spuId;

    /**
     * 商品标题
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String spuName;

    /**
     * 商品主图
     */
    @Field(type = FieldType.Keyword, index = false)
    private String pic;

    /**
     * 无背景图
     */
    @Field(type = FieldType.Keyword, index = false)
    private String unBackgroundPic;

    /**
     * 排序
     */
    @Field(type = FieldType.Integer, index = false)
    private Integer sortNum;

    /**
     * 商品渠道类型
     */
    @Field(type = FieldType.Integer)
    private List<String> channelTypes;
}
