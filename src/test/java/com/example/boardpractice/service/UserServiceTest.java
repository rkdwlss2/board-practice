package com.example.boardpractice.service;

import com.example.boardpractice.entity.Users;
import com.example.boardpractice.exception.NotFoundException;
import com.example.boardpractice.repository.UserRepository;
import com.example.boardpractice.web.dto.user.UserSignupRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입시, 입력한 이메일에 해당하는 유저정보가 없어 false를 리턴해야 된다. 그리고 저장한 유저 정보를 반환해야한다.")
    public void 회원가입_서비스테스트() {
        // given
        UserSignupRequestDto dto = new UserSignupRequestDto();
        dto.setEmail("russell@gmail.com");
        dto.setPassword("Kka!12345");
        dto.setNickname("russell");

        Users user = Users.builder()
                .email("russell@gmail.com")
                .build();

        // 명시화를 위해 BDD 모키토 사용
        given(userRepository.existsByEmail(dto.getEmail()))
                .willReturn(false);

        given(userRepository.save(any(Users.class)))
                .willReturn(user);

        //when
        Users result = userService.registerUser("russell@gmail.com","russell","Kka!12345");

        //then
        assertEquals("russell@gmail.com",result.getEmail());

        verify(userRepository).existsByEmail(dto.getEmail());
        verify(userRepository).save(any(Users.class));
    }

    @Test
    @DisplayName("회원가입시, 입력한 이메일에 해당하는 유저정보가 있어 true를 리턴하고, throw로 IllegalArgumentException에러를 던져야 한다. 문구는 이미 사용 중인 이메일 입니다. ")
    public void 회원가입_서비스_이메일중복테스트(){
        //given
        String email = "russell@gmail.com";
        String nickname = "russell";
        String password = "Kka!12345";

        given(userRepository.existsByEmail(email))
                .willReturn(true);
        //when
        assertThrows(IllegalArgumentException.class,()->userService.registerUser(email,nickname,password));

        //then
        verify(userRepository).existsByEmail(email);
        verify(userRepository,never()).save(any(Users.class));
    }

    @Test
    @DisplayName("닉네임 수정 할때 사용하는 서비스, 유저 ID로 조회를 하고 유저 정보를 반환, 입력받은 닉네임으로 변경한다.")
    public void 닉네임수정_서비스_성공테스트(){
        //given
        Users user = Users.builder()
                .email("russell@test.com")
                .password("Kka!12345")
                .nickname("russell")
                .build();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));


        //when
        Users result = userService.updateUserNickname(1L,"changedNickname");

        //then
        assertEquals("changedNickname",result.getNickname());
        assertEquals("changedNickname",user.getNickname());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("닉네임 수정시, 로그인한 본인 정보를 수정하지 않을 경우, Throw로 NotFoundException 사용자를 찾을수 없다 반환")
    public void 닉네임수정_서비스_실패테스트() {
        //given
        given(userRepository.findById(1L))
                .willReturn(Optional.empty());

        //when&then
        assertThrows(NotFoundException.class,()->userService.updateUserNickname(1L,"changedNickname"));

        // findById 1번 호출
        verify(userRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("패스워드 수정, 유저 ID로 사전 조회하여 pw,및 사전정보 조회, 입력받은 pw로 수정 (패스워드와, 패스워드 확인 일치하였을 경우)")
    public void 패스워드수정_서비스_성공테스트() {
        //given
        Users user = Users.builder()
                .email("russell@test.com")
                .password("Kka!12345")
                .nickname("russell")
                .build();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        //when
        Users result = userService.updateUserPassword(1L,"changedPassword","changedPassword");


        //then
        assertEquals("changedPassword",result.getPassword());
        assertEquals("changedPassword",user.getPassword());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("패스워드 수정, 유저 ID로 사전 조회하여 pw,및 사전정보 조회, 입력받은 pw로 수정 (패스워드와, 패스워드 확인 불일치 하였을 경우)")
    public void 패스워드수정_서비스_패스워드불일치_실패테스트() {
        //given
        Users user = Users.builder()
                .email("russell@test.com")
                .password("Kka!12345")
                .nickname("russell")
                .build();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        //when&then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->userService.updateUserPassword(1L,"Kka!12345","Kka!1234"));

        // findById 1번 호출
        assertThat(exception.getMessage())
                .isEqualTo("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        verify(userRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("패스워드 수정, 유저 ID로 사전 조회하여 pw,및 사전정보 조회 실패 테스트")
    public void 패스워드수정_서비스_실패테스트(){
        //given
        given(userRepository.findById(1L))
                .willReturn(Optional.empty());

        //when&then
        NotFoundException exception = assertThrows(NotFoundException.class,()->userService.updateUserPassword(1L,"Kka!12345","Kka!12345"));


        // findById 1번 호출
        assertThat(exception.getMessage())
                .isEqualTo("사용자를 찾을 수 없습니다.");
        verify(userRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("회원 탈퇴, 유저 ID로 사전 조회후 존재하는 경우 -> 해당 사용자 삭제")
    public void 회원탈퇴_서비스_성공테스트(){
        //given
        Users user = Users.builder()
                .email("russell@test.com")
                .password("Kka!12345")
                .nickname("russell")
                .build();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        //when
        userService.deleteUser(1L,"russell@test.com");

        //then
        verify(userRepository,times(1)).delete(user);
    }

    @Test
    @DisplayName("회원 탈퇴, 유저 ID로 사전조회시 회원정보가 존재하지 않을때 -> 사용자를 찾을 수 없습니다. NotFoundException 발생")
    public void 회원탈퇴_서비스_실패테스트(){
        //given
        given(userRepository.findById(1L))
                .willReturn(Optional.empty());

        //when&then
        NotFoundException exception = assertThrows(NotFoundException.class,()->userService.deleteUser(1L,"russell@test.com"));

        // findById 1번 호출
        assertThat(exception.getMessage())
                .isEqualTo("사용자를 찾을 수 없습니다.");
    }


    @Test
    @DisplayName("로그인 테스트 성공 -> 이메일 정보로 유저 사전에 찾고, 입력받은 비밀번호화 비교 성공했을때")
    public void 로그인_서비스_성공테스트(){
        //given
        Users user = Users.builder()
                .email("russell@test.com")
                .password("Kka!12345")
                .nickname("russell")
                .build();
        given(userRepository.findByEmail("russell@test.com"))
                .willReturn(Optional.of(user));
        //when
        Users result = userService.loginUser("russell@test.com","Kka!12345");

        //then
        assertEquals("russell@test.com",result.getEmail());
        assertEquals("Kka!12345",result.getPassword());
    }

    @Test
    @DisplayName("로그인 테스트 실패 -> 해당하는 이메일 유저가 없을때")
    public void 로그인_서비스_유저불일치_실패테스트() {
        //given
        given(userRepository.findByEmail("russell@test.com"))
                .willReturn(Optional.empty());

        //when&then
        NotFoundException exception = assertThrows(NotFoundException.class,()->userService.loginUser("russell@test.com","Kka!12345"));

        // 이메일 불일치
        assertThat(exception.getMessage())
                .isEqualTo("이메일이 일치하지 않습니다.");

    }

    @Test
    @DisplayName("로그인 테스트 실패 -> 비밀번호 불일치")
    public void 로그인_서비스_비밀번호불일치_실패테스트(){
        //given
        Users user = Users.builder()
                .email("russell@test.com")
                .password("Kka!12345")
                .nickname("russell")
                .build();

        given(userRepository.findByEmail("russell@test.com"))
                .willReturn(Optional.of(user));

        //when&then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->userService.loginUser("russell@test.com","Kka!1234"));

        // 비밀번호 불일치
        assertThat(exception.getMessage())
                .isEqualTo("비밀번호가 일치하지 않습니다.");
    }

}
