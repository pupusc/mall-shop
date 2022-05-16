package com.wanmi.sbc.goods.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.goods.bo.MetaBookContentQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaBookContent;
import com.wanmi.sbc.goods.provider.MetaBookContentProvider;
import com.wanmi.sbc.goods.vo.IntegerIdVO;
import com.wanmi.sbc.goods.vo.MetaBookContentAddReqVO;
import com.wanmi.sbc.goods.vo.MetaBookContentByBookIdVO;
import com.wanmi.sbc.goods.vo.MetaBookContentEditReqVO;
import com.wanmi.sbc.goods.vo.MetaBookContentQueryByIdResVO;
import com.wanmi.sbc.goods.vo.MetaBookContentQueryByPageReqVO;
import com.wanmi.sbc.goods.vo.MetaBookContentQueryByPageResVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 书籍内容描述(MetaBookContent)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@RestController
@RequestMapping("metaBookContent")
public class MetaBookContentController {
    /**
     * 书籍内容描述-服务对象
     */
    @Resource
    private MetaBookContentProvider metaBookContentProvider;

    /**
     * 书籍内容描述-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookContentQueryByPageResVO>> queryByPage(@RequestBody MetaBookContentQueryByPageReqVO pageRequest) {
        MetaBookContentQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookContentQueryByPageReqBO.class);
        BusinessResponse<List<MetaBookContent>> list = this.metaBookContentProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 书籍内容描述-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookContentQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaBookContent> resBO = this.metaBookContentProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 书籍内容描述-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaBookContentAddReqVO addReqVO) {
        MetaBookContent addReqBO = new MetaBookContent();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaBookContentProvider.insert(addReqBO).getContext());
    }

    /**
     * 书籍内容描述-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaBookContentEditReqVO editReqVO) {
        MetaBookContent editReqVBO = new MetaBookContent();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaBookContentProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 书籍内容描述-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaBookContentProvider.deleteById(id.getId());
    }


    /**
     * 书籍内容查询（出版内容）
     */
    @PostMapping("queryByBookId")
    public BusinessResponse<List<MetaBookContentByBookIdVO>> queryByBookId(@RequestBody IntegerIdVO id) {
        // TODO: 2022/5/16
        return BusinessResponse.success(null);
    }

    /**
     * 书籍内容编辑（出版内容）
     */
    @PostMapping("editByBookId")
    public BusinessResponse<Boolean> editByBookId(@RequestBody MetaBookContentByBookIdVO editReqVOs) {
        // TODO: 2022/5/16
        return BusinessResponse.success(true);
    }
}

