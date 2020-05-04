#team join all @a[team=!all]

gamerule randomTickSpeed 10
worldborder warning distance 0

scoreboard players set Round skygrid_hidden 0

execute as @e[tag=banner_marker] at @s run fill ~-7 ~-7 ~-7 ~7 ~40 ~7 air

execute if score KeepInventory skygrid_settings > zero skygrid_hidden run gamerule keepInventory true
execute unless score KeepInventory skygrid_settings > zero skygrid_hidden run gamerule keepInventory false

scoreboard objectives setdisplay sidebar skygrid_values

scoreboard players reset @a join_yellow
scoreboard players reset @a join_red
scoreboard players reset @a join_green
scoreboard players reset @a join_blue

execute if entity @a[tag=red] run scoreboard players operation Red skygrid_values = TeamHealth skygrid_settings
execute if entity @a[tag=blue] run scoreboard players operation Blue skygrid_values = TeamHealth skygrid_settings
execute if entity @a[tag=green] run scoreboard players operation Green skygrid_values = TeamHealth skygrid_settings
execute if entity @a[tag=yellow] run scoreboard players operation Yellow skygrid_values = TeamHealth skygrid_settings

gamemode survival @a[team=!lobby]

function skygridgame:peace
