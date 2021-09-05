package com.wanmi.sbc.goods.classify.service;

import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.classify.model.root.BookListModelClassifyRelDTO;
import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import com.wanmi.sbc.goods.classify.repository.ClassifyRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 8:23 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class ClassifyService {

    @Resource
    private ClassifyRepository classifyRepository;

    /**
     * 获取类目列表
     * @param bookListModelIdList
     * @return
     */
    public List<ClassifyDTO> listNoPage(List<Integer> bookListModelIdList) {
        return classifyRepository.findAll(this.packageWhere(bookListModelIdList));
    }


    private Specification<ClassifyDTO> packageWhere(List<Integer> bookListModelIdList) {
        return new Specification<ClassifyDTO>() {
            @Override
            public Predicate toPredicate(Root<ClassifyDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();

                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
                if (!CollectionUtils.isEmpty(bookListModelIdList)) {
//                    root.get("id")
                    CriteriaBuilder.In<Integer> in = criteriaBuilder.in(root.get("id"));
                    for (Integer id : bookListModelIdList) {
                        in.value(id);
                    }
                    conditionList.add(in);
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }
}
