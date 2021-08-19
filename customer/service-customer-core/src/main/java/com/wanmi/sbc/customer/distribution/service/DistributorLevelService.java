package com.wanmi.sbc.customer.distribution.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.constant.CustomerErrorCode;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerQueryRequest;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.customer.distribution.model.entity.DistributorLevelBase;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomer;
import com.wanmi.sbc.customer.distribution.model.root.DistributorLevel;
import com.wanmi.sbc.customer.distribution.repository.DistributorLevelRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>分销员等级DAO</p>
 *
 * @author gaomuwei
 */
@Service
public class DistributorLevelService {

	@Autowired
	private DistributorLevelRepository distributorLevelRepository;

	@Autowired
	private DistributionCustomerService distributionCustomerService;

	/**
	 * 查询分销员等级基础信息列表（仅包含字段：分销员等级ID、分销员等级名称）
	 * @return
	 */
	public List<DistributorLevelBase> listBaseDistributorLevel(){
		return distributorLevelRepository.listBaseDistributorLevel();
	}

	/**
	 * 查询所有分销员等级
	 */
	public List<DistributorLevel> findAll() {
		return distributorLevelRepository.findAllList();
	}

	/**
	 * 根据分销员等级ID集合查询分销员等级信息
	 * @param distributorLevelIds
	 * @return
	 */
	public List<DistributorLevel> findByDistributorLevelIdIn(List<String> distributorLevelIds){
		return distributorLevelRepository.findByDistributorLevelIdIn(distributorLevelIds);
	}

	/**
	 * 根据分销员等级ID查询分销员等级信息
	 * @param distributorLevelId
	 * @return
	 */
	public DistributorLevel findByDistributorLevelId(String distributorLevelId){
		return distributorLevelRepository.findByDistributorLevelId(distributorLevelId);
	}

	/**
	 * 根据会员ID查询分销员等级信息
	 * @param customerId
	 * @return
	 */
	public DistributorLevelVO getByCustomerId(String customerId) {
		DistributionCustomer distributionCustomer = distributionCustomerService.getByCustomerIdAndDelFlag(customerId);
		if (Objects.isNull(distributionCustomer)){
			return null;
		}
		DistributorLevel distributorLevel = findByDistributorLevelId(distributionCustomer.getDistributorLevelId());
		if (Objects.isNull(distributorLevel))
		{
			return null;
		}
		return KsBeanUtil.copyPropertiesThird(distributorLevel, DistributorLevelVO.class);
	}


	/**
	 * 批量保存分销等级
	 */
    @Transactional(rollbackFor = Exception.class)
	public List<DistributorLevel> batchSave(List<DistributorLevel> levels) {

		List<DistributorLevel> oldLevels = distributorLevelRepository.findAllList();

		// 1.将新增/编辑的等级合并至原等级中
		levels.forEach(level -> {
			if (StringUtils.isEmpty(level.getDistributorLevelId())) {
				// 新增
				level.setSort(oldLevels.size() + 1);
				oldLevels.add(level);
			} else {
				// 编辑
				Optional<DistributorLevel> oldLevel = oldLevels.stream().filter(
						old -> level.getDistributorLevelId().equals(old.getDistributorLevelId())).findFirst();
				if (oldLevel.isPresent()) {
					KsBeanUtil.copyPropertiesThird(level, oldLevel.get());
				}
			}
		});

		// 2.校验分销员等级
		validDistributorLevels(oldLevels);

		LocalDateTime nowTime = LocalDateTime.now();
		oldLevels.forEach(item -> {
            item.setDelFlag(DeleteFlag.NO);
            item.setUpdateTime(nowTime);
        });

		// 3.保存
		return distributorLevelRepository.saveAll(oldLevels);
	}

