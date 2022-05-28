package com.soybean.elastic.spu.model.sub;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * Description: 活动信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/29 12:40 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SubActivityNew {

    /**
     * 活动id
     */
    @Field(type = FieldType.Keyword)
    private String activityId;

    /**
     * 活动名称
     */
    @Field(type = FieldType.Keyword)
    private String activityName;

    /**
     * 活动类别 1 优惠券活动 2 促销活动
     */
    @Field(type = FieldType.Long)
    private Integer activityCategory;

    /**
     * 优惠券id
     */
    @Field(type = FieldType.Keyword)
    private List<String> couponIds;
}
