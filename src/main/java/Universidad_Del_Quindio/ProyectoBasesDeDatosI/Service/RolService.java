package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Rol;

import java.time.LocalDateTime;
import java.util.List;

public interface RolService {

    /**
     * Obtiene la lista completa de roles registrados en el sistema.
     *
     * @return lista de {@link Rol}
     */
    List<Rol> listarTodos();

    /**
     * Busca un rol por su identificador.
     *
     * @param idRol identificador del rol (PK de la tabla roles).
     * @return el {@link Rol} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un rol con ese id.
     */
    Rol obtenerPorId(Byte idRol);

    /**
     * Crea y persiste un nuevo rol en la base de datos.
     *
     * @param rol entidad {@link Rol} con la información a guardar.
     * @return el rol guardado con su id generado.
     */
    Rol crear(Rol rol);

    /**
     * Actualiza los datos de un rol existente.
     *
     * @param idRol         identificador del rol a actualizar.
     * @param rolActualizado objeto {@link Rol} con los nuevos datos (solo se usa el campo rol).
     * @return el rol actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un rol con ese id.
     */
    Rol actualizar(Byte idRol, Rol rolActualizado);

    /**
     * Elimina un rol por su identificador.
     *
     * @param idRol identificador del rol a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un rol con ese id.
     */
    void eliminar(Byte idRol);

    // ===== Métodos que envuelven las consultas del RolRepository =====

    /**
     * Busca un rol por su nombre exacto.
     *
     * @param nombreRol nombre exacto del rol (columna rol).
     * @return el rol encontrado o null si no existe.
     */
    Rol obtenerPorNombre(String nombreRol);

    /**
     * Busca roles cuyo nombre contenga el texto indicado (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro del nombre del rol.
     * @return lista de roles cuyo nombre contiene el texto dado.
     */
    List<Rol> buscarPorNombreConteniendo(String texto);

    /**
     * Obtiene los roles creados dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de roles creados en ese rango.
     */
    List<Rol> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Lista todos los roles ordenados alfabéticamente por el campo rol.
     *
     * @return lista de roles ordenados ascendentemente por nombre.
     */
    List<Rol> listarOrdenadosPorNombre();

    /**
     * Cuenta cuántas asignaciones existen en la tabla orden_trabajo_mecanico por cada rol.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idRol (Byte o Number)</li>
     *     <li>[1] → nombreRol (String)</li>
     *     <li>[2] → cantidadAsignaciones (Long)</li>
     * </ul>
     *
     * @return lista de registros con la información [idRol, nombreRol, cantidadAsignaciones].
     */
    List<Object[]> contarAsignacionesPorRol();

    /**
     * Obtiene los roles que no tienen ninguna asignación en orden_trabajo_mecanico.
     *
     * @return lista de roles sin asignaciones.
     */
    List<Rol> listarRolesSinAsignaciones();

    /**
     * Calcula las horas totales trabajadas por cada rol en un rango de fechas,
     * tomando como referencia el campo created_at de orden_trabajo_mecanico.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idRol (Byte o Number)</li>
     *     <li>[1] → nombreRol (String)</li>
     *     <li>[2] → horasTotales (BigDecimal/Double)</li>
     * </ul>
     *
     * @param inicio fecha/hora de inicio del rango (inclusive).
     * @param fin    fecha/hora de fin del rango (inclusive).
     * @return lista de registros con [idRol, nombreRol, horasTotales].
     */
    List<Object[]> obtenerHorasTotalesPorRolEnRangoFechas(LocalDateTime inicio, LocalDateTime fin);
}
