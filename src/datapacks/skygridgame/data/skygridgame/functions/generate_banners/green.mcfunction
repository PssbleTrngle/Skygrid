kill @e[tag=green_banner]
clear @a green_banner

execute as @e[tag=green_banner_marker] at @s align xyz run setblock ~ ~-1 ~ minecraft:bedrock
execute as @e[tag=green_banner_marker] at @s align xyz run setblock ~ ~ ~ minecraft:green_banner[rotation=0]

execute as @e[tag=green_banner_marker] at @s unless entity @e[type=shulker,distance=..2] align xyz run summon minecraft:shulker ~.5 ~-1 ~.5  {Glowing:1b,NoAI:1b,Invulnerable:1b}
execute as @e[tag=green_banner_marker] at @s run team join green @e[type=shulker,sort=nearest,limit=1]

tag @a[tag=green] remove notified

title @a[tag=green] times 1 20 1
title @a[tag=green] title ""
title @a[tag=green] subtitle [{"text":"Your banner has been returned","color":"green"}]

tag @a remove has_green_banner