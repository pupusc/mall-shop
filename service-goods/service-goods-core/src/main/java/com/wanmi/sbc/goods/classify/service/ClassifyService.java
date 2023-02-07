package com.wanmi.sbc.goods.classify.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.request.BaseSortProviderRequest;
import com.wanmi.sbc.goods.api.request.classify.ClassifyProviderRequest;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import com.wanmi.sbc.goods.classify.model.root.ClassifyGoodsRelDTO;
import com.wanmi.sbc.goods.classify.repository.ClassifyRepository;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 8:23 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service

public class ClassifyService {

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Resource
    private ClassifyRepository classifyRepository;

    @Resource
    private ClassifyGoodsRelService classifyGoodsRelService;

    private ClassifyProviderResponse  classifyDTO2ClassifyProviderResponse (ClassifyDTO classifyParam) {
        ClassifyProviderResponse parent = new ClassifyProviderResponse();
        parent.setId(classifyParam.getId());
        parent.setClassifyName(classifyParam.getClassifyName());
        parent.setChildrenList(new ArrayList<>());
        parent.setOrderNum(classifyParam.getOrderNum());
        parent.setHasShowIndex(classifyParam.getHasShowIndex());
        parent.setIndexOrderNum(classifyParam.getIndexOrderNum());
        return parent;
    }

    /**
     * 获取类目列表
     * @param classifyIdList
     * @return
     */
    public List<ClassifyDTO> listNoPage(Collection<Integer> classifyIdList) {
        return classifyRepository.findAll(this.packageWhere(classifyIdList, null, null));
    }


    /**
     * 获取所有的类目列表
     * @return
     */
    public List<ClassifyProviderResponse> listClassify(){
        Sort sort = Sort.by(Sort.Direction.ASC, "orderNum").and(Sort.by(Sort.Direction.ASC, "updateTime"));
        List<ClassifyDTO> classifyDTOList = classifyRepository.findAll(this.packageWhere(null, null, null), sort);
        List<ClassifyProviderResponse>  result = new ArrayList<>();
        Map<Integer, ClassifyProviderResponse> resultMap = new HashMap<>();
        for (ClassifyDTO classifyParam : classifyDTOList) {
            if (classifyParam.getParentId() == null || classifyParam.getParentId() == 0) {
                ClassifyProviderResponse parent = this.classifyDTO2ClassifyProviderResponse(classifyParam);
                resultMap.put(classifyParam.getId(), parent);
                result.add(parent);
            }
        }

        for (ClassifyDTO classifyParam : classifyDTOList) {
            if (classifyParam.getParentId() == null || classifyParam.getParentId() == 0) {
                continue;
            }
            ClassifyProviderResponse parent = resultMap.get(classifyParam.getParentId());
            if (parent == null) {
                continue;
            }
            ClassifyProviderResponse children = this.classifyDTO2ClassifyProviderResponse(classifyParam);
            children.setId(classifyParam.getId());
            children.setClassifyName(classifyParam.getClassifyName());
//            children.setOrderNum(classifyParam.getOrderNum());
//            children.setHasShowIndex(classifyParam.getHasShowIndex());
//            children.setIndexOrderNum(classifyParam.getIndexOrderNum());
            parent.getChildrenList().add(children);
        }

        return result;
    }

    /**
     * 根据父id 获取子分类列表
     * @param parentClassifyIdList
     * @return
     */
    public List<ClassifyDTO> listChildClassifyNoPageByParentId(Collection<Integer> parentClassifyIdList) {
        Sort sort = Sort.by(Sort.Direction.ASC, "orderNum").and(Sort.by(Sort.Direction.ASC, "updateTime"));
        return classifyRepository.findAll(this.packageWhere(null, parentClassifyIdList, null), sort);
    }

    /**
     * 首页展示的分类列表
     * @return
     */
    public List<ClassifyProviderResponse> listIndexClassify() {
        List<ClassifyProviderResponse> result = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.ASC, "indexOrderNum").and(Sort.by(Sort.Direction.ASC, "updateTime"));

        //1
        List<ClassifyDTO> resultList = classifyRepository.findAll(this.packageWhere(null, null, 1), sort);

        //2
        //List<ClassifyDTO> resultList = classifyRepository.findAllsort();


        //3
        //begin
        /*String sql = "select * from t_classify where del_flag=? and has_show_index=1 order by index_order_num asc, update_time asc";
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql,ClassifyDTO.class);
        query.setParameter(1,0);
        List<ClassifyDTO> resultList = query.getResultList();*/
        //end

        //4
        //begin
        //String sql = "select * from t_classify where del_flag=? and has_show_index=1 order by index_order_num asc, update_time asc";
        //EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        //Query query = entityManager.createNativeQuery(sql);
        //query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        //query.setParameter(1,0);
        //List<Map> resultList2 = query.getResultList();
        //end

