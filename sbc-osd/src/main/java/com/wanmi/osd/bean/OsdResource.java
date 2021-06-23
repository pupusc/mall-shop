package com.wanmi.osd.bean;

import lombok.*;

import java.io.InputStream;
import java.io.Serializable;

/**
 * osd资源类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OsdResource implements Serializable {

    /**
     * 文件流
     */
    @NonNull
    private InputStream osdInputStream;

    /**
     * 上传资源key
     */
    @NonNull
    private String osdResourceKey;
}
