package com.wanmi.sbc.goodsPool;

import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.goodsPool.service.impl.*;
import com.wanmi.sbc.setting.bean.dto.GoodsPoolDto;
import com.wanmi.sbc.setting.bean.dto.MixedComponentTabDto;
import com.wanmi.sbc.setting.bean.enums.BookType;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

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
        if (BookType.SKIP.toValue().equals(bookType)) {return beanFactoryHelper.getBean(SkipAdvertPoolServiceImpl.class);}
        throw new RuntimeException("不存在的商品池类型");
    }
}
