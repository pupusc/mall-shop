package com.wanmi.sbc.crm.autotag.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.request.autotag.AutoTagQueryRequest;
import com.wanmi.sbc.crm.autotag.model.root.AutoTag;
import com.wanmi.sbc.crm.autotag.model.root.AutoTagInit;
import com.wanmi.sbc.crm.autotag.repository.AutoTagInitRepository;
import com.wanmi.sbc.crm.autotag.repository.AutoTagRepository;
import com.wanmi.sbc.crm.autotagsql.repository.AutoTagSqlRepository;
import com.wanmi.sbc.crm.bean.enums.TagType;
import com.wanmi.sbc.crm.bean.vo.AutoTagInitVO;
import com.wanmi.sbc.crm.bean.vo.AutoTagVO;
import com.wanmi.sbc.crm.bean.vo.PreferenceTagListVo;
import com.wanmi.sbc.crm.preferencetagdetail.repository.PreferenceTagDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>自动标签业务逻辑</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@Service("AutoTagService")
public class AutoTagService {
	@Autowired
	private AutoTagRepository autoTagRepository;

    @Autowired
    private AutoTagInitRepository autoTagInitRepository;

    @Autowired
    private PreferenceTagDetailRepository preferenceTagDetailRepository;

    @Autowired
	private AutoTagSqlRepository autoTagSqlRepository;



	/**
	 * 新增自动标签
	 * @author dyt
	 */
	@Transactional
	public AutoTag add(AutoTag entity) {
        entity.setCustomerCount(0L);
        entity.setDelFlag(DeleteFlag.NO);
        entity.setCreateTime(LocalDateTime.now());
		autoTagRepository.save(entity);
		return entity;
	}

	/**
	 * 修改自动标签
	 * @author dyt
	 */
	@Transactional
	public AutoTag modify(AutoTag entity) {
        entity.setUpdateTime(LocalDateTime.now());
		autoTagRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除自动标签
	 * @author dyt
	 */
	@Transactional
	public void deleteById(Long id) {
		autoTagRepository.deleteById(id);
        preferenceTagDetailRepository.deleteByTagId(id);
		autoTagSqlRepository.deleteAutoTagSqlByAutoTagId(id);
	}

	/**
	 * 批量删除自动标签
	 * @author dyt
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		autoTagRepository.deleteByIdList(ids);
	}

	/**
	 * 单个查询自动标签
	 * @author dyt
	 */
	public AutoTag getOne(Long id){
		if(Objects.nonNull(id)){
			return autoTagRepository.findByIdAndDelFlag(id, DeleteFlag.NO)
					.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "自动标签不存在"));
		} else {
			return null;
		}

	}

	/**
	 * 分页查询自动标签
	 * @author dyt
	 */
	public Page<AutoTag> page(AutoTagQueryRequest queryReq){
		return autoTagRepository.findAll(
				AutoTagWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

    /**
     * 初始化自动标签
     * @author dyt
     */
    @Transactional
    public List<AutoTag> init(List<Long> tagIds, String createPerson) {
        List<AutoTag> tags = this.list(AutoTagQueryRequest.builder().delFlag(DeleteFlag.NO)
                .type(TagType.PREFERENCE).build());
		Set<Long> systemTagIdSet = tags.stream().map(AutoTag::getSystemTagId).distinct().collect(Collectors.toSet());
        List<AutoTagInit> initList = autoTagInitRepository.findByIdIn(tagIds);
        List<AutoTag> autoTags = new ArrayList<>();
        initList.stream().filter(init -> !systemTagIdSet.contains(init.getId()))
                .forEach(init -> {
                    AutoTag tag = new AutoTag();
                    KsBeanUtil.copyPropertiesThird(init, tag);
					tag.setDelFlag(DeleteFlag.NO);
                    tag.setId(init.getId());
                    tag.setCreatePerson(createPerson);
                    tag.setSystemFlag(Boolean.TRUE);
					tag.setSystemTagId(init.getId());
                    this.add(tag);
					autoTags.add(tag);
                });
        return autoTags;
    }

    /**
     * 查询引用系统标签
     * @author dyt
     */
    @Transactional
    public List<AutoTagInitVO> systemList() {
        List<AutoTagInitVO> initList = KsBeanUtil.convert(autoTagInitRepository.findAll(), AutoTagInitVO.class);
        List<AutoTag> tags = this.list(AutoTagQueryRequest.builder().delFlag(DeleteFlag.NO)
                .type(TagType.PREFERENCE).build());
        Set<String> tagNameSet = tags.stream().map(AutoTag::getTagName).distinct().collect(Collectors.toSet());
		Set<Long> tagIdSet = tags.stream().map(AutoTag::getSystemTagId).distinct().collect(Collectors.toSet());
        //标识是否已被同步
        initList.forEach(init -> {
            init.setExistsFlag(Boolean.FALSE);
            if (tagIdSet.contains(init.getId())) {
                init.setExistsFlag(Boolean.TRUE);
            }
        });
        return initList;
    }

	/**
	 * 列表查询自动标签
	 * @author dyt
	 */
	public List<AutoTag> list(AutoTagQueryRequest queryReq){
		return autoTagRepository.findAll(AutoTagWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 非系统自动标签总数
	 */
	public Long getCount(){
		return autoTagRepository.count(AutoTagWhereCriteriaBuilder.build(
				AutoTagQueryRequest.builder().systemFlag(Boolean.FALSE).delFlag(DeleteFlag.NO).build()));
	}

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public AutoTagVO wrapperVo(AutoTag autoTag) {
		if (autoTag != null){
			AutoTagVO autoTagVO = KsBeanUtil.convert(autoTag, AutoTagVO.class);
			return autoTagVO;
		}
		return null;
	}

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public PreferenceTagListVo wrapperPreferenceTagListVo(AutoTag autoTag) {
		if (autoTag != null){
			PreferenceTagListVo preferenceTagListVo = KsBeanUtil.convert(autoTag, PreferenceTagListVo.class);
			return preferenceTagListVo;
		}
		return null;
	}

	/**
	 * 列表查询自动标签
	 * @author dyt
	 */
	public int countAllByIds(AutoTagQueryRequest queryReq){
		return autoTagRepository.countByIds(queryReq.getIdList());
	}
}

