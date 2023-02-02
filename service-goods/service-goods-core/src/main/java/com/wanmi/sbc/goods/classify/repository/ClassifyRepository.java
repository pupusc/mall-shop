package com.wanmi.sbc.goods.classify.repository;


import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ClassifyRepository extends JpaRepository<ClassifyDTO, Integer>, JpaSpecificationExecutor<ClassifyDTO> {

    @Query(value = "select * from t_classify where del_flag=0 and has_show_index=1 order by index_order_num asc, update_time asc", nativeQuery = true)
    List<ClassifyDTO> findAllsort();


    /**
     * 采集数据
     * @return
     */
    @Query(value = "select * from t_classify where update_time >=?1 and update_time < ?2 and id > ?3 order by id asc limit ?4", nativeQuery = true)
    List<ClassifyDTO> collectClassifyByTime(LocalDateTime beginTime, LocalDateTime endTime, Integer fromId, Integer pageSize);

    /**
     * 采集数据 根据一级分类获取二级分类
     * @return
     */
    @Query(value = "select * from t_classify where parent_id in ?1", nativeQuery = true)
    List<ClassifyDTO> collectClassifyByPClassifyIds(List<Integer> classifyIds);

    /**
     * 采集数据 根据分类id获取店铺信息
     * @return
     */
    @Query(value = "select * from t_classify where del_flag = 0 and id in ?1", nativeQuery = true)
    List<ClassifyDTO> collectClassifyByIds(List<Integer> classifyIds);

}
