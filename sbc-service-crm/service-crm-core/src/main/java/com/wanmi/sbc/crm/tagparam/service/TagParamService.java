package com.wanmi.sbc.crm.tagparam.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamQueryRequest;
import com.wanmi.sbc.crm.bean.vo.TagParamVO;
import com.wanmi.sbc.crm.tagparam.model.root.TagParam;
import com.wanmi.sbc.crm.tagparam.repository.TagParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>标签参数业务逻辑</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@Service("TagParamService")
public class TagParamService {
	@Autowired
	private TagParamRepository tagParamRepository;

	/**
	 * 新增标签参数
	 * @author dyt
	 */
	@Transactional
	public TagParam add(TagParam entity) {
		tagParamRepository.save(entity);
		return entity;
	}

	/**
	 * 修改标签参数
	 * @author dyt
	 */
	@Transactional
	public TagParam modify(TagParam entity) {
		tagParamRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除标签参数
	 * @author dyt
	 */
	@Transactional
	public void deleteById(Long id) {
		tagParamRepository.deleteById(id);
	}

	/**
	 * 批量删除标签参数
	 * @author dyt
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		tagParamRepository.deleteByIdIn(ids);
	}

	/**
	 * 单个查询标签参数
	 * @author dyt
	 */
	public TagParam getOne(Long id){
		return tagParamRepository.findById(id)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "标签参数不存在"));
	}

	/**
	 * 分页查询标签参数
	 * @author dyt
	 */
	public Page<TagParam> page(TagParamQueryRequest queryReq){
		return tagParamRepository.findAll(
				TagParamWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询标签参数
	 * @author dyt
	 */
	public List<TagParam> list(TagParamQueryRequest queryReq){
		return tagParamRepository.findAll(TagParamWhereCriteriaBuilder.build(queryReq));
	}

    /**
     * 根据编号批量查询标签参数
     * @param ids
     * @return
     */
	public List<TagParam> listByIds(List<Long> ids){
        return tagParamRepository.findByIdIn(ids);
    }

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public TagParamVO wrapperVo(TagParam tagParam) {
		if (tagParam != null){
			TagParamVO tagParamVO = KsBeanUtil.convert(tagParam, TagParamVO.class);
			return tagParamVO;
		}
		return null;
	}
}

