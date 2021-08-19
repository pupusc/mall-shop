package com.wanmi.sbc.customer.distribution.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.distribution.model.entity.DistributorLevelBase;
import com.wanmi.sbc.customer.distribution.model.root.DistributorLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>分销员等级DAO</p>
 *
 * @author gaomuwei
 */
@Repository
public interface DistributorLevelRepository extends JpaRepository<DistributorLevel, String>,
        JpaSpecificationExecutor<DistributorLevel> {

	/**
	 * 查询分销员等级基础信息列表（仅包含字段：分销员等级ID、分销员等级名称）
	 * @return
	 */
	@Query(value = "select new com.wanmi.sbc.customer.distribution.model.entity.DistributorLevelBase(c.distributorLevelId,c.distributorLevelName) FROM DistributorLevel c " +
			" where c.delFlag = '0' order by c.sort ")
	List<DistributorLevelBase> listBaseDistributorLevel();

	/**
	 * 查询分销员等级列表
	 */
	List<DistributorLevel> findByDelFlag(DeleteFlag delFlag);

	/**
	 * 根据分销员等级ID集合查询分销员等级信息
	 * @param distributorLevelIds
	 * @return
	 */
	List<DistributorLevel>	findByDistributorLevelIdIn(List<String> distributorLevelIds);

	/**
	 * 根据分销员等级ID查询分销员等级信息
	 * @param distributorLevelId
	 * @return
	 */
	DistributorLevel findByDistributorLevelId(String distributorLevelId);

	/**
	 * 删除分销员等级
	 */
	@Modifying
	@Query("update DistributorLevel d set d.delFlag = '1', d.updateTime = now() where d.distributorLevelId = ?1 ")
	int deleteByLevelId(String levelId);

	/**
	 * 查询所有分销员等级
	 * @return
	 */
	@Query(" from DistributorLevel d where d.delFlag = '0' order by d.sort asc")
	List<DistributorLevel> findAllList();

	/**
	 * 修改普通分销员等级名称
	 * @return
	 */
	@Modifying
	@Query("update DistributorLevel d set d.distributorLevelName = ?1 where d.sort = 1 and d.delFlag = 0 ")
	int updateNormalDistributorLevelName(String name);
}
