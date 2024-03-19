package com.batch.sample.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class DataService{
	
	@Autowired
    JobLauncher jobLauncher;
 
    @Autowired
    Job userJob;
    
    @Autowired
    Job countryJob;

    @Scheduled(fixedRate = 5000)
	public void run() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		
    	runJob(userJob);
    	runJob(countryJob);
    
    }
    
    private void runJob(Job job) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException { 
    	
    	
            JobParameters jobParameters = new JobParametersBuilder() 
                    .addString("jobName", job.getName()+ System.currentTimeMillis()) 
                    .toJobParameters(); 
  
            JobExecution jobExecution = jobLauncher.run(job, jobParameters); 
            
  
    }

}
