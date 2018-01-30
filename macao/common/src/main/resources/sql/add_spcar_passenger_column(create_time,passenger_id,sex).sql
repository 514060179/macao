ALTER TABLE `spcar_passenger`
ADD COLUMN `create_time`  datetime NULL AFTER `isgn`;

ALTER TABLE `spcar_passenger`
ADD COLUMN `passenger_id`  int(11) NULL AFTER `create_time`;


ALTER TABLE `spcar_passenger`
ADD COLUMN `sex`  int(3) NULL DEFAULT 0 COMMENT '性别0男1女' AFTER `passenger_id`;




ALTER TABLE `spcar_passenger`
ADD COLUMN `vip`  int(1) NULL DEFAULT 0 COMMENT '是否为VIP，0为否1为是' AFTER `sex`;

ALTER TABLE `taxigouser`
ADD COLUMN `sex`  int(1) NULL COMMENT '性别0男1女' AFTER `sign`,
ADD COLUMN `vip`  int(1) NULL COMMENT '是否为vip0否1是' AFTER `sex`;



