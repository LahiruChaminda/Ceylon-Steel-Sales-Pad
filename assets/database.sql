DROP TABLE IF EXISTS tbl_item;

DROP TABLE IF EXISTS tbl_category;

DROP TABLE IF EXISTS tbl_customer;

DROP TABLE IF EXISTS tbl_driver;

DROP TABLE IF EXISTS tbl_distributor;

DROP TABLE IF EXISTS tbl_unproductive_call;

DROP TABLE IF EXISTS tbl_outlet;

DROP TABLE IF EXISTS tbl_order_detail;

DROP TABLE IF EXISTS tbl_order;

DROP TABLE IF EXISTS tbl_rep_location;

DROP TABLE IF EXISTS tbl_vehicle;

DROP TABLE IF EXISTS tbl_payment;

DROP TABLE IF EXISTS tbl_invoice;

DROP TABLE IF EXISTS tbl_attendance_history;

CREATE TABLE tbl_category ( 
    categoryId          INTEGER PRIMARY KEY,
    categoryDescription TEXT    NOT NULL 
);

CREATE TABLE tbl_item ( 
    itemId          INTEGER           PRIMARY KEY,
    categoryId      INTEGER           NOT NULL
                                      REFERENCES tbl_category ( categoryId ) ON UPDATE CASCADE,
    itemCode        TEXT              NOT NULL,
    itemDescription TEXT              CHECK ( itemDescription != '' ),
    price           DECIMAL( 50, 2 ) 
);

CREATE TABLE tbl_customer ( 
    customerId   INTEGER NOT NULL
                         PRIMARY KEY,
    customerName TEXT    NOT NULL 
);

CREATE TABLE tbl_vehicle ( 
    vehicleNo TEXT NOT NULL
                   PRIMARY KEY 
);

CREATE TABLE tbl_driver ( 
    driverName TEXT NOT NULL,
    driverNIC  TEXT NOT NULL
                    UNIQUE 
);

CREATE TABLE tbl_distributor ( 
    distributorId   INTEGER NOT NULL
                            PRIMARY KEY,
    distributorName TEXT    NOT NULL 
);

CREATE TABLE tbl_outlet ( 
    outletId   INTEGER NOT NULL
                       PRIMARY KEY,
    outletName TEXT    NOT NULL 
);

CREATE TABLE tbl_order ( 
    orderId       INTEGER           NOT NULL
                                    PRIMARY KEY AUTOINCREMENT,
    distributorId INTEGER           REFERENCES tbl_distributor ( distributorId ) ON UPDATE CASCADE,
    outletId      INTEGER           REFERENCES tbl_outlet ( outletId ) ON UPDATE CASCADE,
    customerId    INTEGER           REFERENCES tbl_customer ( customerId ) ON UPDATE CASCADE,
    orderDate     LONG,
    deliveryDate  LONG,
    driverName    TEXT              CHECK ( driverName != '' ),
    driverNIC     TEXT              CHECK ( driverNIC != '' ),
    vehicleNo     TEXT              CHECK ( vehicleNo != '' ),
    total         DECIMAL( 50, 2 ),
    batteryLevel  INTEGER           NOT NULL,
    longitude     REAL              NOT NULL,
    latitude      REAL              NOT NULL,
    type          TEXT              NOT NULL,
    remarks       TEXT,
    syncStatus    INT               DEFAULT 0 
);

CREATE TABLE tbl_order_detail ( 
    orderId  INTEGER           NOT NULL
                               REFERENCES tbl_order ( orderId ) ON DELETE CASCADE
                                                                ON UPDATE CASCADE,
    itemId   INTEGER           NOT NULL
                               REFERENCES tbl_item ( itemId ) ON UPDATE CASCADE,
    price    DECIMAL( 50, 2 )  NOT NULL,
    discount REAL,
    quantity REAL,
    UNIQUE ( orderId, itemId ) 
);

CREATE TABLE tbl_rep_location ( 
    repLocationId INTEGER NOT NULL
                          PRIMARY KEY AUTOINCREMENT,
    repId         INT     NOT NULL,
    longitude     REAL    NOT NULL,
    latitude      REAL    NOT NULL,
    batteryLevel  INTEGER NOT NULL,
    gpsTime       LONG 
);

CREATE TABLE tbl_unproductive_call ( 
    unProductiveCallId INTEGER NOT NULL
                               PRIMARY KEY AUTOINCREMENT,
    outletId           INT     NOT NULL
                               REFERENCES tbl_outlet ( outletId ) ON UPDATE CASCADE,
    batteryLevel       INT     NOT NULL,
    repId              INTEGER NOT NULL,
    reason             REAL    NOT NULL,
    longitude          REAL    NOT NULL,
    latitude           REAL    NOT NULL,
    time               LONG    NOT NULL,
    syncStatus         INT     DEFAULT 0 
);

CREATE TABLE tbl_invoice ( 
    salesOrderId    LONG              NOT NULL
                                      PRIMARY KEY,
    outletId        INT               NOT NULL
                                      REFERENCES tbl_outlet ( outletId ) ON UPDATE CASCADE,
    date            DATE              NOT NULL,
    distributorCode TEXT              NOT NULL,
    pendingAmount   DECIMAL( 50, 2 )  NOT NULL,
    invoiceAmount   DECIMAL( 50, 2 )  NOT NULL,
    deliveryDate    DATE              NOT NULL 
);

CREATE TABLE tbl_payment ( 
    salesOrderId    LONG              NOT NULL
                                      REFERENCES tbl_invoice ( salesOrderId ) ON DELETE CASCADE
                                                                              ON UPDATE CASCADE,
    paidValue       DECIMAL( 50, 2 )  DEFAULT 0.00
                                      NOT NULL,
    bank            TEXT              DEFAULT '',
    paidDate        DATE              NOT NULL,
    paymentMethod   TEXT              DEFAULT '',
    chequeNo        TEXT,
    realizationDate TEXT              DEFAULT '',
    status          INT               DEFAULT 0 
);

CREATE TABLE tbl_attendance_history ( 
    date         DATE,
    checkInTime  TEXT,
    checkOutTime TEXT
);