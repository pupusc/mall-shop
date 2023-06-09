package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRelationAddBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRelationDelBO;
import com.wanmi.sbc.bookmeta.bo.RelationAddBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/06/16:27
 * @Description:
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaRelationProvider")
public interface MetaBookRelationProvider {
    @PostMapping("/goods/${application.goods.version}/MetaRelationProvider/insertBookRelation")
    Integer insert(@RequestBody @NotNull RelationAddBO addBO);

    @PostMapping("/goods/${application.goods.version}/MetaRelationProvider/deleteBookRelation")
    Integer delete(@RequestBody @NotNull MetaBookRelationAddBO addBO);

    @PostMapping("/goods/${application.goods.version}/MetaRelationProvider/deleteBookRelationKey")
    Integer deleteKey(@RequestBody @NotNull MetaBookRelationDelBO addBO);

    @PostMapping("/goods/${application.goods.version}/MetaRelationProvider/deleteBookRelationBook")
    Integer deleteBook(@RequestBody @NotNull MetaBookRelationDelBO addBO);

    @PostMapping("/goods/${application.goods.version}/MetaRelationProvider/getAllBookRelationBook")
    List<MetaBookRelationAddBO> selectAll(@RequestBody @NotNull MetaBookRelationDelBO addBO);

    @PostMapping("/goods/${application.goods.version}/MetaRelationProvider/updateRelationBook")
    int updateAll(@RequestBody @NotNull MetaBookRelationAddBO addBO);
}
