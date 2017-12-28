/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import clientes.clienteDateev;
import clientes.clienteEventos;
import clientes.clienteFileev;
import clientes.clienteNotificacion;
import clientes.clientePuntuacion;
import clientes.clienteTag;
import clientes.clienteTagUsuario;
import clientes.clienteTagevento;
import clientes.clienteUsuario;
import entity.Dateev;
import entity.Evento;
import entity.Fileev;
import entity.Notificacion;
import entity.Puntuacion;
import entity.Tag;
import entity.Tagevento;
import entity.Tagusuario;
import entity.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Dani
 */
@Named(value = "diarioSurBean")
@SessionScoped
public class DiarioSurBean implements Serializable {

    /**
     * Creates a new instance of diarioSurBean
     */
    private Usuario usuario = new Usuario();
    private double usuarioLatitud = 0.0;
    private double usuarioLongitud = 0.0;

    private Evento evento = new Evento();
    private int edit = 0;

    private Dateev fecha = new Dateev();
    private String listaDias = "";
    private String[] arFecha;

    private String tagsEvento = "";
    private String tagsUsuario = "";

    private String diaBusqueda;
    private int distMaxima;
    private String precioMax;

    private String usuarioFoto;

    public void rrssLogin() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            Map params = externalContext.getRequestParameterMap();

