package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaFigureAwardQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaFigureAward;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 人物获奖(MetaFigureAward)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaFigureAwardProvider")
public interface MetaFigureAwardProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaFigureAward/queryById")
    BusinessResponse<MetaFigureAward> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaFigureAward/queryByPage")
    BusinessResponse<List<MetaFigureAward>> queryByPage(@RequestBody @Valid MetaFigureAwardQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaFigureAward 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaFigureAward/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaFigureAward metaFigureAward);

    /**
     * 修改数据
     *
     * @param metaFigureAward 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaFigureAward/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaFigureAward metaFigureAward);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaFigureAward/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
