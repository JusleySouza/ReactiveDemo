package com.ju.springboot.reactive;

import java.time.Duration;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
class ReactivedemoApplicationTests {

	@Test
	void testMono() {
		Mono<String> mono = Mono.just("MacBook Pro");
		mono.log().map(data -> data.toUpperCase()).subscribe(System.out::println);
	}

	@Test
	void testFlux() throws InterruptedException {
		
		Flux.fromIterable(Arrays.asList("MacBook Pro", "Iphone", "Dell"))
		.delayElements(Duration.ofSeconds(2))
		.log().map(data -> data.toUpperCase())
		.subscribe(new OrderConsumer());
		
		Thread.sleep(6000);
	}
	
}
