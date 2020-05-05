execute as @a[tag=!joined] run function skygridgame:tp_lobby
tp @a[tag=!joined] 0 50 0
recipe give @a[tag=!joined] *

tag @a[tag=!joined] add joined

execute as @e[type=item] store result score @s age run data get entity @s Age

execute as @a unless entity @s[team=all] unless entity @s[team=red] run tag @s remove red
execute as @a unless entity @s[team=all] unless entity @s[team=yellow] run tag @s remove yellow
execute as @a unless entity @s[team=all] unless entity @s[team=blue] run tag @s remove blue
execute as @a unless entity @s[team=all] unless entity @s[team=green] run tag @s remove green

tag @a[team=red,tag=!red] add red
tag @a[team=yellow,tag=!yellow] add yellow
tag @a[team=blue,tag=!blue] add blue
tag @a[team=green,tag=!green] add green

effect give @a[team=lobby] minecraft:saturation 1 10 true
effect give @a[team=lobby] minecraft:invisibility 1 10 true

execute if score Timer skygrid_hidden > zero skygrid_hidden run bossbar set skygrid visible true
execute if score Timer skygrid_hidden <= zero skygrid_hidden run bossbar set skygrid visible false

execute store result score Red skygrid_hidden if entity @a[tag=red]
execute store result score Blue skygrid_hidden if entity @a[tag=blue]
execute store result score Green skygrid_hidden if entity @a[tag=green]
execute store result score Yellow skygrid_hidden if entity @a[tag=yellow]

execute store result score Red_1 skygrid_hidden if entity @a[tag=red]
execute store result score Blue_1 skygrid_hidden if entity @a[tag=blue]
execute store result score Green_1 skygrid_hidden if entity @a[tag=green]
execute store result score Yellow_1 skygrid_hidden if entity @a[tag=yellow]

scoreboard players set Teams skygrid_hidden 0
scoreboard players operation Teams skygrid_hidden += Red_1 skygrid_hidden
scoreboard players operation Teams skygrid_hidden += Blue_1 skygrid_hidden
scoreboard players operation Teams skygrid_hidden += Green_1 skygrid_hidden
scoreboard players operation Teams skygrid_hidden += Yellow_1 skygrid_hidden

tp @a[team=lobby,y=-100000,dy=100000] 0 49 0
execute as @a[gamemode=survival] at @s if entity @s[y=-100000,dy=100000] run kill @s

#tag @a[nbt={Inventory:[{id:"minecraft:red_banner"}]}] add has_red_banner
#tag @a[nbt={Inventory:[{id:"minecraft:yellow_banner"}]}] add has_yellow_banner
#tag @a[nbt={Inventory:[{id:"minecraft:green_banner"}]}] add has_green_banner
#tag @a[nbt={Inventory:[{id:"minecraft:blue_banner"}]}] add has_blue_banner

#execute as @a[tag=has_red_banner] at @s run particle minecraft:dust 1 0 0 2 ~ ~2.2 ~ 0 0 0 0 1
#execute as @a[tag=has_blue_banner] at @s run particle minecraft:dust 0 0 1 2 ~ ~2.2 ~ 0 0 0 0 1
#execute as @a[tag=has_yellow_banner] at @s run particle minecraft:dust 1 1 0 2 ~ ~2.2 ~ 0 0 0 0 1
#execute as @a[tag=has_green_banner] at @s run particle minecraft:dust 0 1 0 2 ~ ~2.2 ~ 0 0 0 0 1

effect give @a[tag=has_red_banner] glowing 1 0 true
effect give @a[tag=has_yellow_banner] glowing 1 0 true
effect give @a[tag=has_green_banner] glowing 1 0 true
effect give @a[tag=has_blue_banner] glowing 1 0 true

tag @e[type=item,nbt={Item:{id:"minecraft:red_banner"}}] add red_banner
tag @e[type=item,nbt={Item:{id:"minecraft:yellow_banner"}}] add yellow_banner
tag @e[type=item,nbt={Item:{id:"minecraft:green_banner"}}] add green_banner
tag @e[type=item,nbt={Item:{id:"minecraft:blue_banner"}}] add blue_banner

