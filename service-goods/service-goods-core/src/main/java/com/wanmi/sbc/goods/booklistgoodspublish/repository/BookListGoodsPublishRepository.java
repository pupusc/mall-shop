package com.wanmi.sbc.goods.booklistgoodspublish.repository;


import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodPublishLinkModelResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookListGoodsPublishRepository extends JpaRepository<BookListGoodsPublishDTO, Integer>, JpaSpecificationExecutor<BookListGoodsPublishDTO> {


    /**
     * 书单关联发布表，获取结果信息
     * @param businessTypeList
     * @param category
     * @param spuId
     * @return
     */
    @Query("select new com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodPublishLinkModelResponse " +
            "(bookList.id as bookListModelId, bookList.name, bookList.desc, bookList.businessType, bookList.headImgUrl, " +
            "bookList.headImgHref, bookList.pageHref, bookList.publishState, bookList.version, bookList.createTime, bookList.updateTime, bookList.delFlag, " +
            "goods.chooseRuleId, goods.spuId, goods.spuNo, goods.skuId, goods.skuNo, goods.orderNum) " +
            " from BookListGoodsPublishDTO goods, BookListModelDTO bookList where goods.bookListId = bookList.id " +
            "and bookList.businessType in ?1 and goods.category = ?2 and goods.spuId = ?3")
    List<BookListGoodPublishLinkModelResponse> listGoodsPublishLinkModel(List<Integer> businessTypeList, Integer category, String spuId);
}
