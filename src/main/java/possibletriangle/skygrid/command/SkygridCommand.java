package possibletriangle.skygrid.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import possibletriangle.skygrid.Skygrid;

public class SkygridCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(Skygrid.MODID)
                        .then(PossibleCommand.register())
                        .then(GenerateCommand.register())
        );
    }

}
