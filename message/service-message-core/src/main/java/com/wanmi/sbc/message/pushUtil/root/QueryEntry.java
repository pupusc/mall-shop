package com.wanmi.sbc.message.pushUtil.root;

import com.wanmi.sbc.message.bean.enums.MethodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description: 请求参数
 * @create: 2020-01-10 15:48
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryEntry implements Serializable {
    private static final long serialVersionUID = 5618086573074692995L;

    // 任务ID
    private String taskId;

    // 应用key
    private String key;

    // 密钥
    private String appMasterSecret;

    // 请求类型 0：查询 1：撤销任务
    private MethodType type;
}