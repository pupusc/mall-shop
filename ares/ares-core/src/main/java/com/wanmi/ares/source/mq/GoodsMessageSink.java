package com.wanmi.ares.source.mq;

import com.wanmi.ares.constants.MQConstant;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author: songhanlin
 * @Date: Created In 10:45 2019-04-04
 * @Description: 商品消息接收处理 Sink
 */
public interface GoodsMessageSink {

    @Input(MQConstant.Q_ARES_GOODS_SKU_CREATE)
    SubscribableChannel createSku();

    @Input(MQConstant.Q_ARES_GOODS_SKU_MODIFY)
    SubscribableChannel modifySku();

    @Input(MQConstant.Q_ARES_GOODS_SKU_DELETE)
    SubscribableChannel deleteSku();

    @Input(MQConstant.Q_ARES_GOODS_SPU_DELETE)
    SubscribableChannel deleteSpu();

    @Input(MQConstant.Q_ARES_GOODS_SKU_UP)
    SubscribableChannel addedSku();

    @Input(MQConstant.Q_ARES_GOODS_CATE_CREATE)
    SubscribableChannel createCate();

    @Input(MQConstant.Q_ARES_GOODS_CATE_MODIFY)
    SubscribableChannel modifyCate();

    @Input(MQConstant.Q_ARES_GOODS_CATE_DELETE)
    SubscribableChannel deleteCate() ;

    /**
     * 创建店铺分类
     */
    @Input(MQConstant.Q_ARES_STORE_CATE_CREATE)
    SubscribableChannel createStoreCate();

    /**
     * 修改店铺分类
     */
    @Input(MQConstant.Q_ARES_STORE_CATE_MODIFY)
    SubscribableChannel modifyStoreCate();

    /**
     * 删除店铺分类
     */
    @Input(MQConstant.Q_ARES_STORE_CATE_DELETE)
    SubscribableChannel deleteStoreCate();

    @Input(MQConstant.Q_ARES_GOODS_BRAND_CREATE)
    SubscribableChannel createBrand();

    @Input(MQConstant.Q_ARES_GOODS_BRAND_MODIFY)
    SubscribableChannel modifyBrand();

    @Input(MQConstant.Q_ARES_GOODS_BRAND_DELETE)
    SubscribableChannel deleteBrand();

}
