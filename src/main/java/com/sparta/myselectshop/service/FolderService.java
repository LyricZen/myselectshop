package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private  final FolderRepository folderRepository;

    // 1. 회원의 폴더 생성 service
    //로그인한 회원에 폴더들 등록
    public void addFolders(List<String> foldernames, User user) {
        //회원이 생성한 폴더들이 있는지 조회
        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, foldernames);
        //회원이 생성한 폴더를 넣어줄 새폴더 생성
        List<Folder> folderList = new ArrayList<>();

        //들어온 폴더 이름과 존재하는 폴더이름을 대입해보는 쿼리
        for (String foldername : foldernames) {
            if (!isExistFolderName(foldername, existFolderList)) {
                //중복되는 폴더이름이 없다면!!
                Folder folder = new Folder(foldername, user);
                folderList.add(folder);
            } else {
                throw new IllegalArgumentException("폴더명이 중복되었습니다.");
            }
        }
        folderRepository.saveAll(folderList);
    }

    // 2. 회원이 저장한 폴더 조회 service
    public List<FolderResponseDto> getFolders(User user) {
        //회원이 생성한 새폴더 조회
        List<Folder> folderList = folderRepository.findAllByUser(user);
        // reponse를 통해 보여줄 폴더 생성
        List<FolderResponseDto> responseDtoList = new ArrayList<>();

        // reponse에 회원이 생성한 폴더 하나씩 추가
        for (Folder folder : folderList) {
            responseDtoList.add(new FolderResponseDto(folder));
        }
        return responseDtoList;
    }

    // 1. 회원의 폴더 생성 보조 메서드
    //중복되는 폴더가 있는지 참 거짓 확인하는 메서드
    private boolean isExistFolderName(String foldername, List<Folder> existFolderList) {
        for (Folder existFolder : existFolderList) {
            if (foldername.equals(existFolder.getName())) {
                return true;
            }
        }
        return false;
    }
}