        for (ClassifyDTO classifyParam : resultList) {
            result.add(this.classifyDTO2ClassifyProviderResponse(classifyParam));
        }
        return result;
    }

    private Specification<ClassifyDTO> packageWhere(Collection<Integer> classifyIdList, Collection<Integer> parentClassifyIdList, Integer hasShowIndex) {
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
                if (hasShowIndex != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("hasShowIndex"), hasShowIndex));
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }



    /**
     * 新增店铺分类
     * @param classifyProviderRequest
     */
    public void add(ClassifyProviderRequest classifyProviderRequest) {
        ClassifyDTO classifyDTO = new ClassifyDTO();
        if (classifyProviderRequest.getParentId() == null || Objects.equals(classifyProviderRequest.getParentId(), 0)) {
            classifyDTO.setParentId(0);
            classifyDTO.setLevel(1);
        } else {
            classifyDTO.setParentId(classifyProviderRequest.getParentId());
            classifyDTO.setLevel(2);
        }
        //这里会有并发问题
        List<ClassifyDTO> classifyDTOList = this.listChildClassifyNoPageByParentId(Collections.singletonList(0));
        int orderNum = 0;
        if (!CollectionUtils.isEmpty(classifyDTOList)) {
            ClassifyDTO classifyDTOParam = classifyDTOList.get(classifyDTOList.size() - 1);
            orderNum = classifyDTOParam.getOrderNum() + 1;
        }
        classifyDTO.setClassifyName(classifyProviderRequest.getClassifyName());
        classifyDTO.setOrderNum(orderNum); //后续更改
        classifyDTO.setHasShowIndex(0);
        classifyDTO.setIndexOrderNum(0);
        classifyDTO.setCreateTime(new Date());
        classifyDTO.setUpdateTime(new Date());
        classifyDTO.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
        classifyRepository.save(classifyDTO);
    }

    /**
     * 修改店铺分类
     * @param classifyProviderRequest
     */
    public void update(ClassifyProviderRequest classifyProviderRequest) {
        List<ClassifyDTO> classifyDTOList = classifyRepository.findAll(this.packageWhere(Arrays.asList(classifyProviderRequest.getId()), null, null));
        if (CollectionUtils.isEmpty(classifyDTOList)) {
            throw new SbcRuntimeException("K-000009");
        }
        ClassifyDTO classifyDTO = classifyDTOList.get(0);
        if (!StringUtils.isEmpty(classifyProviderRequest.getClassifyName())) {
            classifyDTO.setClassifyName(classifyProviderRequest.getClassifyName());
        }
        if (classifyProviderRequest.getHasShowIndex() != null) {
            classifyDTO.setHasShowIndex(classifyProviderRequest.getHasShowIndex());
        }

        if (classifyProviderRequest.getIndexOrderNum() != null) {
            classifyDTO.setIndexOrderNum(classifyProviderRequest.getIndexOrderNum());
        }
//        if (classifyProviderRequest.getOrderNum() != null) {
//            classifyDTO.setOrderNum(classifyProviderRequest.getOrderNum());
//        }
        if (classifyProviderRequest.getParentId() != null) {
            classifyDTO.setParentId(classifyProviderRequest.getParentId());
        }
        classifyRepository.save(classifyDTO);
    }

    /**
     * 删除店铺分类
     * @param classifyProviderRequest
     */
    public void delete(ClassifyProviderRequest classifyProviderRequest) {
        List<ClassifyDTO> classifyDTOList = classifyRepository.findAll(this.packageWhere(Collections.singletonList(classifyProviderRequest.getId()), null, null));
        if (CollectionUtils.isEmpty(classifyDTOList)) {
            throw new SbcRuntimeException("K-000009");
        }
        ClassifyDTO classifyDTO = classifyDTOList.get(0);
        if (Objects.equals(classifyDTO.getParentId(), 0)) {
            //查看店铺分类下是否存在子分类
            List<ClassifyDTO> classifyDTOChildList = classifyRepository.findAll(this.packageWhere(null, Collections.singletonList(classifyDTO.getId()), null));
            if (!CollectionUtils.isEmpty(classifyDTOChildList)) {
                throw new SbcRuntimeException("K-000009");
            }
        } else {
            //查看店铺分类下的商品列表
            List<ClassifyGoodsRelDTO> classifyGoodsRelDTOList = classifyGoodsRelService.listClassifyRelByClassifyId(Collections.singletonList(classifyDTO.getId()));
            if (!CollectionUtils.isEmpty(classifyGoodsRelDTOList)) {
                throw new SbcRuntimeException("K-000009");
            }
        }

        //删除分类
        classifyDTO.setDelFlag(DeleteFlagEnum.DELETE.getCode());
        classifyDTO.setUpdateTime(new Date());
        classifyRepository.save(classifyDTO);
    }


    /**
     * 排序
     * @param sortProviderRequestList
     */
    public void sort(List<BaseSortProviderRequest> sortProviderRequestList) {
        //获取商品
        Set<Integer> imageIdSet = sortProviderRequestList.stream().map(BaseSortProviderRequest::getId).collect(Collectors.toSet());
        List<ClassifyDTO> classifyDTOList = this.listNoPage(imageIdSet);
        if (classifyDTOList.size() != imageIdSet.size()) {
            throw new SbcRuntimeException("K-000009");
        }
        Map<Integer, ClassifyDTO> collect = classifyDTOList.stream().collect(Collectors.toMap(ClassifyDTO::getId, Function.identity(), (k1, k2) -> k1));
        for (BaseSortProviderRequest sortProviderParam : sortProviderRequestList) {
            ClassifyDTO classifyDTO = collect.get(sortProviderParam.getId());
            if (classifyDTO == null) {
                throw new SbcRuntimeException("K-000009");
            }
            classifyDTO.setOrderNum(sortProviderParam.getOrderNum());
            classifyRepository.save(classifyDTO);
        }

    }



}
