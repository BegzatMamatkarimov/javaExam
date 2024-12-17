package com.arbek.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LocalUser implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer userId;

  @NotBlank(message = "The username field can't be blank")
  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  @Size(min = 8, message = "The password must have at least 8 characters")
  @NotBlank(message = "The password field can't be blank")
  private String password;

  @OneToOne(mappedBy = "localUser")
  private RefreshToken refreshToken;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserType userType;

  @Column
  private String firstName;

  @Column
  private String lastName;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(userType.name()));
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
