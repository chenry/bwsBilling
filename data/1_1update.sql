ALTER TABLE `bwsbilling`.`customer` ADD COLUMN `credit_bal` DECIMAL(6,2) COMMENT 'In case the customer pays more than they owe' AFTER `comment`;

ALTER TABLE `bwsbilling`.`invoice` ADD COLUMN `bill_date` DECIMAL(6,2) NOT NULL COMMENT 'start date of the billing period' AFTER `paid_amount`;

ALTER TABLE `bwsbilling`.`invoice` ADD COLUMN `close_date` DATE  AFTER `bill_date`;

ALTER TABLE `bwsbilling`.`invoice` MODIFY COLUMN `invoice_id` INTEGER UNSIGNED NOT NULL;

update bwsbilling.customer
set customer_id = customer_id + 5134