package org.example.beer.inventory.service.web.controllers;

import org.example.beer.inventory.service.repositories.BeerInventoryRepository;
import org.example.beer.inventory.service.web.mappers.BeerInventoryMapper;
import org.example.brewery.model.BeerInventoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerInventoryController {
    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerInventoryMapper beerInventoryMapper;

    @GetMapping("/api/v1/beer/{beerId}/inventory")
    List<BeerInventoryDto> listBeersById(@PathVariable UUID beerId){
        log.debug("Finding inventory for beerId: {}", beerId);

        List<BeerInventoryDto> test = beerInventoryRepository.findAllByBeerId(beerId)
                .stream().map(beerInventoryMapper::beerInventoryToDto)
                .collect(Collectors.toList());

        return test;
    }

}
