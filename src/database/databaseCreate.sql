CREATE DATABASE bwsBilling;

USE bwsBilling;

CREATE TABLE `billing_cycle_group` (
  `billing_cycle_group_id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `billing_cycle_group_desc` VARCHAR(45) NOT NULL
);

CREATE TABLE `customer` (
  `customer_id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `first_name` VARCHAR(45),
  `last_name` VARCHAR(45),
  `company_name` VARCHAR(45),
  `address1` VARCHAR(45) NOT NULL,
  `address2` VARCHAR(45),
  `city` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `zipcode` VARCHAR(45) NOT NULL,
  `alt_address1` VARCHAR(45),
  `alt_address2` VARCHAR(45),
  `alt_city` VARCHAR(45),
  `alt_state` VARCHAR(45),
  `alt_zipcode` VARCHAR(45),
  `comment` VARCHAR(255),
  `rental_charge` DECIMAL(6,2) NOT NULL,
  `billing_cycle_id` INTEGER UNSIGNED NOT NULL,
  `install_date` DATE NOT NULL,
  `creation_date` DATETIME NOT NULL,
  `last_update_stamp` DATETIME NOT NULL,
  `close_date` DATE,
  `billing_start_month_id` INTEGER NOT NULL,
    FOREIGN KEY `FK_customer_1` (`billing_cycle_id`)
    REFERENCES `billing_cycle` (`billing_cycle_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
)
COMMENT = 'Customer Information';

CREATE TABLE `invoice` (
  `invoice_id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `customer_id` INTEGER UNSIGNED NOT NULL,
  `create_date` DATETIME NOT NULL,
  `due_date` DATE  NOT NULL,
  `paid_amount` DECIMAL(6,2) NOT NULL,
    FOREIGN KEY `FK_invoice_1` (`customer_id`)
    REFERENCES `customer` (`customer_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);


CREATE TABLE `invoice_line_item` (
  `invoice_line_item_id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `invoice_id` INTEGER UNSIGNED NOT NULL,
  `item_desc` VARCHAR(100) NOT NULL,
  `charge_amt` DECIMAL(6,2) NOT NULL,
    FOREIGN KEY `FK_invoice_line_item_1` (`invoice_id`)
    REFERENCES `invoice` (`invoice_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

GRANT ALL PRIVILEGES ON *.* TO 'bws_admin'@'localhost' 
IDENTIFIED BY 'bws_admin' WITH GRANT OPTION;

GRANT SELECT,INSERT,UPDATE,DELETE ON bwsbilling.* TO 'bws_appl'@'localhost'
IDENTIFIED BY 'bws_appl';

INSERT INTO billing_cycle_group(billing_cycle_group_id, billing_cycle_group_desc)
VALUES (100, 'Annual');

INSERT INTO billing_cycle_group(billing_cycle_group_id, billing_cycle_group_desc)
VALUES (200, 'Semi-Annual');

INSERT INTO billing_cycle_group(billing_cycle_group_id, billing_cycle_group_desc)
VALUES (300, 'Quarterly');

INSERT INTO billing_cycle_group(billing_cycle_group_id, billing_cycle_group_desc)
VALUES (400, 'Monthly');
