package top.fifthlight.touchcontroller.common.control.action

import kotlinx.serialization.Serializable
import top.fifthlight.combine.core.data.Text
import top.fifthlight.touchcontroller.api.v1.action.GameAction
import top.fifthlight.touchcontroller.api.v1.action.GameActionInstance
import top.fifthlight.touchcontroller.common.util.registry.RegistrySerializer

@Serializable(with = GameActionInstanceSerializerProviderImpl::class)
class GameActionInstanceImpl(
    val hidden: Boolean = false,
    val name: Text,
    val action: GameAction,
) : GameActionInstance {
    operator fun invoke() = action.action()
}

class GameActionInstanceSerializerProviderImpl : RegistrySerializer<GameActionInstanceImpl>(
    registry = GameActions.registry,
    serialName = "GameActionInstance",
    unknown = { GameActions.unknown },
)
