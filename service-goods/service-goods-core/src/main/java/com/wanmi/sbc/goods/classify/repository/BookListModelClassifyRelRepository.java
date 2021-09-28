package com.wanmi.sbc.goods.classify.repository;

import com.wanmi.sbc.goods.classify.response.BookListModelClassifyLinkResponse;
import com.wanmi.sbc.goods.classify.model.root.BookListModelClassifyRelDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 7:42 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Repository
public interface BookListModelClassifyRelRepository extends JpaRepository<BookListModelClassifyRelDTO, Integer>, JpaSpecificationExecutor<BookListModelClassifyRelDTO> {



    /**
     * 书单关联发布表，获取结果信息
     * @param businessTypeList
     * @return
     */
    @Query("select DISTINCT new com.wanmi.sbc.goods.classify.response.BookListModelClassifyLinkResponse " +
            "(bookList.id as bookListModelId, bookList.name, bookList.desc, bookList.businessType, bookList.headImgUrl, " +
            "bookList.headImgHref, bookList.pageHref, bookList.publishState, bookList.version, bookList.createTime, bookList.updateTime, bookList.delFlag) " +
//            "classify.id as classifyId, classify.parentId as classifyParentId, classify.classifyName, classify.adImgUrl, classify.adImgHref) " +
            " from BookListModelClassifyRelDTO classifyRel, BookListModelDTO bookList, ClassifyDTO  classify , BookListGoodsPublishDTO publish" +
            " where classifyRel.bookListModelId = bookList.id and classifyRel.classifyId = classify.id and bookList.id = publish.bookListId" +
            " and classifyRel.delFlag = 0 and bookList.delFlag = 0 and classify.delFlag = 0 and publish.delFlag = 0 " +
            "and bookList.businessType in ?1 and classifyRel.classifyId in ?2 and publish.category in ?3 and bookList.id not in ?4 order by bookList.updateTime desc")
    Page<BookListModelClassifyLinkResponse> listBookListModelClassifyLink
            (Collection<Integer> businessTypeList, Collection<Integer> classifyIdColl, Collection<Integer> categoryIdColl, Collection<Integer> unShowBookListModelIdColl, Pageable pageable);
}
