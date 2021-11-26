package com.wanmi.sbc.crm.tagdimension.mapper;

import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionBigJsonRequest;
import com.wanmi.sbc.crm.bean.vo.TagDimensionVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagdimensionMapper {

    /**
     * 偏好类标签一级行为及默认值初始化数据
     * @return
     */
    public List<TagDimensionVO> selectPreferenceTagList();

    /**
     * 偏好类标签二级指标及默认值初始化数据
     * @return
     */
    public List<TagDimensionVO> selectPreferenceParamList();


    /**
     * 指标类标签一级行为,二级指标及默认值初始化数据
     * @return
     */
    public List<TagDimensionVO> selectQuotaValueTagList();


    /**
     * 综合类，范围类标签一级行为,二级指标及默认值初始化数据
     * @return
     */
    public List<TagDimensionVO> selectOtherTagList(TagDimensionBigJsonRequest request);


}
