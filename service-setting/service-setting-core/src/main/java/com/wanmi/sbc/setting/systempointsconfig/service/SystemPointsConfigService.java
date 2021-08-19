package com.wanmi.sbc.setting.systempointsconfig.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.constant.SettingErrorCode;
import com.wanmi.sbc.setting.api.request.SystemPointsConfigModifyRequest;
import com.wanmi.sbc.setting.bean.enums.PointsUsageFlag;
import com.wanmi.sbc.setting.bean.enums.SettingRedisKey;
import com.wanmi.sbc.setting.redis.RedisService;
import com.wanmi.sbc.setting.systempointsconfig.model.root.SystemPointsConfig;
import com.wanmi.sbc.setting.systempointsconfig.repository.SystemPointsConfigRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>积分设置业务逻辑</p>
 * @author yxz
 * @date 2019-03-28 16:24:21
 */
@Service("SystemPointsConfigService")
public class SystemPointsConfigService {

	@Autowired
	private SystemPointsConfigRepository systemPointsConfigRepository;

	@Autowired
	private RedisService redisService;

	/**
	 * 查询积分设置
	 *
	 * @return
	 */
	public SystemPointsConfig querySystemPointsConfig() {
		List<SystemPointsConfig> configs = systemPointsConfigRepository.findByDelFlag(DeleteFlag.NO);
		SystemPointsConfig systemPointsConfig = new SystemPointsConfig();
		if (CollectionUtils.isEmpty(configs)) {
			// 如果数据库里无数据，初始化
			systemPointsConfig.setPointsUsageFlag(PointsUsageFlag.ORDER);
			systemPointsConfig.setStatus(EnableStatus.DISABLE);
			systemPointsConfig.setPointsWorth(Constants.POINTS_WORTH);
			systemPointsConfig.setDelFlag(DeleteFlag.NO);
			systemPointsConfig.setCreateTime(LocalDateTime.now());
			systemPointsConfig.setSwitchUsageTime(LocalDateTime.now());
			systemPointsConfig = systemPointsConfigRepository.saveAndFlush(systemPointsConfig);
		} else {
			systemPointsConfig = configs.get(0);
		}

		return systemPointsConfig;
	}

	/**
	 * 修改积分设置
	 *
	 * @param request
	 */
	@Transactional
	public void modifySystemPointsConfig(SystemPointsConfigModifyRequest request) {
		// 根据配置id查询积分设置详情
		SystemPointsConfig systemPointsConfig = systemPointsConfigRepository.findByPointsConfigIdAndDelFlag(
				request.getPointsConfigId(), DeleteFlag.NO);
		// 积分设置不存在
		if (Objects.isNull(systemPointsConfig)) {
			throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
		}

		//切换抵扣方式的验证
		if(!systemPointsConfig.getPointsUsageFlag().equals(request.getPointsUsageFlag())){
			//时间切换
			if(systemPointsConfig.getSwitchUsageTime() == null
					|| (!systemPointsConfig.getSwitchUsageTime().toLocalDate().isEqual(LocalDate.now()))){
				systemPointsConfig.setSwitchUsageTime(LocalDateTime.now());
			}else{
				throw new SbcRuntimeException(SettingErrorCode.POINT_USED_SWITCH_ERROR);
			}
		}
		redisService.delete(SettingRedisKey.SYSTEM_POINTS_CONFIG.toValue());
		request.setUpdateTime(LocalDateTime.now());
		KsBeanUtil.copyProperties(request, systemPointsConfig);
		systemPointsConfigRepository.save(systemPointsConfig);
	}

}
