package com.wanmi.sbc.goods.provider;

import com.wanmi.sbc.goods.bo.MetaBookContentQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaBookContent;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 书籍内容描述(MetaBookContent)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaBookContentProvider")
public interface MetaBookContentProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookContent/queryById")
    BusinessResponse<MetaBookContent> queryById(@RequestParam @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaBookContent/queryByPage")
    BusinessResponse<List<MetaBookContent>> queryByPage(@RequestBody @Valid MetaBookContentQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaBookContent 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookContent/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaBookContent metaBookContent);

    /**
     * 修改数据
     *
     * @param metaBookContent 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookContent/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaBookContent metaBookContent);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaBookContent/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
