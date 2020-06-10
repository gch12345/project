package Fred.controller;

import Fred.model.Comment;
import Fred.model.User;
import Fred.service.CommentService;
import Fred.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class CommentsController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/a/{id}/comments", method = RequestMethod.POST)
    public String addComment(@PathVariable("id") Long id, String content, HttpSession session) {
        Comment comment = new Comment();
        comment.setArticleId(id);
        comment.setContent(content);
        comment.setCreatedAt(new Date());
        User user = (User) session.getAttribute("user");
        comment.setUser(user);
        comment.setUserId(user.getId());
        int num = commentService.insert(comment);
        return "redirect:/a/" + id;
    }
}
