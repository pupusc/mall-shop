package com.wanmi.sbc.goodsPool;

import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.goodsPool.service.impl.AdvertisementPoolServiceImpl;
import com.wanmi.sbc.goodsPool.service.impl.AssignPoolServiceImpl;
import com.wanmi.sbc.goodsPool.service.impl.GoodsPoolServiceImpl;
import com.wanmi.sbc.goodsPool.service.impl.VideoPoolServiceImpl;
import com.wanmi.sbc.setting.bean.enums.BookType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/3/3 16:28
 */
@Component
public class PoolFactory extends ApplicationObjectSupport {

    @Autowired
    private BeanFactoryHelper beanFactoryHelper;
    /**
     * 商品池类型实例化
     * @return
     */
    public PoolService getPoolService(Integer bookType) {
        if (bookType == null) {return null;}
        if (BookType.BOOK.toValue().equals(bookType)) {return beanFactoryHelper.getBean(GoodsPoolServiceImpl.class);}
        if (BookType.ADVERTISEMENT.toValue().equals(bookType)) {return beanFactoryHelper.getBean(AdvertisementPoolServiceImpl.class);}
        if (BookType.VIDEO.toValue().equals(bookType)) {return beanFactoryHelper.getBean(VideoPoolServiceImpl.class);}
        if (BookType.ASSIGN.toValue().equals(bookType)) {return beanFactoryHelper.getBean(AssignPoolServiceImpl.class);}
        throw new RuntimeException("不存在的商品池类型");
    }
}
