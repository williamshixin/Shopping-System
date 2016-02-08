Shopping-System

A shopping system build with JFrames, Oracle 11g and JDBC

Run the program under eclipse with JDK 1.7

Database Schema:
Photos( PhotoID, Taken_Time, Description)
Products(ProductID，ProName，ProCategory，Brand, Price，StockQty)
Addresses(AddrID, StreetAddr, AptNumber, CITY, Zip, State, Latitude, Longitude, MobilePhone)
AddrGeoLoc(AddrID, GeoLoc)
AddrID is a FK referencing to Addresses (AddrID)
Users (UserEmail, Birthdate, BirthMonth,Age,FName, LName, AccNumber, ExpDate, CSC, NickName, Gender, JoinTime, PhotoID, AddrID )
AddrID is a FK referencing to Addresses (AddrID)
PhotoID is a FK referencing to Photos (PhotoID)
SELLERS(UserEmail, SellerPassword)
UserEmail is a FK referencing to Users(UserEmail)
Customers(UserEmail,CustPassword)
UserEmail is a FK referencing to Users(UserEmail)
Reviews(ReviewID,AuthorID,Rating,Comtent,ProductID,PostTime)
AuthorID is a FK referencing to Customers(UserID)
ProductID is a FK referencing to Products(ProductsID)
Orders( OrderID,ShippedTime,EstiArriTime,SignedTime,TotalPrice,TrackingNum,PlaceTime,AccNum,ExpDate,CSC , FillTime,CustomerEmail ShipAddr)
CustomerEmail is a FK referencing to Customers(UserEmail)
ShipAddr is a FK referencing to Addresses(AddrID)
LikeReview(CustomerEmail,ReviewID,LikeTime )
CustomerEmail is a FK referencing to Customers(UserEmail)
ReviewID is a FK referencing to Reviews(ReviewID)
ProductSeller(SellerEmail,ProductID,Quantity )
SellerEmail is a FK referencing to Sellers(UserEmail)
ProductID is a FK referencing to Products(ProductsID)
ProductPhoto(ProductID,PhotoID)
ProductID is a FK referencing to Products(ProductsID)
PhotoID is a FK referencing to Photos(PhotoID)
OrderProductQty(OrderID,ProductID,Quantity)
OrderID is a FK referencing to Orders(OrderID)
ProductID is a FK referencing to Products(ProductsID)! !!! Index: index_ADDRGEO on ADDRGEOLOC (GEOLOC), this is because we need the index to execute spacial query


