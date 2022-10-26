package com.soybean.elastic.api.resp;

import com.soybean.common.resp.CommonPageResp;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 聚合结果
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/20 2:45 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsSpuNewAggResp<S> implements Serializable {

    /**
     * 聚合 标签 信息
     */
    private List<LabelAggs> labelAggs;

    /**
     * 店铺分类
     */
    private List<ClassifyAggs> classifyAggs;

    /**
     * 作者名
     */
    private List<AuthorAggs> authorAggs;

    /**
     * 出版社
     */
    private List<PublisherAggs> publisherAggs;

    /**
     * 奖项
     */
    private List<AwardAggs> awardAggs;


    /**
     * 从书
     */
    private List<ClumpAggs> clumpAggs;

    /**
     * 请求参数信息
     */
    private List<LabelAggs> reqs;

    /**
     * 结果对象信息
     */
    private CommonPageResp<S> result;


    /**
     * 标签名
     */
    @Data
    public static class LabelAggs {
        /**
         * 分类名称{@link com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum}
         */
        private Integer category;

        /**
         * 标签名称
         */
        private String labelName;
    }

    /**
     * 店铺分类
     */
    @Data
    public static class ClassifyAggs {
        private String classifyName;
    }

    /**
     * 作者名
     */
    @Data
    public static class AuthorAggs {
        private String authorName;
    }

    /**
     * 出版社
     */
    @Data
    public static class PublisherAggs {
        private String publisherName;
    }

    /**
     * 奖项
     */
    @Data
    public static class AwardAggs {
        private String awardName;
    }

    /**
     * 从书
     */
    @Data
    public static class ClumpAggs {

        private String clumpName;
    }
}
