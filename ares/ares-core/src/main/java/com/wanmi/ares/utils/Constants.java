
package com.wanmi.ares.utils;

/**
 * 常量类
 *
 * @author daiyitian
 * @version 0.0.1
 * @since 2017年4月11日 下午3:46:12
 */
public final class Constants {

    public static final String companyId = "0";

    public static final String bossId = "0";

    public static final String rootPath = "wanmi-ares/";

    /**
     * 商品模块生成类型
     */
    public static final class GoodsGenerateType {

        /**
         * 今日
         */
        public static final int TODAY = 0;

        /**
         * 今日
         */
        public static final int YESTERDAY = 1;

        /**
         * 近7日
         */
        public static final int LATEST7DAYS = 2;

        /**
         * 近30日
         */
        public static final int LATEST30DAYS = 3;

        /**
         * 按月
         */
        public static final int MONTH = 4;
    }


    private Constants() {
    }

}
