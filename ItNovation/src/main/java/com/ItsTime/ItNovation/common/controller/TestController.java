package com.ItsTime.ItNovation.common.controller;

import com.ItsTime.ItNovation.common.dto.ApiResult;
import com.ItsTime.ItNovation.common.utils.ApiUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/person")
    public ApiResult<ArrayList<Person>> successApiFormat() {
        ArrayList<Person> people = new ArrayList<>();
        people.add(new Person("p1", 1));
        people.add(new Person("p2", 2));
        return ApiUtils.success(people);

    }

    @GetMapping("/error")
    public ApiResult failApiformat() {
        return ApiUtils.error("error", 400);
    }

}
