package com.wanmi.sbc.message.bean.constant;

/**
 * @program: sbc-micro-service
 * @description: 友盟推送异常码
 * @create: 2020-01-08 17:39
 **/
public class PushErrorCode {
    // 接口配置空
    public final static String UMENG_CONFIG_NULL = "K-300501";

    // 网络请求失败
    public final static String UMENG_HTTP_FAIL = "K-300502";

    // 网络请求IO异常
    public final static String UMENG_HTTP_IO_ERROR = "K-300503";

    // 文件上传失败
    public final static String UMENG_FILE_UPLOAD_NULL = "K-300504";

    // 组装参数异常
    public final static String UMENG_PARAM_ERROR = "K-300505";

    // 友盟接口返回失败
    public final static String UMENG_RETURN_FAIL = "K-300506";

    // 任务已开始，不可操作
    public final static String UMENG_NOT_CANCEL = "K-300507";
}