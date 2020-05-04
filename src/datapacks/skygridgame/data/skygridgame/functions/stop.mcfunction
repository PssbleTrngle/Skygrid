schedule clear skygridgame:peace
schedule clear skygridgame:battle

gamerule randomTickSpeed 0

scoreboard players set Timer skygrid_hidden 0
scoreboard objectives setdisplay sidebar
scoreboard players reset Red skygrid_values
scoreboard players reset Blue skygrid_values
scoreboard players reset Green skygrid_values
scoreboard players reset Yellow skygrid_values

scoreboard players enable @a join_yellow
scoreboard players enable @a join_red
scoreboard players enable @a join_green
scoreboard players enable @a join_blue

scoreboard players reset Round skygrid_hidden

function skygridgame:destroy_banners/all
kill @e[type=item]

execute as @a[team=!lobby] run function skygridgame:tp_lobby
