package com.wanmi.sbc.goods.classify.service;

import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import com.wanmi.sbc.goods.classify.repository.ClassifyRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param classifyIdList
     * @return
     */
    public List<ClassifyDTO> listNoPage(List<Integer> classifyIdList) {
        return classifyRepository.findAll(this.packageWhere(classifyIdList, null));
    }

    /**
     * 获取所有的类目列表
     * @return
     */
    public List<ClassifyProviderResponse> listClassify(){
        Sort sort = Sort.by(Sort.Direction.ASC, "orderNum");
        List<ClassifyDTO> classifyDTOList = classifyRepository.findAll(this.packageWhere(null, null), sort);
        List<ClassifyProviderResponse>  result = new ArrayList<>();
        Map<Integer, ClassifyProviderResponse> resultMap = new HashMap<>();
        for (ClassifyDTO classifyParam : classifyDTOList) {
            if (classifyParam.getParentId() == null || classifyParam.getParentId() == 0) {
                ClassifyProviderResponse parent = new ClassifyProviderResponse();
                parent.setId(classifyParam.getId());
                parent.setClassifyName(classifyParam.getClassifyName());
                parent.setChildrenList(new ArrayList<>());
                resultMap.put(classifyParam.getId(), parent);
                result.add(parent);
            } else {
                ClassifyProviderResponse parent = resultMap.get(classifyParam.getParentId());
                if (parent == null) {
                    continue;
                }
                ClassifyProviderResponse children = new ClassifyProviderResponse();
                children.setId(classifyParam.getId());
                children.setClassifyName(classifyParam.getClassifyName());
                parent.getChildrenList().add(children);
            }
        };
        return result;
    }

    /**
     * 根据父id 获取子分类列表
     * @param parentClassifyIdList
     * @return
     */
    public List<ClassifyDTO> listChildClassifyNoPageByParentId(Collection<Integer> parentClassifyIdList) {
        return classifyRepository.findAll(this.packageWhere(null, parentClassifyIdList));
    }

    private Specification<ClassifyDTO> packageWhere(List<Integer> classifyIdList, Collection<Integer> parentClassifyIdList) {
        return new Specification<ClassifyDTO>() {
            @Override
            public Predicate toPredicate(Root<ClassifyDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();

                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
                if (!CollectionUtils.isEmpty(classifyIdList)) {
                    conditionList.add(root.get("id").in(classifyIdList));
                }
                if (!CollectionUtils.isEmpty(parentClassifyIdList)) {
                    conditionList.add(root.get("parentId").in(parentClassifyIdList));
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }
}
