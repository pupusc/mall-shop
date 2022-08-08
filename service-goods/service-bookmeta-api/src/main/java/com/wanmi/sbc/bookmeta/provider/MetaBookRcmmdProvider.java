package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdByBookIdReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdByBookIdResBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdQueryByPageReqBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 书籍推荐(MetaBookRcmmd)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaBookRcmmdProvider")
public interface MetaBookRcmmdProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookRcmmd/queryById")
    BusinessResponse<MetaBookRcmmdBO> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaBookRcmmd/queryByPage")
    BusinessResponse<List<MetaBookRcmmdBO>> queryByPage(@RequestBody @Valid MetaBookRcmmdQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaBookRcmmd 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookRcmmd/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaBookRcmmdBO metaBookRcmmd);

    /**
     * 修改数据
     *
     * @param metaBookRcmmd 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookRcmmd/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaBookRcmmdBO metaBookRcmmd);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaBookRcmmd/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

    @PostMapping("/goods/${application.goods.version}/metaBookRcmmd/queryByBookId")
    BusinessResponse<MetaBookRcmmdByBookIdResBO> queryByBookId(@RequestBody @NotNull Integer bookId);

    @PostMapping("/goods/${application.goods.version}/metaBookRcmmd/editByBookId")
    BusinessResponse<Boolean> editByBookId(@RequestBody MetaBookRcmmdByBookIdReqBO editBO);
}
