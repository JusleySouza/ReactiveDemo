package com.ju.springboot.reactive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ju.springboot.reactive.vaccine.Vaccine;
import com.ju.springboot.reactive.vaccine.VaccineConsumer;
import com.ju.springboot.reactive.vaccine.VaccineProvider;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class ReactivedemoApplicationTests {
	
	@Autowired
	VaccineProvider provider;
	
	@Test
	void testVaccineProvider_reactive() {
		StepVerifier.create(provider.provideVaccines())
		.expectSubscription()
		.expectNext(new Vaccine("Pfizer"))
		.expectNext(new Vaccine("J&J"))
		.expectNext(new Vaccine("CoronaVac"))
		.expectComplete().verify();
	}
	
	@Test
	void testVaccineProvider_reactive_assertThat() {
		StepVerifier.create(provider.provideVaccines())
		.expectSubscription()
		.assertNext(vaccine -> {
			assertThat(vaccine.getName()).isNotNull();
			assertTrue(vaccine.isDelivered());
			assertEquals("Pfizer", vaccine.getName());
		})
		.expectNext(new Vaccine("J&J"))
		.expectNext(new Vaccine("CoronaVac"))
		.expectComplete().verify();
	}
	
	@Test
	void testVaccineProvider_reactive_expectNextCount() {
		StepVerifier.create(provider.provideVaccines())
		.expectSubscription()
		.expectNextCount(3)
		.expectComplete().verify();
	}
	
	@Test
	void testVaccineProvider() {
		provider.provideVaccines().subscribe(new VaccineConsumer());
	}

	@Test
	void testMono() {
		Mono<String> mono = Mono.just("MacBook Pro");
		mono.log().map(data -> data.toUpperCase()).subscribe(System.out::println);
	}

	@Test
	void testFlux() throws InterruptedException {
		
		Flux.fromIterable(Arrays.asList("MacBook Pro", "Iphone", "Dell","MacBook Pro", "Iphone", "Dell","MacBook Pro", "Iphone", "Dell"))
		//.delayElements(Duration.ofSeconds(2))
		.log().map(data -> data.toUpperCase())
		.subscribe(new Subscriber<String>() {
			
			private long count = 0;
			private Subscription subscription;

			@Override
			public void onSubscribe(Subscription subscription) {
				//subscription.request(Long.MAX_VALUE);
				this.subscription = subscription;
				subscription.request(3);
			}

			@Override
			public void onNext(String order) {
				count++;
				if(count >= 3) {
					count = 0;
					subscription.request(3);
				}
				System.out.println(order);
			}

			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onComplete() {
				System.out.println("Completely Done!!");
			}
		});
		
		//Thread.sleep(6000);
	}
	
}
