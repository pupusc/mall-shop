package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookExtendQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.provider.MetaBookExtendProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookExtendAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookExtendEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookExtendQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookExtendQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookExtendQueryByPageResVO;
import com.wanmi.sbc.bookmeta.bo.MetaBookExtendBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 书籍扩展属性(MetaBookExtend)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@RestController
@RequestMapping("metaBookExtend")
public class MetaBookExtendController {
    /**
     * 书籍扩展属性-服务对象
     */
    @Resource
    private MetaBookExtendProvider metaBookExtendProvider;

    /**
     * 书籍扩展属性-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookExtendQueryByPageResVO>> queryByPage(@RequestBody MetaBookExtendQueryByPageReqVO pageRequest) {
        MetaBookExtendQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookExtendQueryByPageReqBO.class);
        BusinessResponse<List<MetaBookExtendBO>> list = this.metaBookExtendProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 书籍扩展属性-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookExtendQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaBookExtendBO> resBO = this.metaBookExtendProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 书籍扩展属性-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaBookExtendAddReqVO addReqVO) {
        MetaBookExtendBO addReqBO = new MetaBookExtendBO();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaBookExtendProvider.insert(addReqBO).getContext());
    }

    /**
     * 书籍扩展属性-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaBookExtendEditReqVO editReqVO) {
        MetaBookExtendBO editReqVBO = new MetaBookExtendBO();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaBookExtendProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 书籍扩展属性-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaBookExtendProvider.deleteById(id.getId());
    }

}

