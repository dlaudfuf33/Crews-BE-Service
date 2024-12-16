package org.crews.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.UUID;

@Component
public class S3Util {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;
    @Value("${cloud.aws.cloud-front}")
    private String cloudFrontDomain;
    private S3Client s3Client;

    @PostConstruct
    private void init() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3Client = S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    public String uploadToS3(byte[] data, String s3Key) {
        // S3에 업로드 요청
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3Key)
                        .contentType("image/png")
                        .build(),
                RequestBody.fromBytes(data)
        );
        return s3Key;
    }

    public void deleteAllFilesInFolder(String folderPath) {
        // 특정 폴더 내 모든 파일 삭제
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(folderPath + "/")
                .build();

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);
        List<S3Object> objects = listResponse.contents();

        for (S3Object object : objects) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(object.key())
                    .build());
        }
    }

    public String generateUniqueFileName(String folderPath, String fileName) {
        // UUID를 포함한 고유한 파일 이름 생성
        return String.format("%s/%s-%s", folderPath, UUID.randomUUID(), fileName);
    }

    public String generateCloudFrontUrl(String s3Key) {
        // CloudFront URL 생성
        return String.format("%s%s", cloudFrontDomain, s3Key);
    }
}
