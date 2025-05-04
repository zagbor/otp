package com.zagbor.otp.service.impl;

import com.zagbor.otp.entity.User;
import com.zagbor.otp.service.MessageSender;
import com.zagbor.otp.smpp.SmppProperties;
import lombok.AllArgsConstructor;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.BindTransmitter;
import org.smpp.pdu.SubmitSM;
import org.smpp.pdu.SubmitSMResp;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SmsSender implements MessageSender {

    private final SmppProperties properties;

    @Override
    public void send(User user, String code) {
        Session session = null;

        try {
            TCPIPConnection connection = new TCPIPConnection(properties.getHost(), properties.getPort());
            connection.setReceiveTimeout(20000); // таймаут получения
            session = new Session(connection);

            BindRequest bindRequest = new BindTransmitter();
            bindRequest.setSystemId(properties.getSystemId());
            bindRequest.setPassword(properties.getPassword());
            bindRequest.setSystemType(properties.getSystemType());
            bindRequest.setInterfaceVersion((byte) 0x34);
            bindRequest.setAddressRange((byte) properties.getAddrTon(), (byte) properties.getAddrNpi(), "");
            bindRequest.setAddressRange("");

            BindResponse bindResponse = session.bind(bindRequest);
            System.out.println("Bind response: " + bindResponse.debugString());

            SubmitSM request = new SubmitSM();

            request.setSourceAddr(properties.getSourceAddr());
            request.setDestAddr(user.getPhoneNumber());
            request.setShortMessage(code);

            SubmitSMResp response = session.submit(request);
            System.out.println("Message sent. Message ID: " + response.getMessageId());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                try {
                    session.unbind();
                    session.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}