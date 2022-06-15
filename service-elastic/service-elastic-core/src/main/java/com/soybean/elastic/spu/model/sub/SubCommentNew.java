package com.soybean.elastic.spu.model.sub;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

/**
 * Description: 店铺分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/19 2:53 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SubCommentNew {

    /**
     * 总评数
     */
    @Field(type = FieldType.Integer)
    private Integer evaluateSum;

    /**
     * 好评数
     */
    @Field(type = FieldType.Integer)
    private Integer goodEvaluateSum;

    /**
     * 好评率
     */
    @Field(type = FieldType.Keyword)
    private String goodEvaluateRatio;
}
