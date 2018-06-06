# JSDB

`MongoDB` like script engine binding to relational databases (using JSDB driver). Implementation provide `db` object
to access table and execute any queries.

Currently project contains two modules: `jsdb-lib` and `jsdb-cli`:

* `jsdb-lib` - JSDB library, provides binding between JSDC and script engine.
* `jsdb-cli` - command line interface for library.

## Script interface

Library provides `db` object to all scripts. The object represents JDBC connection to any relational database. Like
`MongoDB` this object provides several methods to query data from tables or execute any SQL.

### Database object

Database object has following methods:

* execute(string) - execute insert, update or delete query (represented by string) and returns number of updated rows;
* query(string) - execute select query (represented by given string) and returns result set wrapper object;
* table name - returns collection builder for given table. Collection builder has additional methods (where, limit,
  skip, take) to create optimal query.

### Result set wrapper object

Result set wrapper represents wrapper over JDBC result set with additional methods. Object has following methods:

* take(count) - returns new wrapper which take only `count` rows from source;
* skip(count) - returns new wrapper which skip `count` rows from source and return all other;
* map(mapper) - returns new wrapper which map every row from source to another object;
* filter(filter) - returns new wrapper which contains only rows where filter function returns `true`;
* fold(accumulator, reducer) - fold all rows from source to single object, returns result of last `reduce` call;
* forEach(consumer) - call `consumer` function for every row in source, does not return anything;
* collect() - collect all rows from source to a list, returns list of all rows;
* distinct() - collect all rows from source to a set, returns set of all rows;
* count() - return number of rows in the source.

### Collection object

Collection has same methods as Result set wrapper. Additional methods to control query:

* project(fields) - set fields to query from the table. If called without arguments - query returns all fields;
* where(conditions) - add where condition to query. All conditions combined using `and` operation. If called without
  arguments - query will not contain `where` clause;
* limit(n) - add `limit` clause to query. If called without parameters - query will not contain `limit` clause.

## Field mappimg

To use database specific things and extension (like PostGIS) from script engine. Library provide field mappers. Mappers
have two different types:

* table mappers - work only for particular field in table. Table mappers will be applied only for table name queries
  (db.table_name). Table and field names are case sensitive;
* field mappers - work only for SQL queries. Wrapper use fields name to call mapper for particular field. Field names
  are case sensitive.

## Examples

Following script executes select query from table `pg_database` where field `datname` != `postgres`. For every row in
result set it select only `datname` field and collect all values to list:

```javascript
db.pg_database
  	.where("datname != 'postgres'") // add where condition.
  	.map(function(row) { return row.datname; }) // extract only database name from row.
  	.collect(); // collect all the names to single list.
```

Following script do the same thing, but using SQL query:

```javascript
db.pg_databasequery("select datname from pg_database where datname != 'postgres'") // execute SQL query
  	.map(function(row) { return row.datname; }) // extract only database name from row.
  	.collect(); // collect all the names to single list.
```

Next script shows example how to query tables with dot character in table name:

```javascript
db.["pg_catalog.pg_tables"] // access to table with non identifier like name
	.map(function(row) { return row.tableowner; })
	.distinct();
```

Next script shows update operation:

```javascript
db.execute("update schema.table set field = 3 where name = 'row'");
```

Update operation returns number of changed rows as integer value.
