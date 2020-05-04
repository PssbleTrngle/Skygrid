schedule clear skygridgame:battle

function skygridgame:generate_top

gamerule naturalRegeneration false

effect clear @a[team=!lobby] minecraft:invisibility

team modify all collisionRule always
team modify all friendlyFire true

team join red @a[team=all,tag=red]
team join yellow @a[team=all,tag=yellow]
team join green @a[team=all,tag=green]
team join blue @a[team=all,tag=blue]

title @a[team=!lobby] times 3 15 3
title @a[team=!lobby] title ""
title @a[team=!lobby] subtitle [{"text":"Battle!","color":"red"}]

spawnpoint @a[tag=red] -4 61 0
spawnpoint @a[tag=yellow] 0 61 4
spawnpoint @a[tag=blue] 4 61 0
spawnpoint @a[tag=green] 0 61 -4

execute if score LastPeace skygrid_settings > Round skygrid_hidden run scoreboard players set Timer skygrid_hidden 60
execute unless score LastPeace skygrid_settings > Round skygrid_hidden run scoreboard players set Timer skygrid_hidden 0
execute if score LastPeace skygrid_settings > Round skygrid_hidden run execute store result bossbar skygrid max run scoreboard players get Timer skygrid_hidden
execute if score LastPeace skygrid_settings > Round skygrid_hidden run schedule function skygridgame:peace 60s

bossbar set skygrid name "Time until peace"
bossbar set skygrid color green

time set midnight
worldborder set 95