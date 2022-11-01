package com.soybean.elastic.spu.model.sub;

import com.soybean.elastic.collect.constant.ConstantUtil;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Description: 标签信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/19 1:16 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SubLabelNew {

    /**
     * 标签名
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String labelName;

    /**
     * 1、49包邮标签 {@link com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum}
     */
    @Field(type = FieldType.Integer)
    private Integer category;
}
