gamerule doTileDrops false
worldborder center 0 0

function skygridgame:clear_top
fill -32 36 -32 32 40 32 air

skygrid generate -32 40 -32 32 66 32 skygridgame:mid
schedule function skygridgame:clear_items 1t
schedule function skygridgame:generate_bottom 2s