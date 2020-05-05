schedule clear skygridgame:peace

team modify all collisionRule pushOtherTeams
team modify all friendlyFire false
team join all @a[team=!lobby]

scoreboard players add Round skygrid_hidden 1

gamerule naturalRegeneration true

function skygridgame:clear_top
function skygridgame:generate

spreadplayers 0 0 8 16 false @a[team=!lobby]
tag @a[team=!lobby] add setSpawn

execute if score PeaceInvisible skygrid_settings > zero skygrid_hidden run effect give @a[team=!lobby] minecraft:invisibility 100000 1 true

execute if score PeaceBlocks skygrid_settings > zero skygrid_hidden run give @a[team=!lobby] glass 10

execute as @a[team=!lobby] run function skygridgame:freeze
schedule function skygridgame:unfreeze_all 4s

title @a[team=!lobby] times 3 25 3
title @a[team=!lobby] title ""

execute if score LastPeace skygrid_settings = Round skygrid_hidden run title @a[team=!lobby] subtitle [{"text":"Last peace phase","color":"gold"}]
execute unless score LastPeace skygrid_settings = Round skygrid_hidden run title @a[team=!lobby] subtitle [{"text":"Gather Resources","color":"gold"}]

execute if score LastPeace skygrid_settings = Round skygrid_hidden run scoreboard players set Timer skygrid_hidden 300
execute unless score LastPeace skygrid_settings = Round skygrid_hidden run scoreboard players set Timer skygrid_hidden 120

execute store result bossbar skygrid max run scoreboard players get Timer skygrid_hidden

execute unless score LastPeace skygrid_settings = Round skygrid_hidden run schedule function skygridgame:battle 120s
execute if score LastPeace skygrid_settings = Round skygrid_hidden run schedule function skygridgame:battle 300s

bossbar set skygrid name "Time until battle"
bossbar set skygrid color red

time set noon
execute if score PeaceBorder skygrid_settings > zero skygrid_hidden run worldborder set 66
execute unless score PeaceBorder skygrid_settings > zero skygrid_hidden run worldborder set 95

execute if score ClearOnPeace skygrid_settings > zero skygrid_hidden run tag @a remove has_blue_banner
execute if score ClearOnPeace skygrid_settings > zero skygrid_hidden run tag @a remove has_yellow_banner
execute if score ClearOnPeace skygrid_settings > zero skygrid_hidden run tag @a remove has_green_banner
execute if score ClearOnPeace skygrid_settings > zero skygrid_hidden run tag @a remove has_red_banner