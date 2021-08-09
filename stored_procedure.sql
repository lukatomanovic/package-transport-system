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