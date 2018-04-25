package com.github.fontys.jms.listeners;

import com.github.fontys.jms.messaging.StandardMessage;

import javax.jms.JMSException;

public interface ClientInterfaceObject {
    void receivedAction(StandardMessage standardMessage) throws JMSException;
}
