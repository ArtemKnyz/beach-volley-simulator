package org.beachvolley.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VolleyController {
    @GetMapping("/play")
    public String play() {
        // ваш код симуляции
        return "Симуляция завершена. Смотрите консоль.";
    }
}
