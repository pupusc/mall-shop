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
public class RfmGroupDataVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

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
}
