package com.wanmi.sbc.goods.SuspensionV2.service;

import com.wanmi.sbc.common.enums.DeleteFlag;

import com.wanmi.sbc.goods.SuspensionV2.model.Suspension;
import com.wanmi.sbc.goods.SuspensionV2.repository.SuspensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("SuspensionService")
public class SuspensionService {


    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    public List<Suspension> getSuspensionByType(Long type) {

        String sql = "select * from t_window where type=? ";
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql,Suspension.class);
        query.setParameter(1,type);
        List<Suspension> resultList = query.getResultList();
        return resultList;
    }
}
