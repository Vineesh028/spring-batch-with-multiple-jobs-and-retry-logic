package com.batch.sample.step;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.batch.sample.entity.CountryEntity;
import com.batch.sample.repository.CountryRepository;


@Component
public class CountryDataWriter implements ItemWriter<CountryEntity> {

	@Autowired
	CountryRepository repository;

	@Override
	public void write(List<? extends CountryEntity> items) throws Exception {
		
		repository.saveAll(items);
		
	}

}
