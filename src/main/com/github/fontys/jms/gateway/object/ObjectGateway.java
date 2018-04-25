package com.github.fontys.jms.gateway.object;

import com.github.fontys.jms.gateway.MessageReceiverGateway;
import com.github.fontys.jms.gateway.MessageSenderGateway;
import com.github.fontys.jms.listeners.ClientInterfaceObject;
import com.github.fontys.jms.messaging.StandardMessage;
import com.github.fontys.jms.serializer.ObjectSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;

public class ObjectGateway<OBJECT> {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiverGateway;
    private ObjectSerializer serializer;
    private ClientInterfaceObject clientInterface;

    private final Class<OBJECT> objectClass;

    public ObjectGateway(ClientInterfaceObject clientInterface, String senderChannel, String receiverChannel, String provider, Class<OBJECT> objectClass) throws JMSException, NamingException {
        this.sender = new MessageSenderGateway(senderChannel, provider);
        this.objectClass = objectClass;
        this.serializer = new ObjectSerializer(objectClass);
        this.receiverGateway = new MessageReceiverGateway(receiverChannel, provider);
        this.clientInterface = clientInterface;
        this.receiverGateway.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    onReplyArrived(serializer.standardMessageFromString(((TextMessage) message).getText()));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ObjectGateway(MessageListener messageListener, String senderChannel, String receiverChannel, String provider) throws JMSException, NamingException {
        this.sender = new MessageSenderGateway(senderChannel, provider);
        this.receiverGateway = new MessageReceiverGateway(receiverChannel, provider);
        this.receiverGateway.setListener(messageListener);
        this.objectClass = null;
    }

    public void send(StandardMessage sm) throws JMSException {
        sender.send(sender.createTextMessage(serializer.standardMessageToString(sm)));
    }

    public void send(String string) throws JMSException{
        sender.send(sender.createTextMessage(string));
    }

    public void onReplyArrived(StandardMessage sm) throws JMSException {
        if(sm != null) {
            clientInterface.receivedAction(sm);
        }else{
            throw new JMSException("Received a message with a null value");
        }
    }
}