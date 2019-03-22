package com.denizhan.householder.Network;

/*
    Yazacak Olan: Nehir
    Açıklama: UDP protokolünde byte gönderme
*/

import com.denizhan.householder.Network.Tools.COMMANDS;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPSender {

    // Alici adresi
    private InetAddress local_address;

    // Veri gonderme soketi
    private DatagramSocket sendingSocket;

    // Veri paketini tutmak için datagram paketi
    private DatagramPacket sendingPacket;

    public UDPSender(String ipAdress){
        try {
            sendingSocket = new DatagramSocket();
            local_address = InetAddress.getByName("192.168.43.1");//karsi tarafin ipsi
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] data){

        // paketi veri boyutuna göre guncelle
        sendingPacket = new DatagramPacket(data, data.length, local_address, 4445);
        try {
            sendingSocket.send(sendingPacket); // sokete veri yollama
        } catch (IOException e) {
            e.printStackTrace();
            // OlaGan hata paketin veri boyutunun udp buyuk olması
        }
    }
    public void sendCommand(COMMANDS command){
        sendingPacket = new DatagramPacket(command.toString().getBytes(), command.toString().getBytes().length, local_address, 4004); // paketi veri boyutuna göre güncelle
        try {
            sendingSocket.send(sendingPacket); // sokete veriyi gönder
        } catch (IOException e) {
            e.printStackTrace();
            // Olağan hata paketin veri boyutunun udp büyük olması
        }
    }

}