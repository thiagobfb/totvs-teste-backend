package com.thiagobfb.totvstestebackend.usuario.model;

import com.thiagobfb.totvstestebackend.usuario.enums.Profile;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "username", nullable = false)
    @Getter
    @Setter
    private String username;

    @Column(name = "password", nullable = false)
    @Getter
    @Setter
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profile", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "profile")
    private Set<Integer> profiles;

    public Usuario() {
        addProfile(Profile.CLIENTE);
    }

    public Set<Profile> getProfiles() {
        return profiles.stream().map(Profile::toEnum).collect(Collectors.toSet());
    }

    public void addProfile(Profile profile) {
        if (profiles == null) {
            profiles = new HashSet<>();
        }
        this.profiles.add(profile.getCod());
    }
}
