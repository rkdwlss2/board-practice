package com.example.boardpractice.web.dto.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDto {
    private String fileType;
    private String fileName;
    private String filePath;
    private String fileSize;
}
