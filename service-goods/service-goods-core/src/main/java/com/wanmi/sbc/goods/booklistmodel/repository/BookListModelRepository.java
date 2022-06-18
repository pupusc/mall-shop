package com.wanmi.sbc.goods.booklistmodel.repository;

import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/8/31 7:55 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Repository
public interface BookListModelRepository extends JpaRepository<BookListModelDTO, Integer>, JpaSpecificationExecutor<BookListModelDTO> {


    @Modifying
    @Query("update BookListModelDTO m set m.delFlag = 1 where m.id = ?1 and m.delFlag = 0 and m.version =?2 and m.publishState = 0")
    Integer deleteBookListModelByCustomer(Integer id, Integer version);


    @Query("from BookListModelDTO blm where blm.businessType = 2 and blm.publishState = 2 and blm.delFlag = 0  order by RAND()")
    List<BookListModelDTO> findPublishBook();


    /**
     * 采集数据
     * @return
     */
    @Query(value = "select * from t_book_list_model where update_time >=?1 and update_time < ?2 and business_type = ?3 and id > ?4 order by id asc limit ?5", nativeQuery = true)
    List<BookListModelDTO> collectBookListId(LocalDateTime beginTime, LocalDateTime endTime, List<Integer> businessTypes, Integer fromId, Integer pageSize);


    /**
     * 根据id采集数据
     * @return
     */
    @Query(value = "select * from t_book_list_model where del_flag = 0 and id in ?1", nativeQuery = true)
    List<BookListModelDTO> collectBookListByBookListIds(List<Integer> bookListIds);
}
