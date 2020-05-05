execute as @a[tag=blue] at @s run function skygridgame:death/notify
execute as @a[tag=!blue,team=!lobby] at @s run function skygridgame:death/notify_others

scoreboard players remove Blue skygrid_values 1
execute if score Blue skygrid_values <= zero skygrid_hidden run clear @a blue_banner
execute if score Blue skygrid_values > zero skygrid_hidden positioned 0 50 0 run function skygridgame:generate_banners/blue
execute if score Blue skygrid_values <= zero skygrid_hidden as @a[tag=blue] run function skygridgame:tp_lobby
execute if score Blue skygrid_values <= zero skygrid_hidden as @a[tag=blue] run function skygridgame:destroy_banners/blue