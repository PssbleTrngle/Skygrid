execute if score Timer skygrid_hidden > zero skygrid_hidden run scoreboard players remove Timer skygrid_hidden 1

execute store result bossbar skygrid value run scoreboard players get Timer skygrid_hidden
bossbar set minecraft:skygrid players @a[team=!lobby]

schedule function skygridgame:second 1s