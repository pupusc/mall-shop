package com.wanmi.sbc.crm.tagdimension.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionBigJsonRequest;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionQueryRequest;
import com.wanmi.sbc.crm.bean.vo.TagDimensionVO;
import com.wanmi.sbc.crm.tagdimension.mapper.TagdimensionMapper;
import com.wanmi.sbc.crm.tagdimension.model.root.TagDimension;
import com.wanmi.sbc.crm.tagdimension.repository.TagDimensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>标签维度业务逻辑</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@Service("TagDimensionService")
public class TagDimensionService {
	@Autowired
	private TagDimensionRepository tagDimensionRepository;

	@Autowired
	private TagdimensionMapper tagdimensionMapper;

	/**
	 * 新增标签维度
	 * @author dyt
	 */
	@Transactional
	public TagDimension add(TagDimension entity) {
		tagDimensionRepository.save(entity);
		return entity;
	}

	/**
	 * 修改标签维度
	 * @author dyt
	 */
	@Transactional
	public TagDimension modify(TagDimension entity) {
		tagDimensionRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除标签维度
	 * @author dyt
	 */
	@Transactional
	public void deleteById(Long id) {
		tagDimensionRepository.deleteById(id);
	}

	/**
	 * 批量删除标签维度
	 * @author dyt
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		tagDimensionRepository.deleteByIdIn(ids);
	}

	/**
	 * 单个查询标签维度
	 * @author dyt
	 */
	public TagDimension getOne(Long id){
		return tagDimensionRepository.findById(id)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "标签维度不存在"));
	}

	/**
	 * 分页查询标签维度
	 * @author dyt
	 */
	public Page<TagDimension> page(TagDimensionQueryRequest queryReq){
		return tagDimensionRepository.findAll(
				TagDimensionWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询标签维度
	 * @author dyt
	 */
	public List<TagDimension> list(TagDimensionQueryRequest queryReq){
		return tagDimensionRepository.findAll(TagDimensionWhereCriteriaBuilder.build(queryReq));
	}

    /**
     * 根据编号批量查询标签维度
     * @param ids
     * @return
     */
    public List<TagDimension> listByIds(List<Long> ids){
        return tagDimensionRepository.findByIdIn(ids);
    }

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public TagDimensionVO wrapperVo(TagDimension tagDimension) {
		if (tagDimension != null){
			TagDimensionVO tagDimensionVO = KsBeanUtil.convert(tagDimension, TagDimensionVO.class);
			return tagDimensionVO;
		}
		return null;
	}

	/**
	 * 偏好类标签一级行为及默认值初始化数据
	 * @return
	 */
	public List<TagDimensionVO> selectPreferenceTagList(){
		return tagdimensionMapper.selectPreferenceTagList();
	}

	/**
	 * 偏好类标签二级指标及默认值初始化数据
	 * @return
	 */
	public List<TagDimensionVO> selectPreferenceParamList(){
		return tagdimensionMapper.selectPreferenceParamList();
	}

	/**
	 * 综合类，范围类标签一级行为,二级指标及默认值初始化数据
	 * @return
	 */
	public List<TagDimensionVO> selectOtherTagList(TagDimensionBigJsonRequest request){
		return tagdimensionMapper.selectOtherTagList(request);
	}

	/**
	 * 指标类标签一级行为,二级指标及默认值初始化数据
	 * @return
	 */
	public List<TagDimensionVO> selectQuotaValueTagList(){
		return  tagdimensionMapper.selectQuotaValueTagList();
	}

}