function skygridgame:tick/check_deaths
function skygridgame:tick/banners_breaking
function skygridgame:tick/banner_markers

execute if entity @a[team=!red,tag=has_red_banner] run tag @a[tag=!notified,team=red] add notify
execute if entity @a[team=!blue,tag=has_blue_banner] run tag @a[tag=!notified,team=blue] add notify
execute if entity @a[team=!yellow,tag=has_yellow_banner] run tag @a[tag=!notified,team=yellow] add notify
execute if entity @a[team=!green,tag=has_green_banner] run tag @a[tag=!notified,team=green] add notify

execute as @a[tag=notify] at @s run playsound minecraft:entity.elder_guardian.curse player @s ~ ~ ~ 0.5
title @a[tag=notify] times 1 20 1
title @a[tag=notify] title [{"text":"Your banner has been captured","color":"red"}]
tag @a[tag=notify] add notified
tag @a[tag=notify] remove notify

#execute if entity @a[tag=red,gamemode=survival] unless entity @a[tag=!red,gamemode=survival] run function skygridgame:win/red
#execute if entity @a[tag=green,gamemode=survival] unless entity @a[tag=!green,gamemode=survival] run function skygridgame:win/green
#execute if entity @a[tag=blue,gamemode=survival] unless entity @a[tag=!blue,gamemode=survival] run function skygridgame:win/blue
#execute if entity @a[tag=yellow,gamemode=survival] unless entity @a[tag=!yellow,gamemode=survival] run function skygridgame:win/yellow

team join yellow @a[scores={join_yellow=1..}]
team join red @a[scores={join_red=1..}]
team join green @a[scores={join_green=1..}]
team join blue @a[scores={join_blue=1..}]

scoreboard players set @a join_yellow 0
scoreboard players set @a join_red 0
scoreboard players set @a join_green 0
scoreboard players set @a join_blue 0

execute as @a[tag=has_blue_banner,scores={died=1..}] run function skygridgame:generate_banners/blue
execute as @a[tag=has_green_banner,scores={died=1..}] run function skygridgame:generate_banners/green
execute as @a[tag=has_red_banner,scores={died=1..}] run function skygridgame:generate_banners/red
execute as @a[tag=has_yellow_banner,scores={died=1..}] run function skygridgame:generate_banners/yellow

scoreboard players set @a died 0

execute as @e[tag=blue_banner_marker] at @s as @a[distance=..6,tag=!blue,team=all] run function skygridgame:wither
execute as @e[tag=green_banner_marker] at @s as @a[distance=..6,tag=!green,team=all] run function skygridgame:wither
execute as @e[tag=yellow_banner_marker] at @s as @a[distance=..6,tag=!yellow,team=all] run function skygridgame:wither
execute as @e[tag=red_banner_marker] at @s as @a[distance=..6,tag=!red,team=all] run function skygridgame:wither

execute as @a[tag=setSpawn] at @s unless block ~ ~-1 ~ chorus_flower unless block ~ ~-1 ~ end_stone unless block ~ ~-1 ~ chorus_plant unless block ~ ~-1 ~ air run spawnpoint @s ~ ~ ~
execute as @a[tag=setSpawn] at @s unless block ~ ~-1 ~ chorus_flower unless block ~ ~-1 ~ end_stone unless block ~ ~-1 ~ chorus_plant unless block ~ ~-1 ~ air run tag @s remove setSpawn

title @a[tag=has_blue_banner] actionbar [{"text":"You are holding the blue banner","color":"blue"}]
title @a[tag=has_red_banner] actionbar [{"text":"You are holding the red banner","color":"red"}]
title @a[tag=has_green_banner] actionbar [{"text":"You are holding the green banner","color":"green"}]
title @a[tag=has_yellow_banner] actionbar [{"text":"You are holding the yellow banner","color":"yellow"}]