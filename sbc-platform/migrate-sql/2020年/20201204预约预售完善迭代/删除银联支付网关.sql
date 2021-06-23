-- 银联支付存在问题，暂时关闭入口，考虑到数据关联性，不做物理删除，只改所属店铺id
use `sbc-pay`;
update pay_gateway set store_id=-100 where name = 'UNIONB2B' and store_id=-1;