
####################兼容历史数据，需要全部表全部迁移完之后执行##################################################################
## 更新客户和店铺的绑定的时间
update replay_store_customer_rela a join customer_and_level b on(a.customer_id=b.customer_id and a.store_id = b.store_id) set a.create_time = b.bind_time;
# 更新客户表的check_time
update replay_customer a join customer b on(a.customer_id= b.id) set a.check_time=b.check_date;


ALTER TABLE `replay_customer`
ADD INDEX `check_time_index`(`check_time`) USING BTREE;

ALTER TABLE `replay_trade`
ADD INDEX `create_time_index`(`create_time`) USING BTREE;

ALTER TABLE `s2b_statistics`.`replay_trade`
ADD INDEX `pay_state`(`pay_state`) USING BTREE;

ALTER TABLE `s2b_statistics`.`replay_trade`
ADD UNIQUE INDEX `tid_index`(`tid`);


ALTER TABLE `s2b_statistics`.`replay_trade_item`
ADD INDEX `sku_id_index`(`sku_id`) USING BTREE;