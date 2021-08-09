CREATE DATABASE DeliveryCompany
go

USE DeliveryCompany
go

CREATE TABLE [City]
( 
	[idCity]             integer  IDENTITY  NOT NULL ,
	[name]               varchar(100)  NOT NULL ,
	[postalCode]         varchar(100)  NOT NULL ,
	CONSTRAINT [XPKCity] PRIMARY KEY  CLUSTERED ([idCity] ASC)
)
go

CREATE TABLE [District]
( 
	[idDistrict]         integer  IDENTITY  NOT NULL ,
	[name]               varchar(100)  NOT NULL ,
	[idCity]             integer  NOT NULL ,
	[xCord]              integer  NOT NULL ,
	[yCord]              integer  NOT NULL ,
	CONSTRAINT [XPKDistrict] PRIMARY KEY  CLUSTERED ([idDistrict] ASC),
	CONSTRAINT [R_1] FOREIGN KEY ([idCity]) REFERENCES [City]([idCity])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
go

CREATE TABLE [User]
( 
	[username]           varchar(100)  NOT NULL ,
	[firstName]          varchar(100)  NOT NULL ,
	[password]           varchar(100)  NOT NULL ,
	[lastname]           varchar(100)  NOT NULL ,
	[countPackagesSent]  integer  NULL 
	CONSTRAINT [Zero_1114540783]
		 DEFAULT  0,
	CONSTRAINT [XPKUser] PRIMARY KEY  CLUSTERED ([username] ASC)
)
go

CREATE TABLE [Vehicle]
( 
	[licencePlateNumber] varchar(100)  NOT NULL ,
	[fuelType]           integer  NOT NULL 
	CONSTRAINT [TipGoriva_2071896759]
		CHECK  ( fuelType BETWEEN 0 AND 2 ),
	[fuelConsumtion]     decimal(10,3)  NOT NULL ,
	CONSTRAINT [XPKVehicle] PRIMARY KEY  CLUSTERED ([licencePlateNumber] ASC)
)
go

CREATE TABLE [Courier]
( 
	[username]           varchar(100)  NOT NULL ,
	[countPackagesDelivered] integer  NULL 
	CONSTRAINT [Zero_946039388]
		 DEFAULT  0,
	[profit]             decimal(10,3)  NULL 
	CONSTRAINT [Zero_1632387326]
		 DEFAULT  0,
	[status]             integer  NULL 
	CONSTRAINT [Zero_1866347005]
		 DEFAULT  0
	CONSTRAINT [StatusVoznje_2041740765]
		CHECK  ( status BETWEEN 0 AND 1 ),
	[licencePlateNumber] varchar(100)  NOT NULL ,
	CONSTRAINT [XPKCourier] PRIMARY KEY  CLUSTERED ([username] ASC),
	CONSTRAINT [XAK1LicencePlateNumber] UNIQUE ([licencePlateNumber]  ASC),
	CONSTRAINT [R_3] FOREIGN KEY ([username]) REFERENCES [User]([username])
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT [R_12] FOREIGN KEY ([licencePlateNumber]) REFERENCES [Vehicle]([licencePlateNumber])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
go

CREATE TABLE [Package]
( 
	[idPackage]          integer  IDENTITY  NOT NULL ,
	[idDistrictFrom]     integer  NOT NULL ,
	[idDistrictTo]       integer  NOT NULL ,
	[type]               integer  NOT NULL 
	CONSTRAINT [TipPaketa_979703]
		CHECK  ( type BETWEEN 0 AND 2 ),
	[weight]             decimal(10,3)  NOT NULL ,
	[senderUserName]     varchar(100)  NOT NULL ,
	[courierUsername]    varchar(100)  NULL ,
	[status]             integer  NULL 
	CONSTRAINT [Zero_2069707000]
		 DEFAULT  0
	CONSTRAINT [StatusIsporuke_1912000249]
		CHECK  ( status BETWEEN 0 AND 3 ),
	[timePackageAccepted] datetime  NULL ,
	[price]              decimal(10,3)  NULL ,
	CONSTRAINT [XPKPackage] PRIMARY KEY  CLUSTERED ([idPackage] ASC),
	CONSTRAINT [R_13] FOREIGN KEY ([idDistrictFrom]) REFERENCES [District]([idDistrict])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT [R_15] FOREIGN KEY ([idDistrictTo]) REFERENCES [District]([idDistrict])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT [R_18] FOREIGN KEY ([senderUserName]) REFERENCES [User]([username])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT [R_21] FOREIGN KEY ([courierUsername]) REFERENCES [Courier]([username])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
go

CREATE TABLE [TransportOffer]
( 
	[idPackage]          integer  NOT NULL ,
	[username]           varchar(100)  NOT NULL ,
	[pricePercentage]    decimal(10,3)  NOT NULL ,
	[idTransportOffer]   integer  IDENTITY  NOT NULL ,
	CONSTRAINT [XPKTransportOffer] PRIMARY KEY  CLUSTERED ([idTransportOffer] ASC),
	CONSTRAINT [R_17] FOREIGN KEY ([username]) REFERENCES [Courier]([username])
		ON DELETE CASCADE
		ON UPDATE NO ACTION,
	CONSTRAINT [R_16] FOREIGN KEY ([idPackage]) REFERENCES [Package]([idPackage])
		ON DELETE CASCADE
		ON UPDATE NO ACTION
)
go

CREATE TABLE [Drive]
( 
	[idDrive]            integer  IDENTITY  NOT NULL ,
	[username]           varchar(100)  NOT NULL ,
	[idPackage]          integer  NOT NULL ,
	CONSTRAINT [XPKDistrictInDrive] PRIMARY KEY  CLUSTERED ([idDrive] ASC),
	CONSTRAINT [R_25] FOREIGN KEY ([username]) REFERENCES [Courier]([username])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT [R_27] FOREIGN KEY ([idPackage]) REFERENCES [Package]([idPackage])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
go

CREATE TABLE [CourierRequest]
( 
	[username]           varchar(100)  NOT NULL ,
	[licencePlateNumber] varchar(100)  NOT NULL ,
	CONSTRAINT [XPKCourierRequest] PRIMARY KEY  CLUSTERED ([username] ASC),
	CONSTRAINT [R_6] FOREIGN KEY ([licencePlateNumber]) REFERENCES [Vehicle]([licencePlateNumber])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT [R_5] FOREIGN KEY ([username]) REFERENCES [User]([username])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
go

CREATE TABLE [Administrator]
( 
	[username]           varchar(100)  NOT NULL ,
	CONSTRAINT [XPKAdministrator] PRIMARY KEY  CLUSTERED ([username] ASC),
	CONSTRAINT [R_2] FOREIGN KEY ([username]) REFERENCES [User]([username])
		ON DELETE CASCADE
		ON UPDATE CASCADE
)
go


USE [DeliveryCompany]
GO

CREATE PROCEDURE spInsertCourier
	@username varchar(100),
	@licencePlateNumber varchar(100)
AS
BEGIN
	declare @userCnt int
	declare @vehicleCnt int
	declare @usedValuesInCourier int


	select @userCnt=count(*)
	from [User]
	where [User].[username]=@username

	if(@userCnt<=0)
		return -1;

	select @vehicleCnt=count(*)
	from [Vehicle]
	where [Vehicle].[licencePlateNumber]=@licencePlateNumber


	if(@vehicleCnt<=0)
		return -1;

	select @usedValuesInCourier=count(*)
	from [Courier]
	where [Courier].[username]=@username or [Courier].[licencePlateNumber]=@licencePlateNumber

	if(@usedValuesInCourier>0)
		return -1;

	insert into [Courier](username,licencePlateNumber,status,countPackagesDelivered,profit)
	values(@username,@licencePlateNumber,0,0,0)

END
GO

CREATE TRIGGER TR_TransportOffer_Delete_On_Offer_Acceptance
   ON  dbo.Package 
   AFTER UPDATE
AS 
BEGIN

	SET NOCOUNT ON;
	if UPDATE(timePackageAccepted)
	begin

		declare @packageId int
		declare @cursorPackage cursor
		set @cursorPackage = cursor for 
		select idPackage from deleted


		open @cursorPackage
		fetch next from @cursorPackage into @packageId

		while @@FETCH_STATUS = 0 
		begin
			
			delete from TransportOffer where idPackage=@packageId;
			fetch next from @cursorPackage into @packageId
		end
		
	

	
		close @cursorPackage
		deallocate @cursorPackage 
	end

END
GO
