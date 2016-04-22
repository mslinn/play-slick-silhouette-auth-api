package persistence

import play.api.db.slick.HasDatabaseConfigProvider

trait SlickAccess extends DbTablesDefinitions with HasDatabaseConfigProvider[DbProfile]
