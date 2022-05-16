package com.wanmi.sbc.goods.provider;

import com.wanmi.sbc.goods.bo.MetaFigureQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaFigure;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 人物(MetaFigure)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-16 11:27:00
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaFigureProvider")
public interface MetaFigureProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaFigure/queryById")
    BusinessResponse<MetaFigure> queryById(@RequestParam @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaFigure/queryByPage")
    BusinessResponse<List<MetaFigure>> queryByPage(@RequestBody @Valid MetaFigureQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaFigure 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaFigure/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaFigure metaFigure);

    /**
     * 修改数据
     *
     * @param metaFigure 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaFigure/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaFigure metaFigure);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaFigure/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
