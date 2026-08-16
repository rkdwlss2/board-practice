package com.example.boardpractice.service;

import com.example.boardpractice.common.FileConstant;
import com.example.boardpractice.common.utill.FileUtil;
import com.example.boardpractice.config.S3Config;
import com.example.boardpractice.repository.UserRepository;
import com.example.boardpractice.web.dto.file.FileInfoDto;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {

    private final S3Client s3Client;

    private final S3Config s3Config;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${upload.directory}")
    private String uploadDirectory;

    /**
     * [파일 업로드]
     * Multipart 파일을 입력받아 서버 내부 스토리지에 저장.
     * @param [MultipartFile 파일]
     * @return [FileinfoDto 파일 정보]
     */
    public FileInfoDto uploadFile(MultipartFile file) throws FileUploadException {

        String originalFileName = file.getOriginalFilename();
        String mimeType = file.getContentType();

        //최대용량 체크
        if (file.getSize() > FileConstant.MAX_FILE_SIZE) {
            throw new FileUploadException("10MB 이하 파일만 업로드 할 수 있습니다.");
        }


        //MIMETYPE 체크
        if (!FileUtil.isImageFile(mimeType)) {
            throw new FileUploadException("이미지 파일만 업로드할 수 있습니다.");
        }

        String key = "images/" + generateUniqueFileName(originalFileName);

        try{
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(mimeType)
                            .contentLength(file.getSize())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(),file.getSize())
            );
        }catch (IOException e){
            throw new FileUploadException("S3 upload failed.");
        }
        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + key;

        return new FileInfoDto(file.getContentType(),
                file.getOriginalFilename(),
                fileUrl,
                Long.toString(file.getSize()));
    }


    /**
     * [중복방지를 위한 파일 고유명 생성]
     * @param fileExtension 확장자
     * @return String 파일 고유이름
     */
    private String generateUniqueFileName(String originalFileName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        // Random 객체 생성
        Random random = new Random();
        // 0 이상 100 미만의 랜덤한 정수 반환
        String randomNumber = Integer.toString(random.nextInt(Integer.MAX_VALUE));
        String timeStamp = dateFormat.format(new Date());
        return timeStamp + randomNumber + originalFileName;
    }

    public String generatePresignedUrl(String filename, String contentType){
        String s3Key = "uploads/" + UUID.randomUUID() + "_" + filename;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .contentType(contentType)
                .build();;

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest).build();
        return s3Config.s3Presigner().presignPutObject(presignRequest).url().toString();
    }
}
