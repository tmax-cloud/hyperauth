package com.tmax.hyperauth;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class Main {
	public static void main(String[] args) {
		try {
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");
			System.out.println(" Main Reached !!!!!!!!!!!!!!!!!");

			JobDetail job = JobBuilder.newJob( UserDeleteJob.class )
					.withIdentity( "UserDeleteJob" ).build();

			CronTrigger cronTrigger = TriggerBuilder
					.newTrigger()
					.withIdentity( "UserDeleteCronTrigger" )
					.withSchedule(
							CronScheduleBuilder.cronSchedule( "*/10 * * * * ?" ))
					.build();

			Scheduler sch = new StdSchedulerFactory().getScheduler();
			sch.start(); sch.scheduleJob( job, cronTrigger );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}