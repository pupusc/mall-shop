package com.wanmi.sbc.goods.provider;

import com.wanmi.sbc.goods.bo.MetaAwardQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaAward;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 奖项(MetaAward)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaAwardProvider")
public interface MetaAwardProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaAward/queryById")
    BusinessResponse<MetaAward> queryById(@RequestParam @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaAward/queryByPage")
    BusinessResponse<List<MetaAward>> queryByPage(@RequestBody @Valid MetaAwardQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaAward 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaAward/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaAward metaAward);

    /**
     * 修改数据
     *
     * @param metaAward 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaAward/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaAward metaAward);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaAward/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
