package org.demo.quartzpersistent.job;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component(value = "quartzManager")
public class QuartzManager {

    //@Autowired
    //private Scheduler scheduler;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    private static String DEFAULT_JOBGROUP_NAME = "DEFAULT_JOBGROUP_NAME";
    private static String DEFAULT_TRIGGERGROUP_NAME = "DEFAULT_TRIGGERGROUP_NAME";

    public void addJob(String jobName, String triggerName, Class jobClass, String cron) {
        addJob(jobName, DEFAULT_JOBGROUP_NAME, triggerName, DEFAULT_TRIGGERGROUP_NAME, jobClass, cron);
    }

    public void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, String cron) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName).withSchedule(cronScheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
            if(scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void removeJob(String jobName, String triggerName) {
        removeJob(jobName, DEFAULT_JOBGROUP_NAME, triggerName, DEFAULT_TRIGGERGROUP_NAME);
    }

    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            JobKey jobKey = new JobKey(jobName, jobGroupName);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void modifyJobCron(String jobName, String triggerName, String cron) {
        modifyJobCron(jobName, DEFAULT_JOBGROUP_NAME, triggerName, DEFAULT_TRIGGERGROUP_NAME, cron);
    }

    public void modifyJobCron(String jobName, String jobGroupName, String triggerName, String triggerGroupName, String cron) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = new JobKey(jobName, jobGroupName);
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            Class objClass = jobDetail.getJobClass();
            removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
            addJob(jobName, triggerName, objClass, cron);

            /*if(scheduler.checkExists(triggerKey)) {
                // 方式一：调用rescheduleJob修改任务触发时间
                //CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                //Trigger trigger = scheduler.getTrigger(triggerKey);
                //CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                //Trigger trigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
                //scheduler.rescheduleJob(triggerKey, trigger);
                //scheduler.resumeTrigger(triggerKey);

                // 方式二：先删除再创建一个新的Job
                //JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                //Class objClass = jobDetail.getJobClass();
                //removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
                //addJob(jobName, triggerName, objClass, cron);
            }*/
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void startJobs() {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            if(!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void shutdownJobs() {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            if(!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


}
