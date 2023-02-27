package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.BookSeletor;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BookSeletorMapper extends Mapper<BookSeletor> {
    List<BookSeletor> queryAllByLimit(@Param("bookSeletor")BookSeletor bookSeletor, @Param("limitIndex")Integer pageInt, @Param("limitSize")Integer pageSize);

    List<String> selectIsbnById(@Param("id")Integer id);
}
