CREATE TABLE `bwsbilling`.`payment` (
  `payment_id` INTEGER UNSIGNED NOT NULL DEFAULT 0 AUTO_INCREMENT PRIMARY KEY,
  `invoice_id` INTEGER UNSIGNED NOT NULL DEFAULT 0,
  `payment_amt` DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  `payment_type` VARCHAR(2) NOT NULL,
  `applied_date` DATE NOT NULL
)
COMMENT = 'This table will record the payments that are applied to the invoices by the customer';

CREATE TABLE `bwsbilling`.`pending_services` (
  `pending_service_id` INTEGER UNSIGNED NOT NULL DEFAULT 0 AUTO_INCREMENT PRIMARY KEY,
  `customer_id` INTEGER UNSIGNED NOT NULL DEFAULT 0,
  `description` VARCHAR(45) NOT NULL,
  `charge_amt` DOUBLE UNSIGNED NOT NULL
)
COMMENT = 'This table will record the pending services that will be applied the next time the invoice is created for the specific customer';

