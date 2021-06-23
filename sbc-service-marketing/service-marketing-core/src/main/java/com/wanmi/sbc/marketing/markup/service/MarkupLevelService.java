package com.wanmi.sbc.marketing.markup.service;

import com.wanmi.sbc.marketing.markup.repository.MarkupLevelRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.marketing.markup.model.root.MarkupLevel;
import com.wanmi.sbc.marketing.api.request.markuplevel.MarkupLevelQueryRequest;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>加价购活动业务逻辑</p>
 * @author he
 * @date 2021-02-04 16:11:01
 */
@Service("MarkupLevelService")
public class MarkupLevelService {
	@Autowired
	private MarkupLevelRepository markupLevelRepository;
	
	/** 
	 * 新增加价购活动
	 * @author he
	 */
	@Transactional
	public MarkupLevel add(MarkupLevel entity) {
		markupLevelRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改加价购活动
	 * @author he
	 */
	@Transactional
	public MarkupLevel modify(MarkupLevel entity) {
		markupLevelRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除加价购活动
	 * @author he
	 */
	@Transactional
	public void deleteById(Long id) {
		markupLevelRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除加价购活动
	 * @author he
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		ids.forEach(id -> markupLevelRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询加价购活动
	 * @author he
	 */
	public MarkupLevel getById(Long id){
		return markupLevelRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询加价购活动
	 * @author he
	 */
	public Page<MarkupLevel> page(MarkupLevelQueryRequest queryReq){
		return markupLevelRepository.findAll(
				com.wanmi.sbc.marketing.markup.service.MarkupLevelWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询加价购活动
	 * @author he
	 */
	public List<MarkupLevel> list(MarkupLevelQueryRequest queryReq){
		return markupLevelRepository.findAll(
				MarkupLevelWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author he
	 */
	public MarkupLevelVO wrapperVo(MarkupLevel markupLevel) {
		if (markupLevel != null){
			MarkupLevelVO markupLevelVO=new MarkupLevelVO();
			KsBeanUtil.copyPropertiesThird(markupLevel,markupLevelVO);
			return markupLevelVO;
		}
		return null;
	}
}
