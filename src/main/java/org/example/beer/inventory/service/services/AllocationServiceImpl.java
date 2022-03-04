package org.example.beer.inventory.service.services;

import org.example.brewery.model.BeerOrderDto;
import org.example.brewery.model.BeerOrderLineDto;
import org.example.beer.inventory.service.domain.BeerInventory;
import org.example.beer.inventory.service.repositories.BeerInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class AllocationServiceImpl implements AllocationService {

    private final BeerInventoryRepository repository;

    @Override
    public Boolean allocateOrder(BeerOrderDto beerOrderDto) {
        log.debug("Allocating|OrderId:{}", beerOrderDto.getId());

        AtomicInteger totalOrdered = new AtomicInteger();
        AtomicInteger totalAllocated = new AtomicInteger();

        beerOrderDto.getBeerOrderLines().forEach(line -> {
            int quantityAllocated = line.getQuantityAllocated() != null ? line.getQuantityAllocated() : 0;
            int orderQuantity = line.getOrderQuantity() != null ? line.getOrderQuantity() : 0;

            if ((orderQuantity - quantityAllocated) > 0) {
                allocateBeerOrderLine(line);
            }

            totalOrdered.set(totalOrdered.get() + line.getOrderQuantity());
            totalAllocated.set(totalAllocated.get() + quantityAllocated);
        });

        log.debug("Total Ordered: {}| Total Allocated: {}", totalOrdered.get(), totalAllocated.get());

        return totalOrdered.get() == totalAllocated.get();
    }

    @Override
    public void deallocateOrder(BeerOrderDto beerOrderDto) {
        beerOrderDto.getBeerOrderLines().forEach(beerOrderLineDto -> {
            BeerInventory beerInventory = BeerInventory.builder()
                    .beerId(beerOrderLineDto.getBeerId())
                    .upc(beerOrderLineDto.getUpc())
                    .quantityOnHand(beerOrderLineDto.getQuantityAllocated())
                    .build();

            BeerInventory savedInventory = repository.save(beerInventory);

            log.debug("Saved Inventory for beer upc: {}| inventory id: {}", savedInventory.getUpc(), savedInventory.getId());
        });
    }

    private void allocateBeerOrderLine(BeerOrderLineDto line) {
        List<BeerInventory> beerInventoryList = repository.findAllByUpc(line.getUpc());

        beerInventoryList.forEach(beerInventory -> {
            int inventory = beerInventory.getQuantityOnHand() == null ? 0 : beerInventory.getQuantityOnHand();
            int orderQty = line.getOrderQuantity() == null ? 0 : line.getOrderQuantity();
            int allocatedQty = line.getQuantityAllocated() == null ? 0 : line.getQuantityAllocated();
            int qtyToAllocate = orderQty - allocatedQty;

            if (inventory >= qtyToAllocate) { //full allocation
                inventory = inventory - qtyToAllocate;
                line.setQuantityAllocated(orderQty);
                beerInventory.setQuantityOnHand(inventory);

                repository.saveAndFlush(beerInventory);
            } else if (inventory > 0) { //partial allocation
                line.setQuantityAllocated(allocatedQty + inventory);
                beerInventory.setQuantityOnHand(0);
            }

            if(beerInventory.getQuantityOnHand() == 0){
                repository.delete(beerInventory);
            }
        });
    }
}
