execute as @a[tag=red] at @s run function skygridgame:death/notify
execute as @a[tag=!red,team=!lobby] at @s run function skygridgame:death/notify_others

scoreboard players remove Red skygrid_values 1
execute if score Red skygrid_values <= zero skygrid_hidden run clear @a red_banner
execute if score Red skygrid_values > zero skygrid_hidden positioned 0 50 0 run function skygridgame:generate_banners/red
execute if score Red skygrid_values <= zero skygrid_hidden as @a[tag=red] run function skygridgame:tp_lobby
execute if score Red skygrid_values <= zero skygrid_hidden as @a[tag=red] run function skygridgame:destroy_banners/red