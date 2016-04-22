package persistence.model

import persistence._
import play.api.db.slick.HasDatabaseConfigProvider

trait DbAccess extends TablesDefinitions with HasDatabaseConfigProvider[DbProfile]
