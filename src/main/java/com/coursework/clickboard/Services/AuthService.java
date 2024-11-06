package com.coursework.clickboard.Services;
import com.coursework.clickboard.Models.DTO.Vk.SignInDTO;
import com.coursework.clickboard.Exceptions.CustomExceptions.AuthenticationFailureException;
import com.coursework.clickboard.Utils.JwtUtil;
import com.coursework.clickboard.Models.Database.User.TwoFactorCodeDTO;
import com.coursework.clickboard.Models.Database.User.User;
import com.coursework.clickboard.Models.DTO.Vk.ApiResponse;
import com.coursework.clickboard.Models.DTO.User.UserTokenDTO;
import com.coursework.clickboard.Models.DTO.Vk.VkUserPartialDTO;
import com.coursework.clickboard.Utils.PasswordGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
@Service
@Transactional
public class AuthService{
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ApiService apiService;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Async("taskExecutor")
    public CompletableFuture<ApiResponse> exchangeAndRetrieveProfile(String silentToken, String uuid) {
        return apiService.exchangeSilentAuthToken(silentToken, uuid)
                .thenCompose(vkApiResponse -> {
                    Optional<User> user = userService.getByVkId(vkApiResponse.getResponse().getUserId());
                    if (user.isPresent()) {
                        return authenticateUser(new SignInDTO(vkApiResponse.getResponse().getUserId()));
                    }
                    return apiService.getProfileInfo(vkApiResponse.getResponse().getAccessToken())
                            .thenApply(profileInfo -> new VkUserPartialDTO(
                                    vkApiResponse.getResponse().getUserId(),
                                    profileInfo.getResponse().get(0).getFirstName(),
                                    profileInfo.getResponse().get(0).getLastName(),
                                    true,
                                    "User data"
                            ));
                })
                .exceptionally(e -> {
                    throw new CompletionException(new AuthenticationFailureException(e.getMessage()));
                });
    }

    @Async("taskExecutor")
    public CompletableFuture<ApiResponse> authenticateUser(SignInDTO request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                UserDetails userDetails;
                User user;
                boolean isVk = false;
                if (request.getVkId() != 0) {
                    Optional<User> findUser = userService.getByVkId(request.getVkId());
                    isVk = true;
                    if (findUser.isEmpty()) throw new UsernameNotFoundException("Пользователь с таким vkId не найден.");
                    else user = findUser.get();
                } else {
                    try{
                        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
                    } catch (Exception e){
                        throw new AuthenticationFailureException("Неверный логин или пароль");
                    }
                    user = userService.getByUsername(request.getUsername());

                    if (user.isTwoFactorEnabled()) {
                        generateAndSend2FACode(user.getUsername());
                        return new ApiResponse(true,"Код 2FA отправлен на ваш электронный адрес. Пожалуйста, подтвердите, чтобы завершить авторизацию."){};
                    }
                }
                userDetails = userService.loadUserByUsername(user.getUsername());
                return new UserTokenDTO(jwtUtil.generateToken(userDetails), user.getUsername(), true, "token", user.isChildModeEnabled(), isVk);
            } catch (Exception e) {
                throw new CompletionException(new AuthenticationFailureException(e.getMessage()));
            }
        });
    }

    public ApiResponse validateAndGenerateJwt(TwoFactorCodeDTO twoFactorCodeDTO) throws Exception {
        User user = userService.getByUsername(twoFactorCodeDTO.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь " + twoFactorCodeDTO.getUsername() + " не найден.");
        }

        try {
            if (verify2FACode(twoFactorCodeDTO.getCode(), user)) {
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
                return new UserTokenDTO(jwtUtil.generateToken(userDetails), user.getUsername(), true, "2FA код успешно подтвержден.", user.isChildModeEnabled(), user.getVkId() != null);
            } else {
                throw new AuthenticationFailureException("Неверный 2FA код.");
            }
        } catch (AuthenticationFailureException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Ошибка при генерации JWT: " + e.getMessage(), e);
        }
    }

    public boolean verify2FACode(String code, User user){
        return user.getConfirmationCode().equals(code) &&
                user.getConfirmationCodeExpiration().isAfter(LocalDateTime.now());
    }

    public void generateAndSend2FACode(String username) throws Exception {
        String code = String.format("%06d", new Random().nextInt(999999));
        User findUser = userService.getByUsername(username);
        findUser.setConfirmationCode(code);
        findUser.setConfirmationCodeExpiration(LocalDateTime.now().plusMinutes(10));
        try{
            userService.update(findUser);
            emailService.sendSimpleMessage(findUser.getEmail(), "Подтверждение входа", "Ваш 2FA код: " + code);
        }
        catch (Exception e){
            throw new Exception("Ошибка отправки 2FA кода");
        }
    }

    public ApiResponse resetPassword(String email) throws Exception {
        try {
            logger.info("Start found user");
            logger.info("email is " + email);
            User user = userService.getByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("Пользователь не найден.");
            }
            logger.info("User founded");
            String newPassword = PasswordGenerator.generatePassword(8);
            if (newPassword.isEmpty()) {
                throw new IllegalStateException("Не удалось сгенерировать новый пароль.");
            }

            logger.info("New password is " + newPassword);

            user.setPassword(passwordEncoder.encode(newPassword));
            userService.update(user);
            logger.info("New password set");

            emailService.sendSimpleMessage(email, "Сброс пароля", "Ваш новый пароль: " + newPassword + ". Вы можете сменить пароль в личном профиле.");
            return new ApiResponse(true, "Пароль успешно сброшен. Ожидайте уведомления на почту"){};
        } catch (UsernameNotFoundException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
}
