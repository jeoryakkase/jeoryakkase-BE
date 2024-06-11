package com.example.savingsalt.config.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {
    private final AmazonS3 amazonS3Client;

    public S3Service(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    // 단일 파일 업로드
    public String upload(MultipartFile multipartFile) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String uploadFileName = multipartFile.getOriginalFilename() + "/" + timestamp;

        PutObjectRequest putObjectRequest = new PutObjectRequest(
            "my.eliceproject.s3.bucket",
            uploadFileName,
            multipartFile.getInputStream(),
            objectMetadata
        );

        amazonS3Client.putObject(putObjectRequest);

        return String.format(
            "https://s3.ap-southeast-2.amazonaws.com/my.eliceproject.s3.bucket/"
                + uploadFileName);
    }
}
