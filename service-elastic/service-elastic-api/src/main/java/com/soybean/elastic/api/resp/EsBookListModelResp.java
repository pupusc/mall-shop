package com.soybean.elastic.api.resp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 1:51 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsBookListModelResp implements Serializable {

    /**
     * 书单id
     */
    private Long bookListId;

    /**
     * 书单/榜单类型
     */
    private Integer bookListBusinessType;

    /**
     * 书单/榜单名称
     */
    private String bookListName;

    /**
     * 书单榜单简介
     */
    private String bookListDesc;

    /**
     * 是否置顶 0否 1 是
     */
    private Integer hasTop;

    /**
     * 发布状态 0 草稿 1 已编辑未发布 2 已发布
     */
    private Integer publishState;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 商品信息
     */
    private List<Spu> spus;


    @Data
    public class Spu {

        private String spuId;

        /**
         * 商品标题
         */
        private String spuName;

        /**
         * 排序
         */
        private Integer sortNum;

        /**
         * 商品渠道类型
         */
        private List<String> channelTypes;
    }
}
