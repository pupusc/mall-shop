package com.wanmi.sbc.customer.api.constant;

/**
 * <p>商品分类异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午4:35.
 */
public final class DepartmentErrorCode {
    private DepartmentErrorCode() {
    }
    /**
     * 相关父类不存在
     */
    public final static String PARENT_CATE_NOT_EXIST = "K-400101";

    /**
     * 同一级别的部门名称不允许重复
     */
    public final static String NAME_ALREADY_EXIST = "K-400102";

    /**
     * 部门不存在
     */
    public final static String NOT_EXIST = "K-400103";

    /**
     * 数据不正确，请使用模板文件填写
     */
    public final static String DATA_FILE_FAILD = "K-400104";

    /**
     * 模板不存在
     */
    public final static String TEMPLATE_NOT_EXIST = "K-400105";

    /**
     * 该部门下存在员工、不可删除
     */
    public final static String EXIST_EMPLOYEE_NUM = "K-400106";
}
