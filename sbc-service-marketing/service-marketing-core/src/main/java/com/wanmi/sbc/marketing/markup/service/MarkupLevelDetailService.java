package com.wanmi.sbc.marketing.markup.service;

import com.wanmi.sbc.marketing.api.request.markupleveldetail.MarkupLevelDetailQueryRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.marketing.markup.repository.MarkupLevelDetailRepository;
import com.wanmi.sbc.marketing.markup.model.root.MarkupLevelDetail;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelDetailVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>加价购活动业务逻辑</p>
 * @author he
 * @date 2021-02-04 16:11:24
 */
@Service("MarkupLevelDetailService")
public class MarkupLevelDetailService {
	@Autowired
	private MarkupLevelDetailRepository markupLevelDetailRepository;

	/**
	 * 新增加价购活动
	 * @author he
	 */
	@Transactional
	public MarkupLevelDetail add(MarkupLevelDetail entity) {
		markupLevelDetailRepository.save(entity);
		return entity;
	}

	/**
	 * 修改加价购活动
	 * @author he
	 */
	@Transactional
	public MarkupLevelDetail modify(MarkupLevelDetail entity) {
		markupLevelDetailRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除加价购活动
	 * @author he
	 */
	@Transactional
	public void deleteById(Long id) {
		markupLevelDetailRepository.deleteById(id);
	}

	/**
	 * 批量删除加价购活动
	 * @author he
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		ids.forEach(id -> markupLevelDetailRepository.deleteById(id));
	}

	/**
	 * 单个查询加价购活动
	 * @author he
	 */
	public MarkupLevelDetail getById(Long id){
		return markupLevelDetailRepository.findById(id).orElse(null);
	}

	/**
	 * 分页查询加价购活动
	 * @author he
	 */
	public Page<MarkupLevelDetail> page(MarkupLevelDetailQueryRequest queryReq){
		return markupLevelDetailRepository.findAll(
				MarkupLevelDetailWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询加价购活动
	 * @author he
	 */
	public List<MarkupLevelDetail> list(MarkupLevelDetailQueryRequest queryReq){
		return markupLevelDetailRepository.findAll(
				MarkupLevelDetailWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author he
	 */
	public MarkupLevelDetailVO wrapperVo(MarkupLevelDetail markupLevelDetail) {
		if (markupLevelDetail != null){
			MarkupLevelDetailVO markupLevelDetailVO=new MarkupLevelDetailVO();
			KsBeanUtil.copyPropertiesThird(markupLevelDetail,markupLevelDetailVO);
			return markupLevelDetailVO;
		}
		return null;
	}
}
