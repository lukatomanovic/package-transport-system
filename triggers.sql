
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
