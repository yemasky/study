import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    @Test
    void encode() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("user");
        String password2 = bCryptPasswordEncoder.encode("admin");
        System.out.println(password);
        System.out.println(password2);
    }
}
