package it.polimi.ingsw.model.events.messages.updates;

import it.polimi.ingsw.model.events.messages.Message;
import it.polimi.ingsw.view.VirtualView;


public interface ModelUpdateMessage extends Message {
    void updateView(VirtualView view) ;
}
