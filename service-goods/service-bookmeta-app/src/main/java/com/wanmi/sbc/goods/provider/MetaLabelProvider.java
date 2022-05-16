package com.wanmi.sbc.goods.provider;

import com.wanmi.sbc.goods.bo.MetaLabelQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaLabel;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 标签(MetaLabel)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-16 17:15:50
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaLabelProvider")
public interface MetaLabelProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/queryById")
    BusinessResponse<MetaLabel> queryById(@RequestParam @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/queryByPage")
    BusinessResponse<List<MetaLabel>> queryByPage(@RequestBody @Valid MetaLabelQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaLabel 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaLabel metaLabel);

    /**
     * 修改数据
     *
     * @param metaLabel 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaLabel metaLabel);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
