package auth.persistence.model

import auth.persistence.AuthDbProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

trait AuthDbAccess extends TablesDefinitions with HasDatabaseConfigProvider[AuthDbProfile]

trait AuthDatabaseConfigProvider extends DatabaseConfigProvider
