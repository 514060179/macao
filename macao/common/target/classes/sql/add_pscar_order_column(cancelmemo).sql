
ALTER TABLE `yinghai_taxigo`.`spcar_order`   
  ADD COLUMN `cancelmemo` VARCHAR(1024) NULL COMMENT '取消订单原因' AFTER `tbc`;