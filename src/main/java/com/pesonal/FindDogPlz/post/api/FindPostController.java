package com.pesonal.FindDogPlz.post.api;

import com.pesonal.FindDogPlz.global.auth.AuthMember;
import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.application.FindPostService;
import com.pesonal.FindDogPlz.post.dto.FindPostReqDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/find")
public class FindPostController {

    private final FindPostService findPostService;

    @PostMapping
    public ResponseEntity<String> createFindPost(@RequestBody @Valid FindPostReqDto dto, @AuthMember Member member) {
        if (member == null) throw new CustomException(ErrorCode.AUTHENTICATION_ERROR);
        findPostService.createFindPost(dto, member);
        return ResponseEntity.ok().body("완료");
    }

    @PutMapping
    public ResponseEntity<String> updateFindPost(@RequestParam Long id, @RequestBody @Valid FindPostReqDto dto, @AuthMember Member member) {
        if (member == null) throw new CustomException(ErrorCode.AUTHENTICATION_ERROR);
        findPostService.updateFindPost(id, dto, member);
        return ResponseEntity.ok().body("완료");
    }
}