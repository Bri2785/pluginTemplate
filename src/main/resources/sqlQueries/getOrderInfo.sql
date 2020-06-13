SELECT
   so.shipToAddress,
   so.shipToCity,
   so.shipToZip,
   shipCountry.name AS shipToCountry,
   shipState.code AS shipToState,
   so.shipToName,
   so.num,
   so.dateFirstShip,
      so.phone,
      so.email,
   qbClass.name AS qbClassName,
   locationGroup.name AS lgName
FROM so
   LEFT JOIN soItem ON soItem.soId = so.id
   LEFT JOIN product ON product.id = soItem.productId
   INNER JOIN locationgroup ON locationGroup.id = so.locationGroupId
   LEFT JOIN CountryConst shipCountry ON shipCountry.id = so.shipToCountryId
   LEFT JOIN StateConst shipState ON shipState.id = so.shipToStateId
   LEFT JOIN qbClass ON qbClass.id = so.qbClassId

WHERE so.id = %1$s
LIMIT 1