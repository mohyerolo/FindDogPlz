package com.pesonal.FindDogPlz.post.api;

import com.pesonal.FindDogPlz.global.auth.AuthMember;
import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.application.LostPostService;
import com.pesonal.FindDogPlz.post.dto.LostPostDetailDto;
import com.pesonal.FindDogPlz.post.dto.LostPostOutlineDto;
import com.pesonal.FindDogPlz.post.dto.LostPostReqDto;
import com.pesonal.FindDogPlz.post.dto.LostPostUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/lost")
public class LostPostController {

    private final LostPostService lostPostService;

    @PostMapping
    public ResponseEntity<String> createLostPost(@RequestBody @Valid LostPostReqDto dto, @AuthMember Member member) {
        if (member == null) {
            throw new CustomException(ErrorCode.AUTHENTICATION_ERROR);
        }
        lostPostService.createLostPost(dto, member);
        return ResponseEntity.ok().body("완료");
    }

    @PutMapping
    public ResponseEntity<Long> updateLostPost(@RequestParam Long id, @RequestBody @Valid LostPostUpdateDto dto, @AuthMember Member member) {
        if (member == null) {
            throw new CustomException(ErrorCode.AUTHENTICATION_ERROR);
        }
        return ResponseEntity.ok(lostPostService.updateLostPost(id, dto, member));
    }

    @GetMapping("/all")
    public ResponseEntity<Slice<LostPostOutlineDto>> searchAllByNoOffset(@RequestParam(required = false) Long lastLostPostId,
                                                                         @RequestParam(required = false, defaultValue = "9") int size) {
        return ResponseEntity.ok(lostPostService.getAllLostPost(lastLostPostId, size));
    }

    @GetMapping
    public ResponseEntity<LostPostDetailDto> getLostPostById(@RequestParam Long id) {
        return ResponseEntity.ok(lostPostService.getLostPostById(id));
    }

    @PutMapping("/end")
    public ResponseEntity<String> closeLostPost(@RequestParam Long id, @AuthMember Member member) {
        lostPostService.closeLostPost(id, member);
        return ResponseEntity.ok().body("완료");
    }
}
