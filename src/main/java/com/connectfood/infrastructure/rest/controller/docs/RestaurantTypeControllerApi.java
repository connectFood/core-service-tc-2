package com.connectfood.infrastructure.rest.controller.docs;

import java.util.List;
import java.util.UUID;

import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.restauranttype.RestaurantTypeRequest;
import com.connectfood.infrastructure.rest.dto.restauranttype.RestaurantTypeResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping("/v1/restaurants-types")
@Tag(name = "Restaurant type Controller", description = "Operations related to restaurant type management")
public interface RestaurantTypeControllerApi {

  @GetMapping
  ResponseEntity<PageResponse<List<RestaurantTypeResponse>>> search(
      @RequestParam(required = false) final String name,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size,
      @RequestParam(required = false) final String sort,
      @RequestParam(required = false) final String direction);

  @GetMapping("/{uuid}")
  ResponseEntity<BaseResponse<RestaurantTypeResponse>> find(@PathVariable final UUID uuid);

  @PostMapping
  ResponseEntity<BaseResponse<RestaurantTypeResponse>> create(
      @Valid @RequestBody final RestaurantTypeRequest request);

  @PutMapping(path = "/{uuid}")
  ResponseEntity<BaseResponse<RestaurantTypeResponse>> update(
      @PathVariable final UUID uuid, @Valid @RequestBody final RestaurantTypeRequest request);

  @DeleteMapping(path = "/{uuid}")
  ResponseEntity<Void> delete(@PathVariable final UUID uuid);
}
