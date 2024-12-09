package org.crews.utils;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.UUID;

@Component
public class S3Util {

    private final S3Client s3Client;
    private final String bucketName = "crews-bucket";
    private final String cloudFrontDomain = "https://djogyo1sj025q.cloudfront.net"; // CloudFront 도메인

    public S3Util() {
        this.s3Client = S3Client.builder()
                .region(Region.AP_NORTHEAST_2) // 리전 설정
                .credentialsProvider(ProfileCredentialsProvider.create())
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
        return String.format("%s/%s", cloudFrontDomain, s3Key);
    }
}
