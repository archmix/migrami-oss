CREATE OR REPLACE TABLE migrami_snapshot(
  category    VARCHAR(100) NOT NULL,
  script_name  VARCHAR(100) NOT NULL,
  checksum    CHAR(32) NOT NULL,
  PRIMARY KEY (category, script_name)
)