package com.technoidentity.entity;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Data
public class SharedModel {


    @Basic
    @Column(name = "created_by")
    private UUID createdBy;
    @Basic
    @Column(name = "updated_by")
    private UUID updatedBy;

    @Basic
    @Column(name="created_at",columnDefinition= "TIMESTAMP WITH TIME ZONE")
    public Date createdAt;
    @Basic
    @Column(name="updated_at" ,columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private Date updatedAt;
    @Basic
    @Column(name="status")
    private Integer status;


}

