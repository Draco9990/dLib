package dLib.external;

import dLib.util.DLibLogger;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ExternalEditorCommunicationClient {
    /** Variables */
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Socket connection;

    private boolean terminate;

    /** Constructors */
    public ExternalEditorCommunicationClient(){
    }

    public boolean initialize(){
        this.connection = new Socket();

        this.terminate = false;

        try{
            connection.connect(new InetSocketAddress("127.0.0.1", 8081), 5000);
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        }catch (Exception e){
            System.err.println("Failed to initialize client due to "+ e.getLocalizedMessage());
            e.printStackTrace();

            return false;
        }

        if(!connection.isConnected()) return false;

        try{
            new Thread(){
                @Override
                public void run() {
                    super.run();

                    while(!terminate){
                        try{
                            Object receivedMessage;
                            synchronized (in){
                                receivedMessage = in.readObject();
                            }

                            if(receivedMessage instanceof String){
                                NetworkMessage networkMessage = NetworkMessage.fromString((String) receivedMessage);

                                if(networkMessage != null){
                                    ExternalEditorCommunicationManager.addMessage(networkMessage);
                                }
                            }
                        }
                        catch (EOFException | ClassCastException | NullPointerException ignored){
                            //Various exceptions that happen when starting/closing/reconnecting, ignore them
                        }
                        catch (RuntimeException | SocketException fatalException){
                            System.err.println("Fatal error occured while reading client message due to " + fatalException.getLocalizedMessage() + ". Shutting the client down.");
                            fatalException.printStackTrace();

                            terminate();
                        }
                        catch (Exception e){
                            System.err.println("Error occurred while reading client message due to " + e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }catch (Exception e){
            System.err.println("Failed to initialize client due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return true;
    }

    public void terminate(){
        this.terminate = true;

        try{
            connection.close();
        }catch (Exception e){
            System.err.println("Failed to terminate client due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        try{
            in.close();
        }catch (Exception e){
            System.err.println("Failed to terminate client due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        try{
            out.close();
        }catch (Exception e){
            System.err.println("Failed to terminate client due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        connection = null;
        in = null;
        out = null;
    }

    public boolean isConnected(){
        return connection != null && connection.isConnected();
    }

    public void sendMessage(NetworkMessage obj) {
        if(out == null) return;

        try{
            out.writeObject(obj.toString());
            out.flush();
            out.reset();
        } catch (Exception e){
            DLibLogger.logError("Failed to send message due to " + e.getMessage());
            e.printStackTrace();
        }
    }
}
