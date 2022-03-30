CREATE TABLE book
(
   id        INT AUTO_INCREMENT,
   title     VARCHAR(200),
   price   INT,
   PRIMARY KEY (id)
);

CREATE TABLE book_discount_details
(
   id                INT AUTO_INCREMENT,
   different_copies   INT,
   discount          INT,
   PRIMARY KEY (id)
);


 