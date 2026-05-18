# Documento de Diseño - Microservicios (v3.1)

> **Nota:** MS Taxonomy (v3.0 → MS 5) fue fusionado dentro de Catalog para simplificar la arquitectura.  
> La tabla `genero` y la relación `animanga_genero` viven en `catalog_db`.

## MS 1: Identity (IAM + Authorization) 🆗
Gestión de usuarios, autenticación y permisos.

### Tablas (4)

#### rol
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Integer (PK) | Auto-increment |
| nombre | String | Admin, Gestor, Soporte, Usuario |

#### permiso
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Integer (PK) | Auto-increment |
| accion | String | EDITAR_CATALOGO, VALIDAR_CURACION, etc. |
| descripcion | String | Descripción del permiso |

#### usuario
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long (PK) | Auto-increment |
| username | String | Unique, not null |
| email | String | Unique, not null |
| password_hash | String | Not null |
| estadoCuenta | Enum (ACTIVO, INACTIVO) | Estado de la cuenta |
| id_rol | Integer (FK) | FK to rol |

#### rol_permiso (Tabla intermedia @ManyToMany)
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idRol | Integer (FK) | FK to rol |
| idPermiso | Integer (FK) | FK to permiso |

---

## MS 2: Auditor 🆗
Registro de auditoría del sistema.

### Tablas (1)

#### auditoria_sistema
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idLog | Long (PK) | Auto-increment |
| idUsuario | Long | ID del usuario que realizó la acción |
| descripcionAccion | String | Descripción de la acción |
| tablaAfectada | String | Tabla afectada |
| fechaHora | LocalDateTime | Fecha y hora de la acción |

---

## MS 3: Perfil 🆗
Datos sociales del usuario.

### Tablas (1)

#### perfil
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idPerfil | Long (PK) | Auto-increment |
| idUsuario | Long | Unique, FK remota a usuario |
| avatarUrl | String | URL del avatar |
| biografia | String | Biografía del usuario |
| fechaRegistro | LocalDateTime | Fecha de registro |
| preferencias | Text | JSON con preferencias |

---

## MS 4: Catalog 🆗
Catálogo maestro de anime y manga + clasificación por géneros.

### Tablas (4)

#### tipo_animanga
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idTipo | Integer (PK) | Auto-increment |
| nombre | String | Anime, Manga |

#### genero
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idGenero | Integer (PK) | Auto-increment |
| nombre | String | Shonen, Seinen, Shojo, etc. |

#### animanga
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idAnimanga | Long (PK) | Auto-increment |
| titulo | String | Título de la obra |
| descripcion | Text | Sinopsis extendida |
| fechaEstreno | LocalDate | Fecha de estreno |
| estado_emision | Enum (EN_CURSO, FINALIZADO) | Estado de emisión |
| idTipo | Integer (FK) | FK to tipo_animanga |
| id_estudio | Long | FK remota a entidad (estudio) |
| id_autor | Long | FK remota a entidad (autor) |

#### animanga_genero (Tabla intermedia @ManyToMany)
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id_animanga | Long (FK) | FK to animanga |
| id_genero | Integer (FK) | FK to genero |

---

## MS 5: Curation
Flujo de control de calidad para propuestas.

### Tablas (2)

#### propuesta_importacion
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idPropuesta | Long (PK) | Auto-increment |
| idUsuarioPropone | Long | FK remota a usuario |
| datosJson | Text | JSON con datos propuestos |
| estadoCuracion | String | PENDIENTE, APROBADO, RECHAZADO |
| comentarioRechazo | String | nullable - Motivo del rechazo |

#### historial_curacion
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idCuracion | Long (PK) | Auto-increment |
| idPropuesta | Long | FK to propuesta_importacion |
| idModerador | Long | FK remota a usuario |
| fechaDecision | LocalDateTime | Fecha de decisión |
| decision | String | APROBADO, RECHAZADO |

---

## MS 6: Media
Gestión de recursos multimedia.

### Tablas (1)

#### recurso_multimedia
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idRecurso | Long (PK) | Auto-increment |
| idAnimanga | Long | FK remota a animanga |
| tipoRecurso | String | PORTADA, BANNER, GALERIA |
| urlRecurso | String | URL del recurso |
| optimizacion | String | WebP 800x600, etc. |

