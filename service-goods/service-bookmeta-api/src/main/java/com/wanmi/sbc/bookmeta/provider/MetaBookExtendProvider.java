package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaBookExtendBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookExtendQueryByPageReqBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 书籍扩展属性(MetaBookExtend)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaBookExtendProvider")
public interface MetaBookExtendProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookExtend/queryById")
    BusinessResponse<MetaBookExtendBO> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaBookExtend/queryByPage")
    BusinessResponse<List<MetaBookExtendBO>> queryByPage(@RequestBody @Valid MetaBookExtendQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaBookExtend 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookExtend/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaBookExtendBO metaBookExtend);

    /**
     * 修改数据
     *
     * @param metaBookExtend 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookExtend/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaBookExtendBO metaBookExtend);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaBookExtend/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
