package com.connectfood.infrastructure.rest.controller;

import java.util.UUID;

import com.connectfood.infrastructure.AbstractIntegrationTest;
import com.connectfood.infrastructure.rest.dto.restauranttype.RestaurantTypeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(
    classes = RestaurantTypeControllerIntegrationTest.TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class RestaurantTypeControllerIntegrationTest extends AbstractIntegrationTest {

  private static final String BASE_PATH = "/v1/restaurants-types";

  @SpringBootApplication(scanBasePackages = "com.connectfood")
  static class TestApplication {
  }

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("POST /restaurants-types → should create restaurant type")
  void shouldCreateRestaurantType() throws Exception {

    final var request = new RestaurantTypeRequest(
        "Japanese",
        "Food from Japan"
    );

    mockMvc.perform(post(BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.content.uuid", notNullValue()))
        .andExpect(jsonPath("$.content.name", is("Japanese")));
  }

  @Test
  @DisplayName("GET /restaurants-types/{uuid} → should find restaurant type by uuid")
  void shouldFindRestaurantTypeByUuid() throws Exception {

    final UUID uuid = createRestaurantType("Italian");

    mockMvc.perform(get(BASE_PATH + "/{uuid}", uuid))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.content.uuid", is(uuid.toString())))
        .andExpect(jsonPath("$.content.name", is("Italian")));
  }

  @Test
  @DisplayName("GET /restaurants-types → should search restaurant types by name")
  void shouldSearchRestaurantTypes() throws Exception {

    createRestaurantType("Mexican");
    createRestaurantType("Mexican Grill");

    mockMvc.perform(get(BASE_PATH)
            .param("name", "mex")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", not(org.hamcrest.Matchers.empty())))
        .andExpect(jsonPath("$.content[*].name",
            hasItem(containsStringIgnoringCase("Mexican"))
        ))
        .andExpect(jsonPath("$.page", is(0)))
        .andExpect(jsonPath("$.size", is(10)));
  }

  @Test
  @DisplayName("PUT /restaurants-types/{uuid} → should update restaurant type")
  void shouldUpdateRestaurantType() throws Exception {

    final UUID uuid = createRestaurantType("Thai");

    final var updateRequest = new RestaurantTypeRequest(
        "Thai Food",
        "Updated description"
    );

    mockMvc.perform(put(BASE_PATH + "/{uuid}", uuid)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.content.uuid", is(uuid.toString())))
        .andExpect(jsonPath("$.content.name", is("Thai Food")));
  }

  @Test
  @DisplayName("DELETE /restaurants-types/{uuid} → should delete restaurant type")
  void shouldDeleteRestaurantType() throws Exception {

    final UUID uuid = createRestaurantType("French");

    mockMvc.perform(delete(BASE_PATH + "/{uuid}", uuid))
        .andExpect(status().isNoContent());

    mockMvc.perform(get(BASE_PATH + "/{uuid}", uuid))
        .andExpect(status().isNotFound());
  }

  private UUID createRestaurantType(final String name) throws Exception {
    final var request = new RestaurantTypeRequest(name, "desc");

    final var result = mockMvc.perform(post(BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andReturn();

    return JsonTestUtils.readUuid(
        result.getResponse()
            .getContentAsString(),
        "$.content.uuid"
    );
  }
}
