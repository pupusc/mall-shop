package com.wanmi.sbc.goods.brand.repository;

import com.wanmi.sbc.goods.brand.entity.GoodsBrandBase;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品品牌数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface GoodsBrandRepository extends JpaRepository<GoodsBrand, Long>, JpaSpecificationExecutor<GoodsBrand>{

    List<GoodsBrand> findByBrandIdIn(List<Long> brandIds);

    /**
     * 根据品牌ID 批量查询
     *
     * @return
     */
    @Query("select new com.wanmi.sbc.goods.brand.entity.GoodsBrandBase(g.brandId , g.brandName ) from GoodsBrand g where g.brandId in ?1 and g.delFlag = 0 ")
    List<GoodsBrandBase> findAllByBrandIdIn(List<Long> ids);
}
