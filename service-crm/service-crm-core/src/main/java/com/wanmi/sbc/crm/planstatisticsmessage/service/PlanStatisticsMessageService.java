package com.wanmi.sbc.crm.planstatisticsmessage.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.bean.vo.PlanStatisticsMessageVO;
import com.wanmi.sbc.crm.planstatisticsmessage.model.root.PlanStatisticsMessage;
import com.wanmi.sbc.crm.planstatisticsmessage.repository.PlanStatisticsMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>运营计划效果统计站内信收到人/次统计数据业务逻辑</p>
 * @author lvzhenwei
 * @date 2020-02-05 15:08:00
 */
@Service("PlanStatisticsMessageService")
public class PlanStatisticsMessageService {
	@Autowired
	private PlanStatisticsMessageRepository planStatisticsMessageRepository;

	/**
	 * 单个查询运营计划效果统计站内信收到人/次统计数据
	 * @author lvzhenwei
	 */
	public PlanStatisticsMessage getOne(Long id){
		return planStatisticsMessageRepository.findById(id).orElse(null);
	}

	/**
	 * 将实体包装成VO
	 * @author lvzhenwei
	 */
	public PlanStatisticsMessageVO wrapperVo(PlanStatisticsMessage planStatisticsMessage) {
		if (planStatisticsMessage != null){
			PlanStatisticsMessageVO planStatisticsMessageVO = KsBeanUtil.convert(planStatisticsMessage, PlanStatisticsMessageVO.class);
			return planStatisticsMessageVO;
		}
		return null;
	}
}

