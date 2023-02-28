package com.wanmi.sbc.goods.jpa;

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
import java.math.BigInteger;
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

public class JpaManager {

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    /**
     //String sql = "select * from t_classify where del_flag=? and has_show_index=1 order by index_order_num asc, update_time asc";
     //Object[] obj = new Object[]{0};
     //List resultList2 = jpaManager.queryForList(sql,obj);
     //System.out.println(resultList2);
     * @param sql
     * @param obj
     * @return
     *
     */
    public List queryForList(String sql, Object[] obj) {

        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        for(int i=0;i<obj.length;i++){
            int num = i+1;
            Object value =  obj[i];
            query.setParameter(num,value);

        }
        List<Map> resultList = query.getResultList();

        if(entityManager != null){
            entityManager.close();
        }

        return resultList;

    }

    public List queryForList(String sql) {

        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map> resultList = query.getResultList();

        if(entityManager != null){
            entityManager.close();
        }

        return resultList;

    }

    public Long count(String sql, Object[] obj) {

        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql);
        //query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        for(int i=0;i<obj.length;i++){
            int num = i+1;
            Object value =  obj[i];
            query.setParameter(num,value);

        }

        BigInteger bi = (BigInteger)query.getSingleResult();  //就是这个getSingleResult()方法
        Long countNum = (long)bi.intValue();

        if(entityManager != null){
            entityManager.close();
        }

        return countNum;

    }

    public PersistenceResult queryForPr(String inSql, Object[] obj,int pagenum,int pagesize) {

        String sql  = "select t2.* from (" + inSql + ") t2 limit " + ((pagenum - 1) * pagesize) + "," + pagesize + " " ;

        String countSql = " select count(*) from (" + inSql + " )xc1";

        List resultList = queryForList(sql,obj);
        long total = count(countSql,obj);


        PersistenceResult pr = new PersistenceResult();
        pr.setResultList(resultList);
        double pages = (double)total;
        pr.setTotal(total);

        pr.setPagecount((int) Math.ceil(pages / (double) pagesize));


        return pr;

    }


}
