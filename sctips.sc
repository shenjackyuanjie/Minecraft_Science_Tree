

__config() -> {
    'scope' -> 'global',
    'stay_loaded' -> true,

};

__on_tick() -> (
    wandering_trader = entity_selector('@e[type=minecraft:wandering_trader]');
    for(wandering_trader,
        have_effect = query(_, 'effect', 'glowing');
        if(!have_effect,
            modify(_, 'effect', 'glowing', '60')))
)

__command() -> (
    wandering_trader = entity_selector('@e[type=minecraft:wandering_trader]');
    for(wandering_trader,
    print(query(_, 'location'));)
)

create_datapack('test_datapack', {
    'data' -> {
        'minecraft' -> {
            'tags' -> {
                'functions' -> {
                    'load.json' -> {
                        'values' -> [
                            'shenjack:start'
                        ]
                    }
                }
            }
        }
    }
});

script run create_datapack('test_datapack', {'data' -> {'minecraft' -> {'tags' -> {'functions' -> {'load.json' -> {'values' -> ['shenjack:start']}}}},'shenjack' -> {'functions' -> {'start.mcfuntion' -> 'spawn rates ambient 0\nkill @e[type=bat]'}}}});