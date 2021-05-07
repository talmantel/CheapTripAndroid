# Package ru.z8.louttsev.cheaptripmobile.shared.persistence

Application infrastructure layer.

## Description

Contains local data storage implementation.

## Summary

Type                  | Name                            | Description
----------------------|---------------------------------|-----------------------------------------------------
abstract class        | **LocalDbStorage**              | Data storage based on local database.
class                 | **LocationDb**                  | Locations storage implementation.
class                 | **RouteDb**                     | Routes storage implementation.
class                 | **FullDbDataSource**            | Fake remote data source implementation based on a copy of the server DB.  *Will be deprecated later.*