/*============================================================================*/
/* DBMS: MySQL 5*/
/* Created on : 02/08/2019 15:28:19                                           */
/*============================================================================*/


/*============================================================================*/
/*                                  TABLES                                    */
/*============================================================================*/
CREATE TABLE `T_CATEGORY` ( 
  `ID`                            VARCHAR(10) NOT NULL,
  `NAME`                          VARCHAR(40),
  `DESCRIPTION`                   VARCHAR(255),
CONSTRAINT `PK_T_CATEGORY` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_PRODUCT` ( 
  `ID`         VARCHAR(10) NOT NULL,
  `NAME`       VARCHAR(40),
  `DESCRIPTION` VARCHAR(255),
  `CATEGORY_FK` VARCHAR(10),
CONSTRAINT `PK_T_PRODUCT` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_ITEM` ( 
  `ID`         VARCHAR(10) NOT NULL,
  `NAME`       VARCHAR(40),
  `UNIT_COST`  DECIMAL(8,2),             
  `PRODUCT_FK` VARCHAR(10),
  `IMAGE_PATH` VARCHAR(255),
CONSTRAINT `PK_T_ITEM` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_USER` ( 
  `ID`         VARCHAR(10) NOT NULL,
  `FIRSTNAME`  VARCHAR(40) NOT NULL,
  `LASTNAME`   VARCHAR(40) NOT NULL,
  `TELEPHONE`  VARCHAR(10),
  `STREET1`    VARCHAR(50),
  `STREET2`    VARCHAR(50),
  `CITY`       VARCHAR(25),
  `STATE`      VARCHAR(25),
  `ZIPCODE`    VARCHAR(10),
  `COUNTRY`    VARCHAR(25),
  `CREDIT_CARD_NUMBER` VARCHAR(25),
  `CREDIT_CARD_TYPE` VARCHAR(25),
  `CREDIT_CARD_EXPIRY_DATE` VARCHAR(10),
  `EMAIL`      VARCHAR(255),
  `ROLE`       VARCHAR(20),
CONSTRAINT `PK_T_USER` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_COUNTER` ( 
  `NAME`       VARCHAR(10) NOT NULL,
  `VALUE`      INT,
CONSTRAINT `PK_T_COUNTER` PRIMARY KEY (`NAME`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_ORDER` ( 
  `ID`         BIGINT NOT NULL,
  `ORDER_DATE` TIMESTAMP(2) NOT NULL,
  `FIRSTNAME`  VARCHAR(50) NOT NULL,
  `LASTNAME`   VARCHAR(50) NOT NULL,
  `STREET1`    VARCHAR(50) NOT NULL,
  `STREET2`    VARCHAR(50),
  `CITY`       VARCHAR(25) NOT NULL,
  `STATE`      VARCHAR(25),
  `ZIPCODE`    VARCHAR(10) NOT NULL,
  `COUNTRY`    VARCHAR(25) NOT NULL,
  `CREDIT_CARD_NUMBER` VARCHAR(25),
  `CREDIT_CARD_TYPE` VARCHAR(25),
  `CREDIT_CARD_EXPIRY_DATE` VARCHAR(10),
  `USER_FK`    VARCHAR(10),
CONSTRAINT `PK_T_ORDER` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_ORDER_LINE` ( 
  `ID`         BIGINT NOT NULL,
  `QUANTITY`   INT NOT NULL,
  `UNIT_COST`  DECIMAL(8,2),
  `ITEM_FK`    VARCHAR(10),
  `ORDER_FK`   BIGINT,
CONSTRAINT `PK_T_ORDER_LINE` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

/*============================================================================*/
/*                               FOREIGN KEYS                                 */
/*============================================================================*/
ALTER TABLE `T_PRODUCT`
    ADD CONSTRAINT `FK_CATEGORY_FK`
        FOREIGN KEY (`CATEGORY_FK`)
            REFERENCES `T_CATEGORY` (`ID`)
 ;
 

ALTER TABLE `T_ITEM`
    ADD CONSTRAINT `FK_PRODUCT_FK`
        FOREIGN KEY (`PRODUCT_FK`)
            REFERENCES `T_PRODUCT` (`ID`)
 ;


ALTER TABLE `T_ORDER`
    ADD CONSTRAINT `FK_USER_FK`
        FOREIGN KEY (`USER_FK`)
            REFERENCES `T_USER` (`ID`)
 ;
 

ALTER TABLE `T_ORDER_LINE`
    ADD CONSTRAINT `FK_ITEM_FK`
        FOREIGN KEY (`ITEM_FK`)
            REFERENCES `T_ITEM` (`ID`)
 ;
 
ALTER TABLE `T_ORDER_LINE`
    ADD CONSTRAINT `FK_ORDER_FK`
        FOREIGN KEY (`ORDER_FK`)
            REFERENCES `T_ORDER` (`ID`)
 ;
  
 
-- Cleanup
DELETE FROM T_ORDER_LINE;
DELETE FROM T_ORDER;
DELETE FROM T_USER;
DELETE FROM T_ITEM;
DELETE FROM T_PRODUCT;
DELETE FROM T_CATEGORY;
DELETE FROM T_COUNTER;

-- Load
INSERT INTO T_CATEGORY VALUES ('FISH', 'Fish', 'Any of numerous cold-blooded aquatic vertebrates characteristically having fins, gills, and a streamlined body' );
INSERT INTO T_CATEGORY VALUES ('DOGS', 'Dogs', 'A domesticated carnivorous mammal related to the foxes and wolves and raised in a wide variety of breeds' );
INSERT INTO T_CATEGORY VALUES ('REPTILES', 'Reptiles', 'Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle' );
INSERT INTO T_CATEGORY VALUES ('CATS', 'Cats', ' Small carnivorous mammal domesticated since early times as a catcher of rats and mice and as a pet and existing in several distinctive breeds and varieties' );
INSERT INTO T_CATEGORY VALUES ('BIRDS', 'Birds', 'Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings' );

INSERT INTO T_PRODUCT VALUES ('FISW01', 'Angelfish', 'Saltwater fish from Australia', 'FISH');
INSERT INTO T_PRODUCT VALUES ('FISW02', 'Tiger Shark', 'Saltwater fish from Australia', 'FISH');
INSERT INTO T_PRODUCT VALUES ('FIFW01', 'Koi', 'Freshwater fish from Japan', 'FISH');
INSERT INTO T_PRODUCT VALUES ('FIFW02', 'Goldfish', 'Freshwater fish from China', 'FISH');

INSERT INTO T_PRODUCT VALUES ('K9BD01', 'Bulldog', 'Friendly dog from England', 'DOGS');
INSERT INTO T_PRODUCT VALUES ('K9PO02', 'Poodle', 'Cute dog from France', 'DOGS');
INSERT INTO T_PRODUCT VALUES ('K9DL01', 'Dalmation', 'Great dog for a fire station', 'DOGS');
INSERT INTO T_PRODUCT VALUES ('K9RT01', 'Golden Retriever', 'Great family dog', 'DOGS');
INSERT INTO T_PRODUCT VALUES ('K9RT02', 'Labrador Retriever', 'Great hunting dog', 'DOGS');
INSERT INTO T_PRODUCT VALUES ('K9CW01', 'Chihuahua', 'Great companion dog', 'DOGS');

INSERT INTO T_PRODUCT VALUES ('RPSN01', 'Rattlesnake', 'Doubles as a watch dog', 'REPTILES');
INSERT INTO T_PRODUCT VALUES ('RPLI02', 'Iguana', 'Friendly green friend', 'REPTILES');

INSERT INTO T_PRODUCT VALUES ('FLDSH01', 'Manx', 'Great for reducing mouse populations', 'CATS');
INSERT INTO T_PRODUCT VALUES ('FLDLH02', 'Persian', 'Friendly house cat, doubles as a princess', 'CATS');

INSERT INTO T_PRODUCT VALUES ('AVCB01', 'Amazon Parrot', 'Great companion for up to 75 years', 'BIRDS');
INSERT INTO T_PRODUCT VALUES ('AVSB02', 'Finch', 'Great stress reliever', 'BIRDS');

INSERT INTO T_ITEM  VALUES ('EST1', 'Large', '10.00', 'FISW01', 'fish1.jpg');
INSERT INTO T_ITEM  VALUES ('EST2', 'Thootless', '10.00', 'FISW01', 'fish1.jpg');
INSERT INTO T_ITEM  VALUES ('EST3', 'Spotted', '12.00', 'FISW02', 'fish4.jpg');
INSERT INTO T_ITEM  VALUES ('EST4', 'Spotless', '12.00', 'FISW02', 'fish4.jpg');
INSERT INTO T_ITEM  VALUES ('EST5', 'Male Adult', '12.00', 'FIFW01', 'fish3.jpg');
INSERT INTO T_ITEM  VALUES ('EST6', 'Female Adult', '12.00', 'FIFW01', 'fish3.jpg');
INSERT INTO T_ITEM  VALUES ('EST7', 'Male Puppy', '12.00', 'FIFW02', 'fish2.jpg');
INSERT INTO T_ITEM  VALUES ('EST8', 'Female Puppy', '12.00', 'FIFW02', 'fish2.jpg');

INSERT INTO T_ITEM  VALUES ('EST9', 'Spotless Male Puppy', '22.00', 'K9BD01', 'dog1.jpg');
INSERT INTO T_ITEM  VALUES ('EST10', 'Spotless Female Puppy', '22.00', 'K9BD01', 'dog1.jpg');
INSERT INTO T_ITEM  VALUES ('EST11', 'Spotted Male Puppy', '32.00', 'K9PO02', 'dog2.jpg');
INSERT INTO T_ITEM  VALUES ('EST12', 'Spotted Female Puppy', '32.00', 'K9PO02', 'dog2.jpg');
INSERT INTO T_ITEM  VALUES ('EST13', 'Tailed', '62.00', 'K9DL01', 'dog3.jpg');
INSERT INTO T_ITEM  VALUES ('EST14', 'Tailless', '62.00', 'K9DL01', 'dog3.jpg');
INSERT INTO T_ITEM  VALUES ('EST15', 'Tailed', '82.00', 'K9RT01', 'dog4.jpg');
INSERT INTO T_ITEM  VALUES ('EST16', 'Tailless', '82.00', 'K9RT01', 'dog4.jpg');
INSERT INTO T_ITEM  VALUES ('EST17', 'Tailed', '100.00', 'K9RT02', 'dog5.jpg');
INSERT INTO T_ITEM  VALUES ('EST18', 'Tailless', '100.00', 'K9RT02', 'dog5.jpg');
INSERT INTO T_ITEM  VALUES ('EST19', 'Female Adult', '100.00', 'K9CW01', 'dog6.jpg');
INSERT INTO T_ITEM  VALUES ('EST20', 'Female Adult', '110.00', 'K9CW01', 'dog6.jpg');

INSERT INTO T_ITEM  VALUES ('EST21', 'Female Adult', '20.00', 'RPSN01', 'snake1.jpg');
INSERT INTO T_ITEM  VALUES ('EST22', 'Male Adult', '20.00', 'RPSN01', 'snake1.jpg');
INSERT INTO T_ITEM  VALUES ('EST211', 'Female Adult', '20.00', 'RPLI02', 'lizard1.jpg');
INSERT INTO T_ITEM  VALUES ('EST221', 'Male Adult', '20.00', 'RPLI02', 'lizard1.jpg');

INSERT INTO T_ITEM  VALUES ('EST23', 'Male Adult', '120.00','FLDSH01', 'cat1.jpg');
INSERT INTO T_ITEM  VALUES ('EST24', 'Female Adult', '120.00', 'FLDSH01', 'cat1.jpg');
INSERT INTO T_ITEM  VALUES ('EST231', 'Male Adult', '75.00','FLDLH02', 'cat2.jpg');
INSERT INTO T_ITEM  VALUES ('EST241', 'Female Adult', '80.00', 'FLDLH02', 'cat2.jpg');

INSERT INTO T_ITEM  VALUES ('EST25', 'Male Adult', '120.00', 'AVCB01', 'bird2.jpg');
INSERT INTO T_ITEM  VALUES ('EST26', 'Female Adult', '120.00', 'AVCB01', 'bird2.jpg');
INSERT INTO T_ITEM  VALUES ('EST27', 'Male Adult', '80.00', 'AVSB02', 'bird1.jpg');
INSERT INTO T_ITEM  VALUES ('EST28', 'Female Adult', '70.00', 'AVSB02', 'bird1.jpg');

INSERT INTO T_USER VALUES ('bill000', 'Bill', 'Gates', '654 046 12', '27 West Side', 'Story', 'Alabama', 'Texas', '8401', 'USA', '1231 4564 2222', 'Visa', '02/14', 'bill.gates@microsoft.com','ROLE_USER');
INSERT INTO T_USER VALUES ('job5', 'Steve', 'Jobs', '548 157 15', '154 Star Boulevard', '', 'San Francisco', 'WC', '5455', 'USA', '', '', '', 'steve.jobs@apple.com','ROLE_USER');
INSERT INTO T_USER VALUES ('stb01', 'Stéphane', 'Bruyère', '', '', '', '', '', '', '', '', '', '', 'stb@gmail.com','ROLE_ADMIN');
INSERT INTO T_USER VALUES ('marco01', 'Marc', 'Zurckerberg', '', '', '', '', '', '', '', '', '', '', 'mz@facebook.com','ROLE_FRANCHISEE');
INSERT INTO T_USER VALUES ('jeff01', 'Jeff', 'Bezos', '', '', '', '', '', '', '', '', '', '', 'jeff@amazon.com','ROLE_FRANCHISEE');
