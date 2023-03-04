package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.GoodSearchKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/16:50
 * @Description:
 */
@Repository
public interface GoodsSearchKeyMapper {

    int insertGoodsSearchKey(GoodSearchKey goodSearchKey);

    int updateGoodsSearchKey(GoodSearchKey goodSearchKey);

    int deleteGoodsSearchKey(int id);
    List<GoodSearchKey> getAllGoodsSearchKey(@Param("spuId")String spuId, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    List<GoodSearchKey> getGoodsNameBySpuId(@Param("name") String name);

    Integer getAllGoodsSearchKeyCount(@Param("spuId") String spuId);
}
