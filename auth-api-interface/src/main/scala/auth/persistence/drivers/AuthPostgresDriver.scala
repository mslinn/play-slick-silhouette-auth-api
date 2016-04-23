package auth.persistence.drivers

trait AuthPostgresDriver extends PostgresDriver {
  override val api: AuthPostgresDriverApi.type = AuthPostgresDriverApi

  object AuthPostgresDriverApi extends PostgresDriverApi with ModelMappingSupport
}

