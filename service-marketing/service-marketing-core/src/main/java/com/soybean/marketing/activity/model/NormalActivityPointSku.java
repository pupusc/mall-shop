package com.soybean.marketing.activity.model;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 1:38 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@Entity
@Table(name = "t_activity_point_sku")
public class NormalActivityPointSku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * normalActivityId
     */
    @Column(name = "normal_activity_id")
    private Integer normalActivityId;

    /**
     * SkuId
     */
    @Column(name = "sku_id")
    private String skuId;

    /**
     * SkuNo
     */
    @Column(name = "sku_no")
    private String skuNo;

    /**
     * SpuId
     */
    @Column(name = "spu_id")
    private String SpuId;

    /**
     * spuNo
     */
    @Column(name = "spu_no")
    private String spuNo;

//    /**
//     * 排序数
//     */
//    @Column(name = "sort_num")
//    private Integer sortNum;

    /**
     *  数量
     */
    @Column(name = "num")
    private Integer num;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @Column(name = "del_flag")
    private DeleteFlag delFlag;
}
