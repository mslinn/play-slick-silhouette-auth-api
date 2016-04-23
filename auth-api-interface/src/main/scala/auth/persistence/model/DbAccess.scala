package auth.persistence.model

import auth.persistence.DbProfile
import play.api.db.slick.HasDatabaseConfigProvider

trait DbAccess extends TablesDefinitions with HasDatabaseConfigProvider[DbProfile]
