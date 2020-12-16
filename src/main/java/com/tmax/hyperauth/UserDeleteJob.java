package com.tmax.hyperauth;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class UserDeleteJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(" Timer Wake Up !!!!!");
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
