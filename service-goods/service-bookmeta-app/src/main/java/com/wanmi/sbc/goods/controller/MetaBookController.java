package com.wanmi.sbc.goods.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.goods.bo.MetaBookQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaBook;
import com.wanmi.sbc.goods.provider.MetaBookProvider;
import com.wanmi.sbc.goods.vo.IntegerIdVO;
import com.wanmi.sbc.goods.vo.MetaBookAddReqVO;
import com.wanmi.sbc.goods.vo.MetaBookEditReqVO;
import com.wanmi.sbc.goods.vo.MetaBookQueryByIdResVO;
import com.wanmi.sbc.goods.vo.MetaBookQueryByPageReqVO;
import com.wanmi.sbc.goods.vo.MetaBookQueryByPageResVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 书籍(MetaBook)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@RestController
@RequestMapping("metaBook")
public class MetaBookController {
    /**
     * 书籍-服务对象
     */
    @Resource
    private MetaBookProvider metaBookProvider;

    /**
     * 书籍-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookQueryByPageResVO>> queryByPage(@RequestBody MetaBookQueryByPageReqVO pageRequest) {
        MetaBookQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookQueryByPageReqBO.class);
        BusinessResponse<List<MetaBook>> list = this.metaBookProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 书籍-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaBook> resBO = this.metaBookProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 书籍-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaBookAddReqVO addReqVO) {
        MetaBook addReqBO = new MetaBook();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaBookProvider.insert(addReqBO).getContext());
    }

    /**
     * 书籍-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaBookEditReqVO editReqVO) {
        MetaBook editReqVBO = new MetaBook();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaBookProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 书籍-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaBookProvider.deleteById(id.getId());
    }

}

