package com.wanmi.ares.source.model.root;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ReplayStoreLevel
 * @Description 客户统计--客户订货量报表统计request类
 * @Author lvzhenwei
 * @Date 2019/9/20 11:09
 **/
@Data
public class ReplayStoreLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long storeLevelId;

    /**
     * 店铺编号
     */
    private Long storeId;

    /**
     * 等级名称
     */
    private String levelName;

}
