package com.example.beer.inventory.service.web.mappers;

import com.example.beer.inventory.service.domain.BeerInventory;
import com.example.brewery.model.BeerInventoryDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerInventoryMapper {
    BeerInventory dtoToBeerInventory(BeerInventoryDto dto);

    BeerInventoryDto beerInventoryToDto(BeerInventory beerInventory);
}
