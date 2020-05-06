# Skygrid
[Download](https://www.curseforge.com/minecraft/mc-mods/skygrid/files)

A Minecraft mod realizing the Skygrid map by Sethbling as an entire world generator. If there are any feature request or issues, post them on the issue tracker or contact me via Twitter.

This mods adds the new Skygrid world generation type. This will generate random blocks from the dimensions configuration file and place them in a grid.

## Supported Mods
- About any wood types from other mods
- [Quark](https://www.curseforge.com/minecraft/mc-mods/quark)
- [Botania](https://www.curseforge.com/minecraft/mc-mods/botania)
- [Endergetic Expansion](https://www.curseforge.com/minecraft/mc-mods/endergetic)
- [Upgrade Aquatic](https://www.curseforge.com/minecraft/mc-mods/upgrade-aquatic)
- [The Midnight](https://www.curseforge.com/minecraft/mc-mods/the-midnight)

There will be more mods added as soon as they update to 1.15.2. 
If you have any requests, open a ticket on the issue tracker.

## Configuration
Every dimension is configured via XML Files located in the data folder.
This means you can fully customize every world and dimension by creating a Datapack.

The configuration for the overworld would be located at
```
datapacks\[your pack]\data\minecraft\skygrid\dimensions\overworld.xml
```
 

The one for the midnight dimension at
```
datapacks\[your pack]\data\midnight\skygrid\dimensions\midnight.xml
```

 

You can also add custom dimension named "[your namespace]:[your name]" by creating a xml file at 
```
datapacks\[your pack]\data\[your namespace]\skygrid\dimensions\[your name].xml
```
and including the `<create>` element.

The config files for the two included example custom dimensions "aqua" and "cave" can be found here.
The config files for the vanilla dimensions can be found here
It is recommended to mention the XML Schema file if you are using an editor like VSCode or Intellij Idea to get auto completion and schema errors when you are editing.

## Images
...of the supported dimensions can be found under under the [Curse Page](https://www.curseforge.com/minecraft/mc-mods/skygrid/screenshots)

## Commands
There are two commands added by this mod, both prefixed with `skygrid`:

```/skygrid generate [x1] [y1] [z1] [x2] [y2] [z2] {skygrid config}```

Similar to the fill command, generates the given skygrid configuration (current dimension if not specified) in the give range.

```/skygrid blocks {skygrid config} contains {block}```

Returns the weight at which the given block can generate in the specified skygrid configuration, if at all

```/skygrid blocks {skygrid config} export {name}```

Exports all weights of the given skygrid configuration into an .csv file which you can open with excel
 

 

 
