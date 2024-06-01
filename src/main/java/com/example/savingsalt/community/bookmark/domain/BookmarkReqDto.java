package com.example.savingsalt.community.bookmark.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkReqDto {
    private Long boardId;
    private Long memberId;
}
