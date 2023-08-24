import com.possible_triangle.skygrid.minigame.command.MinigameCommand
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback

object MinigameEntrypoint : ModInitializer {

    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { d, c, _ -> MinigameCommand.register(d, c) }
    }

}