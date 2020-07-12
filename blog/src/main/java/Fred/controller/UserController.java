package Fred.controller;

import Fred.model.Category;
import Fred.model.User;
import Fred.service.CategoryService;
import Fred.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/login")
    public String login(String username, String password, HttpServletRequest request) {
        if (username == null || password == null)
            return "login";
        User user = userService.login(username, password);
        if (user == null) {
            return "login";
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return "/";
        }
    }

    @RequestMapping("/register")
    public String register(String username, String password, String nickname, HttpServletRequest request, User user, Category category) {
        if (username == null || password == null)
            return "register";
        user.setNickname(nickname);
        user.setPassword(password);
        user.setUsername(username);
        user.setAvatar("https://picsum.photos/id/1/400/300");
        userService.insert(user);
        user = userService.login(username, password);
        category.setName("默认");
        category.setUserId(user.getId());
        categoryService.insert(category);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        return "/";
    }
}
