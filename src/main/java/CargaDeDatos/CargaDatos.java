/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CargaDeDatos;

import Conexion.ClienteDAO;
import Conexion.DestinoDAO;
import Conexion.PagoDAO;
import Conexion.PaqueteDAO;
import Conexion.ProveedorDAO;
import Conexion.ReservacionDAO;
import Conexion.ServicioDAO;
import Conexion.UsuarioDAO;
import Logica.Cliente;
import Logica.Destino;
import Logica.Paquete;
import Logica.Proveedor;
import Logica.Reservacion;
import Logica.Servicio;
import Logica.Usuario;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author fernan
 */
public class CargaDatos {
    
    private UsuarioDAO dao = new UsuarioDAO();
    private Set<String> destinosLeidos = new HashSet<>();
    private DestinoDAO destinoDAO = new DestinoDAO();
    private ProveedorDAO proveedorDAO = new ProveedorDAO();
    private Set<String> proveedoresLeidos = new HashSet<>();
    private PaqueteDAO paqueteDAO = new PaqueteDAO();
    private Set<String> paquetesLeidos = new HashSet<>();
    private ServicioDAO servicioDAO = new ServicioDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ReservacionDAO reservacionDAO = new ReservacionDAO();
    private Set<String> reservacionesLeidas = new HashSet<>();
    
    public void procesarLinea(String linea) {
        
        if (linea == null || linea.trim().isEmpty()) {
            return;
        }
        
        if (linea.startsWith("USUARIO")) {
            procesarUsuario(linea);
        }
        if (linea.startsWith("DESTINO")) {
            procesarDestino(linea);
        }
        if (linea.startsWith("PROVEEDOR")) {
            procesarProveedor(linea);
        }
        if (linea.startsWith("PAQUETE")) {
            procesarPaquete(linea);
        }
        if (linea.startsWith("SERVICIO_PAQUETE")) {
            procesarServicioPaquete(linea);
        }
        if (linea.startsWith("CLIENTE")) {
            procesarCliente(linea);
        }
        if (linea.startsWith("RESERVACION")) {
            procesarReservacion(linea);
        }
        if (linea.startsWith("PAGO")) {
            procesarPago(linea);
        }
    }
    
    private void procesarUsuario(String linea) {
        
        try {
            if (!linea.contains("(") || !linea.contains(")")) {
                System.out.println("Error de formato: " + linea);
                return;
            }
            
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            
            String[] partes = datos.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            if (partes.length != 3) {
                System.out.println("Formato incorrecto: " + linea);
                return;
            }
            
            String username = partes[0].replace("\"", "").trim();
            String password = partes[1].replace("\"", "").trim();
            int tipo = Integer.parseInt(partes[2].trim());

            //VALIDAR USERNAME VACIO
            if (username.isEmpty()) {
                System.out.println("Username invalido (vacio): " + linea);
                return;
            }
            
            if (password.length() < 6) {
                System.out.println("Password invalida: " + username);
                return;
            }
            
            if (dao.existeUsuario(username)) {
                System.out.println("Usuario ya existe: " + username);
                return;
            }
            
            String rol;
            
            try {
                rol = obtenerRol(tipo);
            } catch (Exception e) {
                System.out.println("Tipo invalido: " + linea);
                return;
            }
            
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setPassword(password);
            u.setRol(rol);
            
            boolean creado = dao.crearUsuario(u);
            
            if (creado) {
                System.out.println("Usuario creado: " + username);
            } else {
                System.out.println("Error al insertar: " + username);
            }
            
        } catch (Exception e) {
            System.out.println("Error en linea: " + linea);
        }
    }
    
    private String obtenerRol(int tipo) {
        switch (tipo) {
            case 1:
                return "ATENCION";
            case 2:
                return "OPERACIONES";
            case 3:
                return "ADMIN";
            default:
                throw new IllegalArgumentException("Tipo invalido");
        }
    }
    
    private void procesarDestino(String linea) {
        
        try {
            if (!linea.contains("(") || !linea.contains(")")) {
                System.out.println("Error de formato: " + linea);
                return;
            }
            
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            
            String[] partes = datos.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            if (partes.length != 3) {
                System.out.println("Formato incorrecto: " + linea);
                return;
            }
            
            String nombre = partes[0].replace("\"", "").trim();
            String pais = partes[1].replace("\"", "").trim();
            String descripcion = partes[2].replace("\"", "").trim();

            // VALIDACIONES
            if (nombre.isEmpty()) {
                System.out.println("Nombre vacio: " + linea);
                return;
            }
            
            if (pais.isEmpty()) {
                System.out.println("Pais vacio: " + linea);
                return;
            }
            
            if (descripcion.isEmpty()) {
                System.out.println("Descripcion vacia: " + linea);
                return;
            }

            // evitar duplicados en archivo
            if (destinosLeidos.contains(nombre)) {
                System.out.println("Destino repetido en archivo: " + nombre);
                return;
            }
            destinosLeidos.add(nombre);
            Destino d = new Destino();
            d.setNombre(nombre);
            d.setPais(pais);
            d.setDescripcion(descripcion);

            //valores opcionales (no vienen en archivo)
            d.setClima("No especificado");
            d.setImagen("");
            
            boolean creado = destinoDAO.crearDestino(d);
            
            if (creado) {
                System.out.println("Destino creado: " + nombre);
            } else {
                System.out.println("Error al insertar destino: " + nombre);
            }
            
        } catch (Exception e) {
            System.out.println("Error en linea DESTINO: " + linea);
        }
    }
    
