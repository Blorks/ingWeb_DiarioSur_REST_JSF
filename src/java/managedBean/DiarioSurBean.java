/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import clientes.clienteDateev;
import clientes.clienteEventos;
import clientes.clienteTag;
import clientes.clienteTagevento;
import clientes.clienteUsuario;
import entity.Dateev;
import entity.Evento;
import entity.Tag;
import entity.Tagevento;
import entity.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
    
    private Evento evento = new Evento();
    
    private Dateev fecha = new Dateev();
    private String listaDias = "";
    private String[] arFecha;
    
    private String tagsEvento = "";
    private String tagsUsuario = "";

    
    @PostConstruct
    public void init(){
        usuario.setRol("");
    }

    //Borrar hasta aquí
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

    

    public DiarioSurBean() {
    }

    //METODOS REFERENTES A LOS EVENTOS
    public int ultimoIDEventoIncrementado(){
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

    public void editarEvento() {
        clienteEventos cliente = new clienteEventos();
        cliente.edit_XML(Response.class, evento.getId().toString());
    }
    
    private int actualizarIDEvento(){
        int id = 0;
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.ultimoIDInsertado_XML(Response.class);
        
        if (r.getStatus() == 200) {
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>(){};
            List<Evento> lista = r.readEntity(genericType);
            
            id = lista.get(0).getId();
        }
        return id;
    }

    public String nuevoEvento() {
        clienteEventos cliente = new clienteEventos();
        clienteDateev clienteFecha = new clienteDateev();

        
        //Adjunto el usuario creador
        evento.setUsuarioId(usuario);

        //Adjunto la fecha del evento
        adjuntarFecha();
        
        //Adjunto si está revisado o no
        if(esPeriodista()){
            evento.setEstarevisado(1);
        }else{
            evento.setEstarevisado(0);
        }

        cliente.create_XML(evento);
        evento.setId(actualizarIDEvento());
        
        //Adjunto tags al evento
        adjuntarTagsEvento();
        
        fecha.setEventoId(actualizarIDEvento());
        clienteFecha.edit_XML(fecha, fecha.getId().toString());

        return "index";
    }

    public String borrarEvento(Evento e) {

        return "/";
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

    //METODOS REFERENTES A LOS TAGS
    public List<Tag> encontrarTagsDeUsuario() {
        return null;
    }

    public List<Tag> encontrarTagsDeEvento() {
        clienteTagevento cliente = new clienteTagevento();
        clienteTag cliente2 = new clienteTag();
        List<Tag> tagsEventoTemp = new ArrayList<>();
        
        Response r = cliente.encontrarTagEv_XML(Response.class, evento.getId().toString());
        if (r.getStatus() == 200) {
            GenericType<List<Tagevento>> genericType = new GenericType<List<Tagevento>>(){};
            GenericType<List<Tag>> genericType2 = new GenericType<List<Tag>>(){};
            List<Tagevento> lista = r.readEntity(genericType);
            
            for(int i=0; i<lista.size(); i++){
                r = cliente2.encontrarTagPorNombre_XML(Response.class, lista.get(i).getTagId().getNombre());
                
                if(r.getStatus() == 200){
                    List<Tag> lista2 = r.readEntity(genericType2);
                    tagsEventoTemp.add(lista2.get(0));
                }
            }
        }
        return tagsEventoTemp;     
    }
    
    private Tag crearTag(String strTag){
        clienteTag cliente = new clienteTag();
        Tag tag = new Tag();
        tag.setNombre(strTag);
        
        cliente.create_XML(tag);
        
        Response r = cliente.encontrarTagPorNombre_XML(Response.class, strTag);
        if (r.getStatus() == 200) {
            GenericType<List<Tag>> genericType = new GenericType<List<Tag>>(){};
            List<Tag> lista = r.readEntity(genericType);
            
            tag = lista.get(0);
        }

        return tag;
    }
    
    public void adjuntarTagsEvento(){
        clienteTagevento cliente = new clienteTagevento();
        String[] partes = tagsEvento.split(",");
        String sinEspacio;
        Tag tagCreado;
        Tagevento tagEv = new Tagevento();

        
        for(int i=0;i<partes.length; i++){
            sinEspacio = partes[i].trim().toLowerCase();
            tagCreado = crearTag(sinEspacio);
            
            tagEv.setEventoId(evento);
            tagEv.setTagId(tagCreado);
            
            cliente.create_XML(tagEv);
        }  
    }

    public String eliminarTagDeUsuario(int tagUsuario) {
        return "perfil";
    }

    public String eliminarTagEvento(int tagEvento) {
        //clienteTag cliente = new clienteTag();
        
        //Response r = cliente.find_XML(Response.class, tagEvento);
        
        
        
        
        
        
        return "evento";
    }

    //METODOS REFERENTES A LOS USUARIOS
    public String autorEvento(Usuario user) {
        return user.getNombre() + " " + user.getApellidos();
    }

    //METODOS REFERENTES A LAS FECHAS
    public String mostrarFechaDeEvento(Evento ev){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        
        if(ev.getDateevId().getEsunico() == 1){
            return formato.format(ev.getDateevId().getDia());
        }else if(ev.getDateevId().getTodoslosdias() == 1){
            return formato.format(ev.getDateevId().getDesde()) + " - " + formato.format(ev.getDateevId().getHasta());
        }else{
            arFecha = ev.getDateevId().getListadias().trim().split(",");
            
            return arFecha[0] + " y varias fechas más.";
        }
    }
    
    private void crearFecha() {
        clienteDateev cliente = new clienteDateev();
        cliente.create_XML(fecha);
    }
    
    private int actualizarIDFecha(){
        int id = 0;
        clienteDateev cliente = new clienteDateev();
        Response r = cliente.ultimoIDInsertado_XML(Response.class);
        
        if (r.getStatus() == 200) {
            GenericType<List<Dateev>> genericType = new GenericType<List<Dateev>>(){};
            List<Dateev> lista = r.readEntity(genericType);
            
            id = lista.get(0).getId();
        }
        return id;
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

            crearFecha();

            fecha.setId(actualizarIDFecha());

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

            crearFecha();

            fecha.setId(actualizarIDFecha());

        } else {
            fecha.setDia(null);
            fecha.setDesde(null);
            fecha.setHasta(null);
            fecha.setListadias(listaDias);

            crearFecha();

            fecha.setId(actualizarIDFecha());

        }

        evento.setDateevId(fecha);
    }

    //METODOS DE MOVIMIENTO ENTRE JSF
    public String volver() {
        return "index";
    }

    public String verEvento(Evento e) {
        evento = e;
        return "evento";
    }

    public String irEditarEvento(Evento e) {
        evento = e;
        return "subirevento.xhtml";
    }

    public String irPerfil() {
        return "perfil.xhtml";
    }

    public String irMisEvento() {
        /*tagUs = new AdjuntarTagUser();
        usuario = logginUsuario;*/
        return "perfil.xhtml";
    }

    public String irTodosLosEventos() {
        return "todosloseventos.xhtml";
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

    public void logIn() {
        clienteUsuario cliente = new clienteUsuario();
        Response r = cliente.encontrarUsuarioPorEmail_XML(Response.class, usuario.getEmail());

        if (r.getStatus() == 200) {
            GenericType<List<Usuario>> genericType = new GenericType<List<Usuario>>() {
            };
            List<Usuario> usuarios = r.readEntity(genericType);

            usuario = usuarios.get(0);
        } else {
            usuario.setEmail("");
        }
    }

    public String logout() {
        usuario.setEmail("");

        return "index";
    }

    public boolean esPeriodista() {
        return usuario.getRol().equals("Periodista");
    }
}
