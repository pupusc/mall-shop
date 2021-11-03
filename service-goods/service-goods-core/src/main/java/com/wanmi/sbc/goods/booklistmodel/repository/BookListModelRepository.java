package com.wanmi.sbc.goods.booklistmodel.repository;

import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}