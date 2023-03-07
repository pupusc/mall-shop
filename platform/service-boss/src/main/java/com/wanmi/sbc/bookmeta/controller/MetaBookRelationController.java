package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.MetaBookRelationAddBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRelationDelBO;
import com.wanmi.sbc.bookmeta.provider.MetaBookRelationProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookRelationAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookRelationDelReqVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/06/16:25
 * @Description:
 */
@RestController
@RequestMapping("/metaBookRelation")
public class MetaBookRelationController {
    @Resource
    MetaBookRelationProvider metaBookRelationProvider;
    @PostMapping("insertMetaBookRelation")
    public BusinessResponse<Integer> insert(@RequestBody MetaBookRelationAddReqVO reqVO) {
        MetaBookRelationAddBO convert = KsBeanUtil.convert(reqVO, MetaBookRelationAddBO.class);
        Integer insert = metaBookRelationProvider.insert(convert);
            return BusinessResponse.success(insert);
    }

    @PostMapping("deleteAllMetaBookRelation")
    public BusinessResponse<Integer> delete(@RequestBody MetaBookRelationAddReqVO reqVO) {
        MetaBookRelationAddBO convert = KsBeanUtil.convert(reqVO, MetaBookRelationAddBO.class);
        Integer insert = metaBookRelationProvider.delete(convert);
        return BusinessResponse.success(insert);
    }

    @PostMapping("deleteRelationKey")
    public BusinessResponse<Integer> deleteRelationKey(@RequestBody MetaBookRelationDelReqVO reqVO) {
        MetaBookRelationDelBO convert = KsBeanUtil.convert(reqVO, MetaBookRelationDelBO.class);
        Integer deleteKey = metaBookRelationProvider.deleteKey(convert);
        return BusinessResponse.success(deleteKey);
    }

    @PostMapping("deleteRelationBook")
    public BusinessResponse<Integer> deleteRelationBook(@RequestBody MetaBookRelationDelReqVO reqVO) {
        MetaBookRelationDelBO convert = KsBeanUtil.convert(reqVO, MetaBookRelationDelBO.class);
        Integer deleteBook = metaBookRelationProvider.deleteBook(convert);
        return BusinessResponse.success(deleteBook);
    }

    @PostMapping("getRelationBook")
    public BusinessResponse<Integer> getRelations(@RequestBody MetaBookRelationAddReqVO reqVO) {
        MetaBookRelationDelBO convert = KsBeanUtil.convert(reqVO, MetaBookRelationDelBO.class);
        Integer deleteBook = metaBookRelationProvider.deleteBook(convert);
        return BusinessResponse.success(deleteBook);
    }
}
