package com.example.auth.bean;

import com.example.auth.entities.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @DynamicUpdate 注解时，Spring JPA 会根据实体的实际修改字段生成更新
 *                SQL。也就是说，只有你修改了的字段会被更新，而未修改的字段会保持原值。
 */
@Entity
@Table(name = "user")
@Data
@DynamicUpdate
public class User extends AuditableEntity<User> implements UserDetails {

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "用户名字")
    private String username;

    @Column(nullable = false)
    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "用户是否在使用")
    private boolean enabled;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "账号是否过期")
    private boolean accountNonExpired;

    @Schema(description = "账号是否锁定")
    private boolean accountNonLocked;

    @Schema(description = "权限是否过期")
    private boolean credentialsNonExpired;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "wechat_openid", unique = true, length = 64)
    @Schema(description = "微信OpenID")
    private String wechatOpenid;

    @Column(name = "wechat_unionid", length = 64)
    @Schema(description = "微信UnionID")
    private String wechatUnionid;

    @Column(name = "nickname", length = 100)
    @Schema(description = "昵称")
    private String nickname;

    @Column(name = "avatar_url", length = 500)
    @Schema(description = "头像URL")
    private String avatarUrl;

    @Column(name = "mobile", length = 20)
    @Schema(description = "手机号")
    private String mobile;

    @Column(name = "qq", length = 20)
    @Schema(description = "QQ号")
    private String qq;

    @Column(name = "single_signature", length = 10)
    @Schema(description = "单字署名")
    private String singleSignature;

    @Column(name = "double_signature", length = 20)
    @Schema(description = "二字署名")
    private String doubleSignature;

    @Column(name = "four_signature", length = 50)
    @Schema(description = "四字署名")
    private String fourSignature;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());

        authorities.addAll(roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet()));

        return authorities;
    }

    // UserDetails 接口的其他方法
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

}
