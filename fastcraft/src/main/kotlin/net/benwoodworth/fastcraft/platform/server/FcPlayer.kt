package net.benwoodworth.fastcraft.platform.server

import net.benwoodworth.fastcraft.platform.text.FcLocale
import net.benwoodworth.fastcraft.platform.text.FcText
import java.util.*

interface FcPlayer {

    val username: String

    var customName: String?

    val uuid: UUID

    val locale: FcLocale

    val isOnline: Boolean

    fun sendMessage(message: FcText)

    fun hasPermission(permission: String): Boolean
}
