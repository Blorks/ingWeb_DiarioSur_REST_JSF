/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dani
 */
@Entity
@Table(name = "EVENTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evento.findAll", query = "SELECT e FROM Evento e")
    , @NamedQuery(name = "Evento.findByTitulo", query = "SELECT e FROM Evento e WHERE e.titulo = :titulo")
    , @NamedQuery(name = "Evento.findBySubtitulo", query = "SELECT e FROM Evento e WHERE e.subtitulo = :subtitulo")
    , @NamedQuery(name = "Evento.findByDescripcion", query = "SELECT e FROM Evento e WHERE e.descripcion = :descripcion")
    , @NamedQuery(name = "Evento.findById", query = "SELECT e FROM Evento e WHERE e.id = :id")
    , @NamedQuery(name = "Evento.findByDireccionfisica", query = "SELECT e FROM Evento e WHERE e.direccionfisica = :direccionfisica")
    , @NamedQuery(name = "Evento.findByPrecio", query = "SELECT e FROM Evento e WHERE e.precio = :precio")
    , @NamedQuery(name = "Evento.findByLatitud", query = "SELECT e FROM Evento e WHERE e.latitud = :latitud")
    , @NamedQuery(name = "Evento.findByLongitud", query = "SELECT e FROM Evento e WHERE e.longitud = :longitud")
    , @NamedQuery(name = "Evento.findByEstarevisado", query = "SELECT e FROM Evento e WHERE e.estarevisado = :estarevisado")})
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 4000)
    @Column(name = "TITULO")
    private String titulo;
    @Size(max = 4000)
    @Column(name = "SUBTITULO")
    private String subtitulo;
    @Size(max = 4000)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 4000)
    @Column(name = "DIRECCIONFISICA")
    private String direccionfisica;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRECIO")
    private Double precio;
    @Column(name = "LATITUD")
    private Double latitud;
    @Column(name = "LONGITUD")
    private Double longitud;
    @Column(name = "ESTAREVISADO")
    private Integer estarevisado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventoId")
    private List<Calendario> calendarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventoId")
    private List<Tagevento> tageventoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventoId")
    private List<Archivos> archivosList;
    @JoinColumn(name = "DATEEV_ID", referencedColumnName = "ID")
    @ManyToOne
    private Dateev dateevId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne
    private Usuario usuarioId;

    public Evento() {
    }

    public Evento(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDireccionfisica() {
        return direccionfisica;
    }

    public void setDireccionfisica(String direccionfisica) {
        this.direccionfisica = direccionfisica;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Integer getEstarevisado() {
        return estarevisado;
    }

    public void setEstarevisado(Integer estarevisado) {
        this.estarevisado = estarevisado;
    }

    @XmlTransient
    public List<Calendario> getCalendarioList() {
        return calendarioList;
    }

    public void setCalendarioList(List<Calendario> calendarioList) {
        this.calendarioList = calendarioList;
    }

    @XmlTransient
    public List<Tagevento> getTageventoList() {
        return tageventoList;
    }

    public void setTageventoList(List<Tagevento> tageventoList) {
        this.tageventoList = tageventoList;
    }

    @XmlTransient
    public List<Archivos> getArchivosList() {
        return archivosList;
    }

    public void setArchivosList(List<Archivos> archivosList) {
        this.archivosList = archivosList;
    }

    public Dateev getDateevId() {
        return dateevId;
    }

    public void setDateevId(Dateev dateevId) {
        this.dateevId = dateevId;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evento)) {
            return false;
        }
        Evento other = (Evento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Evento[ id=" + id + " ]";
    }
    
}
