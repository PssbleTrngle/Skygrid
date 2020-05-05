execute as @a[tag=green] at @s run function skygridgame:death/notify
execute as @a[tag=!green,team=!lobby] at @s run function skygridgame:death/notify_others

scoreboard players remove Green skygrid_values 1
execute if score Green skygrid_values <= zero skygrid_hidden run clear @a green_banner
execute if score Green skygrid_values > zero skygrid_hidden positioned 0 50 0 run function skygridgame:generate_banners/green
execute if score Green skygrid_values <= zero skygrid_hidden as @a[tag=green] run function skygridgame:tp_lobby
execute if score Green skygrid_values <= zero skygrid_hidden as @a[tag=green] run function skygridgame:destroy_banners/green