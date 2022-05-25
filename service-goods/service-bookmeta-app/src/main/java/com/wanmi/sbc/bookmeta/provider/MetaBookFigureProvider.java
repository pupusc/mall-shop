package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaBookFigureQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 书籍人物(MetaBookFigure)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaBookFigureProvider")
public interface MetaBookFigureProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookFigure/queryById")
    BusinessResponse<MetaBookFigure> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaBookFigure/queryByPage")
    BusinessResponse<List<MetaBookFigure>> queryByPage(@RequestBody @Valid MetaBookFigureQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaBookFigure 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookFigure/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaBookFigure metaBookFigure);

    /**
     * 修改数据
     *
     * @param metaBookFigure 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookFigure/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaBookFigure metaBookFigure);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaBookFigure/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
