package com.wanmi.ares.constants;

/**
 * <p>MQ常量池</p>
 * Created by of628-wenzhi on 2017-10-10-下午3:10.
 */
public class MQConstant {
    /*
     * destination name
     *
     *  命名规范：
            a. 格式：消息类型.持有消息的服务简称.业务命名
            b. 消息类型的取值：q:表示queue；t:表示topic
            c. 系统简称采用英文全小写,此处统一为ares
            d. 模块简称采用英文全小写
            e. 业务命名：业务自己根据消息用途来进行命名，使用意义完整的英文描述，采用驼峰命名法
        命名示例：
            q.ares.customer.delete
            q.ares.pvuv.push
            q.ares.sku.create
     */
    //导出任务要求队列(ExportDataRequest)
    public static final String Q_ARES_EXPORT_DATA_REQUEST = "q.ares.export.data.request";

    //商品分类删除(List<Long> ids)(消费时还需要将本来是该分类的商品置为默认分类)
    public static final String Q_ARES_GOODS_CATE_DELETE = "q.ares.goods.cate.delete";
    //商品分类新增(GoodsCateRequest)
    public static final String Q_ARES_GOODS_CATE_CREATE = "q.ares.goods.cate.create";
    //商品分类修改(GoodsCateRequest)(消费时还需要更新所有子分类的父级分类名称)
    public static final String Q_ARES_GOODS_CATE_MODIFY = "q.ares.goods.cate.modify";

    //商品品牌单个删除(Long id)(消费时还需要将本来是该品牌的商品品牌置为空)
    public static final String Q_ARES_GOODS_BRAND_DELETE = "q.ares.goods.brand.delete";
    //商品品牌新增(CustomerLevelRequest)
    public static final String Q_ARES_GOODS_BRAND_CREATE = "q.ares.goods.brand.create";
    //商品品牌修改(CustomerLevelRequest)
    public static final String Q_ARES_GOODS_BRAND_MODIFY = "q.ares.goods.brand.modify";

    //商品spu批量删除(List<String> spuIds)
    public static final String Q_ARES_GOODS_SPU_DELETE = "q.ares.goods.spu.delete";
    //商品sku批量删除(List<String> skuIds)
    public static final String Q_ARES_GOODS_SKU_DELETE = "q.ares.goods.sku.delete";
    //商品新增(GoodsInfoRequest)
    public static final String Q_ARES_GOODS_SKU_CREATE = "q.ares.goods.sku.create";
    //商品修改(GoodsInfoRequest)
    public static final String Q_ARES_GOODS_SKU_MODIFY = "q.ares.goods.sku.modify";
    //批量修改商品上下架(List<GoodsInfoRequest>-只有id,addedFlag,addedTime)
    public static final String Q_ARES_GOODS_SKU_UP = "q.ares.goods.sku.up";

    //业务员批量删除(List<String> ids)(消费时还需要将本来是该业务员下属的会员的业务员id置为system的员工id)
    public static final String Q_ARES_EMPLOYEE_DELETE = "q.ares.employee.delete";
    //业务员新增(EmployeeRequest)
    public static final String Q_ARES_EMPLOYEE_CREATE = "q.ares.employee.create";
    //业务员修改(EmployeeRequest)(如果修改前是业务员,修改后不是业务员,需要清理该业务员下属的会员(参考删除))
    public static final String Q_ARES_EMPLOYEE_MODIFY = "q.ares.employee.modify";

    //会员批量删除(List<String> ids)
    public static final String Q_ARES_CUSTOMER_DELETE = "q.ares.customer.delete";
    //会员新增,会员注册(CustomerAddRequest)
    public static final String Q_ARES_CUSTOMER_CREATE = "q.ares.customer.create";
    //会员修改,注册完善信息(CustomerBaseRequest)
    public static final String Q_ARES_CUSTOMER_MODIFY = "q.ares.customer.modify";
    //修改会员审核状态(CustomerCheckRequest-除创建时间)
    public static final String Q_ARES_CUSTOMER_CHECK = "q.ares.customer.check";

    //会员等级单个删除(Long id)(消费时还需要将本来是该等级的会员置为默认等级)
    public static final String Q_ARES_CUSTOMER_LEVEL_DELETE = "q.ares.customer.level.delete";
    //会员等级新增(CustomerLevelRequest)
    public static final String Q_ARES_CUSTOMER_LEVEL_CREATE = "q.ares.customer.level.create";
    //会员等级修改(CustomerLevelRequest)
    public static final String Q_ARES_CUSTOMER_LEVEL_MODIFY = "q.ares.customer.level.modify";

    //店铺会员单个删除(String id)
    public static final String Q_ARES_STORE_CUSTOMER_DELETE = "q.ares.store.customer.delete";
    //店铺会员新增(CustomerAndLevelRequest)
    public static final String Q_ARES_STORE_CUSTOMER_CREATE = "q.ares.store.customer.create";
    //店铺会员修改(CustomerAndLevelRequest)
    public static final String Q_ARES_STORE_CUSTOMER_MODIFY = "q.ares.store.customer.modify";

    //店铺商品分类删除(List<String> ids)(消费时还需要在适当的时候将本来是该分类的商品置为默认分类)
    public static final String Q_ARES_STORE_CATE_DELETE = "q.ares.store.cate.delete";
    //店铺商品分类新增(StoreCateRequest)
    public static final String Q_ARES_STORE_CATE_CREATE = "q.ares.store.cate.create";
    //店铺商品分类修改(StoreCateRequest)
    public static final String Q_ARES_STORE_CATE_MODIFY = "q.ares.store.cate.modify";

    //店铺入驻(StoreRequest)
    public static final String Q_ARES_STORE_CREATE = "q.ares.store.create";
    //店铺信息修改(StoreRequest)
    public static final String Q_ARES_STORE_MODIFY = "q.ares.store.modify";

    //流量同步
    public static final String Q_FLOW_CUSTOMER_SYNCHRONIZATION = "q.flow.customer.synchronization";

    //订单创建(OrderDataRequest)
    public static final String Q_ARES_ORDER_CREATE = "q.ares.order.create";
    //多笔订单创建(List<OrderDataRequest>)
    public static final String Q_ARES_ORDER_LIST_CREATE = "q.ares.order.list.create";
    //订单付款(OrderDataRequest)
    public static final String Q_ARES_ORDER_PAY = "q.ares.order.pay";
    //订单退货退款(OrderDataRequest)
    public static final String Q_ARES_ORDER_RETURN = "q.ares.order.return";

}
