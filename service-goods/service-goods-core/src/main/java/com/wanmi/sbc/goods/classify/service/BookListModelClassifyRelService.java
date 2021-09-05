package com.wanmi.sbc.goods.classify.service;

import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.classify.model.root.BookListModelClassifyRelDTO;
import com.wanmi.sbc.goods.classify.repository.BookListModelClassifyRelRepository;
import com.wanmi.sbc.goods.classify.request.BookListModelClassifyRelRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 7:38 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class BookListModelClassifyRelService {

    @Resource
    private BookListModelClassifyRelRepository bookListModelClassifyRelRepository;


    /**
     * 新增商品对应分类
     * @param bookListModelClassifyRequest
     */
    @Transactional
    public void add(BookListModelClassifyRelRequest bookListModelClassifyRequest) {

        //获取商品分类，查看当前参数是否有效 TODO


        //1删除当前 书单对一个的类目
        List<BookListModelClassifyRelDTO> bookListModelClassifyList = this.listNoPage(bookListModelClassifyRequest.getBookListModelId());
        if (!CollectionUtils.isEmpty(bookListModelClassifyList)) {
            bookListModelClassifyList.forEach(e -> {
                e.setDelFlag(DeleteFlagEnum.DELETE.getCode());
                e.setUpdateTime(new Date());
            });
            bookListModelClassifyRelRepository.saveAll(bookListModelClassifyList);
        }
        //2 新增 书单对应类目
        List<BookListModelClassifyRelDTO> addList = new ArrayList<>();
        for (Integer classifyIdParam : bookListModelClassifyRequest.getClassifyIdList()) {
            BookListModelClassifyRelDTO bookListModelClassifyDTO = new BookListModelClassifyRelDTO();
            bookListModelClassifyDTO.setBookListModelId(bookListModelClassifyRequest.getBookListModelId());
            bookListModelClassifyDTO.setClassifyId(classifyIdParam);
            bookListModelClassifyDTO.setCreateTime(new Date());
            bookListModelClassifyDTO.setUpdateTime(new Date());
            bookListModelClassifyDTO.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
        }
        bookListModelClassifyRelRepository.saveAll(addList);
    }


    public List<BookListModelClassifyRelDTO> listNoPage(Integer bookListModelId) {
        return bookListModelClassifyRelRepository.findAll(this.packageWhere(bookListModelId));
    }


    private Specification<BookListModelClassifyRelDTO> packageWhere(Integer bookListModelId) {
        return new Specification<BookListModelClassifyRelDTO>() {
            @Override
            public Predicate toPredicate(Root<BookListModelClassifyRelDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();

                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
                if (bookListModelId != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("bookListModelId"), bookListModelId));
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
            }
        };
    }
}
