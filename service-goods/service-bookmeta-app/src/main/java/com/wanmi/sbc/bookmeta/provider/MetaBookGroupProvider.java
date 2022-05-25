package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaBookGroupQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBookGroup;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 书组(MetaBookGroup)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaBookGroupProvider")
public interface MetaBookGroupProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookGroup/queryById")
    BusinessResponse<MetaBookGroup> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaBookGroup/queryByPage")
    BusinessResponse<List<MetaBookGroup>> queryByPage(@RequestBody @Valid MetaBookGroupQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaBookGroup 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookGroup/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaBookGroup metaBookGroup);

    /**
     * 修改数据
     *
     * @param metaBookGroup 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookGroup/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaBookGroup metaBookGroup);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaBookGroup/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
