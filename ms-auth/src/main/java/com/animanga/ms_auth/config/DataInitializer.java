package com.animanga.ms_auth.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.animanga.ms_auth.model.Permiso;
import com.animanga.ms_auth.model.Rol;
import com.animanga.ms_auth.repository.PermisoRepository;
import com.animanga.ms_auth.repository.RolRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    @Override
    public void run(String... args) {
        if (rolRepository.count() == 0) {
            crearRoles();
        }
        if (permisoRepository.count() == 0) {
            crearPermisos();
        }
    }

    private void crearRoles() {
        Rol admin = new Rol();
        admin.setNombre("Admin");
        rolRepository.save(admin);

        Rol gestor = new Rol();
        gestor.setNombre("Gestor");
        rolRepository.save(gestor);

        Rol usuario = new Rol();
        usuario.setNombre("Usuario");
        rolRepository.save(usuario);

    }

    private void crearPermisos() {
        List<Permiso> todos = Arrays.asList(
            new Permiso("USUARIO_CREAR", "Crear usuarios"),
            new Permiso("USUARIO_LISTAR", "Listar usuarios"),
            new Permiso("USUARIO_ELIMINAR", "Eliminar usuarios"),
            new Permiso("ROL_CREAR", "Crear roles"),
            new Permiso("ROL_LISTAR", "Listar roles"),
            new Permiso("PERMISO_CREAR", "Crear permisos"),

            new Permiso("ANIMANGA_CREAR", "Crear animanga"),
            new Permiso("ANIMANGA_EDITAR", "Editar animanga"),
            new Permiso("ANIMANGA_ELIMINAR", "Eliminar animanga"),
            new Permiso("ANIMANGA_LISTAR", "Listar animanga"),
            new Permiso("GENERO_CREAR", "Crear géneros"),
            new Permiso("TIPO_CREAR", "Crear tipos de animanga"),

            new Permiso("ENTIDAD_CREAR", "Crear entidades (estudios, autores)"),
            new Permiso("ENTIDAD_EDITAR", "Editar entidades"),
            new Permiso("ENTIDAD_ELIMINAR", "Eliminar entidades"),
            new Permiso("ENTIDAD_LISTAR", "Listar entidades"),
            new Permiso("NACIONALIDAD_CREAR", "Crear nacionalidades"),
            new Permiso("TIPO_ENTIDAD_CREAR", "Crear tipos de entidad"),

            new Permiso("PROPUESTA_CREAR", "Crear propuestas de importación"),
            new Permiso("PROPUESTA_APROBAR", "Aprobar/rechazar propuestas"),
            new Permiso("PROPUESTA_LISTAR", "Listar propuestas"),
            new Permiso("PROPUESTA_ELIMINAR", "Eliminar propuestas"),

            new Permiso("PERFIL_CREAR", "Crear perfiles de usuario"),
            new Permiso("PERFIL_EDITAR", "Editar perfiles"),
            new Permiso("PERFIL_ELIMINAR", "Eliminar perfiles"),
            new Permiso("PERFIL_LISTAR", "Listar perfiles"),

            new Permiso("AUDITORIA_VER", "Ver logs de auditoría"),

            new Permiso("MEDIA_SUBIR", "Subir archivos multimedia"),
            new Permiso("MEDIA_ELIMINAR", "Eliminar archivos multimedia"),
            new Permiso("MEDIA_LISTAR", "Listar archivos multimedia"),

            new Permiso("REVIEW_CREAR", "Crear reseñas"),
            new Permiso("REVIEW_ELIMINAR", "Eliminar reseñas"),
            new Permiso("COMENTARIO_CREAR", "Comentar reseñas"),
            new Permiso("LIKE_DAR", "Dar like a reseñas"),

            new Permiso("TICKET_CREAR", "Crear tickets de soporte"),
            new Permiso("TICKET_ATENDER", "Atender y cerrar tickets"),
            new Permiso("TICKET_LISTAR", "Listar tickets"),

            new Permiso("BIBLIOTECA_AGREGAR", "Agregar a biblioteca personal"),
            new Permiso("BIBLIOTECA_ACTUALIZAR", "Actualizar progreso en biblioteca"),
            new Permiso("BIBLIOTECA_ELIMINAR", "Eliminar de biblioteca"),
            new Permiso("BIBLIOTECA_VER", "Ver biblioteca")
        );

        permisoRepository.saveAll(todos);

        Rol admin = rolRepository.findByNombre("Admin").orElse(null);
        Rol gestor = rolRepository.findByNombre("Gestor").orElse(null);
        Rol usuario = rolRepository.findByNombre("Usuario").orElse(null);

        if (admin != null) {
            admin.setPermisos(new HashSet<>(todos));
            rolRepository.save(admin);
        }

        if (gestor != null) {
            Set<Permiso> permisosGestor = new HashSet<>(Arrays.asList(
                findByAccion("ANIMANGA_CREAR"), findByAccion("ANIMANGA_EDITAR"), findByAccion("ANIMANGA_LISTAR"),
                findByAccion("GENERO_CREAR"), findByAccion("TIPO_CREAR"),
                findByAccion("ENTIDAD_CREAR"), findByAccion("ENTIDAD_EDITAR"), findByAccion("ENTIDAD_LISTAR"),
                findByAccion("NACIONALIDAD_CREAR"), findByAccion("TIPO_ENTIDAD_CREAR"),
                findByAccion("PROPUESTA_LISTAR"), findByAccion("PROPUESTA_APROBAR"),
                findByAccion("PERFIL_LISTAR"),
                findByAccion("AUDITORIA_VER"),
                findByAccion("MEDIA_LISTAR"), findByAccion("MEDIA_SUBIR"),
                findByAccion("TICKET_LISTAR"), findByAccion("TICKET_ATENDER"),
                findByAccion("USUARIO_LISTAR")
            ));
            permisosGestor.remove(null);
            gestor.setPermisos(permisosGestor);
            rolRepository.save(gestor);
        }

        if (usuario != null) {
            Set<Permiso> permisosUsuario = new HashSet<>(Arrays.asList(
                findByAccion("ANIMANGA_LISTAR"),
                findByAccion("ENTIDAD_LISTAR"),
                findByAccion("PROPUESTA_CREAR"), findByAccion("PROPUESTA_LISTAR"),
                findByAccion("PERFIL_CREAR"), findByAccion("PERFIL_EDITAR"), findByAccion("PERFIL_LISTAR"),
                findByAccion("MEDIA_LISTAR"), findByAccion("MEDIA_SUBIR"),
                findByAccion("REVIEW_CREAR"), findByAccion("COMENTARIO_CREAR"), findByAccion("LIKE_DAR"),
                findByAccion("TICKET_CREAR"), findByAccion("TICKET_LISTAR"),
                findByAccion("BIBLIOTECA_AGREGAR"), findByAccion("BIBLIOTECA_ACTUALIZAR"),
                findByAccion("BIBLIOTECA_ELIMINAR"), findByAccion("BIBLIOTECA_VER")
            ));
            permisosUsuario.remove(null);
            usuario.setPermisos(permisosUsuario);
            rolRepository.save(usuario);
        }
    }

    private Permiso findByAccion(String accion) {
        return permisoRepository.findByAccion(accion);
    }
}
