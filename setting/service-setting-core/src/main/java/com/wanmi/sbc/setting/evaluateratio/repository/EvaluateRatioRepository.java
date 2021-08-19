package com.wanmi.sbc.setting.evaluateratio.repository;

import com.wanmi.sbc.setting.evaluateratio.model.root.EvaluateRatio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * <p>商品评价系数设置DAO</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@Repository
public interface EvaluateRatioRepository extends JpaRepository<EvaluateRatio, String>,
        JpaSpecificationExecutor<EvaluateRatio> {
	
}
