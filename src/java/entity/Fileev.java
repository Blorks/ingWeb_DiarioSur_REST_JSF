/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dani
 */
@Entity
@Table(name = "FILEEV")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fileev.findAll", query = "SELECT f FROM Fileev f")
    , @NamedQuery(name = "Fileev.findById", query = "SELECT f FROM Fileev f WHERE f.id = :id")
    , @NamedQuery(name = "Fileev.findByUrl", query = "SELECT f FROM Fileev f WHERE f.url = :url")
    , @NamedQuery(name = "Fileev.findByUsuarioId", query = "SELECT f FROM Fileev f WHERE f.usuarioId = :usuarioId")})
public class Fileev implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 4000)
    @Column(name = "URL")
    private String url;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USUARIO_ID")
    private int usuarioId;
    @OneToMany(mappedBy = "fileevId")
    private List<Usuario> usuarioList;

    public Fileev() {
    }

    public Fileev(Integer id) {
        this.id = id;
    }

    public Fileev(Integer id, int usuarioId) {
        this.id = id;
        this.usuarioId = usuarioId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
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
        if (!(object instanceof Fileev)) {
            return false;
        }
        Fileev other = (Fileev) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Fileev[ id=" + id + " ]";
    }
    
}
