-- Ensure all statements have two empty lines between them, otherwise the file reader won't read them correctly


CREATE TABLE Customers(
    ID INTEGER NOT NULL AUTO_INCREMENT,
    FirstName VARCHAR(1000),
    LastName VARCHAR(1000),
    Identification VARCHAR(100),
    Phone VARCHAR(100),
    Email VARCHAR(1000),
    PRIMARY KEY (ID)
);


CREATE TABLE Products(
    ID INTEGER NOT NULL AUTO_INCREMENT,
    Name VARCHAR(1000) NOT NULL,
    Size VARCHAR(1000),
    Quantity INTEGER,
    Price DOUBLE,
    IsActive BOOL,
    BundleOnly BOOL,
    PRIMARY KEY(ID)
);


CREATE TABLE Bundles(
    ID INTEGER NOT NULL AUTO_INCREMENT,
    Name VARCHAR(1000) NOT NULL,
    Products VARCHAR(1000),
    PRIMARY KEY(ID)
);


CREATE TABLE Transactions(
    ID INT NOT NULL AUTO_INCREMENT,
    CustID INT NOT NULL,
    Checkout DATETIME,
    ExpectedReturn DATETIME,
    Subtotal INT,
    IsActive BOOL,
    PRIMARY KEY(ID)
);


CREATE TABLE LineItem(
    ID INT NOT NULL AUTO_INCREMENT,
    CustID INT NOT NULL,
    Checkout DATETIME,
    ExpectedReturn DATETIME,
    Subtotal INT,
    IsActive BOOL,
    PRIMARY KEY(ID)
);