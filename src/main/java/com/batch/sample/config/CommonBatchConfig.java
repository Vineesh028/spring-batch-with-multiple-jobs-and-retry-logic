package com.batch.sample.config;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;

import com.batch.sample.entity.CountryEntity;
import com.batch.sample.entity.UserEntity;
import com.batch.sample.listener.CountryRetryListener;
import com.batch.sample.listener.UserRetryListener;
import com.batch.sample.model.Country;
import com.batch.sample.model.CountryDeserializer;
import com.batch.sample.model.User;
import com.batch.sample.model.UserDeserializer;
import com.batch.sample.step.CountryDataProcessor;
import com.batch.sample.step.CountryDataWriter;
import com.batch.sample.step.UserDataProcessor;
import com.batch.sample.step.UserDataWriter;

@Configuration
@EnableBatchProcessing
public class CommonBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${app.kafka.batch.size}")
	private String batchSize;

	@Value("${app.topic.batch}")
	private String userTopic;

	@Value("${app.topic.second.batch}")
	private String countryTopic;

	@Autowired
	private UserDataProcessor userDataProcessor;

	@Autowired
	private UserDataWriter userDataWriter;

	@Autowired
	private CountryDataProcessor countryDataProcessor;

	@Autowired
	private CountryDataWriter countryDataWriter;

	@Bean
	public Step userStep() {
		return stepBuilderFactory.get("userStep").<User, UserEntity>chunk(10).reader(userReader())
				.processor(userDataProcessor).writer(userDataWriter).faultTolerant().retryLimit(3)
				.retry(Exception.class).backOffPolicy(new ExponentialBackOffPolicy()).listener(new UserRetryListener())
				.build();
	}

	@Bean
	public Job userJob() {
		return jobBuilderFactory.get("userJob").incrementer(new RunIdIncrementer()).start(userStep()).build();
	}

	@Bean
	public Step countryStep() {

		return stepBuilderFactory.get("countryStep").<Country, CountryEntity>chunk(10).reader(countryReader())
				.processor(countryDataProcessor).writer(countryDataWriter)
				.faultTolerant()
				.retryLimit(3)
				.retry(Exception.class)
				.backOffPolicy(new ExponentialBackOffPolicy())
				.listener(new CountryRetryListener())
				.build();
	}

	@Bean
	public Job countryJob() {
		return jobBuilderFactory.get("countryJob").incrementer(new RunIdIncrementer()).start(countryStep()).build();
	}

	@Bean
	KafkaItemReader<String, User> userReader() {
		Properties props = new Properties();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserDeserializer.class);
		props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "batch");
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, batchSize);
		// props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, pollInterval);

		return new KafkaItemReaderBuilder<String, User>().partitions(0).consumerProperties(props).name("user-reader")
				.saveState(true).topic(userTopic).build();
	}

	@Bean
	KafkaItemReader<String, Country> countryReader() {
		Properties props = new Properties();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CountryDeserializer.class);
		props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "batch");
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, batchSize);

		return new KafkaItemReaderBuilder<String, Country>().partitions(0).consumerProperties(props).name("user-reader")
				.saveState(true).topic(countryTopic).build();
	}

}