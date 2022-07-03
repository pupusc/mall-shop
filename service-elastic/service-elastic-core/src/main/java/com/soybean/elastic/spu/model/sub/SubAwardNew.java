package com.soybean.elastic.spu.model.sub;

import com.soybean.elastic.collect.constant.ConstantUtil;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/31 5:05 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SubAwardNew {

    /**
     * 奖品分类 1图书 2人物
     */
    @Field(type = FieldType.Integer)
    private Integer awardCategory;

    /**
     * 奖项名称
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String awardName;
}
