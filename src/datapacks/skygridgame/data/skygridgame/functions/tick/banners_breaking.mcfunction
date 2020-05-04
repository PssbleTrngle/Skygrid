execute as @a[scores={broken_blue=1..},team=!all,tag=!blue] run kill @e[tag=blue_banner,limit=1,sort=nearest]
execute as @a[scores={broken_blue=1..},team=!all,tag=!blue] run tag @s add has_blue_banner
scoreboard players reset @a broken_blue

execute as @a[scores={broken_red=1..},team=!all,tag=!red] run kill @e[tag=red_banner,limit=1,sort=nearest]
execute as @a[scores={broken_red=1..},team=!all,tag=!red] run tag @s add has_red_banner
scoreboard players reset @a broken_red

execute as @a[scores={broken_yellow=1..},team=!all,tag=!yellow] run kill @e[tag=yellow_banner,limit=1,sort=nearest]
execute as @a[scores={broken_yellow=1..},team=!all,tag=!yellow] run tag @s add has_yellow_banner
scoreboard players reset @a broken_yellow

execute as @a[scores={broken_green=1..},team=!all,tag=!green] run kill @e[tag=green_banner,limit=1,sort=nearest]
execute as @a[scores={broken_green=1..},team=!all,tag=!green] run tag @s add has_green_banner
scoreboard players reset @a broken_green