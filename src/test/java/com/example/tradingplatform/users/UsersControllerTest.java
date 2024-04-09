package com.example.tradingplatform.users;

import com.example.tradingplatform.orders.dtos.request.CreateOrderRequestDto;
import com.example.tradingplatform.orders.dtos.response.CreateOrderResponseDto;
import com.example.tradingplatform.shared.externalservices.h2db.model.Orders;
import com.example.tradingplatform.shared.externalservices.h2db.model.Trade;
import com.example.tradingplatform.shared.externalservices.h2db.repositories.ITradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.example.tradingplatform.shared.miscellaneous.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class UsersControllerTest {

    private final static String TOKEN = "Bearer admin";

    @Autowired
    private WebTestClient webTestClient;
    private CreateOrderRequestDto diamondRequest;
    private CreateOrderRequestDto paperRequest;
    private CreateOrderRequestDto ironRequest;

    @BeforeEach
    void setUp()
    {
        // “Diamond” puts a BUY order for security “WSB” with a price of 101 and quantity of 50
        diamondRequest = CreateOrderRequestDto.builder()
                .securityName("WSB")
                .username("Diamond")
                .price(101)
                .quantity(50)
                .build();

        // “Paper” puts a sell order for security “WSB” with a price of 100 and a quantity of 100
        paperRequest = CreateOrderRequestDto.builder()
                .securityName("WSB")
                .username("Paper")
                .price(100)
                .quantity(100)
                .build();

        // "Iron" for additional test
        ironRequest = CreateOrderRequestDto.builder()
                .securityName("WSB")
                .username("Iron")
                .price(90)
                .quantity(40)
                .build();
    }


    @Test
    void givenDiamondPurchaseAndPaperSale_whenCallingTradeGenerator_thenCreateTrade() {
        createOrder(BUY_ORDER_PATH, diamondRequest) // diamond order (n1)
                .flatMap(buyRes ->
                        createOrder(SELL_ORDER_PATH, paperRequest) // paper order (n2)
                                .flatMap(sellRes -> getTradeFromOrderIds(buyRes.getId(), sellRes.getId())))
                .doOnNext(trade -> {
                    assertEquals(1, trade.getBuy_order_id()); // diamond order (n1)
                    assertEquals(2, trade.getSell_order_id()); // paper order (n2)
                    assertEquals(paperRequest.getPrice(), trade.getPrice());
                    assertEquals(diamondRequest.getQuantity(), trade.getQuantity());
                })
                .flatMap(res -> Mono.zip(getOrderInfo(1), getOrderInfo(2)))
                .doOnNext(tuple -> {
                    // Assertions for Orders data after Trade
                    // diamond order (n1)
                    assertEquals(0, tuple.getT1().getQuantity());
                    assertEquals(true, tuple.getT1().getFull_filled());
                    // paper order (n2)
                    assertEquals(50, tuple.getT2().getQuantity());
                    assertEquals(false, tuple.getT2().getFull_filled());
                })
                .subscribe();
    }


    @Test
    void givenPaperSaleAndDiamondPurchase_whenCallingTradeGenerator_thenCreateTrade() {
        createOrder(SELL_ORDER_PATH, paperRequest) // paper order (n1)
                .flatMap(sellRes ->
                        createOrder(BUY_ORDER_PATH, diamondRequest) // diamond order (n2)
                                .flatMap(buyRes -> getTradeFromOrderIds(sellRes.getId(), buyRes.getId())))
                .doOnNext(trade -> {
                    assertEquals(2, trade.getBuy_order_id()); // diamond order (n2)
                    assertEquals(1, trade.getSell_order_id()); // paper order (n1)
                    assertEquals(paperRequest.getPrice(), trade.getPrice());
                    assertEquals(diamondRequest.getQuantity(), trade.getQuantity());
                })
                .flatMap(res -> Mono.zip(getOrderInfo(1), getOrderInfo(2)))
                .doOnNext(tuple -> {
                    // Assertions for Orders data after Trade
                    // paper order (n1)
                    assertEquals(50, tuple.getT1().getQuantity());
                    assertEquals(false, tuple.getT1().getFull_filled());
                    // diamond order (n2)
                    assertEquals(0, tuple.getT2().getQuantity());
                    assertEquals(true, tuple.getT2().getFull_filled());
                })
                .subscribe();
    }

    @Test
    void givenIronAndPaperSales_whenCallingTradeGenerator_thenGeneratesTradeBetweenIronAndDiamond() {
        createOrder(SELL_ORDER_PATH, ironRequest) // order id n1
                .flatMap(ironSaleRes ->
                        createOrder(SELL_ORDER_PATH, paperRequest) // order id n2
                                .flatMap(paperSaleRes -> createOrder(BUY_ORDER_PATH, diamondRequest) // order id n3
                                        .flatMap(buyRes -> getTradeFromOrderIds(ironSaleRes.getId(), buyRes.getId()))))
                .doOnNext(trade -> {
                    // Assertions for generated Trade
                    assertEquals(3, trade.getBuy_order_id()); // order id n3
                    assertEquals(1, trade.getSell_order_id()); // order id n1
                    assertEquals(ironRequest.getPrice(), trade.getPrice());
                    assertEquals(ironRequest.getQuantity(), trade.getQuantity());
                })
                .flatMap(res -> Mono.zip(getOrderInfo(1), getOrderInfo(3)))
                .doOnNext(tuple -> {
                    // Assertions for Orders data after Trade
                    // iron order
                    assertEquals(0, tuple.getT1().getQuantity());
                    assertEquals(true, tuple.getT1().getFull_filled());
                    // diamond order
                    assertEquals(10, tuple.getT2().getQuantity());
                    assertEquals(false, tuple.getT2().getFull_filled());
                })
                .subscribe();
    }

    /**
     * Method to create an order given PATH and request body
     * @param path endpoint path
     * @param requestBody request body
     * @return Mono containing CreateOrderResponseDto
     */
    private Mono<CreateOrderResponseDto> createOrder(
            final String path,
            final CreateOrderRequestDto requestBody
    ) {
        return webTestClient
                .post()
                .uri(V1 + path).accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .body(Mono.just(requestBody), CreateOrderRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(CreateOrderResponseDto.class).getResponseBody().single();
    }

    /**
     * Method to get Trade
     * @param buyOrderId 'buy' order identifier
     * @param sellOrderId 'sell' order identifier
     * @return Mono containing Trade
     */
    private Mono<Trade> getTradeFromOrderIds(final int buyOrderId, final int sellOrderId) {

        return webTestClient
                .get()
                .uri(V1 + GET_TRADE_BY_ORDER_IDS_PATH, buyOrderId, sellOrderId).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Trade.class).getResponseBody().single();
    }

    /**
     * Method to get order data
     * @param orderId order identifier
     * @return Mono containing Order
     */
    private Mono<Orders> getOrderInfo(final int orderId) {

        return webTestClient
                .get()
                .uri(V1 + GET_ORDER_PATH, orderId).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Orders.class).getResponseBody().single();
    }
}
