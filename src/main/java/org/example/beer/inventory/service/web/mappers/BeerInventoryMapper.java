package org.example.beer.inventory.service.web.mappers;

import org.example.beer.inventory.service.domain.BeerInventory;
import org.example.brewery.model.BeerInventoryDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerInventoryMapper {
    BeerInventory dtoToBeerInventory(BeerInventoryDto dto);

    BeerInventoryDto beerInventoryToDto(BeerInventory beerInventory);
}
