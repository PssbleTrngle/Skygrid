execute as @a[tag=yellow] at @s run function skygridgame:death/notify
execute as @a[tag=!yellow,team=!lobby] at @s run function skygridgame:death/notify_others

scoreboard players remove Yellow skygrid_values 1
execute if score Yellow skygrid_values <= zero skygrid_hidden run clear @a yellow_banner
execute if score Yellow skygrid_values > zero skygrid_hidden positioned 0 50 0 run function skygridgame:generate_banners/yellow
execute if score Yellow skygrid_values <= zero skygrid_hidden as @a[tag=yellow] run function skygridgame:tp_lobby
execute if score Yellow skygrid_values <= zero skygrid_hidden as @a[tag=yellow] run function skygridgame:destroy_banners/yellow