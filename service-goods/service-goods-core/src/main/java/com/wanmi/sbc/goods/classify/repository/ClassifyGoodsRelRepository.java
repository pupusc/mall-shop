package com.wanmi.sbc.goods.classify.repository;


import com.wanmi.sbc.goods.classify.model.root.ClassifyGoodsRelDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassifyGoodsRelRepository extends JpaRepository<ClassifyGoodsRelDTO, Integer>, JpaSpecificationExecutor<ClassifyGoodsRelDTO> {

    /**
     * 根据商品ID批量删除
     * @param goodsId 商品ID
     * @return
     */
    @Modifying
    @Query("delete from ClassifyGoodsRelDTO where goodsId = ?1")
    void deleteByGoodsId(String goodsId);
}
