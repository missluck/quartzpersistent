package org.demo.quartzpersistent.controller;

import org.demo.quartzpersistent.job.MyJob;
import org.demo.quartzpersistent.job.QuartzManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zhaol on 2017/10/31.
 */
@Controller
public class TestController {

    @Autowired
    @Qualifier(value = "quartzManager")
    private QuartzManager quartzManager;

    @RequestMapping(value = "/addJob", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String testAddJob() {
        quartzManager.addJob("testJobName", "testTriggerName", MyJob.class, "0/3 * * * * ?");
        return "success";
    }

    @RequestMapping(value = "/modifyJob", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String testModifyJob() {
        quartzManager.modifyJobCron("testJobName", "testTriggerName", "0/6 * * * * ?");
        return "success";
    }

    @RequestMapping(value = "/removeJob", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String testRemoveJob() {
        quartzManager.removeJob("testJobName", "testTriggerName");
        return "success";
    }

}