	/**
	 * 校验分销员等级
	 */
	public void validDistributorLevels(List<DistributorLevel> distributorLevels) {
		distributorLevels.sort(Comparator.comparing(DistributorLevel::getSort));

		// 佣金比例最大值、佣金提成比例最大值
		BigDecimal commissionRateMax = BigDecimal.ZERO;
		BigDecimal percentageRateMax = BigDecimal.ZERO;

		// 当前销售额、当前到账收益额、邀请人数
		BigDecimal salesThreshold = BigDecimal.ZERO;
		BigDecimal recordThreshold = BigDecimal.ZERO;
		Integer inviteThreshold = 0;

		// 分销员等级数量不能超过5个
		if (distributorLevels.size() > 5) {
			throw new SbcRuntimeException(CustomerErrorCode.DISTRIBUTOR_LEVEL_COUNT_ERROR);
		}

		for(int i = 0; i < distributorLevels.size(); i++) {
			DistributorLevel item = distributorLevels.get(i);
			// 计算佣金比例最大值
			if (Objects.nonNull(item.getCommissionRate())
					&& item.getCommissionRate().compareTo(commissionRateMax) == 1) {
				commissionRateMax = item.getCommissionRate();
			}

			// 计算佣金提成比例最大值
			if (Objects.nonNull(item.getPercentageRate())
					&& item.getPercentageRate().compareTo(percentageRateMax) == 1) {
				percentageRateMax = item.getPercentageRate();
			}

			// 下级销售额门槛要比上级的大
			if (DefaultFlag.YES.equals(item.getSalesFlag())) {
				if (item.getSalesThreshold().compareTo(salesThreshold) == 1) {
					salesThreshold = item.getSalesThreshold();
				} else {
					throw new SbcRuntimeException(CustomerErrorCode.DISTRIBUTOR_LEVEL_THRESHOLD_ERROR);
				}
			}

			// 下级当前到账收益额门槛要比上级的大
			if (DefaultFlag.YES.equals(item.getRecordFlag())) {
				if (item.getRecordThreshold().compareTo(recordThreshold) == 1) {
					recordThreshold = item.getRecordThreshold();
				} else {
					throw new SbcRuntimeException(CustomerErrorCode.DISTRIBUTOR_LEVEL_THRESHOLD_ERROR);
				}
			}

			// 下级邀请人数门槛要比上级的大
			if (DefaultFlag.YES.equals(item.getInviteFlag())) {
				if (item.getInviteThreshold().compareTo(inviteThreshold) == 1) {
					inviteThreshold = item.getInviteThreshold();
				} else {
					throw new SbcRuntimeException(CustomerErrorCode.DISTRIBUTOR_LEVEL_THRESHOLD_ERROR);
				}
			}

			// 必须勾选一个升级规则
			if (item.getSort() != 1
					&& DefaultFlag.NO.equals(item.getSalesFlag())
					&& DefaultFlag.NO.equals(item.getRecordFlag())
					&& DefaultFlag.NO.equals(item.getInviteFlag())) {
				throw new SbcRuntimeException(CustomerErrorCode.DISTRIBUTOR_LEVEL_THRESHOLD_EMPTY_ERROR);
			}

		}

		// 存在佣金比例+佣金提成比例相加大于100%的情况
		if (commissionRateMax.add(percentageRateMax).compareTo(new BigDecimal(1)) == 1) {
			throw new SbcRuntimeException(CustomerErrorCode.DISTRIBUTOR_LEVEL_COMMISSION_ERROR);
		}
	}

	/**
	 * 删除分销等级
	 */
	@Transactional(rollbackFor = Exception.class)
	public int delete(String distributorLevelId) {
		// 1.判断当前等级有没有关联分销员
		DistributorLevel distributorLevel = distributorLevelRepository.findById(distributorLevelId).
				orElseThrow(()->new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR));
		DistributionCustomerQueryRequest request = new DistributionCustomerQueryRequest();
		request.setDistributorLevelId(distributorLevelId);
		request.setDelFlag(DeleteFlag.NO);
		List<DistributionCustomer> customers = distributionCustomerService.list(request);
		if (CollectionUtils.isNotEmpty(customers)) {
			// 等级关联了分销员，不能删除
			throw new SbcRuntimeException(CustomerErrorCode.DISTRIBUTOR_LEVEL_BE_RELATED);
		}

		// 2.删除等级
		return distributorLevelRepository.deleteByLevelId(distributorLevelId);
	}


	/**
	 * 修改普通分销员等级名称
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public int updateNormalDistributorLevelName(String name) {
		return distributorLevelRepository.updateNormalDistributorLevelName(name);
	}

	/**
	 * 初始化分销员等级
	 */
	@Transactional(rollbackFor = Exception.class)
	public DistributorLevel initDistributorLevel() {
		// 1.初始化分销员默认等级
		DistributorLevel level = new DistributorLevel();
		level.setDistributorLevelName("普通分销员");
		level.setSort(NumberUtils.INTEGER_ONE);
		level.setCommissionRate(new BigDecimal(1));
		level.setPercentageRate(BigDecimal.ZERO);
		level.setSalesFlag(DefaultFlag.NO);
		level.setRecordFlag(DefaultFlag.NO);
		level.setInviteFlag(DefaultFlag.NO);
		level.setDelFlag(DeleteFlag.NO);
		level = distributorLevelRepository.saveAndFlush(level);

		// 2.将已有分销员设为默认等级
		distributionCustomerService.initDistributorLevel(level.getDistributorLevelId());

		return level;
	}
}
