
CREATE TABLE `parameter` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`hour`  int(1) NOT NULL COMMENT '小时' ,
`price`  int(32) NOT NULL COMMENT '价钱' ,
`coefficient`  decimal(10,0) NULL COMMENT '系数' ,
PRIMARY KEY (`id`)
);
ALTER TABLE `parameter`
MODIFY COLUMN `coefficient`  double(10,2) NOT NULL COMMENT '系数' AFTER `price`;
