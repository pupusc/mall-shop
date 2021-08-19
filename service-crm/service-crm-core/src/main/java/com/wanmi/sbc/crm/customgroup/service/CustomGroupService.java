package com.wanmi.sbc.crm.customgroup.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupListParamRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupListRequest;
import com.wanmi.sbc.crm.customgroup.mapper.CustomGroupMapper;
import com.wanmi.sbc.crm.customgroup.model.CustomGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-12
 * \* Time: 15:29
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Service
public class CustomGroupService {

    @Autowired
    private CustomGroupMapper customGroupMapper;


    public CustomGroup add(CustomGroup customGroup) {
        customGroupMapper.insert(customGroup);
        return customGroup;
    }

    public CustomGroup modify(CustomGroup customGroup) {
        customGroupMapper.updateByPrimaryKeySelective(customGroup);
        return customGroup;
    }

    public int deleteById(Long id) {
        return customGroupMapper.deleteByPrimaryKey(id);
    }

    public CustomGroup queryById(Long id){
        return this.customGroupMapper.selectByPrimaryKey(id);
    }

    public PageInfo<CustomGroup> queryList(BaseQueryRequest request){
        PageHelper.startPage(request.getPageNum()+1,request.getPageSize());
        List<CustomGroup> list = this.customGroupMapper.selectList();

        PageInfo<CustomGroup> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    public List<CustomGroup> queryListForParam(CustomGroupListParamRequest request){
        List<CustomGroup> list = this.customGroupMapper.selectListForParam(request);
        return list;
    }

    public List<CustomGroup> queryAll(){
        return this.customGroupMapper.selectAll();
    }

    public int checkCustomerTag(Long tagId){
        return this.customGroupMapper.checkCustomerTag(tagId);
    }

    /**
     * 根据偏好标签统计
     * @param tagId
     * @return
     */
    public long countByPreferenceTags(Long tagId){
        return this.customGroupMapper.countByPreferenceTags(tagId);
    }

    /**
     * 根据非偏好的自动标签统计
     * @param tagId
     * @return
     */
    public long countByAutoTags(Long tagId){
        return this.customGroupMapper.countByAutoTags(tagId);
    }
}
