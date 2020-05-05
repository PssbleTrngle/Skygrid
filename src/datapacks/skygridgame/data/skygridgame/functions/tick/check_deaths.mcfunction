execute as @a[team=red,tag=has_green_banner] at @s if block ~ ~ ~ minecraft:red_banner run function skygridgame:death/green
execute as @a[team=red,tag=has_blue_banner] at @s if block ~ ~ ~ minecraft:red_banner run function skygridgame:death/blue
execute as @a[team=red,tag=has_yellow_banner] at @s if block ~ ~ ~ minecraft:red_banner run function skygridgame:death/yellow

execute as @a[team=blue,tag=has_green_banner] at @s if block ~ ~ ~ minecraft:blue_banner run function skygridgame:death/green
execute as @a[team=blue,tag=has_red_banner] at @s if block ~ ~ ~ minecraft:blue_banner run function skygridgame:death/red
execute as @a[team=blue,tag=has_yellow_banner] at @s if block ~ ~ ~ minecraft:blue_banner run function skygridgame:death/yellow

execute as @a[team=green,tag=has_red_banner] at @s if block ~ ~ ~ minecraft:green_banner run function skygridgame:death/red
execute as @a[team=green,tag=has_blue_banner] at @s if block ~ ~ ~ minecraft:green_banner run function skygridgame:death/blue
execute as @a[team=green,tag=has_yellow_banner] at @s if block ~ ~ ~ minecraft:green_banner run function skygridgame:death/yellow

execute as @a[team=yellow,tag=has_blue_banner] at @s if block ~ ~ ~ minecraft:yellow_banner run function skygridgame:death/blue
execute as @a[team=yellow,tag=has_red_banner] at @s if block ~ ~ ~ minecraft:yellow_banner run function skygridgame:death/red
execute as @a[team=yellow,tag=has_green_banner] at @s if block ~ ~ ~ minecraft:yellow_banner run function skygridgame:death/green