---

## MS 7: Production 🆗
Entidades de producción (estudios, autores, editoriales).

### Tablas (3)

#### tipo_entidad
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idTipo | Integer (PK) | Auto-increment |
| nombre | String | ESTUDIO, AUTOR, EDITORIAL |

#### entidad
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idEntidad | Integer (PK) | Auto-increment |
| nombre | String | Nombre de la entidad |
| id_tipo | Integer (FK) | FK to tipo_entidad |
| id_nacionalidad | Integer (FK) | FK to nacionalidad |
| fechaNacimiento | LocalDate | nullable — fecha de nacimiento |
| descripcion | Text | nullable — biografía |

#### nacionalidad
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idNacionalidad | Integer (PK) | Auto-increment |
| pais | String | País de origen |

---

## MS 8: Social
Sistema de reseñas y calificaciones.

### Tablas (2)

#### resena
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idResena | Long (PK) | Auto-increment |
| idUsuario | Long | FK remota a usuario |
| idAnimanga | Long | FK remota a animanga |
| titulo | String | Título de la reseña |
| puntuacion | Double | 0.0 - 10.0 |
| comentario | Text | Comentario |
| likeCount | Integer | Contador de likes |
| comentarioCount | Integer | Contador de respuestas |
| fechaCreacion | LocalDateTime | Fecha de creación |

#### voto_resena
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idVoto | Long (PK) | Auto-increment |
| idResena | Long | FK to resena |
| idUsuarioVota | Long | FK remota a usuario |
| esUtil | Boolean | Marcador de utilidad |

---

## MS 9: Library
Biblioteca personal del usuario.

### Tablas (2)

#### biblioteca_usuario
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idBiblioteca | Long (PK) | Auto-increment |
| idUsuario | Long | FK remota a usuario |
| idAnimanga | Long | FK remota a animanga |
| estadoSeguimiento | String | FAVORITO, LEYENDO, COMPLETADO, RETRASADO, PLAN_A_VER |
| fechaAgregado | LocalDateTime | Fecha de agregado |

#### progreso_anime
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idProgreso | Long (PK) | Auto-increment |
| idBiblioteca | Long | FK to biblioteca_usuario |
| capituloActual | Integer | Capítulo actual |
| fechaActualizacion | LocalDateTime | Última actualización |

---

## MS 10: HelpDesk
Sistema de soporte técnico.

### Tablas (1)

#### incidencia
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idIncidencia | Long (PK) | Auto-increment |
| idUsuarioReporta | Long | FK remota a usuario |
| titulo | String | Título de la incidencia |
| descripcion | Text | Descripción detallada |
| categoria | String | TECNICO, CONTENIDO, CUENTA, OTRO |
| prioridad | String | BAJA, MEDIA, ALTA |
| estado | String | ABIERTO, EN_PROCESO, RESUELTO, CERRADO |
| fechaReporte | LocalDateTime | Fecha del reporte |
| fechaResolucion | LocalDateTime | Fecha de resolución |

---

## Resumen: v2.0 → v3.1

| v2.0 | v3.1 |
|------|------|
| MS 1: Identity | MS 1: Identity 🆗 |
| MS 2: Authorization | (fusionado en Identity) |
| MS 3: Auditor | MS 2: Auditor 🆗 |
| MS 4: Perfil | MS 3: Perfil 🆗 |
| MS 5: Catalog | MS 4: Catalog + Taxonomy 🆗 |
| MS 6: Taxonomy | (fusionado en Catalog) |
| MS 7: Curation | MS 5: Curation |
| MS 7: Media | MS 6: Media |
| MS 8: Production | MS 7: Production 🆗 |
| MS 9: Social | MS 8: Social |
| MS 10: Library | MS 9: Library |
| MS 11: HelpDesk | MS 10: HelpDesk |

## Cantidad

- **Microservicios implementados**: 5 de 10
- **Tablas implementadas**: 13 de 21
- **Por implementar**: Curation, Media, Social, Library, HelpDesk

## Distribución sugerida (3 personas)

| Persona | MS |
|---------|-----|
| 1 | Identity, Auditor, Perfil 🆗 |
| 2 | Catalog+Taxonomy, Media, Production 🆗 |
| 3 | Curation, Social, Library, HelpDesk |