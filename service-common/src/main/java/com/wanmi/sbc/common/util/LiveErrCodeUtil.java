package com.wanmi.sbc.common.util;

import java.util.HashMap;
import java.util.Map;

public class LiveErrCodeUtil {

    public static String getErrCodeMessage(Integer errCode){

        Map<Integer,String> message = new HashMap<>();
        message.put(300001,"禁止创建/更新商品 或 禁止编辑&更新房间");
        message.put(300006,"图片上传失败（如：无图片或mediaID过期）");
        message.put(300022,"此房间号不存在");
        message.put(300023,"房间状态 拦截（当前房间状态不允许此操作）");
        message.put(300024,"商品不存在");
        message.put(300025,"商品审核未通过");
        message.put(300015,"商品不存在无法删除");
        message.put(300026,"房间商品数量已经满额");
        message.put(300027,"导入商品失败");
        message.put(300028,"房间名称违规");
        message.put(300029,"主播昵称违规");
        message.put(300030,"主播微信号不合法");
        message.put(300031,"直播间封面图不合规");
        message.put(300032,"直播间分享图违规");
        message.put(300033,"添加商品超过直播间上限");
        message.put(300034,"主播微信昵称长度不符合要求");
        message.put(300035,"主播微信号不存在");
        message.put(300036,"主播微信号未实名认证");
        message.put(300002,"名称长度不符合规则");
        message.put(300003,"价格输入不合规（如：现价比原价大、传入价格非数字等）");
        message.put(300004,"商品名称存在违规违法内容");
        message.put(300005,"商品图片存在违规违法内容");
        message.put(300008,"添加商品失败");
        message.put(300009,"商品审核撤回失败");
        message.put(300010,"商品审核状态不对（如：商品审核中）");
        message.put(300011,"操作非法（API不允许操作非API创建的商品）");
        message.put(300012,"没有提审额度（每天500次提审额度）");
        message.put(300013,"提审失败");
        message.put(300014,"审核中，无法删除（非零代表失败）");
        message.put(300017,"商品未提审");
        message.put(300021,"商品添加成功，审核失败");
        message.put(10001,"小程序直播已关闭，如需使用相关功能请开启!");
        message.put(-1,"微信系统错误!");

        return message.get(errCode);
    }
}
