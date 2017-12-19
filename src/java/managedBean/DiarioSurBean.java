/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import clientes.clienteDateev;
import clientes.clienteEventos;
import clientes.clienteUsuario;
import entity.Dateev;
import entity.Evento;
import entity.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
    private List<Date> listaDias;

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
    
    public List<Date> getListaDias() {
        return listaDias;
    }

    public void setListaDias(List<Date> listaDias) {
        this.listaDias = listaDias;
    }
    
    
    public DiarioSurBean() {
    }
    
    //METODOS REFERENTES A LOS EVENTOS
    
    public List<Evento> mostrarTodosLosEventos(){
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.findAll_XML(Response.class);
        
        if(r.getStatus() == 200){
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>(){};
            List<Evento> eventos = r.readEntity(genericType);
            
            return eventos;
        }
        
        return null;
    }

    public List<Evento> mostrarTodosLosEventosRevisados(){
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.encontrarEventosRevisados_XML(Response.class);
        
        if(r.getStatus() == 200){
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>(){};
            List<Evento> eventos = r.readEntity(genericType);
            
            return eventos;
        }
        
        return null;
    }
    
    public void editarEvento(){
        clienteEventos cliente = new clienteEventos();
        cliente.edit_XML(Response.class, evento.getId().toString());
    }
    
    public void nuevoEvento(){
        clienteEventos cliente = new clienteEventos();
        evento.setUsuarioId(usuario);
        cliente.create_XML(evento);
        
        volver();
        
        /*
        String idTemp="";
        
        Response r = cliente.ultimoIDInsertado_XML(Response.class);
        if(r.getStatus() == 200){
            GenericType<Integer> genericType = new GenericType<Integer>(){};
            idTemp = r.readEntity(genericType).toString();
        }
        
        r = cliente.encontrarEventoByID_XML(Response.class, idTemp);
        if(r.getStatus() == 200){
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>(){};
            List<Evento> eventos = r.readEntity(genericType);
            
            evento = eventos.get(0);
            
            adjuntarFecha();
        }
        */
    }
    
    public String borrarEvento(Evento e)
    {
        
        return "/";
    }
    
    public List<Evento> filtrarEventosDeUsuario(){
        clienteEventos cliente = new clienteEventos();
        Response r = cliente.encontrarEventoByUsuario_XML(Response.class, usuario.getId().toString());
        
        if(r.getStatus() == 200){
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>(){};
            List<Evento> eventos = r.readEntity(genericType);
            
            return eventos;
        }
        
        return null;
    }
    
    
    
    //METODOS REFERENTES A LOS USUARIOS
    
    public String autorEvento(Usuario user){
        return user.getNombre() + " " + user.getApellidos();
    }
    
    
    //METODOS REFERENTES A LAS FECHAS
    private List<Dateev> encontrarFechaPorDia(){
        clienteDateev cliente = new clienteDateev();
        Response r = cliente.encontrarFechaPorDia_XML(Response.class, fecha.getDia().toString());
        
        if(r.getStatus() == 200){
            GenericType<List<Dateev>> genericType = new GenericType<List<Dateev>>(){};
            List<Dateev> fechas = r.readEntity(genericType);
            
            return fechas;
        }
        
        return null;
    }
    
    private List<Dateev> encontrarFechaPorInicioFin(){
        clienteDateev cliente = new clienteDateev();
        Response r = cliente.encontrarFechaPorInicioFin_XML(Response.class, fecha.getDesde().toString(), fecha.getHasta().toString());
        
        if(r.getStatus() == 200){
            GenericType<List<Dateev>> genericType = new GenericType<List<Dateev>>(){};
            List<Dateev> fechas = r.readEntity(genericType);
            
            return fechas;
        }
        
        return null;
    }
    
    private List<Dateev> encontrarFechaPorListaDias(){
        clienteDateev cliente = new clienteDateev();
        Response r = cliente.encontrarFechaPorListaDias_XML(Response.class, getListaDias().toString());
        
        if(r.getStatus() == 200){
            GenericType<List<Dateev>> genericType = new GenericType<List<Dateev>>(){};
            List<Dateev> fechas = r.readEntity(genericType);
            
            return fechas;
        }
        
        return null;
    }
    
    private void crearFecha(){
        clienteDateev cliente = new clienteDateev();
        cliente.create_XML(fecha);
    }
    
    private List<Dateev> comprobarFecha() {
        List<Dateev> listaFecha;
        int activar = 1;
        int desactivar = 0;

        if(fecha.getDia() != null){
            listaFecha = encontrarFechaPorDia();
            
            if(listaFecha.isEmpty()){
                fecha.setEsunico(activar);
                fecha.setDia(fecha.getDia());
                fecha.setTodoslosdias(desactivar);
                fecha.setDesde(null);
                fecha.setHasta(null);
                fecha.setVariosdias(desactivar);
                fecha.setListadias(null);
                
                crearFecha();
                listaFecha = encontrarFechaPorDia();
            }
            
        }else if(fecha.getDesde() != null && fecha.getHasta() != null){
            listaFecha = encontrarFechaPorInicioFin();
            
            if(listaFecha.isEmpty()){
                fecha.setEsunico(desactivar);
                fecha.setDia(null);
                fecha.setTodoslosdias(activar);
                fecha.setDesde(fecha.getDesde());
                fecha.setHasta(fecha.getHasta());
                fecha.setVariosdias(desactivar);
                fecha.setListadias(null);
                
                crearFecha();
                listaFecha = encontrarFechaPorInicioFin();
            }
            
        }else{
            listaFecha = encontrarFechaPorListaDias();
            if(listaFecha.isEmpty()){
                fecha.setEsunico(desactivar);
                fecha.setDia(null);
                fecha.setTodoslosdias(desactivar);
                fecha.setDesde(null);
                fecha.setHasta(null);
                fecha.setVariosdias(activar);
                fecha.setListadias(listaDias.toString());
                
                crearFecha();
                listaFecha = encontrarFechaPorListaDias();
            }
        }
        
        return listaFecha;
    }
    
    public void adjuntarFecha(){
        
        List<Dateev> fechaCreada = comprobarFecha();
        
        evento.setDateevId(fechaCreada.get(0));
        editarEvento();
    }
    
 
    //METODOS DE MOVIMIENTO ENTRE JSF
    public String volver()
    {
        return "index";
    }
    
    public String verEvento(Evento e)
    {
        evento = e;
        return "evento";
    }
    
    public String irEditarEvento(Evento e)
    {
        evento = e;
        return "subirevento.xhtml";
    }
    
    public String irPerfil(){
        return "perfil.xhtml";
    }
    
    public String irTodosLosEventos(){
        return "todosloseventos.xhtml";
    }
    
    public String irValidarEvento(){
        return "validarEvento.xhtml";
    }
    
    public String irCrearEvento(){
        return "subirevento.xhtml";
    }

    
    //CONDICIONES TEST
    public boolean isLogin(){
        return (usuario.getEmail() == null || usuario.getEmail().equals(""));

    }
    
    public String isLoginIncl(){
        return isLogin()? "login.xhtml" : "logout.xhtml";
    }
    
    public void logIn(){
        clienteUsuario cliente = new clienteUsuario();
        Response r = cliente.encontrarUsuarioPorEmail_XML(Response.class, usuario.getEmail());
        
        if(r.getStatus() == 200){
            GenericType<List<Usuario>> genericType = new GenericType<List<Usuario>>(){};
            List<Usuario> usuarios = r.readEntity(genericType);
            
            usuario = usuarios.get(0);
        }else{
            usuario.setEmail("");
        }
    }
    
    public String logout(){
        usuario.setEmail("");
        
        return "index";
    }
    
    public boolean esPeriodista(){
        return usuario.getRol().equals("Periodista");
    }
}
