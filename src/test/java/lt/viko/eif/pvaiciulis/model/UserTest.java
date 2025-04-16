package lt.viko.eif.pvaiciulis.model;

import lt.viko.eif.pvaiciulis.model.UserModel.Address;
import lt.viko.eif.pvaiciulis.model.UserModel.Role;
import lt.viko.eif.pvaiciulis.model.UserModel.User;
import org.junit.jupiter.api.Test;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void testGetPassword() {
        User user = User.builder().password("password123").build();
        assertThat(user.getPassword()).isEqualTo("password123");
    }

    @Test
    public void testGetAuthorities() {
        User user = User.builder().role(Role.USER).build();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Extract and compare the authority names
        assertThat(authorities)
                .extracting(GrantedAuthority::getAuthority)
                .contains("USER");
    }

    @Test
    public void testGetUsername() {
        User user = User.builder().id(1).build();
        assertThat(user.getUsername()).isEqualTo("1");
    }

    @Test
    public void testIsAccountNonExpired() {
        User user = new User();
        assertThat(user.isAccountNonExpired()).isTrue();
    }

    @Test
    public void testIsAccountNonLocked() {
        User user = new User();
        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    public void testIsCredentialsNonExpired() {
        User user = new User();
        assertThat(user.isCredentialsNonExpired()).isTrue();
    }

    @Test
    public void testIsEnabled() {
        User user = new User();
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    public void testLombokGeneratedMethods() {
        User user1 = User.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();

        User user2 = User.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();

        assertThat(user1).isEqualTo(user2); // Tests `equals` and `hashCode`.
        assertThat(user1.toString()).contains("John", "Doe", "john.doe@example.com"); // Tests `toString`.
    }

    @Test
    public void testAddressRelationship() {
        Address address = Address.builder().id(1).city("TestCity").build();
        User user = User.builder().address(address).build();

        assertThat(user.getAddress()).isEqualTo(address);
    }
}