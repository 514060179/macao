ALTER TABLE `yinghai_taxigo`.`spcar_order`
  ADD COLUMN `driver_arrive_time` DATETIME NULL COMMENT '司机到达时间' AFTER `end_address`,
  ADD COLUMN `complete_time` DATETIME NULL COMMENT '完成时间' AFTER `driver_arrive_time`;