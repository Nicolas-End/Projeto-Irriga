package iot.com.projeto_irriga.infra.utils.model;

import iot.com.projeto_irriga.infra.utils.DateUtil;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Date;

public class EntityModel {

    protected Date createdAt;

    protected Date updatedAt;

    @PrePersist
    protected void PrePersit(){
        this.createdAt = DateUtil.GetPresent();
    }

    @PreUpdate
    protected void  PreUpdate(){
        this.updatedAt = DateUtil.GetPresent();
    }
}
