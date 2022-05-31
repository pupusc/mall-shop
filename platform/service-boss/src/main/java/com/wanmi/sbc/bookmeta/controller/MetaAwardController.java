package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaAwardBO;
import com.wanmi.sbc.bookmeta.bo.MetaAwardQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.provider.MetaAwardProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaAwardAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaAwardEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaAwardQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaAwardQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaAwardQueryByPageResVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 奖项(MetaAward)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@RestController
@RequestMapping("metaAward")
public class MetaAwardController {
    /**
     * 奖项-服务对象
     */
    @Resource
    private MetaAwardProvider metaAwardProvider;

    /**
     * 奖项-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaAwardQueryByPageResVO>> queryByPage(@RequestBody MetaAwardQueryByPageReqVO pageRequest) {
        MetaAwardQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaAwardQueryByPageReqBO.class);
        BusinessResponse<List<MetaAwardBO>> list = this.metaAwardProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 奖项-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaAwardQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaAwardBO> resBO = this.metaAwardProvider.queryById(id.getId());
        BusinessResponse<MetaAwardQueryByIdResVO> result = JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
        result.setContext(JSON.parseObject(JSON.toJSONString(resBO.getContext()), MetaAwardQueryByIdResVO.class));

        if (result.getContext() != null) {
            result.getContext().setImageList(StringSplitUtil.split(result.getContext().getImage()));
        }
        return result;
    }

    /**
     * 奖项-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody @Valid MetaAwardAddReqVO addReqVO) {
        addReqVO.setImage(StringSplitUtil.join(addReqVO.getImageList()));
        MetaAwardBO addReqBO = new MetaAwardBO();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaAwardProvider.insert(addReqBO).getContext());
    }

    /**
     * 奖项-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody @Valid MetaAwardEditReqVO editReqVO) {
        editReqVO.setImage(StringSplitUtil.join(editReqVO.getImageList()));
        MetaAwardBO editReqVBO = new MetaAwardBO();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaAwardProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 奖项-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaAwardProvider.deleteById(id.getId());
    }

}

