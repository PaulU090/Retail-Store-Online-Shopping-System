DROP TABLE IF EXISTS Store CASCADE;
DROP TABLE IF EXISTS Product CASCADE;
DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Warehouse CASCADE;
DROP TABLE IF EXISTS Orders CASCADE;
DROP TABLE IF EXISTS ProductSupplyRequests CASCADE;
DROP TABLE IF EXISTS ProductUpdates CASCADE;

CREATE TABLE Users ( userID serial,
                     name char(50) NOT NULL,
                     password char(11) NOT NULL,    
		     latitude decimal(8,6) NOT NULL CHECK(latitude >= 0 AND latitude <= 100), -- check ensures that new registering users or updated user information latitude is within location constraints
                     longitude decimal(9,6) NOT NULL CHECK(longitude >= 0 AND longitude <= 100), -- check ensures that new registering users or updated user information longitude is within location constraints 
                     type char(10) NOT NULL, -- type can be 'customer', 'manager', 'admin' 
                     PRIMARY KEY(userID)
);


CREATE TABLE Store ( storeID integer, 
                     name char(30) NOT NULL,
                     latitude decimal(8, 6) NOT NULL,
                     longitude decimal(9, 6) NOT NULL,
                     managerID integer NOT NULL,
		     dateEstablished date,
		     PRIMARY KEY(storeID), 
                     FOREIGN KEY(managerID) REFERENCES Users(userID)
);

CREATE TABLE Product ( storeID integer NOT NULL, 
                       productName char(30) NOT NULL,
                       numberOfUnits integer NOT NULL CHECK(numberOfUnits >= 0), -- check ensures that any purchases or updates made to product number of units does not allow a larger number of products to be purchased than what is in stock or updated below zero
                       pricePerUnit float NOT NULL CHECK(numberOfUnits >= 0), -- check ensures that any updates made to product price does not allow the price to be changed below zero 
                       PRIMARY KEY(storeID, productName), 
                       FOREIGN KEY(storeID) REFERENCES Store(storeID)
);

CREATE TABLE Warehouse ( WarehouseID integer,
                         area integer,
                         latitude decimal(8,6) NOT NULL,
                         longitude decimal(9,6)  NOT NULL,
                         PRIMARY KEY(WarehouseID)
);

CREATE TABLE Orders ( orderNumber serial NOT NULL,
		      customerID integer NOT NULL,
                      storeID integer NOT NULL,
                      productName char(30) NOT NULL, 
                      unitsOrdered integer NOT NULL, 
                      orderTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- default current timestamp defaults the value to the current timestamp; value does not need to be inserted when defaulted
                      PRIMARY KEY(orderNumber),
                      FOREIGN KEY(customerID) REFERENCES Users(userID) ON DELETE CASCADE, -- delete cascade ensures that customer deletions deletes all of the associated foreign entries
                      FOREIGN KEY(storeID, productName) REFERENCES Product(storeID, productName) ON UPDATE CASCADE -- update cascade ensures that changes to the productName changes all of the associated foreign keys
);

CREATE TABLE ProductSupplyRequests ( requestNumber serial NOT NULL,
				     managerID integer NOT NULL, --User ID of the Manager who makes the supply request
				     warehouseID integer NOT NULL,
                                     storeID integer NOT NULL,
                                     productName char(30) NOT NULL, 
				     unitsRequested integer NOT NULL,
                                     PRIMARY KEY(requestNumber),
				     FOREIGN KEY(managerID) REFERENCES Users(userID), 
                               	     FOREIGN KEY(warehouseID) REFERENCES Warehouse(warehouseID),
                               	     FOREIGN KEY(storeID, productName) REFERENCES Product(storeID, productName) ON UPDATE CASCADE -- update cascade ensures that changes to the productName changes all of the associated foreign keys
);

CREATE TABLE ProductUpdates ( updateNumber serial,	
			      managerID integer NOT NULL,
                              storeID integer NOT NULL,
                       	      productName char(30) NOT NULL, 
                              updatedOn timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- default current timestamp defaults the value to the current timestamp; value does not need to be inserted when defaulted
                              PRIMARY KEY(updateNumber),
                              FOREIGN KEY(managerID) REFERENCES Users(userID),
                              FOREIGN KEY(storeID, productName) REFERENCES Product(storeID, productName) ON UPDATE CASCADE -- update cascade ensures that changes to the productName changes all of the associated foreign keys
);
