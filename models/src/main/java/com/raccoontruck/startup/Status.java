package com.raccoontruck.startup;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "status_history")
public class Status {
    @Id
    @Basic
    @Column(name = "loads_id")
    private Long id;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status_name")
    private EStatus name;

    @Basic
    @Column(name = "status_date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "loads_id", insertable = false, updatable = false, referencedColumnName = "id", nullable = false)
    private Load load;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EStatus getName() {
        return name;
    }

    public void setName(EStatus name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Load getLoad() {
        return load;
    }

    public void setLoad(Load load) {
        this.load = load;
    }
}