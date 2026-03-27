package com.csu.jpetstore.controller.catalog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    // 对应原来的 MAIN_FORM = /WEB-INF/jsp/catalog/main.jsp
    // 现在 thymeleaf 模板路径：templates/catalog/main.html
    private static final String MAIN_FORM = "catalog/main";

    @GetMapping("/mainForm")
    public String mainForm() {
        // 等价于 request.getRequestDispatcher(MAIN_FORM).forward
        return MAIN_FORM;
    }

    @PostMapping("/mainForm")
    public String mainFormPost() {
        return MAIN_FORM;
    }
}