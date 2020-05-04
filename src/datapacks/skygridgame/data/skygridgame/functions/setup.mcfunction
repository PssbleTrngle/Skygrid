team add all
team modify all seeFriendlyInvisibles false
team add lobby

team modify lobby friendlyFire false
team modify lobby collisionRule never
team modify lobby seeFriendlyInvisibles true
team modify lobby prefix "[Lobby] "

team add blue
team modify blue color blue
team modify blue friendlyFire false
team modify blue collisionRule pushOtherTeams

team add yellow
team modify yellow color yellow
team modify yellow friendlyFire false
team modify yellow collisionRule pushOtherTeams

team add green
team modify green color green
team modify green friendlyFire false
team modify green collisionRule pushOtherTeams

team add red
team modify red color red
team modify red friendlyFire false
team modify red collisionRule pushOtherTeams

scoreboard objectives add health health "Health"
scoreboard objectives setdisplay list health

scoreboard objectives add skygrid_values dummy "Skygrid"

scoreboard objectives add age dummy "Age" 

scoreboard objectives add skygrid_hidden dummy "Values"
scoreboard players set zero skygrid_hidden 0
scoreboard players set threshold skygrid_hidden 10
scoreboard players set min_per_team skygrid_hidden 0
scoreboard players set min_teams skygrid_hidden 2

bossbar add skygrid ""
bossbar set skygrid style progress
bossbar set skygrid visible false

gamerule doWeatherCycle false
gamerule doDaylightCycle false
gamerule sendCommandFeedback false
gamerule doImmediateRespawn true
difficulty peaceful

scoreboard objectives add broken_red minecraft.mined:minecraft.red_banner
scoreboard objectives add broken_blue minecraft.mined:minecraft.blue_banner
scoreboard objectives add broken_green minecraft.mined:minecraft.green_banner
scoreboard objectives add broken_yellow minecraft.mined:minecraft.yellow_banner

scoreboard objectives add join_yellow trigger
scoreboard objectives add join_red trigger
scoreboard objectives add join_green trigger
scoreboard objectives add join_blue trigger

scoreboard objectives add died deathCount

scoreboard objectives add skygrid_settings dummy "Settings"

execute if score Falldamage skygrid_settings > zero skygrid_hidden run gamerule fallDamage true
execute unless score Falldamage skygrid_settings > zero skygrid_hidden run gamerule fallDamage false

schedule function skygridgame:second 1t