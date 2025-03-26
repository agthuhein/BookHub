-- MySQL dump 10.13  Distrib 5.7.24, for osx11.1 (x86_64)
--
-- Host: localhost    Database: BookHub_TestDB
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authors` (
  `author_id` int NOT NULL AUTO_INCREMENT,
  `author_name` varchar(255) NOT NULL,
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors`
--

LOCK TABLES `authors` WRITE;
/*!40000 ALTER TABLE `authors` DISABLE KEYS */;
INSERT INTO `authors` VALUES (1,'George Orwell'),(2,'J.K. Rowling'),(3,'Yuval Noah Harari'),(4,'Daniel Kahneman'),(5,'Jane Austen'),(6,'Harper Lee'),(7,'Michael Sipser'),(8,'Joshua Bloch'),(9,'Henry Kissinger'),(10,'Eric Schmidt'),(11,'Craig Mundie'),(13,'William Shakespeare'),(14,'Sir Arthur Conan Doyle'),(16,'​Haruki Murakami');
/*!40000 ALTER TABLE `authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_authors`
--

DROP TABLE IF EXISTS `book_authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `book_authors` (
  `book_id` int NOT NULL,
  `author_id` int NOT NULL,
  PRIMARY KEY (`book_id`,`author_id`),
  KEY `author_id` (`author_id`),
  CONSTRAINT `book_authors_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE,
  CONSTRAINT `book_authors_ibfk_2` FOREIGN KEY (`author_id`) REFERENCES `authors` (`author_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_authors`
--

LOCK TABLES `book_authors` WRITE;
/*!40000 ALTER TABLE `book_authors` DISABLE KEYS */;
INSERT INTO `book_authors` VALUES (1,1),(2,2),(3,3),(4,4),(5,5),(6,6),(7,7),(8,8),(9,9),(9,10),(9,11);
/*!40000 ALTER TABLE `book_authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `books` (
  `book_id` int NOT NULL AUTO_INCREMENT,
  `isbn` varchar(13) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock_quantity` int NOT NULL,
  `published_date` date DEFAULT NULL,
  `publisher_id` int DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`book_id`),
  UNIQUE KEY `isbn` (`isbn`),
  KEY `publisher_id` (`publisher_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `books_ibfk_1` FOREIGN KEY (`publisher_id`) REFERENCES `publishers` (`publisher_id`) ON DELETE RESTRICT,
  CONSTRAINT `books_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'9780451524935','1984','A dystopian novel set in a totalitarian society under constant surveillance.',15.00,45,'1949-06-08',1,1,'2025-03-21 20:35:44','2025-03-23 11:21:24'),(2,'9780747532743','Harry Potter and the Philosopher\'s Stone','A young wizard discovers his magical heritage and attends Hogwarts.',25.00,96,'1997-06-26',2,2,'2025-03-21 20:35:44','2025-03-23 11:21:24'),(3,'9780062316097','Sapiens: A Brief History of Humankind','A deep dive into the history and impact of Homo sapiens.',30.00,67,'2011-06-04',3,3,'2025-03-21 20:35:44','2025-03-23 11:21:24'),(4,'9780307277671','Thinking, Fast and Slow','Exploring two systems of thinking: fast, intuitive, and slow, rational.',22.00,50,'2011-10-25',4,4,'2025-03-21 20:35:44','2025-03-21 20:35:44'),(5,'9780141439563','Pride and Prejudice','A classic romance novel about the manners and matrimonial machinations of British society.',12.00,100,'1813-01-28',5,5,'2025-03-21 20:35:44','2025-03-21 20:35:44'),(6,'9780060850524','To Kill a Mockingbird','A novel dealing with serious issues like racial injustice and moral growth.',20.00,120,'1960-07-11',6,6,'2025-03-21 20:35:44','2025-03-21 20:35:44'),(7,'9780262033848','Introduction to the Theory of Computation','A comprehensive book on computational theory and automata.',50.00,200,'2006-03-01',7,7,'2025-03-21 20:35:44','2025-03-21 20:35:44'),(8,'9780134685991','Effective Java','Best practices for Java programming, covering coding techniques and principles.',40.00,100,'2018-01-06',8,8,'2025-03-21 20:35:44','2025-03-21 20:35:44'),(9,'9781399819114','Genesis: Artificial Intelligence, Hope, and the Human Spirit','A collaborative exploration of AI\'s potential and its profound implications for humanity.',28.00,250,'2024-11-19',9,9,'2025-03-21 20:35:44','2025-03-21 20:35:44');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Dystopian'),(2,'Fantasy'),(3,'History'),(4,'Psychology'),(5,'Romance'),(6,'Classic'),(7,'Computer Science'),(8,'Programming'),(9,'Technology');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_items` (
  `order_item_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `book_id` int NOT NULL,
  `order_quantity` int NOT NULL,
  `price_per_unit` decimal(38,2) NOT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `order_id` (`order_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE RESTRICT,
  CONSTRAINT `order_items_chk_1` CHECK ((`order_quantity` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1,1,1,15.00),(2,1,2,1,25.00),(3,1,3,1,30.00),(7,3,1,2,15.00),(8,3,2,1,25.00),(9,3,3,5,30.00);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `order_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `total_amount` decimal(38,2) NOT NULL,
  `order_status` varchar(255) NOT NULL,
  `payment_method` varchar(255) NOT NULL,
  `shipping_address` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,2,'2025-03-21 21:08:46',70.00,'pending','CREDIT CARD','{\"zip\": \"10711\", \"city\": \"Berlin\", \"street\": \"Karslruher Str. 14\"}','2025-03-21 21:08:46','2025-03-21 21:08:46'),(3,3,'2025-03-23 11:21:24',205.00,'confirmed','PayPal','{\"zip\": \"10711\", \"city\": \"Berlin\", \"street\": \"Karslruher Str. 14\"}','2025-03-23 11:21:24','2025-03-23 11:22:11');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `prevent_duplicate_cancellation` BEFORE UPDATE ON `orders` FOR EACH ROW BEGIN
    IF OLD.order_status = 'cancelled' AND NEW.order_status = 'cancelled' THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'This order has already been cancelled.';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `update_book_quantity_after_order_cancel` AFTER UPDATE ON `orders` FOR EACH ROW BEGIN
    IF OLD.order_status != 'cancelled' AND NEW.order_status = 'cancelled' THEN
        
        UPDATE books b
        JOIN (
            SELECT book_id, SUM(order_quantity) AS total_quantity
            FROM order_items
            WHERE order_id = OLD.order_id
            GROUP BY book_id
        ) oi ON b.book_id = oi.book_id
        SET b.stock_quantity = b.stock_quantity + oi.total_quantity;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `publishers`
--

DROP TABLE IF EXISTS `publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `publishers` (
  `publisher_id` int NOT NULL AUTO_INCREMENT,
  `publisher_name` varchar(255) NOT NULL,
  PRIMARY KEY (`publisher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publishers`
--

LOCK TABLES `publishers` WRITE;
/*!40000 ALTER TABLE `publishers` DISABLE KEYS */;
INSERT INTO `publishers` VALUES (1,'Secker & Warburg'),(2,'Bloomsbury'),(3,'Harper'),(4,'Farrar, Straus and Giroux'),(5,'T. Egerton'),(6,'J.B. Lippincott & Co.'),(7,'MIT Press'),(8,'Addison-Wesley\"'),(9,'John Murray'),(11,'Penguin Random House');
/*!40000 ALTER TABLE `publishers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reviews` (
  `id` varchar(255) NOT NULL,
  `book_id` int DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `review_posted` datetime(6) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `role` varchar(10) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Myat','Su Mon','$2a$10$L72mmtTVa5JiVgeYacvIZuGcdDumVDtcLlyyIxPr5x66i9Yym4SWy','myat86@gmail.com','+123456789','ADMIN','2025-03-21 15:42:42','2025-03-21 15:42:42'),(2,'Aung','Thu Hein','$2a$10$6SZKKV7hY1HPevBPe69v3.5Zn2NkzGQILPwqAC1txJTAWMs4yFxi6','agthuhein.mm@gmail.com','+49 1234567890','USER','2025-03-21 15:46:16','2025-03-21 15:46:16'),(3,'Aung Thu','Hein','$2a$10$aBuVIMcX0lF9lKHz0PU71ODeEaeB8I4bUuzRT0qOBD8AYamAO2eMC','aung.thu@gisma-student.com','+49 1234567890','USER','2025-03-23 11:19:28','2025-03-23 11:19:28');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'BookHub_TestDB'
--
/*!50003 DROP PROCEDURE IF EXISTS `AddOrder` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddOrder`(
    IN p_user_id INTEGER,
    IN p_order_status VARCHAR(50),
    IN p_payment_method VARCHAR(50),
    IN p_shipping_address JSON,
    IN p_order_items JSON,
    OUT p_new_order_id INT
)
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE item_count INT;
    DECLARE book_id_value INT;
    DECLARE order_quantity INT;
    DECLARE price_per_unit DECIMAL(10,2);
    DECLARE available_stock INT;
    DECLARE total_amount DECIMAL(10,2) DEFAULT 0;
    DECLARE success_flag BOOLEAN DEFAULT TRUE;

    
    START TRANSACTION;

    
    SET item_count = JSON_LENGTH(p_order_items);

    
    WHILE i < item_count DO
        SET book_id_value = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_order_items, CONCAT('$[', i, '].book_id'))) AS UNSIGNED);
        SET order_quantity = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_order_items, CONCAT('$[', i, '].quantity'))) AS UNSIGNED);

        
        SELECT price, stock_quantity INTO price_per_unit, available_stock 
        FROM books WHERE book_id = book_id_value;

        
        IF available_stock IS NULL THEN
            SET success_flag = FALSE;
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Book does not exist';
        ELSEIF available_stock = 0 THEN
            SET success_flag = FALSE;
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Book is out of stock';
        ELSEIF available_stock < order_quantity THEN
            SET success_flag = FALSE;
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Not enough stock available';
        ELSE
            
            SET total_amount = total_amount + (price_per_unit * order_quantity);
        END IF;

        
        SET i = i + 1;
    END WHILE;

    
    IF success_flag THEN
        
        INSERT INTO orders (user_id, total_amount, order_status, payment_method, shipping_address)
        VALUES (p_user_id, total_amount, p_order_status, p_payment_method, p_shipping_address);

        
        SET p_new_order_id = LAST_INSERT_ID();

        
        SET i = 0;
        WHILE i < item_count DO
            SET book_id_value = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_order_items, CONCAT('$[', i, '].book_id'))) AS UNSIGNED);
            SET order_quantity = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_order_items, CONCAT('$[', i, '].quantity'))) AS UNSIGNED);

            
            SELECT price INTO price_per_unit FROM books WHERE book_id = book_id_value;

            
            INSERT INTO order_items (order_id, book_id, order_quantity, price_per_unit)
            VALUES (p_new_order_id, book_id_value, order_quantity, price_per_unit);

            
            UPDATE books 
            SET stock_quantity = stock_quantity - order_quantity
            WHERE book_id = book_id_value;

            
            SET i = i + 1;
        END WHILE;

        
        COMMIT;
    ELSE
        
        SET p_new_order_id = 0;
    END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-26 17:52:13
