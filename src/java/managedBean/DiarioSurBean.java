/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import clientes.clienteEventos;
import clientes.clienteUsuario;
import entity.Evento;
import entity.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
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
    
    private Usuario usuario;
    private Evento evento;

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
    
    public DiarioSurBean() {
    }
    
    
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

    public List<Usuario> mostrarTodosLosUsuario(){
        clienteUsuario cliente = new clienteUsuario();
        Response r = cliente.findAll_XML(Response.class);
        
        if(r.getStatus() == 200){
            GenericType<List<Usuario>> genericType = new GenericType<List<Usuario>>(){};
            List<Usuario> usuarios = r.readEntity(genericType);
            
            return usuarios;
        }
        
        return null;
    }
    
    //metodos a implementar cuando dani meta las funciones del servidor, pero al menos está la estructura
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
 
    
    public String volver()
    {
        return "index";
    }
    
    public String verEvento(Evento e)
    {
        evento = e;
        return "evento";
    }
}
