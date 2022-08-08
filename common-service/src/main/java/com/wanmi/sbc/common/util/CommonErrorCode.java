package com.wanmi.sbc.common.util;

/**
 * <p>公共异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午2:58.
 */
public final class CommonErrorCode {
    /**
     * 指定异常，不走国际化，异常信息由B2bRuntimeException字段result设定
     */
    public final static String
            SPECIFIED = "K-999999";


    /**
     * 针对我们的业务权限
     */
    public final static String METHOD_NOT_ALLOWED = "K-999998";


    /**
     * 重复提交
     */
    public final static String REPEAT_REQUEST = "K-999997";

    /**
     * 重复提交
     */
    public final static String INCLUDE_BAD_WORD = "K-999996";

    /**
     * 操作成功
     */
    public final static String SUCCESSFUL = "K-000000";

    /**
     * 操作失败
     */
    public final static String FAILED = "K-000001";

    /**
     * 资源不存在
     */
    public final static String DATA_NOT_EXISTS = "K-000003";

    /**
     * 资源已存在
     */
    public final static String DATA_HAS_EXISTS = "K-000033";

    /**
     * 账号已被禁用
     */
    public final static String EMPLOYEE_DISABLE = "K-000005";

    /**
     * 参数错误
     */
    public final static String PARAMETER_ERROR = "K-000009";

    /**
     * 验证码错误
     */
    public final static String VERIFICATION_CODE_ERROR = "K-000010";

    /**
     * 上传文件失败
     */
    public final static String UPLOAD_FILE_ERROR = "K-000011";

    /**
     * 发送失败
     */
    public final static String SEND_FAILURE = "K-000012";

    /**
     * 您没有权限访问
     */
    public final static String PERMISSION_DENIED = "K-000014";


    /**
     * 非法字符
     */
    public final static String ILLEGAL_CHARACTER = "K-000017";


    /**
     * 功能不可用
     */
    public final static String WEAPP_FORBIDDEN = "K-000019";

    /**
     * 无法调用远程服务
     */
    public final static String THIRD_SERVICE_ERROR = "K-000021";


    /**
     * 阿里云连接异常
     */
    public final static String ALIYUN_CONNECT_ERROR = "K-090702";

    /**
     * 阿里云上传图片失败
     */
    public final static String ALIYUN_IMG_UPLOAD_ERROR = "K-090703";

    private CommonErrorCode() {
    }

    /**
     * 系统未知错误
     */
    public static final String SYSTEM_UNKNOWN_ERROR = "system-unknow-error";


    /**
     * 常用物流公司数量超限错误
     */
    public static final String EXPRESS_MAX_COUNT_ERROR = "K-090901";

    /**
     * 状态值已发生改变
     */
    public static final String STATUS_HAS_BEEN_CHANGED_ERROR = "K-090601";

    /**
     * 验证码失效
     */
    public final static String VERIFICATION_CODE_FAILURE = "K-000022";

    /**
     * 生成的错误文件丢失
     */
    public final static String ERROR_FILE_LOST = "K-000029";

    /**
     * 导入的数据有误
     */
    public final static String IMPORTED_DATA_ERROR = "K-030408";

    /**
     * 导入失败，请重试
     */
    public final static String IMPORT_FAIL = "K-030409";

    /**
     * 导入数据量超过上限{0}条
     */
    public final static String DATA_OUT_LIIT = "K-030410";

    /**
     * 当前客户等级发生变化，请重新下载模板
     */
    public final static String LEVEL_CHANGED_HINT = "K-030411";

    /**
     * 请填写{0}
     */
    public final static String INPUT_HINT = "K-031401";

    /**
     * {0}仅限{1}
     */
    public final static String CONSTRAINT_HINT = "K-031402";




}
