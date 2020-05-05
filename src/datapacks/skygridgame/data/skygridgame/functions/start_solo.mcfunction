execute if score Teams skygrid_hidden = zero skygrid_hidden if entity @a[team=!lobby] run tag @s add starter

execute if entity @s[tag=starter] run team join all @a[team=!lobby]
execute if entity @s[tag=starter] run function skygridgame:start
execute unless entity @s[tag=starter] run tellraw @s "Unable to start"

tag @a remove starter