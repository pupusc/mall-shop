
package com.wanmi.sbc.common.util;

/**
 * 常量类
 *
 * @author daiyitian
 * @version 0.0.1
 * @since 2017年4月11日 下午3:46:12
 */
public final class Constants {

    public final static Integer yes = 1;

    public final static Integer no = 0;

    // 验证码 有效期 5分钟
    public final static Long SMS_TIME = 5L;

    // 导出最大数量 1000
    public final static Integer EXPORT_MAX_SIZE = 1000;

    //分类在每个父类下上限20
    public final static Integer GOODSCATE_MAX_SIZE = 20;

    //品牌上限2000
    public final static Integer BRAND_MAX_SIZE = 2000;

    //采购单限购50
    public final static Integer PURCHASE_MAX_SIZE = 50;

    //商品收藏上限500
    public final static Integer FOLLOW_MAX_SIZE = 500;

    //商品导入文件大小上限2M
    public final static Integer IMPORT_GOODS_MAX_SIZE = 2;

    //商品导入错误文件的文件夹名
    public final static String ERR_EXCEL_DIR = "err_excel";

    //商品导入上传文件的文件夹名
    public final static String EXCEL_DIR = "excel";

    //店铺分类最多可添加2个层级
    public final static int STORE_CATE_GRADE = 2;

    //一级店铺分类最多20个
    public final static int STORE_CATE_FIRST_NUM = 20;

    //每个一级店铺分类最多20个二级分类
    public final static int STORE_CATE_SECOND_NUM = 20;

    //店铺图片分类最多可添加3个层级
    public final static int STORE_IMAGE_CATE_GRADE = 3;

    //每个层级店铺分类最多20个
    public final static int STORE_IMAGE_CATE_NUM = 20;

    //平台最多配置100个物流公司
    public final static int EXPRESS_COMPANY_COUNT = 100;

    //每个店铺最多使用20个物流公司
    public final static int STORE_EXPRESS_COUNT = 20;

    // 分类path的分隔符
    public final static String CATE_PATH_SPLITTER = "\\|";

    public final static String STRING_SLASH_SPLITTER = "/";

    //店铺关注上限200
    public final static Integer STORE_FOLLOW_MAX_SIZE = 200;

    /** 商品分类叶子分类层级(最多三层)*/
    public final static int GOODS_LEAF_CATE_GRADE = 3;

    /**
     * 默认收款账号
     */
    public final static Long DEFAULT_RECEIVABLE_ACCOUNT = -1L;


    /**
     * 营销满金额时最小金额
     */
    public final static double MARKETING_FULLAMOUNT_MIN = 0.01;

    /**
     * 营销满金额时最大金额
     */
    public final static double MARKETING_FULLAMOUNT_MAX = 99999999.99;

    /**
     * 营销满数量时最小数量
     */
    public final static Long MARKETING_FULLCOUNT_MIN = 1L;

    /**
     * 营销满数量时最大数量
     */
    public final static Long MARKETING_FULLCOUNT_MAX = 9999L;


    /**
     * 营销满数量时最小数量
     */
    public final static double MARKETING_DISCOUNT_MIN = 0.01;

    /**
     * 营销满数量时最大数量
     */
    public final static double MARKETING_DISCOUNT_MAX = 0.99;

    /**
     * 满赠赠品最大种数
     */
    public final static int MARKETING_Gift_TYPE_MAX = 20;

    /**
     * 满赠赠品最小数量
     */
    public final static int MARKETING_Gift_MIN = 1;

    /**
     * 满赠赠品最大数量
     */
    public final static int MARKETING_Gift_MAX = 999;

    /**
     * 商品类目默认属性值
     */
    public static final Long GOODS_DEFAULT_REL = Long.valueOf(0);
    /**
     * 商品类目关联属性最大数量
     */
    public static final int GOODS_PROP_REAL_SIZE = 20;
    /**
     * 属性值最大数量
     */
    public static final int GOODS_PROP_DETAIL_REAL_SIZE = 100;

