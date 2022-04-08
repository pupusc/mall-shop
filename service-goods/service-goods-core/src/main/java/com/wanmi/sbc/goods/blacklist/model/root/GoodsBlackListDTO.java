package com.wanmi.sbc.goods.blacklist.model.root;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;

/**
 * Description: 黑名单
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/20 1:59 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@Entity
@Table(name = "t_goods_blacklist")
public class GoodsBlackListDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 业务名字
     */
    @Column(name = "business_name")
    private String businessName;

    /**
     * 业务id
     */
    @Column(name = "business_id")
    private String businessId;

    /**
     * 业务分类 1 新品榜 2 畅销排行榜 3 特价书榜 4、不显示会员价的商品 5、库存编码 6、积分黑名单 7、底部分类 8、首页商品搜索H5和领阅不展示 9、首页商品搜索H5不展示
     */
    @Column(name = "business_category")
    private Integer businessCategory;

    /**
     * 业务类型 1、商品skuId 2、商品spuId 3、一级分类id 4、二级分类id、5、无效
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 版本号
     */
    @Column(name = "version")
    @Version
    private Integer version;

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
     * 已删除：1，未删除：0
     */
    @Column(name = "del_flag")
    private Integer delFlag;

}
