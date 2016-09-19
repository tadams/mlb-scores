package com.tadams.controller;

import com.tadams.utils.VersionFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@Controller
public class HealthCheckController {

    protected static final String HEALTH_CHECK_VIEW = "healthCheck";
    private static final int BYTES_IN_MB = 1000;
    public static final int MULTIPLIER = 100;
    public static final String MEMORY_FORMAT = "%1$,d mb";

    protected OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

    @Autowired
    protected VersionFinder versionFinder;

    @Autowired
    ServletContext servletContext;

    @Autowired
    Environment env;

    @RequestMapping(value="HealthCheck", method = RequestMethod.GET)
    public ModelAndView healthCheck() throws IOException {
        ModelAndView modelAndView = new ModelAndView(HEALTH_CHECK_VIEW);
        modelAndView.addObject("version", versionFinder.getVersion());
        modelAndView.addObject("cpuCnt", osBean.getAvailableProcessors());
        modelAndView.addObject("cpuLoad", getSystemCpuLoad());
        modelAndView.addObject("memUse", getMemoryStatus());
        modelAndView.addObject("env", getActiveProfile());
        return modelAndView;
    }

    private String getMemoryStatus() {
        return  String.format(MEMORY_FORMAT, getInUseMemory() / BYTES_IN_MB) + " / " +
                String.format(MEMORY_FORMAT,Runtime.getRuntime().maxMemory() / BYTES_IN_MB);
    }

    private long getInUseMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    private String getSystemCpuLoad() {
        return String.format("%1$,.2f", osBean.getSystemLoadAverage() * MULTIPLIER);
    }

    private String getActiveProfile() {
        return Stream.concat(stream(env.getActiveProfiles()), stream(env.getDefaultProfiles()))
                  .findFirst()
                  .orElse("No Active Profiles");
    }
}
