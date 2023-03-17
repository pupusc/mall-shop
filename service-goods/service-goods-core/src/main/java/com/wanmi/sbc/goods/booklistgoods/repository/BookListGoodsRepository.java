package com.wanmi.sbc.goods.booklistgoods.repository;

import com.wanmi.sbc.goods.booklistgoods.model.root.BookListGoodsDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishV2DTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/2 1:48 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Repository
public interface BookListGoodsRepository extends JpaRepository<BookListGoodsDTO, Integer>, JpaSpecificationExecutor<BookListGoodsDTO> {
    @Query(value = "select a.*,b.goods_info_name as name from t_book_list_goods as a left join goods_info as b on a.sku_id=b.goods_info_id where book_list_id in ?1 and del_flag = 0", nativeQuery = true)
    List<Map> findByBookListId(Integer id);

    List<BookListGoodsDTO> findBySkuIdAndBookListId(String skuId, Integer bookListId);

    @Modifying
    @Query(value = "update t_book_list_goods set sale_num=?2 , rank_text=?3  where sku_id = ?1", nativeQuery = true)
    int updateBookListGoods(String skuNo, Integer saleNum, String rankText);
}
