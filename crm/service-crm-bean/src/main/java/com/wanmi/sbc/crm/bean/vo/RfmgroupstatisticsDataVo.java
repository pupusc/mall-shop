package com.wanmi.sbc.crm.bean.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RfmgroupstatisticsVo
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/10/15 16:53
 **/
@ApiModel
@Data
public class RfmgroupstatisticsDataVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 人群id
     */
    private Long groupId;

    /**
     * 人群名称
     */
    private String groupName;

    /**
     * 人群定义
     */
    private String groupDefinition;

    /**
     * 人群运营建议
     */
    private String groupAdvise;

    /**
     * 会员人数
     */
    private int customerNum;

    /**
     * 访问数
     */
    private int uvNum;

    /**
     * 成交数
     */
    private int tradeNum;

}
