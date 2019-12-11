package web.apis;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.services.CommentService;

import java.io.IOException;


@RestController
@RequestMapping("api/comments")
@AllArgsConstructor
public class CommentApi {

    @Autowired
    private CommentService commentService;


    @GetMapping("/update")
    public void updateComments() {
        commentService.readFromJson();
    }

    @GetMapping("/toJson")
    public void writeToJson() throws IOException {
        commentService.writeCommentToJson();
    }
}
