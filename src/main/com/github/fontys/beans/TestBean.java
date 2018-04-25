package com.github.fontys.beans;

import com.github.fontys.constant.Constant;
import com.github.fontys.jms.gateway.object.ObjectGateway;
import com.github.fontys.jms.listeners.ClientInterfaceObject;
import com.github.fontys.jms.messaging.RequestReply;
import com.github.fontys.jms.messaging.StandardMessage;
import com.github.fontys.jms.object.VehicleObject;
import com.github.fontys.jms.reply.VehicleReply;
import com.github.fontys.jms.request.VehicleRequest;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;

@Path("/test")
@Stateless
public class TestBean {

    private ObjectGateway testGateway;

    @PostConstruct
    public void init(){
        try {
            this.testGateway = new ObjectGateway(new ClientInterfaceObject() {
                @Override
                public void receivedAction(StandardMessage standardMessage) throws JMSException {

                }
            }, Constant.SENDERCHANNEL, Constant.RECEIVERCHANNEL, Constant.PROVIDER, VehicleObject.class);
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }
    }

    public ObjectGateway getTestGateway() {
        return testGateway;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/call/{message}")
    public Response onMessage(@PathParam("message") String message){
        return Response.ok().build();
    }


}
