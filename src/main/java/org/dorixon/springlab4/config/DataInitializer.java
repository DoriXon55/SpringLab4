package org.dorixon.springlab4.config;

import lombok.RequiredArgsConstructor;
import org.dorixon.springlab4.model.Role;
import org.dorixon.springlab4.repository.RoleRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DataInitializer {
    private final RoleRepository roleRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        List.of("USER", "ADMIN").forEach(role -> roleRepository
                .findByName(role)
                .orElseGet(() -> roleRepository.save(Role.builder().name(role).build())));
    }

}
