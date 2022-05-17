package com.wanmi.sbc.goods.provider;

import com.wanmi.sbc.goods.bo.MetaBookQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaBook;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 书籍(MetaBook)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-17 16:03:45
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaBookProvider")
public interface MetaBookProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/queryById")
    BusinessResponse<MetaBook> queryById(@RequestParam @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/queryByPage")
    BusinessResponse<List<MetaBook>> queryByPage(@RequestBody @Valid MetaBookQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaBook 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaBook metaBook);

    /**
     * 修改数据
     *
     * @param metaBook 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaBook metaBook);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
