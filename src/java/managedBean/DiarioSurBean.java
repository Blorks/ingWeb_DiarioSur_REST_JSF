/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import cliente.DiarioSur_Client;
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
    public DiarioSurBean() {
    }
    
    
    public List<Evento> mostrarTodosLosEventos(){
        DiarioSur_Client cliente = new DiarioSur_Client("evento");
        Response r = cliente.findAll_XML(Response.class);
        
        if(r.getStatus() == 200){
            GenericType<List<Evento>> genericType = new GenericType<List<Evento>>(){};
            List<Evento> eventos = r.readEntity(genericType);
            
            return eventos;
        }
        
        return null;
    }
    
    public List<Usuario> mostrarTodosLosUsuario(){
        DiarioSur_Client cliente = new DiarioSur_Client("usuario");
        Response r = cliente.findAll_XML(Response.class);
        
        if(r.getStatus() == 200){
            GenericType<List<Usuario>> genericType = new GenericType<List<Usuario>>(){};
            List<Usuario> user = r.readEntity(genericType);
            
            return user;
        }
        
        return null;
    }
}
