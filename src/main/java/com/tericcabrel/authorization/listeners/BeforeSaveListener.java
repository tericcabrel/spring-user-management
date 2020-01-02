package com.tericcabrel.authorization.listeners;

import com.tericcabrel.authorization.models.BaseModel;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BeforeSaveListener extends AbstractMongoEventListener<BaseModel> {
    @Override
    public void onBeforeSave(BeforeSaveEvent<BaseModel> event) {
        super.onBeforeSave(event);
        Date dateNow = new Date();

        event.getSource().setCreatedAt(dateNow);
        event.getSource().setUpdatedAt(dateNow);

        System.out.println("OnBeforeSave");
    }
}
