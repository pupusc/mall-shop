package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookIndustryFileQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.mapper.MetaBookIndustryFileMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookIndustryFileProvider;
import com.wanmi.sbc.bookmeta.entity.MetaBookIndustryFile;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 行业数据文件(MetaBookIndustryFile)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Validated
@RestController
public class MetaBookIndustryFileProviderImpl implements MetaBookIndustryFileProvider {
    @Resource
    private MetaBookIndustryFileMapper metaBookIndustryFileMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookIndustryFile> queryById(Integer id) {
        return BusinessResponse.success(this.metaBookIndustryFileMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookIndustryFile>> queryByPage(@Valid MetaBookIndustryFileQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookIndustryFile metaBookIndustryFile = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookIndustryFile.class);
        
        page.setTotalCount((int) this.metaBookIndustryFileMapper.count(metaBookIndustryFile));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaBookIndustryFileMapper.queryAllByLimit(metaBookIndustryFile, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookIndustryFile 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookIndustryFile metaBookIndustryFile) {
        this.metaBookIndustryFileMapper.insertSelective(metaBookIndustryFile);
        return BusinessResponse.success(metaBookIndustryFile.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookIndustryFile 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookIndustryFile metaBookIndustryFile) {
        return BusinessResponse.success(this.metaBookIndustryFileMapper.update(metaBookIndustryFile) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookIndustryFileMapper.deleteById(id) > 0);
    }
}
