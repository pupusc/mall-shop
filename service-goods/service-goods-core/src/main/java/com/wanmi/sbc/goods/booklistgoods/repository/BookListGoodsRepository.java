package com.wanmi.sbc.goods.booklistgoods.repository;

import com.wanmi.sbc.goods.booklistgoods.model.root.BookListGoodsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/2 1:48 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Repository
public interface BookListGoodsRepository extends JpaRepository<BookListGoodsDTO, Integer>, JpaSpecificationExecutor<BookListGoodsDTO> {
}
