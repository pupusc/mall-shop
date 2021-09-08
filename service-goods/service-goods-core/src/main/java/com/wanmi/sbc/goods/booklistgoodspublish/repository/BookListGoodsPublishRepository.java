package com.wanmi.sbc.goods.booklistgoodspublish.repository;


import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse;
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
    @Query("select new com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse " +
            "(bookList.id as bookListModelId, bookList.name, bookList.desc, bookList.businessType, bookList.headImgUrl, " +
            "bookList.headImgHref, bookList.pageHref, bookList.publishState, bookList.version, bookList.createTime, bookList.updateTime, bookList.delFlag, " +
            "goods.chooseRuleId, goods.spuId, goods.spuNo, goods.skuId, goods.skuNo, goods.orderNum) " +
            " from BookListGoodsPublishDTO goods, BookListModelDTO bookList where goods.bookListId = bookList.id " +
            "and bookList.businessType in ?1 and goods.category = ?2 and goods.spuId = ?3 order by bookList.updateTime desc")
    List<BookListGoodsPublishLinkModelResponse> listGoodsPublishLinkModel(List<Integer> businessTypeList, Integer category, String spuId);




    /**
     * 分类关联发布表，获取书单结果信息
     * @param spuId
     * @return
     */
    @Query("select new com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse " +
            "(bookList.id as bookListModelId, bookList.name, bookList.desc, bookList.businessType, bookList.headImgUrl, " +
            "bookList.headImgHref, bookList.pageHref, bookList.publishState, bookList.version, bookList.createTime, bookList.updateTime, bookList.delFlag, " +
            "goods.chooseRuleId, goods.spuId, goods.spuNo, goods.skuId, goods.skuNo, goods.orderNum) " +
            " from BookListGoodsPublishDTO goods, ClassifyDTO classify, BookListModelClassifyRelDTO clasifyRel, BookListModelDTO bookList " +
            " where goods.bookListId = classify.id and classify.id = clasifyRel.classifyId and clasifyRel.bookListModelId = bookList.id" +
            " and bookList.businessType in ?1 and bookList.id not in ?2 and goods.category = ?3 and goods.spuId = ?4 order by bookList.updateTime desc")
    List<BookListGoodsPublishLinkModelResponse> listGoodsPublishLinkClassify(List<Integer> businessTypeList, List<Integer> notInBookListIdList, Integer category, String spuId);
}
