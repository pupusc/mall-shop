
-- 修改基本设置信息表中的会员注册协议字段类型
alter table `sbc-setting`.base_config modify column register_content mediumtext null comment '会员注册协议';

-- 短信模版
update `sbc-message`.`sms_template` set template_code = 'SMS_205458210' where id = 15;
update `sbc-message`.`sms_template` set template_code = 'SMS_205473005' where id = 14;
update `sbc-message`.`sms_template` set template_code = 'SMS_180048827' where id = 8;
update `sbc-message`.`sms_template` set template_code = 'SMS_181862947' where id = 17;

-- 推送节点，金额字段包含人民币符号
update `sbc-message`.push_send_node set node_context = '您有{当天到账优惠券张数}张优惠券到账，优惠总额￥{优惠面值总额}，赶紧花掉他们吧~戳此查看>' where id = 22;
update `sbc-message`.push_send_node set node_context = '您有{当天到账优惠券张数}张优惠券今日到期，优惠总额￥{优惠面值总额}，赶紧花掉他们吧~戳此查看>' where id = 23;
update `sbc-message`.push_send_node set node_context = '您的余额账户今日收入￥{收入金额}，支出￥{支出金额}，如遇异常变动，请及时联系我们哦~点击查看明细>' where id = 28;
update `sbc-message`.push_send_node set node_context = '亲爱的{设置的分销员称呼}，您的推广订单{订单第一行商品名称}已支付成功，预计可赚￥{该笔订单待入账邀新奖励金额}，继续加油哦~点击查看>' where id = 32;
update `sbc-message`.push_send_node set node_context = '亲爱的{设置的分销员称呼}，您邀请的好友{手机号中四位掩码}已成功注册，￥{现金奖励金额}现金奖励，￥{优惠券奖励的总面额}优惠券奖励即将到账，继续加油哦~点击查看>' where id = 33;
update `sbc-message`.push_send_node set node_context = '亲爱的{设置的分销员称呼}，您邀请的好友{手机号中四位掩码}已成功注册，好友成功完成一笔订单就可获得￥{现金奖励金额}现金奖励，￥{优惠券奖励的总面额}优惠券奖励，继续加油哦~点击查看>' where id = 34;
update `sbc-message`.push_send_node set node_context = '亲爱的{设置的分销员称呼}，您邀请的好友{手机号中四位掩码}已成功完成了一笔订单，￥{现金奖励金额}现金奖励，￥{优惠券奖励的总面额}优惠券奖励即将到账，继续加油哦~点击查看>' where id = 35;
-- 短信消息，金额字段包含人民币符号
update `sbc-message`.sms_template set template_content = '您有${number}张优惠券到账，优惠总额￥${money}，赶紧花掉他们吧~' where id = 138;
update `sbc-message`.sms_template set template_content = '您有${number}张优惠券今日到期，优惠总额￥${money}，赶紧花掉他们吧~' where id = 139;
update `sbc-message`.sms_template set template_content = '您的余额账户今日收入￥${money}，支出￥${price}，如遇异常变动，请及时联系我们哦~' where id = 144;
update `sbc-message`.sms_template set template_content = '亲爱的${name}，您的推广订单${product}已支付成功，预计可赚￥${money}，继续加油哦~' where id = 148;
update `sbc-message`.sms_template set template_content = '亲爱的${name}，您邀请的好友${number}已成功注册，￥${money}现金奖励，￥${price}优惠券奖励即将到账，继续加油哦~' where id = 149;
update `sbc-message`.sms_template set template_content = '亲爱的${name}，您邀请的好友${number}已成功注册，好友成功完成一笔订单就可获得￥${money}现金奖励，￥{price}优惠券奖励，继续加油哦~' where id = 150;
update `sbc-message`.sms_template set template_content = '亲爱的${name}，您邀请的好友${number}已成功完成了一笔订单，￥${money}现金奖励，￥${price}优惠券奖励即将到账，继续加油哦~' where id = 151;
-- 站内信通知节点，金额字段包含人民币符号
update `sbc-message`.message_send_node set node_content = '您有{当天到账优惠券张数}张优惠券到账，优惠总额￥{优惠面值总额}，赶紧花掉他们吧~戳此查看>' where id = 22;
update `sbc-message`.message_send_node set node_content = '您有{当天到账优惠券张数}张优惠券今日到期，优惠总额￥{优惠面值总额}，赶紧花掉他们吧~戳此查看>' where id = 23;
update `sbc-message`.message_send_node set node_content = '您的余额账户今日收入￥{收入金额}，支出￥{支出金额}，如遇异常变动，请及时联系我们哦~点击查看明细>' where id = 28;
update `sbc-message`.message_send_node set node_content = '亲爱的{设置的分销员称呼}，您的推广订单{订单第一行商品名称}已支付成功，预计可赚￥{该笔订单待入账邀新奖励金额}，继续加油哦~点击查看>' where id = 32;
update `sbc-message`.message_send_node set node_content = '亲爱的{设置的分销员称呼}，您邀请的好友{手机号中四位掩码}已成功注册，￥{现金奖励金额}现金奖励，￥{优惠券奖励的总面额}优惠券奖励即将到账，继续加油哦~点击查看>' where id = 33;
update `sbc-message`.message_send_node set node_content = '亲爱的{设置的分销员称呼}，您邀请的好友{手机号中四位掩码}已成功注册，好友成功完成一笔订单就可获得￥{现金奖励金额}现金奖励，￥{优惠券奖励的总面额}优惠券奖励，继续加油哦~点击查看>' where id = 34;
update `sbc-message`.message_send_node set node_content = '亲爱的{设置的分销员称呼}，您邀请的好友{手机号中四位掩码}已成功完成了一笔订单，￥{现金奖励金额}现金奖励，￥{优惠券奖励的总面额}优惠券奖励即将到账，继续加油哦~点击查看>' where id = 35;
