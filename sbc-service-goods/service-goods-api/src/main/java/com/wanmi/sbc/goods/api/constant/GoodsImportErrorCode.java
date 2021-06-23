package com.wanmi.sbc.goods.api.constant;

/**
 * <p>商品导入异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午5:16.
 */
public final class GoodsImportErrorCode {

    private GoodsImportErrorCode() {
    }

    /**
     * 商品导出模板未配置
     */
    public final static String NOT_SETTING = "K-030401";

    /**
     * 仅限xlsx、xls格式文件
     */
    public final static String FILE_EXT_ERROR = "K-030402";

    /**
     * 文件大小仅限{0}M
     */
    public final static String FILE_MAX_SIZE = "K-030403";

    /**
     * 自定义错误内容
     */
    public final static String CUSTOM_ERROR = "K-030404";

    /**
     * 数据为空，请完善内容
     */
    public final static String EMPTY_ERROR = "K-030405";

    /**
     * 数据不正确，请使用模板文件填写
     */
    public final static String DATA_FILE_FAILD = "K-030406";

}
