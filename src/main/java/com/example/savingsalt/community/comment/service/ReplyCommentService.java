package com.example.savingsalt.community.comment.service;

import com.example.savingsalt.community.comment.repository.CommentRepository;
import com.example.savingsalt.community.comment.repository.ReplyCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReplyCommentService {

    private final CommentRepository commentRepository;

    private final ReplyCommentRepository replyCommentRepository;

}
