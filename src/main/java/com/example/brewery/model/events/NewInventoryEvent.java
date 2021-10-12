/**
 * Copyright (c) 2021 Planet Payment
 * Long Beach, NY, U.S.A.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of
 * Planet Payment ("Confidential Information").
 */

package com.example.brewery.model.events;

import com.example.brewery.model.BeerDto;
import lombok.NoArgsConstructor;

/**
 * NewInventoryEvent
 *
 * @author Hugo.Freitas
 * @date 10/12/2021
 **/
@NoArgsConstructor
public class NewInventoryEvent extends BeerEvent {

  private static final long serialVersionUID = 5334835717215093237L;

  public NewInventoryEvent(BeerDto beerDto) {
    super(beerDto);
  }
}
