ALTER TABLE `yinghai_taxigo`.`spcar_driver`
  ADD COLUMN `driver_setout_time` DATETIME NULL COMMENT '司机出发时间' AFTER `device_type`,
  ADD COLUMN `driver_arrive_time` DATETIME NULL COMMENT '司机到达时间' AFTER `driver_setout_time`,
  ADD COLUMN `passenger_getin_time` DATETIME NULL COMMENT '乘客到达时间' AFTER `driver_arrive_time`；