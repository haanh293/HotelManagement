package com.hotel.application.service;

import com.hotel.application.port.in.AuthUseCase;
import com.hotel.domain.model.User;
import com.hotel.infrastructure.adapter.out.external.EmailService;
import com.hotel.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.hotel.domain.model.User; // Domain model của bạn
import java.util.Collections;
@Service
public class AuthService implements AuthUseCase {

	private static final Map<String, String> tokenStore = new HashMap<>();

    private final SpringDataUserRepository userRepo;
    private final EmailService emailService; // Service gửi mail bạn đã tạo

    public AuthService(SpringDataUserRepository userRepo, EmailService emailService) {
        this.userRepo = userRepo;
        this.emailService = emailService;
    }
    private static final String GOOGLE_CLIENT_ID = "735228477144-oqnmhglsfd7hpm0ulv9sqopejc94eck3.apps.googleusercontent.com";
    @Override
    public User login(String username, String password) {
        // 1. Tìm user trong DB
        UserJpaEntity entity = userRepo.findByUsername(username).orElse(null);

        // 2. Kiểm tra mật khẩu (So sánh chuỗi thường cho nhanh)
        if (entity != null && entity.getPassword().equals(password)) {
            return new User(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getRole());
        }
        
        // 3. Đăng nhập thất bại
        return null; 
    }
    @Override
    public void register(User user) {
        // 1. Kiểm tra xem tên đăng nhập đã tồn tại chưa
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        // 2. Chuyển đổi từ Domain sang Entity
        UserJpaEntity entity = new UserJpaEntity();
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole()); // Mặc định có thể để là "GUEST" hoặc lấy từ input

        // 3. Lưu vào DB
        userRepo.save(entity);
    }
    @Override
    public String forgotPassword(String email) {
        // B1: Kiểm tra email có tồn tại trong hệ thống không
        if (userRepo.findByUsername(email).isEmpty()) {
            throw new RuntimeException("Email này chưa đăng ký tài khoản!");
        }

        // B2: Tạo mã Token ngẫu nhiên (4 số)
        String token = String.valueOf((int) (Math.random() * 9000) + 1000);

        // B3: Lưu vào bộ nhớ tạm (để tí nữa đối chiếu)
        tokenStore.put(email, token);

        // B4: Gửi Email thật
        String subject = "Xác nhận Quên Mật Khẩu - Hotel Imperial";
        String body = "Xin chào,\n\n" +
                      "Mã xác nhận của bạn là: " + token + "\n" +
                      "Mã này có hiệu lực trong 5 phút. Vui lòng không chia sẻ cho ai.";
        
        emailService.sendEmail(email, subject, body);

        return "Mã xác nhận đã được gửi vào Email. Vui lòng kiểm tra!";
    }

    @Override
    public void resetPassword(String email, String token, String newPassword) {
        // B1: Lấy token đã lưu trong RAM ra
        String storedToken = tokenStore.get(email);

        // B2: Kiểm tra khớp mã
        if (storedToken == null || !storedToken.equals(token)) {
            throw new RuntimeException("Mã xác nhận không đúng hoặc đã hết hạn!");
        }

        // B3: Cập nhật mật khẩu mới vào DB
        UserJpaEntity user = userRepo.findByUsername(email).get();
        user.setPassword(newPassword); // (Lưu ý: Nếu có mã hóa BCrypt thì nhớ mã hóa ở đây)
        userRepo.save(user);

        // B4: Xóa token đi (để không dùng lại được nữa)
        tokenStore.remove(email);
    }
    @Override
    public User loginWithGoogle(String credential) {
        try {
            // 1. Cấu hình bộ xác thực của Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            // 2. Xác thực Token gửi từ Front-end
            GoogleIdToken idToken = verifier.verify(credential);
            
            if (idToken != null) {
                // 3. Nếu Token xịn -> Lấy thông tin người dùng
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                // String name = (String) payload.get("name"); // Lấy tên nếu cần

                // 4. Kiểm tra xem Email này đã có trong DB chưa
                UserJpaEntity entity = userRepo.findByUsername(email).orElse(null);

                if (entity == null) {
                    // 5. Nếu chưa có -> Tự động Đăng ký (Auto Register)
                    entity = new UserJpaEntity();
                    entity.setUsername(email);
                    entity.setPassword("GOOGLE_LOGIN_Pass123"); // Mật khẩu ngẫu nhiên
                    entity.setRole("GUEST"); // Mặc định là khách
                    entity = userRepo.save(entity);
                    
                    // Lưu ý: Đoạn này bạn nên gọi logic tạo Guest profile (thêm vào bảng guests) 
                    // nhưng để đơn giản thì tạo User trước đã.
                }

                // 6. Trả về User đã đăng nhập
                return new User(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getRole());

            } else {
                throw new RuntimeException("Token Google không hợp lệ!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xác thực Google: " + e.getMessage());
        }
    }
}