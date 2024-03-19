package com.batch.sample.step;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.batch.sample.entity.CountryEntity;
import com.batch.sample.model.Country;

@Component
public class CountryDataProcessor implements ItemProcessor<Country, CountryEntity> {
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public CountryEntity process(Country country) throws Exception {
		CountryEntity countryEntity = modelMapper.map(country, CountryEntity.class);
		return countryEntity;
	}

}
