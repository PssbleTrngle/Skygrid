kill @e[tag=blue_banner]
clear @a blue_banner

execute as @e[tag=blue_banner_marker] at @s align xyz run setblock ~ ~-1 ~ minecraft:bedrock
execute as @e[tag=blue_banner_marker] at @s align xyz run setblock ~ ~ ~ minecraft:blue_banner[rotation=4]

execute as @e[tag=blue_banner_marker] at @s unless entity @e[type=shulker,distance=..2] align xyz run summon minecraft:shulker ~.5 ~-1 ~.5  {Glowing:1b,NoAI:1b,Invulnerable:1b}
execute as @e[tag=blue_banner_marker] at @s run team join blue @e[type=shulker,sort=nearest,limit=1]

tag @a[tag=blue] remove notified

title @a[tag=blue] times 1 20 1
title @a[tag=blue] title ""
title @a[tag=blue] subtitle [{"text":"Your banner has been returned","color":"green"}]

tag @a remove has_blue_banner