ALTER TABLE `yinghai_taxigo`.`spcar_order`
  ADD COLUMN `driver_setout_time` DATETIME NULL COMMENT '司机出发时间' AFTER `end_address`;