    private void procesarProveedor(String linea) {
        
        try {
            if (!linea.contains("(") || !linea.contains(")")) {
                System.out.println("Error de formato: " + linea);
                return;
            }
            
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            
            String[] partes = datos.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            if (partes.length != 3) {
                System.out.println("Formato incorrecto: " + linea);
                return;
            }
            
            String nombre = partes[0].replace("\"", "").trim();
            int tipoNum = Integer.parseInt(partes[1].trim());
            String pais = partes[2].replace("\"", "").trim();

            // VALIDACION
            if (nombre.isEmpty()) {
                System.out.println("Nombre proveedor vacio: " + linea);
                return;
            }
            
            if (pais.isEmpty()) {
                System.out.println("Pais proveedor vacio: " + linea);
                return;
            }

            // evitar duplicados en archivo
            if (proveedoresLeidos.contains(nombre)) {
                System.out.println("Proveedor repetido en archivo: " + nombre);
                return;
            }
            proveedoresLeidos.add(nombre);
            
            String tipo;
            
            try {
                tipo = obtenerTipoProveedor(tipoNum);
            } catch (Exception e) {
                System.out.println("Tipo proveedor invalido: " + linea);
                return;
            }
            
            Proveedor p = new Proveedor();
            p.setNombre(nombre);
            p.setTipo(tipo);
            p.setPais(pais);
            p.setContacto("No especificado"); // NO ESTA EN ARCHIVO

            boolean creado = proveedorDAO.crearProveedor(p);
            
            if (creado) {
                System.out.println("Proveedor creado: " + nombre);
            } else {
                System.out.println("Error al insertar proveedor: " + nombre);
            }
            
        } catch (Exception e) {
            System.out.println("Error en linea PROVEEDOR: " + linea);
        }
    }
    
    private String obtenerTipoProveedor(int tipo) {
        switch (tipo) {
            case 1:
                return "AEROLINEA";
            case 2:
                return "HOTEL";
            case 3:
                return "TOUR";
            case 4:
                return "TRASLADO";
            case 5:
                return "OTRO";
            default:
                throw new IllegalArgumentException("Tipo invalido");
        }
    }
    
