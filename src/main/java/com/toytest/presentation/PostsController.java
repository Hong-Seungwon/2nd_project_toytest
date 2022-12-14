package com.toytest.presentation;

import com.toytest.application.PostsService;
import com.toytest.application.dto.CommentDto;
import com.toytest.application.dto.PostsDto;
import com.toytest.application.dto.UserDto;
import com.toytest.application.security.auth.LoginUser;
import com.toytest.domain.Posts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostsController {
    private final PostsService postsService;

    // 글 목록
    @GetMapping("/")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable, @LoginUser UserDto.Response user) {
        Page<Posts> list = postsService.pageList(pageable);

        if (user != null) {
            model.addAttribute("user", user);
        }

        ArrayList pageIndex = new ArrayList();
        for(int i = 0; i < list.getTotalPages(); i++) pageIndex.add(i+1);

        int previous = pageable.previousOrFirst().getPageNumber()+1;
        int next = pageable.next().getPageNumber()+1;

        model.addAttribute("posts", list);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("previous", previous);
        model.addAttribute("next", next);
        model.addAttribute("hasNext", list.hasNext());
        model.addAttribute("hasPrev", list.hasPrevious());

        return "index";
    }

    // 글 작성
    @GetMapping("/posts/write")
    public String write(@LoginUser UserDto.Response user, Model model) {
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "posts/posts-write";
    }

    /* 글 상세보기 */
    @GetMapping("/posts/read/{id}")
    public String read(@PathVariable Long id, @LoginUser UserDto.Response user, Model model) {
        PostsDto.Response dto = postsService.findById(id);
        List<CommentDto.Response> comments = dto.getComments();

        /* 댓글 관련 */
        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        /* 사용자 관련 */
        if (user != null) {
            model.addAttribute("user", user);

            /* 게시글 작성자 본인인지 확인 */
            if (dto.getUserNo().equals(user.getNo())) {
                model.addAttribute("writer", true);
            }

            /* 댓글 작성자 본인인지 확인 */
            if (comments.stream().anyMatch(s -> s.getUserNo().equals(user.getNo()))) {
                model.addAttribute("isWriter", true);
            }
 /*             for (int i = 0; i < comments.size(); i++) {
                boolean isWriter = comments.get(i).getUserNo().equals(user.getNo());
                model.addAttribute("isWriter",isWriter);
            }*/
        }

        postsService.updateView(id); // views++
        model.addAttribute("posts", dto);
        model.addAttribute("user", user);
        return "posts/posts-read";
    }

    // 글 수정
    @GetMapping("/posts/update/{id}")
    public String update(@PathVariable Long id, @LoginUser UserDto.Response user, Model model) {
        PostsDto.Response dto = postsService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("posts", dto);

        return "posts/posts-update";
    }

    // 키워드로 글 검색
    @GetMapping("/posts/search")
    public String search(String keyword, Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable, @LoginUser UserDto.Response user) {
        Page<Posts> searchList = postsService.search(keyword, pageable);

        if (user != null) {
            model.addAttribute("user", user);
        }


        ArrayList pageIndex = new ArrayList();
        for(int i = 0; i < searchList.getTotalPages(); i++) pageIndex.add(i+1);

        int previous = pageable.previousOrFirst().getPageNumber()+1;
        int next = pageable.next().getPageNumber()+1;

        model.addAttribute("searchList", searchList);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("keyword", keyword);
        model.addAttribute("previous", previous);
        model.addAttribute("next", next);
        model.addAttribute("hasNext", searchList.hasNext());
        model.addAttribute("hasPrev", searchList.hasPrevious());

        return "posts/posts-search";
    }
}
