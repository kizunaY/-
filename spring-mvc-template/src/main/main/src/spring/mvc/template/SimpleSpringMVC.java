package spring.mvc.template;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
@Controller
public class SimpleSpringMVC {
    @RequestMapping("/Interval")
    @ResponseBody
    public Long time(){
        LocalDateTime dateTime=LocalDateTime.now();
        LocalDateTime target=LocalDateTime.of(2020, Month.MARCH,1,0,0);
        return Duration.between(dateTime,target).toDays();
    }
}
