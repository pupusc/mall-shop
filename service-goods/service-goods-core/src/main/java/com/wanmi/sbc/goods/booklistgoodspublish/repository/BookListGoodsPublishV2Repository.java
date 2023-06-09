package com.wanmi.sbc.goods.booklistgoodspublish.repository;


import com.wanmi.sbc.goods.api.response.booklistmodel.RankGoodsPublishResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishV2DTO;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface BookListGoodsPublishV2Repository extends JpaRepository<BookListGoodsPublishV2DTO, Integer>, JpaSpecificationExecutor<BookListGoodsPublishV2DTO> {


    /**
     * 书单关联发布表，获取结果信息
     * @param businessTypeList
     * @param category
     * @return
     */
    @Query("select new com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse " +
            "(bookList.id as bookListModelId, bookList.name, bookList.famousName, bookList.desc, bookList.businessType, bookList.headImgUrl, bookList.headSquareImgUrl, " +
            "bookList.headImgHref, bookList.pageHref, bookList.hasTop, bookList.publishState, bookList.version, bookList.createTime, bookList.updateTime, bookList.delFlag, " +
            "publish.chooseRuleId, publish.spuId, publish.spuNo, publish.skuId, publish.skuNo, publish.erpGoodsNo, publish.erpGoodsInfoNo, publish.orderNum) " +
            " from BookListGoodsPublishDTO publish, BookListModelDTO bookList where publish.bookListId = bookList.id " +
            " and publish.delFlag = 0 and bookList.delFlag = 0 and bookList.businessType in ?1 and publish.category = ?2 and publish.spuId in ?3 order by bookList.hasTop desc, bookList.updateTime desc")
    List<BookListGoodsPublishLinkModelResponse> listGoodsPublishLinkModel(List<Integer> businessTypeList, Integer category, Collection<String> spuIdColl);




    /**
     * 分类关联发布表，获取书单结果信息
     * @param spuId
     * @return
     */
    @Query("select new com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse " +
            "(bookList.id as bookListModelId, bookList.name, bookList.famousName, bookList.desc, bookList.businessType, bookList.headImgUrl, bookList.headSquareImgUrl, " +
            "bookList.headImgHref, bookList.pageHref, bookList.hasTop, bookList.publishState, bookList.version, bookList.createTime, bookList.updateTime, bookList.delFlag, " +
            "publish.chooseRuleId, publish.spuId, publish.spuNo, publish.skuId, publish.skuNo, publish.erpGoodsNo, publish.erpGoodsInfoNo, publish.orderNum) " +
            " from BookListGoodsPublishDTO publish, ClassifyDTO classify, BookListModelClassifyRelDTO clasifyRel, BookListModelDTO bookList " +
            " where publish.bookListId = bookList.id and classify.id = clasifyRel.classifyId and clasifyRel.bookListModelId = bookList.id" +
            " and publish.delFlag = 0 and classify.delFlag = 0 and clasifyRel.delFlag = 0 and bookList.delFlag = 0" +
            " and bookList.businessType in ?1 and bookList.id not in ?2 and publish.category = ?3 and classify.id in ?4 order by bookList.hasTop desc, bookList.updateTime desc")
    List<BookListGoodsPublishLinkModelResponse> listGoodsPublishLinkClassify(List<Integer> businessTypeList, List<Integer> notInBookListIdList, Integer category,Collection<Integer> classifyIdColl);


    /**
     * 采集数据
     * @return
     */
    @Query(value = "select * from t_book_list_goods_publish where update_time >=?1 and update_time < ?2 and category = ?3 and id > ?4 order by id asc limit ?5", nativeQuery = true)
    List<BookListGoodsPublishDTO> collectBookListGoodsPublishId(LocalDateTime beginTime, LocalDateTime endTime, Integer category, Integer fromId,Integer pageSize);


    /**
     * 根据id采集数据
     * @return
     */
    @Query(value = "select * from t_book_list_goods_publish where book_list_id in ?1 and del_flag = 0", nativeQuery = true)
    List<BookListGoodsPublishDTO> collectBookListGoodsPublishIdByBookListIds(List<Integer> bookListIds);


    /**
     * 根据商品id采集数据
     * @return
     */
    @Query(value = "select * from t_book_list_goods_publish where spu_id in ?1", nativeQuery = true)
    List<BookListGoodsPublishDTO> collectBookListGoodsPublishIdBySpuIds(List<String> spuIds);

    @Query(value = "select distinct * from t_book_list_goods_publish where del_flag=0 and book_list_id in ?1 group by spu_id order by order_num asc",nativeQuery = true)
    List<BookListGoodsPublishV2DTO> collectDistinctByDelFlagAndBookListIdIn(List<Integer> ids);


    @Query(value = "select a.*,b.goods_info_name as name from t_book_list_goods_publish as a left join goods_info as b on a.sku_id=b.goods_info_id where book_list_id in ?1 and del_flag = 0", nativeQuery = true)
    List<Map> findByBookListId(Integer id);

    List<BookListGoodsPublishV2DTO> findBySkuIdAndBookListId(String skuId,Integer bookListId);

    @Modifying
    @Query(value = "update t_book_list_goods_publish set sale_num=?2 , rank_text=?3 ,update_time=?4  where sku_id = ?1", nativeQuery = true)
    int updateBookListGoodsPublish(String skuId, Integer saleNum, String rankText,LocalDateTime updateTime);
}


