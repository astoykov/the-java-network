-- needed by Flyway + H2 integration
CREATE SCHEMA IF NOT EXISTS "public";

-- set MySQL syntax
SET MODE MySQL;

-- UUID() function
CREATE ALIAS IF NOT EXISTS UUID FOR "org.h2.value.ValueUuid.getNewRandom";
