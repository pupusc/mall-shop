package com.wanmi.sbc.goods.classify.service;

import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import com.wanmi.sbc.goods.classify.model.root.ClassifyGoodsRelDTO;
import com.wanmi.sbc.goods.classify.repository.ClassifyGoodsRelRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/15 1:28 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class ClassifyGoodsRelService {

    @Resource
    private ClassifyGoodsRelRepository classifyGoodsRelRepository;

    /**
     * 根据商品id 获取商品分类列表信息
     * @param goodsIdList
     * @return
     */
    public List<ClassifyGoodsRelDTO> listClassifyIdByGoodsId(Collection<String> goodsIdList) {
        return classifyGoodsRelRepository.findAll(this.packageWhere(goodsIdList, null));
    }


    /**
     * 根据商品分类id 获取商品id列表信息
     * @param classifyIdCollection
     * @return
     */
    public List<ClassifyGoodsRelDTO> listClassifyRelByClassifyId(Collection<Integer> classifyIdCollection) {
        return classifyGoodsRelRepository.findAll(this.packageWhere(null, classifyIdCollection));
    }


    private Specification<ClassifyGoodsRelDTO> packageWhere(Collection<String> goodsIdList, Collection<Integer> classifyIdCollection) {
        return new Specification<ClassifyGoodsRelDTO>() {
            @Override
            public Predicate toPredicate(Root<ClassifyGoodsRelDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();

                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
                if (!CollectionUtils.isEmpty(goodsIdList)) {
                    conditionList.add(root.get("goodsId").in(goodsIdList));
                }
                if (!CollectionUtils.isEmpty(classifyIdCollection)) {
                    conditionList.add(root.get("classifyId").in(classifyIdCollection));
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }
}
