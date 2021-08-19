DROP INDEX bank_no_UNIQUE ON b2b.offline_account;

ALTER TABLE b2b.customer_invoice MODIFY taxpayer_identification_img VARCHAR(200) COMMENT '一般纳税人认证资格复印件';
ALTER TABLE b2b.customer_invoice MODIFY business_license_img VARCHAR(200) COMMENT '营业执照复印件';

# 开票项默认明细选项
INSERT INTO b2b.invoice_project (`project_id`,`project_name`,`del_flag`,`create_time`,`update_time`,`operate_person`)
VALUES ('00000000000000000000000000000000','明细',0,'2099-01-01 00:00:00',NULL,NULL);


ALTER TABLE b2b.offline_account MODIFY account_name VARCHAR(150) COMMENT '账户名称', MODIFY bank_name VARCHAR(150) COMMENT '开户银行';

# 长度加长
ALTER TABLE b2b.refund_order MODIFY refuse_reason VARCHAR(200) COMMENT '拒绝原因';
ALTER TABLE b2b.refund_bill MODIFY refund_comment VARCHAR(200);

ALTER TABLE b2b.order_invoice ADD COLUMN company_info_id BIGINT(20) COMMENT '商家id' after order_no;
ALTER TABLE b2b.order_invoice ADD COLUMN store_id BIGINT(20) COMMENT '店铺id' after company_info_id;

##订单开票表增加纳税人识别号字段
ALTER TABLE order_invoice ADD taxpayer_number VARCHAR(20) NULL COMMENT '纳税人识别号';