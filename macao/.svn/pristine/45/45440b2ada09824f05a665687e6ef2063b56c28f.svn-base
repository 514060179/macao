ALTER TABLE `spcar_order`
ADD COLUMN `cancel_time`  datetime NULL COMMENT '取消订单时间' AFTER `spcar_id`;

ALTER TABLE `spcar_order`
ADD COLUMN `order_id`  int(11) NULL COMMENT '判断是否为续单' AFTER `cancel_time`;


ALTER TABLE `spcar_order`
MODIFY COLUMN `pay_status`  int(11) NULL DEFAULT 0 COMMENT '支付状态：0待付款；1已支付；2申請退款；3已退款；4月结' AFTER `amount`,
ADD COLUMN `monthly`  int(1) NULL DEFAULT 0 COMMENT '是否为月结，0否1是' AFTER `order_id`;