            if (params.size() > 0) {
                usuario = new Usuario();
                usuario.setRol("");

                usuarioFoto = params.get("picture").toString();

                usuario.setEmail(params.get("email").toString());

                if (!logIn()) {
                    usuario.setNombre(params.get("first_name").toString());
                    usuario.setApellidos(params.get("last_name").toString());
                    usuario.setEmail(params.get("email").toString());
                    usuario.setRol("Usuario");
                    nuevoUsuario(usuario);
                }
            }
        } catch (Exception e) {
            System.out.println("Error en RRSS: " + e.getMessage());
        }
    }

    public void nuevoUsuario(Usuario us) {
        clienteUsuario cliente = new clienteUsuario();
        Response r = cliente.encontrarUsuarioPorEmail_XML(Response.class, usuario.getEmail());
        if (r.getStatus() == 200) {
            GenericType<List<Usuario>> genericType = new GenericType<List<Usuario>>() {
            };
            List<Usuario> usuarios = r.readEntity(genericType);

            if (usuarios.isEmpty()) {
                cliente.create_XML(us);

                logIn();
                if (!usuarioFoto.isEmpty()) {
                    adjuntarFotoDePerfil(usuarioFoto);
                }
            }
        }

    }

    //GETTER Y SETTERS
    public String getUsuarioFoto() {
        return usuarioFoto;
    }
    public void setUsuarioFoto(String usuarioFoto) {
        this.usuarioFoto = usuarioFoto;
    }
    public String getPrecioMax() {
        return precioMax;
    }
    public void setPrecioMax(String precioMax) {
        this.precioMax = precioMax;
    }
    public int getDistMaxima() {
        return distMaxima;
    }
    public void setDistMaxima(int distMaxima) {
        this.distMaxima = distMaxima;
    }
    public String getDiaBusqueda() {
        return diaBusqueda;
    }
    public void setDiaBusqueda(String diaBusqueda) {
        this.diaBusqueda = diaBusqueda;
    }
    public Evento getEvento() {
        return evento;
    }
    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Dateev getFecha() {
        return fecha;
    }
    public void setFecha(Dateev fecha) {
        this.fecha = fecha;
    }
    public String getListaDias() {
        return listaDias;
    }
    public void setListaDias(String listaDias) {
        this.listaDias = listaDias;
    }
    public String getTagsEvento() {
        return tagsEvento;
    }
    public void setTagsEvento(String tagsEvento) {
        this.tagsEvento = tagsEvento;
    }
    public String getTagsUsuario() {
        return tagsUsuario;
    }
    public void setTagsUsuario(String tagsUsuario) {
        this.tagsUsuario = tagsUsuario;
    }
    public double getUsuarioLatitud() {
        return usuarioLatitud;
    }
    public void setUsuarioLatitud(double usuarioLatitud) {
        this.usuarioLatitud = usuarioLatitud;
    }
    public double getUsuarioLongitud() {
        return usuarioLongitud;
    }
    public void setUsuarioLongitud(double usuarioLongitud) {
        this.usuarioLongitud = usuarioLongitud;
    }

    @PostConstruct
    public void init() {
        usuario.setRol("");
    }

    public DiarioSurBean() {
    }

    //METODOS REFERENTES A LOS EVENTOS
    public int ultimoIDEventoIncrementado() {
        int id = 0;
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.ultimoIDInsertado_XML(Response.class);

        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);
            id = eventos.get(0).getId() + 1;
        }

        return id;
    }

    public List<Evento> mostrarTodosLosEventos() {
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.findAll_XML(Response.class);

        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            return eventos;
        }

        return null;
    }

    public List<Evento> mostrarTodosLosEventosRevisados() {
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.encontrarEventosRevisados_XML(Response.class);

        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            return eventos;
        }

        return null;
    }

    public List<Evento> mostrarTodosLosEventosNoRevisados() {
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.encontrarEventosNoRevisados_XML(Response.class);

        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            return eventos;
        }

        return null;
    }

    public void editarEvento() {
        clienteEventos cliente = new clienteEventos();
        clienteDateev cliente2 = new clienteDateev();

        String idFechaTemp = evento.getDateevId().getId().toString();

        evento.setDateevId(null);
        cliente.edit_XML(evento, evento.getId().toString());

        cliente2.remove(idFechaTemp);

        //adjuntarFecha();

        cliente.edit_XML(evento, evento.getId().toString());
    }

    private int actualizarIDEvento() {
        int id = 0;
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.ultimoIDInsertado_XML(Response.class);

        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> lista = r.readEntity(genericType);

            id = lista.get(0).getId();
        }
        return id;
    }

    public String nuevoEvento() {
        if (edit == 0) {
            clienteEventos cliente = new clienteEventos();
            clienteDateev clienteFecha = new clienteDateev();

            //Adjunto el usuario creador
            evento.setUsuarioId(usuario);

            //Adjunto la fecha del evento
            adjuntarFecha();

            //Adjunto si está revisado o no
            if (esPeriodista()) {
                evento.setEstarevisado(1);
            } else {
                evento.setEstarevisado(0);
            }

            cliente.create_XML(evento);
            evento.setId(actualizarIDEvento());

            //Adjunto tags al evento
            adjuntarTagsEvento();

            fecha.setEventoId(actualizarIDEvento());
            clienteFecha.edit_XML(fecha, fecha.getId().toString());

            // reset variables
            evento = null;

            evento = new Evento();

            return "index";
        } else {
            editarEvento();
            edit = 0;

            return "todoloseventos.xhtml";
        }
    }

    public String borrarEvento(Evento ev) {
        clienteEventos cliente = new clienteEventos();
        clienteDateev clienteFecha = new clienteDateev();

        Response r = clienteFecha.encontrarFechaPorID_XML(Response.class, ev.getDateevId().getId().toString());
        if (r.getStatus() == 200) {
            GenericType<List<Dateev>> genericType = new GenericType<List<Dateev>>(){};
            List<Dateev> listaFecha = r.readEntity(genericType);

            cliente.remove(ev.getId().toString());

            r = cliente.encontrarEventosPorFecha_XML(Response.class, listaFecha.get(0).getId().toString());
            if(r.getStatus() == 200){
                GenericType<List<Evento>> genericType2 = new GenericType<List<Evento>>(){};
                List<Evento> listaEvento = r.readEntity(genericType2);

                if(listaEvento.isEmpty()){
                    clienteFecha.remove(ev.getDateevId().getId().toString());
                }
            }
        }

        return "todoloseventos.xhtml";
    }

    public List<Evento> filtrarEventosDeUsuario() {
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.encontrarEventoByUsuario_XML(Response.class, usuario.getId().toString());

        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            return eventos;
        }

        return null;
    }

    public String validarEvento(Evento ev) {
        clienteEventos cliente = new clienteEventos();
        Evento eventoTemporal = ev;

        eventoTemporal.setEstarevisado(1);

        cliente.edit_XML(eventoTemporal, ev.getId().toString());

        return "validarEvento.xhtml";
    }

    //METODOS REFERENTES A LOS TAGS
    public List<Tag> encontrarTagsDeUsuario() {
        clienteTagUsuario cliente = new clienteTagUsuario();
        clienteTag cliente2 = new clienteTag();
        List<Tag> tagsUsuarioTemp = new ArrayList<>();

        Response r = cliente.encontrarTagUser_XML(Response.class, usuario.getId().toString());

        if (r.getStatus() == 200) {
            GenericType<List<Tagusuario>> genericType = new GenericType<List<Tagusuario>>() {
            };
            GenericType<List<Tag>> genericType2 = new GenericType<List<Tag>>() {
            };
            List<Tagusuario> lista = r.readEntity(genericType);

            for (int i = 0; i < lista.size(); i++) {
                r = cliente2.encontrarTagPorNombre_XML(Response.class, lista.get(i).getTagId().getNombre());

                if (r.getStatus() == 200) {
                    List<Tag> lista2 = r.readEntity(genericType2);
                    tagsUsuarioTemp.add(lista2.get(0));
                }
            }
        }
        return tagsUsuarioTemp;
    }

    public List<Tag> encontrarTagsDeEvento() {
        clienteTagevento cliente = new clienteTagevento();
        clienteTag cliente2 = new clienteTag();
        List<Tag> tagsEventoTemp = new ArrayList<>();

        Response r = cliente.encontrarTagEv_XML(Response.class, evento.getId().toString());
        if (r.getStatus() == 200) {
            GenericType<List<Tagevento>> genericType = new GenericType<List<Tagevento>>() {
            };
            GenericType<List<Tag>> genericType2 = new GenericType<List<Tag>>() {
            };
            List<Tagevento> lista = r.readEntity(genericType);

            for (int i = 0; i < lista.size(); i++) {
                r = cliente2.encontrarTagPorNombre_XML(Response.class, lista.get(i).getTagId().getNombre());

                if (r.getStatus() == 200) {
                    List<Tag> lista2 = r.readEntity(genericType2);
                    tagsEventoTemp.add(lista2.get(0));
                }
            }
        }
        return tagsEventoTemp;
    }

    public Tag encontrarUnTagsDeEvento() {
        return encontrarTagsDeEvento().size() > 0 ? encontrarTagsDeEvento().get(0) : null;
    }

    private Tag crearTag(String strTag) {
        clienteTag cliente = new clienteTag();
        Tag tag = new Tag();
        tag.setNombre(strTag);

        cliente.create_XML(tag);

        Response r = cliente.encontrarTagPorNombre_XML(Response.class, strTag);
        if (r.getStatus() == 200) {
            GenericType<List<Tag>> genericType = new GenericType<List<Tag>>() {
            };
            List<Tag> lista = r.readEntity(genericType);

            tag = lista.get(0);
        }

        return tag;
    }

    public void adjuntarTagsEvento() {
        clienteTagevento cliente = new clienteTagevento();
        String[] partes = tagsEvento.split(",");
        String sinEspacio;
        Tag tagCreado;
        Tagevento tagEv = new Tagevento();

        for (int i = 0; i < partes.length; i++) {
            sinEspacio = partes[i].trim().toLowerCase();
            tagCreado = crearTag(sinEspacio);

            tagEv.setEventoId(evento);
            tagEv.setTagId(tagCreado);

            cliente.create_XML(tagEv);
        }
    }

    public void adjuntarTagsUsuario() {
        clienteTagUsuario cliente = new clienteTagUsuario();
        String[] partes = tagsUsuario.split(",");
        String sinEspacio;
        Tag tagCreado;
        Tagusuario tagUs = new Tagusuario();

        for (int i = 0; i < partes.length; i++) {
            sinEspacio = partes[i].trim().toLowerCase();
            tagCreado = crearTag(sinEspacio);

            tagUs.setUsuarioId(usuario);
            tagUs.setTagId(tagCreado);

            cliente.create_XML(tagUs);
        }
    }

    public String eliminarTagDeUsuario(Tag tagUsuario) {
        clienteTag cliente = new clienteTag();
        clienteTagUsuario cliente2 = new clienteTagUsuario();

        Response r = cliente2.encontrarTagUserPorTagyUsuario_XML(Response.class, tagUsuario.getId().toString(), usuario.getId().toString());
        if (r.getStatus() == 200) {
            GenericType<List<Tagusuario>> genericType = new GenericType<List<Tagusuario>>() {
            };
            List<Tagusuario> lista = r.readEntity(genericType);

            for (int i = 0; i < lista.size(); i++) {
                cliente2.remove(lista.get(i).getId().toString());
            }

            cliente.remove(tagUsuario.getId().toString());
        }

        return "perfil";
    }

    public String eliminarTagEvento(Tag tagEvento) {
        clienteTag cliente = new clienteTag();
        clienteTagevento cliente2 = new clienteTagevento();

        Response r = cliente2.encontrarTagEvPorTagyEvento_JSON(Response.class, tagEvento.getId().toString(), evento.getId().toString());
        if (r.getStatus() == 200) {
            GenericType<List<Tagevento>> genericType = new GenericType<List<Tagevento>>() {
            };
            List<Tagevento> lista = r.readEntity(genericType);

            for (int i = 0; i < lista.size(); i++) {
                cliente2.remove(lista.get(i).getId().toString());
            }

            cliente.remove(tagEvento.getId().toString());
        }

        return "evento";
    }

    //METODOS REFERENTES A LOS USUARIOS
    public String autorEvento(Usuario user) {
        return user.getNombre() + " " + user.getApellidos();
    }

    //METODOS REFERENTES A LAS FECHAS
    public List<Dateev> mostrarTodasLasFechasUnicas() {
        clienteDateev cliente = new clienteDateev();
        Response r = cliente.encontrarFechaPorUnica_XML(Response.class);

        if (r.getStatus() == 200) {
            GenericType<List<Dateev>> genericType = new GenericType<List<Dateev>>() {
            };
            List<Dateev> lista = r.readEntity(genericType);

            return lista;
        }

        return null;
    }

    public String mostrarFormatoDia(Date dia) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechatmp = sdf.format(dia);
        return fechatmp;
    }

    public List<Dateev> mostrarTodasLasFechasRango() {
        clienteDateev cliente = new clienteDateev();
        Response r = cliente.encontrarFechaPorRango_XML(Response.class);

        if (r.getStatus() == 200) {
            GenericType<List<Dateev>> genericType = new GenericType<List<Dateev>>() {
            };
            List<Dateev> lista = r.readEntity(genericType);

            return lista;
        }

        return null;
    }

    public String mostrarFechaDeEvento(Evento ev) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        if (ev.getDateevId().getEsunico() == 1) {
            return formato.format(ev.getDateevId().getDia());
        } else if (ev.getDateevId().getTodoslosdias() == 1) {
            return formato.format(ev.getDateevId().getDesde()) + " - " + formato.format(ev.getDateevId().getHasta());
        } else {
            arFecha = ev.getDateevId().getListadias().trim().split(",");

            return arFecha[0] + " y varias fechas más.";
        }
    }

    private int actualizarIDFecha() {
        int id = 0;
        clienteDateev cliente = new clienteDateev();
        Response r = cliente.ultimoIDInsertado_XML(Response.class);

        if (r.getStatus() == 200) {
            GenericType<List<Dateev>> genericType = new GenericType<List<Dateev>>() {
            };
            List<Dateev> lista = r.readEntity(genericType);

            id = lista.get(0).getId();
        }
        return id;
    }

    private void crearFechaUnica() {
        boolean encontrado = false;
        Date fechaTemp;
        Date fechaTemp2 = fecha.getDia();
        int test;

        clienteDateev cliente = new clienteDateev();
        Response r = cliente.findAll_XML(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Dateev>> genericType = new GenericType<List<Dateev>>() {
            };
            List<Dateev> lista = r.readEntity(genericType);

            for (int i = 0; i < lista.size(); i++) {
                fechaTemp = lista.get(i).getDia();

                if (fechaTemp != null) {
                    test = fechaTemp.compareTo(fechaTemp2);

                    if (test == 0) {
                        encontrado = true;
                        fecha.setId(lista.get(i).getId());
                    }
                }

            }

            if (encontrado == false) {
                cliente.create_XML(fecha);
                fecha.setId(actualizarIDFecha());
            }
        }
    }

    private void crearFechaRango() {
        boolean encontrado = false;
        Date fechaTemp;
        Date fechaTemp2 = fecha.getDesde();
        Date fechaTemp21 = fecha.getHasta();
        int test, test2;

        clienteDateev cliente = new clienteDateev();
        Response r = cliente.findAll_XML(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Dateev>> genericType = new GenericType<List<Dateev>>() {
            };
            List<Dateev> lista = r.readEntity(genericType);

            for (int i = 0; i < lista.size(); i++) {
                fechaTemp = lista.get(i).getDesde();

                if (fechaTemp != null) {
                    test = fechaTemp.compareTo(fechaTemp2);

                    fechaTemp = lista.get(i).getHasta();

                    test2 = fechaTemp.compareTo(fechaTemp21);

                    if (test == 0 && test2 == 0) {
                        encontrado = true;
                        fecha.setId(lista.get(i).getId());
                    }
                }
            }

            if (encontrado == false) {
                cliente.create_XML(fecha);
                fecha.setId(actualizarIDFecha());
            }
        }
    }
    
        private void crearFechaListaDias() {
        boolean encontrado = false;
        String fechas;

        clienteDateev cliente = new clienteDateev();
        Response r = cliente.findAll_XML(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Dateev>> genericType = new GenericType<List<Dateev>>() {
            };
            List<Dateev> lista = r.readEntity(genericType);

            for (int i = 0; i < lista.size(); i++) {
                if(lista.get(i).getListadias() != null){
                    fechas = lista.get(i).getListadias();
                
                    encontrado = fechas.equals(fecha.getListadias());
                    if(encontrado == true){
                        fecha.setId(lista.get(i).getId());
                    }
                }
                
            }

            if (encontrado == false) {
                cliente.create_XML(fecha);
                fecha.setId(actualizarIDFecha());
            }
        }
    }

    public void adjuntarFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        if (fecha.getEsunico() == 1) {
            try {
                fecha.setDia(formato.parse(listaDias));
            } catch (ParseException ex) {
                Logger.getLogger(DiarioSurBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            fecha.setDesde(null);
            fecha.setHasta(null);
            fecha.setListadias(null);

            crearFechaUnica();

        } else if (fecha.getTodoslosdias() == 1) {
            arFecha = listaDias.trim().split(",");

            try {
                fecha.setDesde(formato.parse(arFecha[0]));
                fecha.setHasta(formato.parse(arFecha[1]));
            } catch (ParseException ex) {
                Logger.getLogger(DiarioSurBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            fecha.setDia(null);
            fecha.setListadias(null);

            crearFechaRango();

        } else {
            fecha.setDia(null);
            fecha.setDesde(null);
            fecha.setHasta(null);
            fecha.setListadias(listaDias);

            crearFechaListaDias();
        }

        evento.setDateevId(fecha);
    }

    //METODOS DE MOVIMIENTO ENTRE JSF
    public String volver() {
        return "index.xhtml";
    }

    public String verEvento(Evento e) {
        evento = e;
        if (evento.getUsuarioId().getFileevId() == null) {
            Fileev f = new Fileev();
            f.setId(0);
            f.setUrl("http://localhost:54747/ingWeb_DiarioSur_REST_JSF/faces/resources/images/user.png");
            evento.getUsuarioId().setFileevId(f);
        }
        return "evento";
    }

    public String irEditarEvento(Evento e) {
        evento = e;
        edit = 1;
        return "subirevento.xhtml";
    }

    public String irPerfil() {
        return "perfil.xhtml";
    }

    public String irMisEvento() {
        return "perfil.xhtml";
    }

    public String irTodosLosEventos() {
        return "todoloseventos.xhtml";
    }

    public String irValidarEvento() {
        return "validarEvento.xhtml";
    }

    public String irCrearEvento() {
        // inicializo al primer caso que viene activado en la pagina

        fecha = new Dateev();
        fecha.setEsunico(1);
        fecha.setTodoslosdias(0);
        fecha.setVariosdias(0);

        if (edit == 0) {
            double precioTemp = 0.0;

            evento.setTitulo("");
            evento.setSubtitulo("");
            evento.setPrecio(precioTemp);
            evento.setDescripcion("");
        }

        return "subirevento.xhtml";
    }

    public String irAnadirTagsEvento() {
        return "anadirTagsEvento";
    }

    //CONDICIONES TEST
    public boolean isLogin() {
        return (usuario.getEmail() == null || usuario.getEmail().equals(""));

    }

    public String isLoginIncl() {
        return isLogin() ? "login.xhtml" : "logout.xhtml";
    }

    public boolean logIn() {
        clienteUsuario cliente = new clienteUsuario();
        Response r = cliente.encontrarUsuarioPorEmail_XML(Response.class, usuario.getEmail());
        if (r.getStatus() == 200) {
            GenericType<List<Usuario>> genericType = new GenericType<List<Usuario>>() {
            };
            List<Usuario> usuarios = r.readEntity(genericType);

            if (!usuarios.isEmpty()) {
                usuario = usuarios.get(0);

                if (usuario.getFileevId() != null) {
                    usuarioFoto = usuario.getFileevId().getUrl();
                }
                return true;
            } else {
                return false;
            }
        } else {
            usuario.setEmail("");
            return false;
        }
    }

    public String logout() {
        usuario = new Usuario();
        usuario.setEmail("");

        return "index";
    }

    public boolean esPeriodista() {
        return usuario.getRol().equals("Periodista");
    }

    //FUNCIONES MOSTRAR EVENTOS POR FILTROS Y ORDEN ALVARO
    public String irEventosFiltradosFecha(Dateev fechaTemp) {
        fecha = fechaTemp;
        return "eventosFiltradosFecha";
    }

    public List<Evento> mostrarEventosFiltradosPorFecha() {
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.encontrarEventosPorFecha_XML(Response.class, fecha.getId().toString());

        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            return eventos;
        }

        return null;
    }

    public String irIntroducirFechaBusqueda() {
        return "introducirFechaBusqueda.xhtml";
    }

    public String irEventosFiltradosDireccion() {
        return "eventosFiltradosDireccion";
    }

    public String irIntroducirDistanciaMaxima() {
        return "introducirDistanciaMaxima.xhtml";
    }

    public String irEventosFiltradosPrecio() {
        return "eventosFiltradosPrecio";
    }

    public String irIntroducirPrecioMaximo() {
        return "introducirPrecioMaximo.xhtml";
    }

    public List<Evento> mostrarEventosFiltradosPorPrecio() {
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.encontrarEventoByPrecioMax_XML(Response.class, precioMax);
        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            return eventos;
        }

        return null;
    }
    
    public String irElegirSentidoAlfabetico() {
        return "elegirSentidoAlfabetico.xhtml";
    }

    public String irEventosOrdenadosAlfabeticamente() {
        return "eventosOrdenadosAlfabeticamente.xhtml";
    }

    public List<Evento> mostrarEventosOrdenadosAlfabeticamente() {
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.ordenarEventosAlfabeticamente_XML(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            return eventos;
        }

        return null;
    }
    
    public String irEventosOrdenadosAlfabeticamenteDESC() {
        return "eventosOrdenadosAlfabeticamenteDESC.xhtml";
    }
    
    public List<Evento> mostrarEventosOrdenadosAlfabeticamenteDESC() {
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.ordenarEventosAlfabeticamenteDESC_XML(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            return eventos;
        }

        return null;
    }
    
    public String irElegirSentidoFecha() {
        return "elegirSentidoFecha.xhtml";
    }

    public String irEventosOrdenadosFecha() {
        return "eventosOrdenadosFecha.xhtml";
    }

    public List<Evento> mostrarEventosOrdenadosPorFecha() {
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.findAll_XML(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            //return eventos;
            Map<Evento, Date> mapa = new HashMap<>();
            //List<Date> fechas = new ArrayList<>();
            for (Evento e : eventos) {
                if (e.getDateevId().getEsunico() == 1) {
                    mapa.put(e, e.getDateevId().getDia());
                } //fechas.add(e.getDateevId().getDia());
                else if (e.getDateevId().getTodoslosdias() == 1) {
                    mapa.put(e, e.getDateevId().getDesde());
                } //fechas.add(e.getDateevId().getDesde());
                else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String[] stringFechas = e.getDateevId().getListadias().trim().split(",");
                    String primerDia = stringFechas[0];
                    try {
                        Date d = sdf.parse(primerDia);
                        mapa.put(e, d);
                        //fechas.add(d);
                    } catch (ParseException ex) {
                        Logger.getLogger(DiarioSurBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            //Ordenar Map por Valor

            HashMap mapaOrdenado = new LinkedHashMap();
            List<Evento> mapaKeys = new ArrayList(mapa.keySet());
            List mapaValues = new ArrayList(mapa.values());
            TreeSet conjuntoOrdenado = new TreeSet(mapaValues);
            Object[] arrayOrdenado = conjuntoOrdenado.toArray();
            int size = arrayOrdenado.length;
            for (int i = 0; i < size; i++) {
                mapaOrdenado.put(mapaKeys.get(mapaValues.indexOf(arrayOrdenado[i])), arrayOrdenado[i]);
            }
            List<Evento> eventosOrdenados = new ArrayList<>();
            /*Iterator it = mapaOrdenado.values().iterator();
            while (it.hasNext()) {
            System.out.println((String)it.next());*/
            Iterator it = mapaOrdenado.keySet().iterator();
            while (it.hasNext()) {
                eventosOrdenados.add((Evento) it.next());
            }
            return eventosOrdenados;
        }

        return null;
    }
    
    public String irEventosOrdenadosFechaDESC() {
        return "eventosOrdenadosFechaDESC.xhtml";
    }
    
    public List<Evento> mostrarEventosOrdenadosPorFechaDESC() {
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.findAll_XML(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            //return eventos;
            Map<Evento, Date> mapa = new HashMap<>();
            //List<Date> fechas = new ArrayList<>();
            for(Evento e : eventos)
            {
                if(e.getDateevId().getEsunico() == 1)
                    mapa.put(e, e.getDateevId().getDia());
                    //fechas.add(e.getDateevId().getDia());
                else if(e.getDateevId().getTodoslosdias() == 1)
                    mapa.put(e, e.getDateevId().getDesde());
                    //fechas.add(e.getDateevId().getDesde());
                else
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String[] stringFechas = e.getDateevId().getListadias().trim().split(",");
                    String primerDia = stringFechas[0];
                    try {
                        Date d = sdf.parse(primerDia);
                        mapa.put(e, d);
                        //fechas.add(d);
                    } catch (ParseException ex) {
                        Logger.getLogger(DiarioSurBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            //Ordenar Map por Valor

            HashMap mapaOrdenado = new LinkedHashMap();
            List<Evento> mapaKeys = new ArrayList(mapa.keySet());
            List mapaValues = new ArrayList(mapa.values());
            TreeSet conjuntoOrdenado = new TreeSet(mapaValues);
            Object[] arrayOrdenado = conjuntoOrdenado.toArray();
            int size = arrayOrdenado.length;
            for (int i=size-1; i>-1; i--)
            {
                mapaOrdenado.put(mapaKeys.get(mapaValues.indexOf(arrayOrdenado[i])),arrayOrdenado[i]);
            }
            List<Evento> eventosOrdenados = new ArrayList<>();
            /*Iterator it = mapaOrdenado.values().iterator();
            while (it.hasNext()) {
            System.out.println((String)it.next());*/
            Iterator it = mapaOrdenado.keySet().iterator();
            while(it.hasNext())
                eventosOrdenados.add((Evento) it.next());
            return eventosOrdenados;
            }
        
        return null;
    }
    
    public String irElegirSentidoPrecio() {
        return "elegirSentidoPrecio.xhtml";
    }

    public String irEventosOrdenadosPrecio() {
        return "eventosOrdenadosPrecio.xhtml";
    }

    public List<Evento> mostrarEventosOrdenadosPorPrecio() {
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.ordenarEventosPrecio_XML(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            return eventos;
        }

        return null;
    }
    
    public String irEventosOrdenadosPrecioDESC() {
        return "eventosOrdenadosPrecioDESC.xhtml";
    }

    public List<Evento> mostrarEventosOrdenadosPorPrecioDESC() {
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.ordenarEventosPrecioDESC_XML(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            return eventos;
        }

        return null;
    }

    private double calcularDistanciaHastaEvento(double latitudEvento, double longitudEvento, double latitudUsuario, double longitudUsuario) {
        double radioTierra = 6371.137;
        double dLat = Math.toRadians(latitudUsuario - latitudEvento) / 2;
        double dLng = Math.toRadians(longitudUsuario - longitudEvento) / 2;

        double sindLat = Math.sin(dLat);
        double sindLng = Math.sin(dLng);

        double val = Math.pow(sindLat, 2) + Math.cos(Math.toRadians(latitudEvento)) * Math.pow(sindLng, 2) * Math.cos(Math.toRadians(latitudUsuario));
        val = Math.sqrt(val);
        double val2 = 2 * radioTierra * Math.asin(val);

        return val2;
    }

    public List<Evento> mostrarEventosFiltradosPorDistancia() {
        clienteEventos cliente = new clienteEventos();
        List<Evento> listaTemp = new ArrayList<>();
        Response r = cliente.findAll_XML(Response.class);

        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>() {
            };
            List<Evento> eventos = r.readEntity(genericType);

            for (int i = 0; i < eventos.size(); i++) {
                double distancia = calcularDistanciaHastaEvento(eventos.get(i).getLatitud(), eventos.get(i).getLongitud(), usuarioLatitud, usuarioLongitud);
                if (distancia <= distMaxima) {
                    listaTemp.add(eventos.get(i));
                }
            }
        }

        return listaTemp;
    }

    //METODOS REFERENTES A LOS FileEv
    public void adjuntarFotoDePerfil(String url) {
        clienteFileev cliente = new clienteFileev();
        clienteUsuario cliente2 = new clienteUsuario();

        Fileev file = new Fileev();
        file.setUrl(url);
        file.setUsuarioId(usuario.getId());

        /*
        cliente.create_XML(file);

        Response r = cliente.encontrarArchivoPorURL_XML(Response.class, url);
        if (r.getStatus() == 200) {
            GenericType<List<Fileev>> genericType = new GenericType<List<Fileev>>() {
            };
            List<Fileev> archivos = r.readEntity(genericType);

            file = archivos.get(0);
        }
         */
        usuario.setFileevId(file); //Al no existir el archivo file, la BD lo crea automaticamente
        cliente2.edit_XML(usuario, usuario.getId().toString());
    }

    /*
    private String mostrarFotoDePerfil() {
        clienteUsuario cliente = new clienteUsuario();
        Response r = cliente.find_XML(Response.class, usuario.getId().toString());

        if (r.getStatus() == 200) {
            GenericType<List<Usuario>> genericType = new GenericType<List<Usuario>>() {
            };
            List<Usuario> usuarios = r.readEntity(genericType);

            return usuarios.get(0).getFileevId().getUrl();
        }

        return null;
    }
     */
    //METODOS REFERENTES A LAS NOTIFICACIONES
    public void crearNotificacion(String contenido, Usuario user) {
        clienteNotificacion cliente = new clienteNotificacion();

        Notificacion not = new Notificacion();
        not.setDescripcion(contenido);
        not.setLeida(0);
        not.setUsuarioId(user);

        cliente.create_XML(not);
    }

    public List<Notificacion> mostrarNotificacionesDeUsuario(Usuario user) {
        clienteNotificacion cliente = new clienteNotificacion();
        Response r = cliente.encontrarTodasLasNotificacionesDeUsuario_XML(Response.class, user.getId().toString());

        if (r.getStatus() == 200) {
            GenericType<List<Notificacion>> genericType = new GenericType<List<Notificacion>>() {
            };
            List<Notificacion> notificaciones = r.readEntity(genericType);

            return notificaciones;
        }

        return null;
    }

    public List<Notificacion> mostrarNotificacionesNoLeidas(Usuario user) {
        clienteNotificacion cliente = new clienteNotificacion();
        Response r = cliente.encontrarNotificacionesDeUsuario_XML(Response.class, user.getId().toString());

        if (r.getStatus() == 200) {
            GenericType<List<Notificacion>> genericType = new GenericType<List<Notificacion>>() {
            };
            List<Notificacion> notificaciones = r.readEntity(genericType);

            return notificaciones;
        }

        return null;
    }

    public void marcarNotificacionComoLeida(Notificacion not) {
        clienteNotificacion cliente = new clienteNotificacion();
        Notificacion notTemp = not;
        notTemp.setLeida(1);

        cliente.edit_XML(notTemp, notTemp.getId().toString());
    }

    //METODOS REFERENTES A LAS PUNTUACIONES
    public void actualizarPuntuacion(double punto, Evento ev) {
        clientePuntuacion cliente = new clientePuntuacion();
        Response r = cliente.encontrarPuntuacionesDeEventoYUsuario_XML(Response.class, ev.getId().toString(), usuario.getId().toString());

        if (r.getStatus() == 200) {
            GenericType<List<Puntuacion>> genericType = new GenericType<List<Puntuacion>>() {
            };
            List<Puntuacion> puntuacion = r.readEntity(genericType);
            Puntuacion pt = new Puntuacion();

            if (puntuacion.isEmpty()) {
                pt.setPuntuacion(punto);
                pt.setEventoId(ev);
                pt.setUsuarioId(usuario);

                cliente.create_XML(pt);
            } else {
                pt = puntuacion.get(0);
                pt.setPuntuacion(punto);

                cliente.edit_XML(pt, pt.getId().toString());
            }
        }
    }

    public double mostrarPuntuacionMedia(Evento ev) {
        clientePuntuacion cliente = new clientePuntuacion();
        Response r = cliente.encontrarPuntuacionesDeEvento_XML(Response.class, ev.getId().toString());

        if (r.getStatus() == 200) {
            GenericType<List<Puntuacion>> genericType = new GenericType<List<Puntuacion>>() {
            };
            List<Puntuacion> puntuaciones = r.readEntity(genericType);
            double puntuacionTotal = 0;

            for (int i = 0; i < puntuaciones.size(); i++) {
                puntuacionTotal = puntuacionTotal + puntuaciones.get(i).getPuntuacion();
            }

            puntuacionTotal = puntuacionTotal / puntuaciones.size();

            return puntuacionTotal;
        }

        return 0;
    }
}
