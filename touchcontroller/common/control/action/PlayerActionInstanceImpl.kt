package top.fifthlight.touchcontroller.common.control.action

import kotlinx.serialization.Serializable
import top.fifthlight.combine.core.data.Text
import top.fifthlight.touchcontroller.api.v1.action.PlayerActionInstance
import top.fifthlight.touchcontroller.common.gal.player.PlayerHandle
import top.fifthlight.touchcontroller.common.util.registry.RegistrySerializer

@Serializable(with = PlayerActionInstanceImplSerializer::class)
class PlayerActionInstanceImpl(
    val hidden: Boolean = false,
    val name: Text,
    val action: (PlayerHandle) -> Unit,
) : PlayerActionInstance {
    operator fun invoke(player: PlayerHandle) = action(player)
}

class PlayerActionInstanceImplSerializer : RegistrySerializer<PlayerActionInstanceImpl>(
    registry = PlayerActions.registry,
    serialName = "PlayerActionInstance",
    unknown = { PlayerActions.unknown },
)
