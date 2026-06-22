package com.animanga.ms_auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.animanga.ms_auth.model.Rol;
import com.animanga.ms_auth.repository.RolRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) {
        if (rolRepository.count() == 0) {
            Rol admin = new Rol();
            admin.setNombre("Admin");
            rolRepository.save(admin);

            Rol gestor = new Rol();
            gestor.setNombre("Gestor");
            rolRepository.save(gestor);

            Rol usuario = new Rol();
            usuario.setNombre("Usuario");
            rolRepository.save(usuario);

            System.out.println("Roles iniciales creados: Admin, Gestor, Usuario");
        }
    }
}
