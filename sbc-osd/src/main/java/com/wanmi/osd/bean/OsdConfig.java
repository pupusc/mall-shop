package com.wanmi.osd.bean;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OsdConfig implements Serializable {
    /**
     * 请求路径
     */
    @NonNull
    private String endPoint;

    /**
     * 用户名
     */
    @NonNull
    private String accessKeyId;

    /**
     * 密钥
     */
    @NonNull
    private String accessKeySecret;

    /**
     * 存储空间名
     */
    @NonNull
    private String bucketName;

    /**
     * 地区
     */
    @NonNull
    private String region;

    /**
     * osd类型
     */
    @NonNull
    private OsdType osdType;
}
