package com.wanmi.sbc.message.smssenddetail.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.message.smssenddetail.repository.SmsSendDetailRepository;
import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import com.wanmi.sbc.message.api.request.smssenddetail.SmsSendDetailQueryRequest;
import com.wanmi.sbc.message.bean.vo.SmsSendDetailVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>短信发送业务逻辑</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@Service("SmsSendDetailService")
public class SmsSendDetailService {
	@Autowired
	private SmsSendDetailRepository smsSendDetailRepository;
	
	/** 
	 * 新增短信发送
	 * @author zgl
	 */
	@Transactional
	public SmsSendDetail add(SmsSendDetail entity) {
		smsSendDetailRepository.save(entity);
		return entity;
	}

    /**
     * 批量保存
     * @param details
     */
    @Transactional
	public void batchAdd(List<SmsSendDetail> details){
        smsSendDetailRepository.saveAll(details);
    }

	/** 
	 * 修改短信发送
	 * @author zgl
	 */
	@Transactional
	public SmsSendDetail modify(SmsSendDetail entity) {
		smsSendDetailRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除短信发送
	 * @author zgl
	 */
	@Transactional
	public void deleteById(Long id) {
		smsSendDetailRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除短信发送
	 * @author zgl
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		ids.forEach(id -> smsSendDetailRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询短信发送
	 * @author zgl
	 */
	public SmsSendDetail getById(Long id){
		return smsSendDetailRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询短信发送
	 * @author zgl
	 */
	public Page<SmsSendDetail> page(SmsSendDetailQueryRequest queryReq){
		return smsSendDetailRepository.findAll(
				SmsSendDetailWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	public long count(SmsSendDetailQueryRequest queryReq){
		return smsSendDetailRepository.count(
				SmsSendDetailWhereCriteriaBuilder.build(queryReq)
		);
	}
	/** 
	 * 列表查询短信发送
	 * @author zgl
	 */
	public List<SmsSendDetail> list(SmsSendDetailQueryRequest queryReq){
		return smsSendDetailRepository.findAll(
				SmsSendDetailWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author zgl
	 */
	public SmsSendDetailVO wrapperVo(SmsSendDetail smsSendDetail) {
		if (smsSendDetail != null){
			SmsSendDetailVO smsSendDetailVO=new SmsSendDetailVO();
			KsBeanUtil.copyPropertiesThird(smsSendDetail,smsSendDetailVO);
			return smsSendDetailVO;
		}
		return null;
	}
}