    private void procesarPaquete(String linea) {
        
        try {
            if (!linea.contains("(") || !linea.contains(")")) {
                System.out.println("Error formato PAQUETE: " + linea);
                return;
            }
            
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            
            String[] partes = datos.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            if (partes.length != 5) {
                System.out.println("Formato incorrecto PAQUETE: " + linea);
                return;
            }
            
            String nombre = partes[0].replace("\"", "").trim();
            String nombreDestino = partes[1].replace("\"", "").trim();
            int duracion = Integer.parseInt(partes[2].trim());
            double precio = Double.parseDouble(partes[3].trim());
            int capacidad = Integer.parseInt(partes[4].trim());

            //VALIDACIONES
            if (nombre.isEmpty()) {
                System.out.println("Nombre paquete vacío: " + linea);
                return;
            }
            
            if (duracion <= 0 || capacidad <= 0) {
                System.out.println("Duración o capacidad inválida: " + linea);
                return;
            }

            // EVITAR DUPLICADOS EN EL ARCHIVO
            if (paquetesLeidos.contains(nombre)) {
                System.out.println("Paquete repetido en archivo: " + nombre);
                return;
            }
            paquetesLeidos.add(nombre);

            // EVITAR DUPLICADOS EN BD
            if (paqueteDAO.existePaquete(nombre)) {
                System.out.println("Paquete ya existe en BD: " + nombre);
                return;
            }

            // OBTENER DESTINO
            int destinoId = destinoDAO.obtenerIdPorNombre(nombreDestino);
            
            if (destinoId == -1) {
                System.out.println("Destino no existe: " + nombreDestino);
                return;
            }

            // CREAMOS OBJEJTO
            Paquete p = new Paquete();
            p.setNombre(nombre);
            p.setDestinoId(destinoId);
            p.setDuracion(duracion);
            p.setDescripcion("Paquete turístico");
            p.setPrecio(precio);
            p.setCapacidad(capacidad);
            
            boolean creado = paqueteDAO.crearPaquete(p);
            
            if (creado) {
                System.out.println("Paquete creado: " + nombre);
            } else {
                System.out.println("Error al crear paquete BD: " + nombre);
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error numérico en PAQUETE: " + linea);
        } catch (Exception e) {
            System.out.println("Error en linea PAQUETE: " + linea);
        }
    }
    
    private void procesarServicioPaquete(String linea) {
        
        try {
            if (!linea.contains("(") || !linea.contains(")")) {
                System.out.println("Error formato SERVICIO: " + linea);
                return;
            }
            
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            
            String[] partes = datos.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            if (partes.length != 4) {
                System.out.println("Formato incorrecto SERVICIO: " + linea);
                return;
            }
            
            String nombrePaquete = partes[0].replace("\"", "").trim();
            String nombreProveedor = partes[1].replace("\"", "").trim();
            String descripcion = partes[2].replace("\"", "").trim();
            double costo = Double.parseDouble(partes[3].trim());

            //VALIDACIONES
            if (descripcion.isEmpty()) {
                System.out.println("Descripcion vacia");
                return;
            }
            
            if (costo <= 0) {
                System.out.println("Costo invalido");
                return;
            }

            //OBTENER IDS
            int paqueteId = paqueteDAO.obtenerIdPorNombre(nombrePaquete);
            int proveedorId = proveedorDAO.obtenerIdPorNombre(nombreProveedor);
            
            if (paqueteId == -1) {
                System.out.println("Paquete no existe: " + nombrePaquete);
                return;
            }
            
            if (proveedorId == -1) {
                System.out.println("Proveedor no existe: " + nombreProveedor);
                return;
            }

            // CREAR SERVICIO
            Servicio s = new Servicio();
            s.setPaqueteId(paqueteId);
            s.setProveedorId(proveedorId);
            s.setNombre(descripcion);
            s.setCosto(costo);
            
            boolean creado = servicioDAO.crearServicio(s);
            
            if (creado) {
                System.out.println("Servicio agregado a paquete: " + nombrePaquete);
            } else {
                System.out.println("Error al insertar servicio: " + descripcion);
            }
            
        } catch (Exception e) {
            System.out.println("Error en linea SERVICIO: " + linea);
        }
    }
    
    private void procesarCliente(String linea) {
        
        try {
            if (!linea.contains("(") || !linea.contains(")")) {
                System.out.println("Error formato CLIENTE: " + linea);
                return;
            }
            
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            
            String[] partes = datos.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            if (partes.length != 6) {
                System.out.println("Formato incorrecto CLIENTE: " + linea);
                return;
            }
            
            String dpi = partes[0].replace("\"", "").trim();
            String nombre = partes[1].replace("\"", "").trim();
            String fechaTexto = partes[2].replace("\"", "").trim();
            String telefono = partes[3].replace("\"", "").trim();
            String correo = partes[4].replace("\"", "").trim();
            String nacionalidad = partes[5].replace("\"", "").trim();

            // VALIDACIONES
            if (dpi.isEmpty()) {
                System.out.println("DPI vacío");
                return;
            }
            
            if (nombre.isEmpty()) {
                System.out.println("Nombre vacío");
                return;
            }
            
            if (telefono.isEmpty()) {
                System.out.println("Teléfono vacío");
                return;
            }
            
            if (correo.isEmpty()) {
                System.out.println("Correo vacío");
                return;
            }

            // DUPLICADO
            if (clienteDAO.buscarPorDpi(dpi) != null) {
                System.out.println("Cliente ya existe: " + dpi);
                return;
            }

            // FECHA
            String fechaFormateada;
            
            try {
                java.time.format.DateTimeFormatter entrada
                        = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                
                java.time.LocalDate fecha = java.time.LocalDate.parse(fechaTexto, entrada);
                
                fechaFormateada = fecha.toString();

            } catch (Exception e) {
                System.out.println("Fecha inválida: " + fechaTexto);
                return;
            }

            // CREACION
            Cliente c = new Cliente();
            c.setDpi(dpi);
            c.setNombre(nombre);
            c.setFechaNacimiento(fechaFormateada);
            c.setTelefono(telefono);
            c.setCorreo(correo);
            c.setNacionalidad(nacionalidad);
            
            boolean creado = clienteDAO.crearCliente(c);
            
            if (creado) {
                System.out.println("Cliente creado: " + nombre);
            } else {
                System.out.println("Error al crear cliente: " + nombre);
            }
            
        } catch (Exception e) {
            System.out.println("Error en linea CLIENTE: " + linea);
        }
    }
    
    private void procesarReservacion(String linea) {
        
        try {
            if (!linea.contains("(") || !linea.contains(")")) {
                System.out.println("Error formato RESERVACION: " + linea);
                return;
            }
            
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            String[] partes = datos.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            if (partes.length != 4) {
                System.out.println("Formato incorrecto RESERVACION: " + linea);
                return;
            }
            
            String nombrePaquete = partes[0].replace("\"", "").trim();
            String username = partes[1].trim();
            String fechaTexto = partes[2].replace("\"", "").trim();
            String dpisTexto = partes[3].replace("\"", "").trim();
            
            if (nombrePaquete.isEmpty() || username.isEmpty()) {
                System.out.println("Datos vacíos en reservación");
                return;
            }

            // FECHA
            String[] f = fechaTexto.split("/");
            String fechaSQL = f[2] + "-" + f[1] + "-" + f[0];

            // IDS
            int paqueteId = paqueteDAO.obtenerIdPorNombre(nombrePaquete);
            int usuarioId = usuarioDAO.obtenerIdPorUsername(username);
            
            if (paqueteId == -1 || usuarioId == -1) {
                System.out.println("Paquete o usuario no existe");
                return;
            }

            //DPIs
            String[] dpis = dpisTexto.split("\\|");
            
            if (dpis.length == 0) {
                System.out.println("No hay pasajeros");
                return;
            }
            
            for (String dpi : dpis) {
                if (clienteDAO.buscarPorDpi(dpi.trim()) == null) {
                    System.out.println("Cliente no existe: " + dpi);
                    return;
                }
            }

            //VALIDAR DUPLICADO
            if (reservacionDAO.existeReservacion(paqueteId, fechaSQL, usuarioId)) {
                System.out.println("Reservación ya existe");
                return;
            }

            //COSTO
            double precio = paqueteDAO.obtenerPrecio(paqueteId);
            double costoTotal = precio * dpis.length;

            //OBJETO
            Reservacion r = new Reservacion();
            r.setFechaViaje(fechaSQL);
            r.setPaqueteId(paqueteId);
            r.setCandidadPersonas(dpis.length);
            r.setCosotTotal(costoTotal);
            r.setIdUsurio(usuarioId);
            r.setDpis(dpis);
            
            int id = reservacionDAO.crearReservacion(r);
            
            if (id > 0) {
                System.out.println("Reservación creada ID: " + id);
            } else {
                System.out.println("Error al crear reservación");
            }
            
        } catch (Exception e) {
            System.out.println("Error en linea RESERVACION: " + linea);
        }
    }
    
    private void procesarPago(String linea) {
        
        try {
            if (!linea.contains("(") || !linea.contains(")")) {
                System.out.println("Error formato PAGO: " + linea);
                return;
            }
            
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            String[] partes = datos.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            if (partes.length != 4) {
                System.out.println("Formato incorrecto PAGO");
                return;
            }
            
            String codigo = partes[0].replace("\"", "").trim();
            double monto = Double.parseDouble(partes[1].trim());
            int metodoNum = Integer.parseInt(partes[2].trim());
            String fechaTexto = partes[3].replace("\"", "").trim();

            //ID RESERVACION
            int reservacionId;
            
            try {
                reservacionId = Integer.parseInt(codigo.replace("RES-", ""));
            } catch (Exception e) {
                System.out.println("Código inválido: " + codigo);
                return;
            }

            //VALIDAR EXISTE RESERVACION 
            if (!reservacionDAO.existeReservacionPorId(reservacionId)) {
                System.out.println("Reservación no existe: " + codigo);
                return;
            }

            // MÉTODO DE PAGO
            String metodo;
            switch (metodoNum) {
                case 1:
                    metodo = "Efectivo";
                    break;
                case 2:
                    metodo = "Tarjeta";
                    break;
                case 3:
                    metodo = "Transferencia";
                    break;
                default:
                    System.out.println("Método inválido");
                    return;
            }

            // FECHA
            java.time.format.DateTimeFormatter formatter
                    = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            java.time.LocalDate fechaLocal
                    = java.time.LocalDate.parse(fechaTexto, formatter);
            
            java.sql.Timestamp fecha
                    = java.sql.Timestamp.valueOf(fechaLocal.atStartOfDay());

            // GUARDAR
            PagoDAO dao = new PagoDAO();
            
            boolean ok = dao.registrarPago(reservacionId, monto, metodo, fecha);
            
            if (ok) {
                System.out.println("Pago registrado: " + codigo);
            } else {
                System.out.println("Error al registrar pago: " + codigo);
            }
            
        } catch (Exception e) {
            System.out.println("Error en línea PAGO: " + linea);
            e.printStackTrace();
        }
    }
}
