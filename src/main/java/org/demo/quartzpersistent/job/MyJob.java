package org.demo.quartzpersistent.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by zhaol on 2017/10/31.
 */

public class MyJob implements Job{

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Say Hello World!");
    }
}
