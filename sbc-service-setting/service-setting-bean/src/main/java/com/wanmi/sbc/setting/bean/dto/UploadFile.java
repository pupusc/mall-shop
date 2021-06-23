package com.wanmi.sbc.setting.bean.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 上传文件
 * Created by chenpeng on 2017/11/30.
 */
@Getter
@Setter
@Builder
public class UploadFile implements Serializable{

    private static final long serialVersionUID = 7871064795556664647L;

    /**
     * 名称
     */
    private String originalFilename;

    /**
     * 类型
     */
    private String fileType;

    /**
     * 内容
     */
    private byte[] content;
}
