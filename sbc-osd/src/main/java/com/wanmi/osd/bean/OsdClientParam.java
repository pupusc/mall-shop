package com.wanmi.osd.bean;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OsdClientParam implements Serializable {
    /**
     * 编号
     */
    private Long id;

    /**
     * 配置键
     */
    private String configKey;

    /**
     * 类型
     */
    @NonNull
    private String configType;

    /**
     * 名称
     */
    private String configName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 内容
     */
    @NonNull
    private String context;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
