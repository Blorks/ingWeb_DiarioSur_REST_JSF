/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dani
 */
@Entity
@Table(name = "DATEEV")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dateev.findAll", query = "SELECT d FROM Dateev d")
    , @NamedQuery(name = "Dateev.findById", query = "SELECT d FROM Dateev d WHERE d.id = :id")
    , @NamedQuery(name = "Dateev.findByEsunico", query = "SELECT d FROM Dateev d WHERE d.esunico = :esunico")
    , @NamedQuery(name = "Dateev.findByDia", query = "SELECT d FROM Dateev d WHERE d.dia = :dia")
    , @NamedQuery(name = "Dateev.findByTodoslosdias", query = "SELECT d FROM Dateev d WHERE d.todoslosdias = :todoslosdias")
    , @NamedQuery(name = "Dateev.findByDesde", query = "SELECT d FROM Dateev d WHERE d.desde = :desde")
    , @NamedQuery(name = "Dateev.findByHasta", query = "SELECT d FROM Dateev d WHERE d.hasta = :hasta")
    , @NamedQuery(name = "Dateev.findByVariosdias", query = "SELECT d FROM Dateev d WHERE d.variosdias = :variosdias")
    , @NamedQuery(name = "Dateev.findByListadias", query = "SELECT d FROM Dateev d WHERE d.listadias = :listadias")
    , @NamedQuery(name = "Dateev.findByEventoId", query = "SELECT d FROM Dateev d WHERE d.eventoId = :eventoId")
    , @NamedQuery(name = "Dateev.findByEventoId2", query = "SELECT d FROM Dateev d WHERE d.eventoId2 = :eventoId2")})
public class Dateev implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ESUNICO")
    private Integer esunico;
    @Column(name = "DIA")
    @Temporal(TemporalType.DATE)
    private Date dia;
    @Column(name = "TODOSLOSDIAS")
    private Integer todoslosdias;
    @Column(name = "DESDE")
    @Temporal(TemporalType.DATE)
    private Date desde;
    @Column(name = "HASTA")
    @Temporal(TemporalType.DATE)
    private Date hasta;
    @Column(name = "VARIOSDIAS")
    private Integer variosdias;
    @Size(max = 4000)
    @Column(name = "LISTADIAS")
    private String listadias;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EVENTO_ID")
    private int eventoId;
    @Column(name = "EVENTO_ID2")
    private Integer eventoId2;

    public Dateev() {
    }

    public Dateev(Integer id) {
        this.id = id;
    }

    public Dateev(Integer id, int eventoId) {
        this.id = id;
        this.eventoId = eventoId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEsunico() {
        return esunico;
    }

    public void setEsunico(Integer esunico) {
        this.esunico = esunico;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public Integer getTodoslosdias() {
        return todoslosdias;
    }

    public void setTodoslosdias(Integer todoslosdias) {
        this.todoslosdias = todoslosdias;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Integer getVariosdias() {
        return variosdias;
    }

    public void setVariosdias(Integer variosdias) {
        this.variosdias = variosdias;
    }

    public String getListadias() {
        return listadias;
    }

    public void setListadias(String listadias) {
        this.listadias = listadias;
    }

    public int getEventoId() {
        return eventoId;
    }

    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }

    public Integer getEventoId2() {
        return eventoId2;
    }

    public void setEventoId2(Integer eventoId2) {
        this.eventoId2 = eventoId2;
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
        if (!(object instanceof Dateev)) {
            return false;
        }
        Dateev other = (Dateev) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Dateev[ id=" + id + " ]";
    }
    
}
