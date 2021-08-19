package com.wanmi.sbc.setting.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 图片数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long>, JpaSpecificationExecutor<Image> {

    /**
     * 根据多个ID编号进行删除
     *
     * @param imageIds 图片ID
     */
    @Modifying
    @Query("update Image w set w.delFlag = '1' ,w.updateTime = now() where w.imageId in ?1")
    void deleteByIds(List<Long> imageIds);

    /**
     * 根据多个ID编号更新分类
     *
     * @param cateId   分类ID
     * @param imageIds 图片ID
     */
    @Modifying
    @Query("update Image w set w.cateId = ?1 ,w.updateTime = now() where w.imageId in ?2")
    void updateCateByIds(Long cateId, List<Long> imageIds);

    /**
     * 根据多个分类编号更新分类
     *
     * @param cateId   分类ID
     * @param cateIds 分类ID
     */
    @Modifying
    @Query("update Image w set w.cateId = ?1 ,w.updateTime = now() where w.cateId in ?2")
    void updateCateByCateIds(Long cateId, List<Long> cateIds);

}
