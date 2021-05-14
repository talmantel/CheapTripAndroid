# Package ru.z8.louttsev.cheaptripmobile.shared.infrastructure.datasource

Application infrastructure layer.

## Description

Contains data source implementations.

*Full DB is fake data source temporarily substitutes remote (network) implementation.*

*It will be deprecated later.* 

## Summary

Type                  | Name                            | Description
----------------------|---------------------------------|-----------------------------------------------------
abstract class        | **FullDbDataSource**            | Data source implementation based on a copy of the server DB.
class                 | **LocationDataSourceFullDb**    | Locations data source implementation based on full DB.
class                 | **RouteDataSourceFullDb**       | Routes data source implementation based on full DB.
*class*               | ***NetworkDataSource***         | *Not yet implemented*