package com.wanmi.sbc.setting.sysswitch;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 系统开关
 * Created by yuanlinling on 2017/4/26.
 */
@Data
@Entity
@Table(name = "system_switch")
public class Switch {

    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 开关名称
     */
    @Column(name = "switch_name")
    private String switchName;

    /**
     *开关状态 0：关闭 1：开启
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 删除标志
     */
    @Column(name = "del_flag")
    @NotNull
    private DeleteFlag delFlag;

}
