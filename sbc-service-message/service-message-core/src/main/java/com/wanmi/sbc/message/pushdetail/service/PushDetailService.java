package com.wanmi.sbc.message.pushdetail.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailQueryRequest;
import com.wanmi.sbc.message.bean.vo.PushDetailVO;
import com.wanmi.sbc.message.pushdetail.model.root.PushDetail;
import com.wanmi.sbc.message.pushdetail.repository.PushDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>推送详情业务逻辑</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@Service("PushDetailService")
public class PushDetailService {
	@Autowired
	private PushDetailRepository pushDetailRepository;

	/**
	 * 新增推送详情
	 * @author Bob
	 */
	@Transactional
	public PushDetail add(PushDetail entity) {
		pushDetailRepository.save(entity);
		return entity;
	}

	/**
	 * 修改推送详情
	 * @author Bob
	 */
	@Transactional
	public PushDetail modify(PushDetail entity) {
		pushDetailRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除推送详情
	 * @author Bob
	 */
	@Transactional
	public void deleteById(String id) {
		pushDetailRepository.deleteById(id);
	}

	/**
	 * 单个查询推送详情
	 * @author Bob
	 */
	public PushDetail getOne(String id){
		return pushDetailRepository.findById(id)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "推送详情不存在"));
	}

	/**
	 * 分页查询推送详情
	 * @author Bob
	 */
	public Page<PushDetail> page(PushDetailQueryRequest queryReq){
		return pushDetailRepository.findAll(
				PushDetailWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询推送详情
	 * @author Bob
	 */
	public List<PushDetail> list(PushDetailQueryRequest queryReq){
		return pushDetailRepository.findAll(PushDetailWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author Bob
	 */
	public PushDetailVO wrapperVo(PushDetail pushDetail) {
		if (pushDetail != null){
			PushDetailVO pushDetailVO = KsBeanUtil.convert(pushDetail, PushDetailVO.class);
			return pushDetailVO;
		}
		return null;
	}
}

