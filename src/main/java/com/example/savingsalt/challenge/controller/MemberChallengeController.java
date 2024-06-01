package com.example.savingsalt.challenge.controller;

import com.example.savingsalt.challenge.service.MemberChallengeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "MemberChallenge", description = "MemberChallenge API")
public class MemberChallengeController {
    private final MemberChallengeService memberChallengeService;
}
