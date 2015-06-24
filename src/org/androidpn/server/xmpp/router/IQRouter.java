/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.xmpp.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.androidpn.server.xmpp.handler.IQAuthHandler;
import org.androidpn.server.xmpp.handler.IQChangePushable4GroupHandler;
import org.androidpn.server.xmpp.handler.IQCreateGroupHandler;
import org.androidpn.server.xmpp.handler.IQGoupMemberListHandler;
import org.androidpn.server.xmpp.handler.IQGroupRequestListHandler;
import org.androidpn.server.xmpp.handler.IQHandleGroupRequestHandler;
import org.androidpn.server.xmpp.handler.IQHandler;
import org.androidpn.server.xmpp.handler.IQJoinGroupHandler;
import org.androidpn.server.xmpp.handler.IQNotificationList4Group;
import org.androidpn.server.xmpp.handler.IQOutMember4GroupHandler;
import org.androidpn.server.xmpp.handler.IQRegisterHandler;
import org.androidpn.server.xmpp.handler.IQRosterHandler;
import org.androidpn.server.xmpp.handler.IQSearchGroupHandler;
import org.androidpn.server.xmpp.handler.IQSendMessage4GroupHandler;
import org.androidpn.server.xmpp.handler.IQUserGroupListHandler;
import org.androidpn.server.xmpp.handler.IQUserLoginHandler;
import org.androidpn.server.xmpp.handler.IQUserRegisterHandler;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.androidpn.server.xmpp.session.SessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;

/** 
 * This class is to route IQ packets to their corresponding handler.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class IQRouter {

    private final Log log = LogFactory.getLog(getClass());

    private SessionManager sessionManager;

    private List<IQHandler> iqHandlers = new ArrayList<IQHandler>();

    private Map<String, IQHandler> namespace2Handlers = new ConcurrentHashMap<String, IQHandler>();

    /**
     * Constucts a packet router registering new IQ handlers.
     */
    public IQRouter() {
    	//.. 添加iqHandlers
        sessionManager = SessionManager.getInstance();
        iqHandlers.add(new IQAuthHandler());
        iqHandlers.add(new IQRegisterHandler());
        iqHandlers.add(new IQRosterHandler());
        
        iqHandlers.add(new IQUserRegisterHandler());
        iqHandlers.add(new IQUserLoginHandler());
        iqHandlers.add(new IQCreateGroupHandler());
        iqHandlers.add(new IQUserGroupListHandler());
        iqHandlers.add(new IQNotificationList4Group());
        iqHandlers.add(new IQSendMessage4GroupHandler());
        iqHandlers.add(new IQSearchGroupHandler());
        iqHandlers.add(new IQJoinGroupHandler());
        iqHandlers.add(new IQHandleGroupRequestHandler());
        iqHandlers.add(new IQGroupRequestListHandler());
        iqHandlers.add(new IQGoupMemberListHandler());
        iqHandlers.add(new IQChangePushable4GroupHandler());
        iqHandlers.add(new IQOutMember4GroupHandler());
    }

    /**
     * Routes the IQ packet based on its namespace.
     * 
     * @param packet the packet to route
     */
    public void route(IQ packet) {
        if (packet == null) {
            throw new NullPointerException();
        }
        JID sender = packet.getFrom();
        ClientSession session = sessionManager.getSession(sender);

        if (session == null
                || session.getStatus() == Session.STATUS_AUTHENTICATED
                || ("jabber:iq:auth".equals(packet.getChildElement().getNamespaceURI())
                || "jabber:iq:register".equals(packet.getChildElement().getNamespaceURI()) 
                || "urn:ietf:params:xml:ns:xmpp-bind".equals(packet.getChildElement().getNamespaceURI()))) {
        	//.. handle(packet)
            handle(packet);
        } else {
            IQ reply = IQ.createResultIQ(packet);
            reply.setChildElement(packet.getChildElement().createCopy());
            reply.setError(PacketError.Condition.not_authorized);
            session.process(reply);
        }
    }

    private void handle(IQ packet) {
        try {
            Element childElement = packet.getChildElement();
            String namespace = null;
            if (childElement != null) {
                namespace = childElement.getNamespaceURI();
            }
            if (namespace == null) {
                if (packet.getType() != IQ.Type.result
                        && packet.getType() != IQ.Type.error) {
                    log.warn("Unknown packet " + packet);
                }
            } else {
            	//.. 根据namespace 获取IQhandler
                IQHandler handler = getHandler(namespace);
                
                if (handler == null) {
                    sendErrorPacket(packet,
                            PacketError.Condition.service_unavailable);
                } else {
                	//..
                    handler.process(packet);
                }
            }

        } catch (Exception e) {
            log.error("Could not route packet", e);
            Session session = sessionManager.getSession(packet.getFrom());
            if (session != null) {
                IQ reply = IQ.createResultIQ(packet);
                reply.setError(PacketError.Condition.internal_server_error);
                session.process(reply);
            }
        }
    }

    /**
     * Senda the error packet to the original sender
     */
    private void sendErrorPacket(IQ originalPacket,
            PacketError.Condition condition) {
        if (IQ.Type.error == originalPacket.getType()) {
            log.error("Cannot reply an IQ error to another IQ error: "
                    + originalPacket);
            return;
        }
        IQ reply = IQ.createResultIQ(originalPacket);
        reply.setChildElement(originalPacket.getChildElement().createCopy());
        reply.setError(condition);
        try {
            PacketDeliverer.deliver(reply);
        } catch (Exception e) {
            // Ignore
        }
    }

    /**
     * Adds a new IQHandler to the list of registered handler.
     * 
     * @param handler the IQHandler
     */
    public void addHandler(IQHandler handler) {
        if (iqHandlers.contains(handler)) {
            throw new IllegalArgumentException(
                    "IQHandler already provided by the server");
        }
        namespace2Handlers.put(handler.getNamespace(), handler);
    }

    /**
     * Removes an IQHandler from the list of registered handler.
     * 
     * @param handler the IQHandler
     */
    public void removeHandler(IQHandler handler) {
        if (iqHandlers.contains(handler)) {
            throw new IllegalArgumentException(
                    "Cannot remove an IQHandler provided by the server");
        }
        namespace2Handlers.remove(handler.getNamespace());
    }

    /**
     * Returns an IQHandler with the given namespace.
     * 先根据namespace 在namespace2Handlers（Map）里找对应handler，
     * 找不到则在iqHandlers（List）里匹配，匹配到的handler添加到namespace2Handlers里
     */
    private IQHandler getHandler(String namespace) {
    	//.. 根据namespace匹配handler
        IQHandler handler = namespace2Handlers.get(namespace);
        if (handler == null) {
            for (IQHandler handlerCandidate : iqHandlers) {
                if (namespace.equalsIgnoreCase(handlerCandidate.getNamespace())) {
                    handler = handlerCandidate;
                    namespace2Handlers.put(namespace, handler);
                    break;
                }
            }
        }
        return handler;
    }

}
