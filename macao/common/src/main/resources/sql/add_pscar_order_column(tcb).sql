ALTER TABLE `spcar_order`
ADD COLUMN `tbc`  int(4) NULL DEFAULT 0 COMMENT '司机是否确认订单0否1是' AFTER `refund_money`;