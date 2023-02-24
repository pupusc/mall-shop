package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.BookSeletorBo;
import com.wanmi.sbc.bookmeta.bo.MetaAwardBO;
import com.wanmi.sbc.bookmeta.entity.BookSeletor;
import com.wanmi.sbc.bookmeta.provider.BookSeletorProvider;
import com.wanmi.sbc.bookmeta.service.BookSeletorService;
import com.wanmi.sbc.common.util.KsBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
public class BookSeletorProviderImpl implements BookSeletorProvider {

    @Autowired
    private BookSeletorService bookSeletorService;

    @Override
    public List<BookSeletorBo> queryById(BookSeletorBo bookSeletorBo) {
        List<BookSeletor> byPage = bookSeletorService.getByPage(DO2BOUtils.objA2objB(bookSeletorBo, BookSeletor.class), bookSeletorBo.getPageIndex(), bookSeletorBo.getPageSize());
        return DO2BOUtils.objA2objB4List(byPage,BookSeletorBo.class);
    }

    @Override
    public int add(BookSeletorBo bookSeletorBo) {
        return bookSeletorService.add(DO2BOUtils.objA2objB(bookSeletorBo,BookSeletor.class));
    }

    @Override
    public int update(BookSeletorBo bookSeletorBo) {
        return bookSeletorService.update(DO2BOUtils.objA2objB(bookSeletorBo,BookSeletor.class));
    }
}
