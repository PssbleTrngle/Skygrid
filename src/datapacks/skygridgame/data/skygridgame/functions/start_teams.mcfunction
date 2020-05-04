execute if score Teams skygrid_hidden >= min_teams skygrid_hidden if score Red skygrid_hidden >= min_per_team skygrid_hidden if score Green skygrid_hidden >= min_per_team skygrid_hidden if score Blue skygrid_hidden >= min_per_team skygrid_hidden if score Yellow skygrid_hidden >= min_per_team skygrid_hidden run tag @s add starter

execute if entity @s[tag=starter] run function skygridgame:start
execute unless entity @s[tag=starter] run tellraw @s "Unable to start"

tag @a remove starter