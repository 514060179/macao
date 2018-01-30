ALTER TABLE `yinghai_taxigo`.`spcar_manager`
  ADD COLUMN `im_name` VARCHAR(255) NULL COMMENT 'im账号' AFTER `remark`,
  ADD COLUMN `sign` VARCHAR(500) NULL COMMENT 'im登录签名' AFTER `im_name`;