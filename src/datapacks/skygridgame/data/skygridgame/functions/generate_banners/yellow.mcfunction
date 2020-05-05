kill @e[tag=yellow_banner]
clear @a yellow_banner

execute as @e[tag=yellow_banner_marker] at @s align xyz run setblock ~ ~-1 ~ minecraft:bedrock
execute as @e[tag=yellow_banner_marker] at @s align xyz run setblock ~ ~ ~ minecraft:yellow_banner[rotation=8]

execute as @e[tag=yellow_banner_marker] at @s unless entity @e[type=shulker,distance=..2] align xyz run summon minecraft:shulker ~.5 ~-1 ~.5  {Glowing:1b,NoAI:1b,Invulnerable:1b}
execute as @e[tag=yellow_banner_marker] at @s run team join yellow @e[type=shulker,sort=nearest,limit=1]

tag @a[tag=yellow] remove notified

title @a[tag=yellow] times 1 20 1
title @a[tag=yellow] title ""
title @a[tag=yellow] subtitle [{"text":"Your banner has been returned","color":"green"}]

tag @a remove has_yellow_banner