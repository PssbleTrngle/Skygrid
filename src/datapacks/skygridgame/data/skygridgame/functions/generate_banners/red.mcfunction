wkill @e[tag=red_banner]
clear @a red_banner

execute as @e[tag=red_banner_marker] at @s align xyz run setblock ~ ~-1 ~ minecraft:bedrock
execute as @e[tag=red_banner_marker] at @s align xyz run setblock ~ ~ ~ minecraft:red_banner[rotation=12]

execute as @e[tag=red_banner_marker] at @s unless entity @e[type=shulker,distance=..2] align xyz run summon minecraft:shulker ~.5 ~-1 ~.5  {Glowing:1b,NoAI:1b,Invulnerable:1b}
execute as @e[tag=red_banner_marker] at @s run team join red @e[type=shulker,sort=nearest,limit=1]

tag @a[tag=red] remove notified

title @a[tag=red] times 1 20 1
title @a[tag=red] title ""
title @a[tag=red] subtitle [{"text":"Your banner has been returned","color":"green"}]

tag @a remove has_red_banner