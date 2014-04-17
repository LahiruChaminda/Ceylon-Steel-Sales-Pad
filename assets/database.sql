drop table if exists tbl_item;
drop table if exists tbl_category;
drop table if exists tbl_customer;
drop table if exists tbl_driver;
drop table if exists tbl_distributor;
drop table if exists tbl_outlet;
drop table if exists tbl_order_detail;
drop table if exists tbl_order;
drop table if exists tbl_rep_location;
drop table if exists tbl_vehicle;
drop table if exists tbl_unproductive_call;

create table tbl_category(
	categoryId integer primary key,
	categoryDescription text not null
);
create table tbl_item(
	itemId integer primary key,
	categoryId integer not null references tbl_category(categoryId),
	itemCode text not null,
	itemDescription text check(itemDescription!=''),
	price real
);
create table tbl_customer(
	customerId integer not null primary key,
	customerName text not null
);
create table tbl_vehicle(
	vehicleNo text not null primary key
);
create table tbl_driver(
	driverName text not null,
	driverNIC text not null unique
);
create table tbl_distributor(
	distributorId integer not null primary key,
	distributorName text not null
);
create table tbl_outlet(
	outletId integer not null primary key,
	outletName text not null
);
create table tbl_order(
	orderId integer not null primary key autoincrement,
	distributorId integer references tbl_distributor(distributorId),
	outletId integer references tbl_outlet(outletId),
	customerId integer references tbl_customer(customerId),
	orderDate long,
	deliveryDate long,
	driverName text check(driverName!=''),
	driverNIC text check(driverNIC!=''),
	vehicleNo text check(vehicleNo!=''),
	total real,
	batteryLevel integer not null,
	longitude real not null,
	latitude real not null,
	type text not null,
    remarks text
);
create table tbl_order_detail(
	orderId integer not null references tbl_order(orderId),
	itemId integer not null references tbl_item(itemId),
	price real not null,
	discount real,
	quantity real
);
create table tbl_rep_location(
    repLocationId integer not null primary key autoincrement,
	repId int not null,
    longitude real not null,
	latitude real not null,
	batteryLevel integer not null,
    gpsTime long
);
create table tbl_unproductive_call(
    unProductiveCallId integer not null primary key autoincrement,
    outletId int not null references tbl_outlet(outletId),
	batteryLevel int not null,
	repId integer not null,
	reason real not null,
	longitude real not null,
	latitude real not null,
    time long not null
);