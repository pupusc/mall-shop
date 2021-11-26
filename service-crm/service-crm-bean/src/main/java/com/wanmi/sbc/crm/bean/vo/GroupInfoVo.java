package com.wanmi.sbc.crm.bean.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName GroupInfoVo
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/1/17 13:42
 **/
@Data
public class GroupInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 人群id
     */
    private String groupId;

    /**
     * 人群名称
     */
    private String groupName;

    /**
     * 人群类型：0：系统分群，1：自定义人群
     */
    private Integer type;

}
