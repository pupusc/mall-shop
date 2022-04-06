package com.wanmi.sbc.goods.common;

import com.wanmi.sbc.goods.bean.dto.GoodsPackDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品打包
 */
@Repository
public interface GoodsPackDetailRepository extends JpaRepository<GoodsPackDetailDTO, Integer>{
    @Modifying
    @Query(value = "delete from goods_pack_detail where pack_id = ?1", nativeQuery = true)
    int removeAllByPackId(String packId);

    @Query(value = "select * from goods_pack_detail where del_flag = 0 and pack_id in (?1)", nativeQuery = true)
    List<GoodsPackDetailDTO> listByPackIds(List<String> packIds);
}