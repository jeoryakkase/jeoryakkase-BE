package com.example.savingsalt.config.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    // 다중 파일 업로드
    public List<String> uploads(List<MultipartFile> multipartFiles) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        String timestamp = String.valueOf(System.currentTimeMillis());

        for (MultipartFile file : multipartFiles) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest;

            String uploadFileName = file.getOriginalFilename() + "/" + timestamp;
            // test01.jpg/0043885293124
            // https://s3.ap-southeast-2.amazonaws.com/my.eliceproject.s3.bucket/test01.jpg/0043885293124

            putObjectRequest = new PutObjectRequest(
                "my.eliceproject.s3.bucket",
                uploadFileName,
                file.getInputStream(),
                objectMetadata
            );

            amazonS3Client.putObject(putObjectRequest);

            String imageUrl = String.format(
                "https://s3.ap-southeast-2.amazonaws.com/my.eliceproject.s3.bucket/"
                    + uploadFileName);

            imageUrls.add(imageUrl);
        }

        return imageUrls;
    }

    public void deleteFile(String imageUrl) throws IOException {
        try {
            amazonS3Client.deleteObject("my.eliceproject.s3.bucket", imageUrl);
        } catch (SdkClientException e) {
            throw new IOException("S3 " + imageUrl + " 객체 삭제에 에러가 발생했습니다.", e);
        }
    }

    public void deleteFiles(List<String> imageUrls) throws IOException {
        for (String imageUrl : imageUrls) {
            try {
                amazonS3Client.deleteObject("my.eliceproject.s3.bucket", imageUrl);
            } catch (SdkClientException e) {
                throw new IOException("S3 " + imageUrl + " 객체 삭제에 에러가 발생했습니다.", e);
            }
        }
    }
}
