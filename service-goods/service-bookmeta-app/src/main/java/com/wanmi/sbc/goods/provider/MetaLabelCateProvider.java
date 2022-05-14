package com.wanmi.sbc.goods.provider;

import com.wanmi.sbc.goods.bo.MetaLabelCateQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaLabelCate;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 标签目录(MetaLabelCate)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaLabelCateProvider")
public interface MetaLabelCateProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaLabelCate/queryById")
    BusinessResponse<MetaLabelCate> queryById(@RequestParam @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaLabelCate/queryByPage")
    BusinessResponse<List<MetaLabelCate>> queryByPage(@RequestBody @Valid MetaLabelCateQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaLabelCate 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaLabelCate/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaLabelCate metaLabelCate);

    /**
     * 修改数据
     *
     * @param metaLabelCate 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaLabelCate/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaLabelCate metaLabelCate);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaLabelCate/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
