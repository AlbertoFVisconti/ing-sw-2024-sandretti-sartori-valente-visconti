//package it.polimi.ingsw.events.messages.client;
//
//import it.polimi.ingsw.events.messages.MessageType;
//import it.polimi.ingsw.network.rmi.VirtualController;
//import it.polimi.ingsw.network.rmi.VirtualMainController;
//
//import java.rmi.RemoteException;
//
//public class PlayerListRequestMessage extends ClientMessage {
//        /**
//         * Builds a ClientMessage that asks the server for the players list.
//         */
//        public PlayerListRequestMessage() { super(MessageType.CONNECT_JOIN_MESSAGE);
//        }
//
//        @Override
//        public void execute(VirtualMainController selector, VirtualController controller) {
//            try {
//                selector.(getPlayerIdentifier());
//            } catch (RemoteException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//}
