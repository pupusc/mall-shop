package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.request.booklistgoodspublish.CollectBookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.collect.CollectBookListGoodsPublishResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.repository.BookListGoodsPublishRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/2 11:36 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectBookListModelService {


    @Autowired
    private BookListGoodsPublishRepository bookListGoodsPublishRepository;


    /**
     * 采集数据，独立于正常的搜索
     * @return
     */
    public List<CollectBookListGoodsPublishResponse> collectBookListGoodsPublish(CollectBookListModelProviderRequest request) {
        List<BookListGoodsPublishDTO> bookListGoodsPublishDTOList = bookListGoodsPublishRepository.
                collectListGoodsPublish(request.getBeginTime(), request.getEndTime(), CategoryEnum.BOOK_LIST_MODEL.getCode(), request.getPageSize());
        List<CollectBookListGoodsPublishResponse> result = new ArrayList<>();
        for (BookListGoodsPublishDTO bookListGoodsPublish : bookListGoodsPublishDTOList) {
            CollectBookListGoodsPublishResponse collectBookListGoodsPublishResponse = new CollectBookListGoodsPublishResponse();
            BeanUtils.copyProperties(bookListGoodsPublish, collectBookListGoodsPublishResponse);
            result.add(collectBookListGoodsPublishResponse);
        }
        return result;
    }


//    private Specification<BookListGoodsPublishDTO> packageBookListGoodsPublishWhere(CollectBookListModelProviderRequest request) {
//        return new Specification<BookListGoodsPublishDTO>() {
//            final List<Predicate> predicateList = new ArrayList<>();
//
//            @Override
//            public Predicate toPredicate(Root<BookListGoodsPublishDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                if (request.getBeginTime() != null) {
//                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("update_time"), request.getBeginTime()));
//                }
//                if (request.getEndTime() != null) {
//                    predicateList.add(criteriaBuilder.lessThan(root.get("update_time"), request.getBeginTime()));
//                }
//                predicateList.add(criteriaBuilder.equal(root.get("category"), CategoryEnum.BOOK_LIST_MODEL.getCode()));
//                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
//            }
//        };
//    }
}
