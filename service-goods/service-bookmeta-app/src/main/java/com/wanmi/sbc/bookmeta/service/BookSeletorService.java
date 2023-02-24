package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.BookSeletor;
import com.wanmi.sbc.bookmeta.mapper.BookSeletorMapper;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;

@Service
public class BookSeletorService {
    @Resource
    private BookSeletorMapper bookSeletorMapper;

    public List<BookSeletor> getByPage(BookSeletor bookSeletor,Integer pageInt,Integer pageSize ){
       return bookSeletorMapper.queryAllByLimit(bookSeletor, pageInt, pageSize);
    }

    public int add(BookSeletor bookSeletor){
        return bookSeletorMapper.insert(bookSeletor);
    }

    public int update(BookSeletor bookSeletor){
        return bookSeletorMapper.updateByPrimaryKeySelective(bookSeletor);
    }
}
