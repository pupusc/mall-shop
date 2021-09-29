//package com.wanmi.sbc.goods.booklistmodelclick.service;
//
//import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
//import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
//import com.wanmi.sbc.goods.booklistmodel.request.BookListModelPageRequest;
//import com.wanmi.sbc.goods.booklistmodelclick.model.root.BookListModelClickDTO;
//import com.wanmi.sbc.goods.booklistmodelclick.repository.BookListModelClickRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.Resource;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//
///**
// * Description:
// * Company    : 上海黄豆网络科技有限公司
// * Author     : duanlongshan@dushu365.com
// * Date       : 2021/9/7 8:52 下午
// * Modify     : 修改日期          修改人员        修改说明          JIRA编号
// ********************************************************************/
//@Service
//@Slf4j
//public class BookListModelClickService {
//
//
//    @Resource
//    private BookListModelClickRepository bookListModelClickRepository;
//
//    /**
//     * 新增点击数量
//     * @param bookListModelId
//     * @param clickCount
//     */
//    public void add(Integer bookListModelId, Integer clickCount){
//        log.info("### BookListModelClickService.add bookListModelId:{} clickCount:{}", bookListModelId, clickCount);
//        List<BookListModelClickDTO> bookListModelClickList =
//                bookListModelClickRepository.findAll(this.packageWhere(null, bookListModelId));
//        BookListModelClickDTO bookListModelClickDTO = new BookListModelClickDTO();
//        if (CollectionUtils.isEmpty(bookListModelClickList)) {
//            bookListModelClickDTO.setBookListModelId(bookListModelId);
//            bookListModelClickDTO.setClickCount(clickCount);
//            bookListModelClickDTO.setVersion(0);
//            bookListModelClickDTO.setCreateTime(new Date());
//            bookListModelClickDTO.setUpdateTime(new Date());
//            bookListModelClickDTO.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
//        } else {
//            if (bookListModelClickList.size() > 1) {
//                log.error("--->> BookListModelClickService.add bookListModelId:{} has more one data", bookListModelId);
//            }
//            bookListModelClickDTO = bookListModelClickList.get(0);
//            bookListModelClickDTO.setClickCount(bookListModelClickDTO.getClickCount() + clickCount);
//        }
//        bookListModelClickRepository.save(bookListModelClickDTO);
//    }
//
//
//    /**
//     * 获取书单点击列表
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    public Page<BookListModelClickDTO> listPage(Collection<Integer> bookListModelIdList, int pageNum, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, "clickCount");
//        return bookListModelClickRepository.findAll(this.packageWhere(bookListModelIdList, null), pageable);
//
//    }
//
//
//
//    /**
//     * condition
//     * @param
//     */
//    private Specification<BookListModelClickDTO> packageWhere(Collection<Integer> bookListModelIdList, Integer bookListModelId) {
//        return new Specification<BookListModelClickDTO>() {
//            @Override
//            public Predicate toPredicate(Root<BookListModelClickDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                final List<Predicate> conditionList = new ArrayList<>();
//
//                //只是获取有效的
//                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
//                if (bookListModelId != null) {
//                    conditionList.add(criteriaBuilder.equal(root.get("bookListModelId"), bookListModelId));
//                }
//                if (!CollectionUtils.isEmpty(bookListModelIdList)) {
//                    conditionList.add(root.get("bookListModelId").in(bookListModelIdList));
//                }
//                return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
//            }
//        };
//    }
//}
