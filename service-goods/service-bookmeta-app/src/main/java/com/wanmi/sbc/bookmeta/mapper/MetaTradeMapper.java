package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaTrade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/16:29
 * @Description:
 */

@Repository
public interface MetaTradeMapper extends Mapper<MetaTrade> {
    List<MetaTrade> getMetaTradeTree(int parentId);

    List<MetaTrade> getAllMetaTradeNode();

    int insertMetaTrade(MetaTrade metaTrade);

    int updateMetaTrade(MetaTrade metaTrade);

    int deleteMetaTrade(int id);

    int isExist(String name);
}


