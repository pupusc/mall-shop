package com.wanmi.sbc.goods.tag.repository;

import com.wanmi.sbc.goods.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    @Query(value = "select distinct t.* from t_tag as t join t_tag_rel as tr on tr.tag_id=t.id where tr.goods_id=:goodsId", nativeQuery = true)
    List<Tag> findByGoods(String goodsId);

    @Modifying
    @Query(value = "delete from t_tag_rel where goods_id=:goodsId", nativeQuery = true)
    void deleteTagsForGoods(String goodsId);

    List<Tag> findAllByTagName(String tagName);

    @Query("from Tag w where  w.id in ?1")
    List<Tag> findByIds(List<Long> ids);
}
