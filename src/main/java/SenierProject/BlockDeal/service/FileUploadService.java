package SenierProject.BlockDeal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${spring.web.resources.static-locations}")  // yml 파일에서 설정한 저장 경로
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        // 고유한 파일 이름 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        // 경로 클리닝
        String cleanedUploadDir = uploadDir.replace("file:///", "");
        Path path = Paths.get(cleanedUploadDir).resolve(fileName);

        // 디렉토리가 존재하지 않으면 생성
        if (!Files.exists(Paths.get(cleanedUploadDir))) {
            Files.createDirectories(Paths.get(cleanedUploadDir));
        }

        // 파일 저장
        Files.copy(file.getInputStream(), path);

        // 저장된 파일 경로 반환
        return "/uploads/" + fileName;
    }
}
