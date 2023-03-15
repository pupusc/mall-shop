package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookLabelBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookLabelQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookLabelReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookQueryByPageBo;
import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import com.wanmi.sbc.bookmeta.mapper.MetaBookLabelMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaLabelMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookLabelProvider;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 标签(MetaBookLabel)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Validated
@RestController
public class MetaBookLabelProviderImpl implements MetaBookLabelProvider {
    @Resource
    private MetaBookLabelMapper metaBookLabelMapper;
    @Resource
    private MetaBookMapper metaBookMapper;
    @Resource
    private MetaLabelMapper metaLabelMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookLabelBO> queryById(Integer id) {
        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaBookLabelMapper.queryById(id), MetaBookLabelBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookLabelBO>> queryByPage(@Valid MetaBookLabelQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookLabel metaBookLabel = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookLabel.class);
        
        page.setTotalCount((int) this.metaBookLabelMapper.count(metaBookLabel));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<MetaBookLabel> bookLabels = this.metaBookLabelMapper.queryAllByLimit(metaBookLabel, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(bookLabels, MetaBookLabelBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookLabel 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookLabelBO metaBookLabel) {
        this.metaBookLabelMapper.insertSelective(DO2BOUtils.objA2objB(metaBookLabel, MetaBookLabel.class));
        return BusinessResponse.success(metaBookLabel.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookLabel 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookLabelBO metaBookLabel) {
        return BusinessResponse.success(this.metaBookLabelMapper.update(DO2BOUtils.objA2objB(metaBookLabel, MetaBookLabel.class)) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookLabelMapper.deleteById(id) > 0);
    }

    @Override
    public BusinessResponse<String> importBookLabel(MetaBookLabelBO metaBookLabel) {
        int updateCount=0;
        int addCount=0;
        Map map=new HashMap<>();
        boolean bookExit = metaBookMapper.existsWithPrimaryKey(metaBookLabel.getBookId());
        if(bookExit){
            boolean labelExit = metaLabelMapper.existsWithPrimaryKey(metaBookLabel.getLabelId());
            if(labelExit){
                List<MetaBookLabel> metaBookLabels = metaBookLabelMapper.queryExitByBookAndLabelId(metaBookLabel.getBookId(), metaBookLabel.getLabelId());
                if(null != metaBookLabels && metaBookLabels.size()!=0){//该数据已存在，更新该条数据
                    metaBookLabel.setId(metaBookLabels.get(0).getId());
                    updateCount=metaBookLabelMapper.update(DO2BOUtils.objA2objB(metaBookLabel, MetaBookLabel.class));
                }else{//该数据不存在，插入该条数据
                    Date date=new Date();
                    metaBookLabel.setCreateTime(date);
                    metaBookLabel.setUpdateTime(date);
                    addCount=metaBookLabelMapper.insert(DO2BOUtils.objA2objB(metaBookLabel, MetaBookLabel.class));
                    //触发图书库更新到es
                    metaBookMapper.updateDelflag(metaBookLabel.getBookId(),date);
                }
            }
        }
        return BusinessResponse.success(updateCount+","+addCount);
    }

    @Override
    public BusinessResponse<List<MetaBookLabelReqBO>> bookAllByPage(MetaBookQueryByPageBo metaBookQueryByPageBo) {
        Page page = metaBookQueryByPageBo.getPage();
        page.setTotalCount((int) metaBookLabelMapper.getAllBookCount(metaBookQueryByPageBo.getQueryBookName()));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<MetaBookLabelQueryByPageReqBO> allBook = metaBookLabelMapper.getAllBook(metaBookQueryByPageBo.getQueryBookName(), page.getOffset(), page.getPageSize());
        List<MetaBookLabelReqBO> metaLabelBOS = KsBeanUtil.convertList(allBook, MetaBookLabelReqBO.class);
        return BusinessResponse.success(metaLabelBOS, page);
    }

    @Override
    public BusinessResponse<List<MetaBookLabelReqBO>> bookLabelAllByPage(MetaBookLabelQueryByPageReqBO metaBookQueryByPageBo) {
        Page page = metaBookQueryByPageBo.getPage();
        page.setTotalCount((int) metaBookLabelMapper.getAllBookLabelCount(metaBookQueryByPageBo));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<MetaBookLabelQueryByPageReqBO> allBook = metaBookLabelMapper.getAllBookLabel(metaBookQueryByPageBo, page.getOffset(), page.getPageSize());
        List<MetaBookLabelReqBO> metaLabelBOS = KsBeanUtil.convertList(allBook, MetaBookLabelReqBO.class);
        return BusinessResponse.success(metaLabelBOS, page);
    }
}
