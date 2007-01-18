CREATE TABLE customer (
 customerid VARCHAR(8) NOT NULL,
 lastname VARCHAR(24) NOT NULL,
 firstname VARCHAR(24) NOT NULL,
 primary key (customerid)
);

INSERT INTO customer VALUES('SUN00001', 'LastName', 'FirstName');
