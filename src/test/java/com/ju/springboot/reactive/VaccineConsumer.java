package com.ju.springboot.reactive;

import java.util.function.Consumer;

import com.ju.springboot.reactive.vaccine.Vaccine;

public class VaccineConsumer implements Consumer<Vaccine> {

	@Override
	public void accept(Vaccine vaccine) {
		System.out.println(vaccine.getName());
		System.out.println(vaccine.isDelivered());
	}

}
