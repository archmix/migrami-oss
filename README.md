# How to use Migrami

Import dependency to your maven pom
```xml
<dependency>
  <groupId>org.archmix</groupId>
  <artifactId>migrami-oss</artifactId>
  <version>${migramiVersion}</version>
</dependency>
```
Migrami SQL API

```java:
      String url = "jdbc:url:format";
      String user = "dbuser";
      String password = "dbpwd";

      Migrami migrami = MigramiSQLEngineBuilder.create().withDatasource(url, user, password)
          .withClasspathScriptLoader("sql-migration", this.category())
          .withTableSnapshotRepository()
          .build();
      migrami.migrate();
```

# License
https://github.com/archmix/community/blob/master/LICENSE.md

# CODE OF CONDUCT
https://github.com/archmix/community/blob/master/CODE_OF_CONDUCT.md
