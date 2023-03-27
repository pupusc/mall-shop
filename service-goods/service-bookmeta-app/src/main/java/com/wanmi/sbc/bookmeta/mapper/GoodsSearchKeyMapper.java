package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.GoodSearchKey;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    List<GoodSearchKey> getAllGoodsSearchKey(@Param("name")String name,@Param("spuId") String spuId, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    List<GoodSearchKey> downLoadQuery();

    List<GoodSearchKey> getGoodsNameBySpuId(@Param("name") String name);

    Integer getAllGoodsSearchKeyCount(@Param("name") String name,@Param("spuId") String spuId);

    List<GoodsVO> getGoodsList(@Param("name")String name, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    Integer getAllGoodsCount(@Param("name")String name);

    int isExistGoodsSearchKey(@Param("name") String name,@Param("spuId") String spuId);

    int isExistGoodsSearchKeyById(@Param("id") Integer id);

    List<Map<String,Object>> getSpuAndSkuByName(@Param("name")String name);

}
