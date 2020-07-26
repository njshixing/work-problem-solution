package cn.raulism.schedule;

import cn.raulism.common.WrapperResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * spring boot code generator
 */
@RestController
@Slf4j
public class ScheduleController {
    @Autowired
    private ScheduleTask scheduleTask;

    @GetMapping("/addTask")
    public WrapperResponse addTask(@NotBlank(message = "编码不能为空") String code, @NotBlank(message = "cron不能为空") String cron) {
        if (ScheduleTask.scheduledFutureMap.containsKey(code)) {
            return WrapperResponse.fail("fail");
        }
        scheduleTask.setCron(cron);
        scheduleTask.setPlanCode(code);
        ScheduleTask.start(scheduleTask);
        return WrapperResponse.success("success");
    }

    @GetMapping("/editTask")
    public WrapperResponse editTask(@NotBlank(message = "编码不能为空") String code, @NotBlank(message = "cron不能为空") String cron) {
        scheduleTask.setCron(cron);
        scheduleTask.setPlanCode(code);
        ScheduleTask.reset(scheduleTask);
        return WrapperResponse.success("success");
    }

    @GetMapping("/delTask")
    public WrapperResponse delTask(@NotBlank(message = "编码不能为空") String code, @NotBlank(message = "cron不能为空") String cron) {
        scheduleTask.setCron(cron);
        scheduleTask.setPlanCode(code);
        ScheduleTask.cancel(scheduleTask);
        return WrapperResponse.success("success");
    }
}