    /**
     * 用户的全平台ID，非店铺客户的默认等级，用于级别价计算
     */
    public static final long GOODS_PLATFORM_LEVEL_ID = 0;

    /**
     * 单品运费模板最大数量
     */
    public static final int FREIGHT_GOODS_MAX_SIZE = 20;

    public static final int ONE = 1;

    /***
     * 采购单distributod_id默认值0
     */
    public  static final String PURCHASE_DEFAULT = "0";


    /**
     * 积分价值
     */
    public static final Long POINTS_WORTH = 100L;

    /**
     * 秒杀活动进行时间
     */
    public static final Long FLASH_SALE_LAST_HOUR = 2L;

    /**
     * 秒杀活动商品抢购资格有效期：5分钟
     */
    public static final Long FLASH_SALE_GOODS_QUALIFICATIONS_VALIDITY_PERIOD = 5L;

    /**
     * 秒杀活动抢购商品订单类型："FLASH_SALE"
     */
    public static final String FLASH_SALE_GOODS_ORDER_TYPE = "FLASH_SALE";

    /**
     * 预约抢购商品订单类型："APPOINTMENT_SALE"
     */
    public static final String APPOINTMENT_SALE_GOODS_ORDER_TYPE = "APPOINTMENT_SALE";

    /**
     * 预售商品订单类型："BOOKING_SALE"
     */
    public static final String BOOKING_SALE_TYPE = "BOOKING_SALE";

    /**
     * 活动商品抢购资格有效期：5分钟
     */
    public static final Long APPOINTMENT_SALE_GOODS_QUALIFICATIONS_VALIDITY_PERIOD = 5L;


    //S2B平台最多配置100个物流公司
    public final static int S2B_EXPRESS_COMPANY_COUNT = 100;


    /**
     * boss默认店铺id
     */
    public static final Long BOSS_DEFAULT_STORE_ID = -1L;

    /**
     * 小程序
     */
    public static final Long BOSS_MINIAPP_STORE_ID = -2L;


    /**
     * 客户导入
     */
    public final static String CUSTOMER_IMPORT_EXCEL_DIR = "customer_import_excel";

    public final static String CUSTOMER_IMPORT_EXCEL_ERR_DIR = "customer_import_err_excel";

    //商品/商品库导入
    public final static String GOODS_EXCEL_DIR = "goods_excel";
    //商品/商品库导入生成的错误文件
    public final static String GOODS_ERR_EXCEL_DIR = "goods_err_excel";

    //部门导入
    public final static String DEPARTMENT_EXCEL_DIR = "department_excel";
    //部门导入生成的错误文件
    public final static String DEPARTMENT_ERR_EXCEL_DIR = "department_err_excel";
    //员工导入
    public final static String EMPLOYEE_EXCEL_DIR = "employee_excel";
    //员工导入生成的错误文件
    public final static String EMPLOYEE_ERR_EXCEL_DIR = "employee_err_excel";

    //商品类目导入
    public final static String GOODS_CATE_EXCEL_DIR = "goods_cate_excel";
    //商品类目导入生成的错误文件
    public final static String GOODS_CATE_ERR_EXCEL_DIR = "goods_cate_err_excel";

    //积分商品导入
    public final static String POINTS_GOODS_EXCEL_DIR = "points_goods_excel";
    //积分商品导入生成的错误文件
    public final static String POINTS_GOODS_ERR_EXCEL_DIR = "points_goods_err_excel";

    //批量发货模板导入
    public final static String BATCH_DELIVER_EXCEL_DIR = "batch_deliver_excel_dir";
    //批量发货模板导入生成的错误文件
    public final static String BATCH_DELIVER_ERR_EXCEL_DIR = "batch_deliver_err_excel_dir";


    //调价导入文件大小上限5M
    public final static Integer IMPORT_PRICE_ADJUST_MAX_SIZE = 5;

    /**
     * 商品设价方式--市场价
     */
    public static final Integer PRICETYPE = 2;

    private Constants() {
    }

}
