package com.wanmi.sbc.goodsPool;

import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.goodsPool.service.impl.AdvertisementPoolServiceImpl;
import com.wanmi.sbc.goodsPool.service.impl.AssignPoolServiceImpl;
import com.wanmi.sbc.goodsPool.service.impl.GoodsPoolServiceImpl;
import com.wanmi.sbc.goodsPool.service.impl.VideoPoolServiceImpl;
import com.wanmi.sbc.setting.bean.dto.GoodsPoolDto;
import com.wanmi.sbc.setting.bean.dto.MixedComponentTabDto;
import com.wanmi.sbc.setting.bean.enums.BookType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/3/3 16:28
 */
@Component
public class PoolFactory {

    @Autowired
    private GoodsPoolServiceImpl goodsPoolService;

    @Autowired
    private AdvertisementPoolServiceImpl advertisementPoolService;

    @Autowired
    private VideoPoolServiceImpl videoPoolService;

    @Autowired
    private AssignPoolServiceImpl assignPoolService;

    /**
     * 商品池类型实例化
     * @return
     */
    public PoolService getPoolService(Integer bookType) {
        if (bookType == null) {return null;}
        if (BookType.BOOK.toValue().equals(bookType)) {return goodsPoolService;}
        if (BookType.ADVERTISEMENT.toValue().equals(bookType)) {return advertisementPoolService;}
        if (BookType.VIDEO.toValue().equals(bookType)) {return videoPoolService;}
        if (BookType.ASSIGN.toValue().equals(bookType)) {return assignPoolService;}
        throw new RuntimeException("不存在的商品池类型");
    }
}
