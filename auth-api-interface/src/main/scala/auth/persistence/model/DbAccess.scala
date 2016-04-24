package auth.persistence.model

import auth.persistence.DbProfile
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }

trait DbAccess extends TablesDefinitions with HasDatabaseConfigProvider[DbProfile]

trait AuthDatabaseConfigProvider extends DatabaseConfigProvider
