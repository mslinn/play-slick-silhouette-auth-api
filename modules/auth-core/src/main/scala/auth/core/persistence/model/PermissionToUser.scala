package auth.core.persistence.model

import java.util.UUID

import auth.core.model.core.Permission

final case class PermissionToUser(permission: Permission, userUuid: UUID)