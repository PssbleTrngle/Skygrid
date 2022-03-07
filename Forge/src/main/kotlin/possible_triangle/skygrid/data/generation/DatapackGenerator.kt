package possible_triangle.skygrid.data.generation

import net.minecraft.data.DataGenerator
import kotlin.io.path.Path

class DatapackGenerator(base: DataGenerator, name: String) : DataGenerator(Path("datapacks", name), base.inputFolders)