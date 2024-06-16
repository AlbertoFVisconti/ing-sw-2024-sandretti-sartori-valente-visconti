package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatTest {
    Player p1,p2,p3,p4;
    Chat chat1, chat2, chat3, chat4, chat5;
    Chat[] chats;
    ChatMessage[] m1, m2, m3, m4, m5;
    ChatMessage[][] expectedChats;
    HashMap<String, Player> nickToPlayer;

    @BeforeEach
    void setup() {
        p1 = new Player("p1", null);
        p2 = new Player("p2", null);
        p3 = new Player("p3", null);
        p4 = new Player("p4", null);

        nickToPlayer = new HashMap<>();
        nickToPlayer.put("p1", p1);
        nickToPlayer.put("p2", p2);
        nickToPlayer.put("p3", p3);
        nickToPlayer.put("p4", p4);

        chat1 = new Chat();
        chat2 = new Chat();
        chat3 = new Chat();
        chat4 = new Chat();
        chat5 = new Chat();

        chats = new Chat[]{chat1, chat2, chat3, chat4, chat5};

        m1 = new ChatMessage[]{
                new ChatMessage("c1m1", "p1", "p2"),
                new ChatMessage("c1m2", "p2", "p1"),
                new ChatMessage("c1m3", "p3", "p1"),
                new ChatMessage("c1m4", "p1", "p2"),
                new ChatMessage("c1m5", "p1", null),
                new ChatMessage("c1m6", "p2", null),
                new ChatMessage("c1m7", "p3", null)
        };

        m2 = new ChatMessage[]{
                new ChatMessage("c2m1", "p1", null),
                new ChatMessage("c2m2", "p1", null),
                new ChatMessage("c2m3", "p1", null),
                new ChatMessage("c2m4", "p1", null),
                new ChatMessage("c2m5", "p1", null),
                new ChatMessage("c2m6", "p1", null),
                new ChatMessage("c2m7", "p2", null),
                new ChatMessage("c2m8", "p2", null),
                new ChatMessage("c2m9", "p2", null),
                new ChatMessage("c2m10", "p2", null),
                new ChatMessage("c2m11", "p2", null),
                new ChatMessage("c2m12", "p2", null)
        };

        m3 = new ChatMessage[]{
                new ChatMessage("c3m1", "p1", "p2"),
                new ChatMessage("c3m2", "p2", "p3"),
                new ChatMessage("c3m3", "p3", "p4"),
                new ChatMessage("c3m4", "p4", "p3"),
                new ChatMessage("c3m5", "p3", "p2"),
                new ChatMessage("c3m6", "p2", "p1")
        };

        m4 = new ChatMessage[]{
                new ChatMessage("c4m1", "p1", "p2"),
                new ChatMessage("c4m2", "p1", "p2"),
                new ChatMessage("c4m3", "p1", "p2"),
                new ChatMessage("c4m4", "p1", "p2"),
                new ChatMessage("c4m5", "p1", "p2"),
                new ChatMessage("c4m6", "p1", "p2")
        };

        m5 = new ChatMessage[]{};

        expectedChats = new ChatMessage[][]{m1,m2,m3,m4,m5};

        for(ChatMessage message : m1) {
            chat1.sendMessage(
                    nickToPlayer.get(message.getSenderNick()),
                    nickToPlayer.get(message.getReceiverNick()),
                    message.getText()
            );
        }

        for(ChatMessage message : m2) {
            chat2.sendMessage(
                    nickToPlayer.get(message.getSenderNick()),
                    nickToPlayer.get(message.getReceiverNick()),
                    message.getText()
            );
        }

        for(ChatMessage message : m3) {
            chat3.sendMessage(
                    nickToPlayer.get(message.getSenderNick()),
                    nickToPlayer.get(message.getReceiverNick()),
                    message.getText()
            );
        }

        for(ChatMessage message : m4) {
            chat4.sendMessage(
                    nickToPlayer.get(message.getSenderNick()),
                    nickToPlayer.get(message.getReceiverNick()),
                    message.getText()
            );
        }

        for(ChatMessage message : m5) {
            chat5.sendMessage(
                    nickToPlayer.get(message.getSenderNick()),
                    nickToPlayer.get(message.getReceiverNick()),
                    message.getText()
            );
        }
    }

    @Test
    void getMessagesChat() {
        List<ChatMessage> messages;

        for(int chatIndex = 0; chatIndex < chats.length; chatIndex++) {
            messages = chats[chatIndex].getMessagesChat(null, null);

            List<ChatMessage> expectedMessages = new ArrayList<>();
            for(ChatMessage message : expectedChats[chatIndex]) {
                if(message.getReceiverNick() == null) {
                    expectedMessages.add(message);
                }
            }

            assertEquals(expectedMessages.size(), messages.size());

            for(int i = 0; i < expectedMessages.size(); i++) {
                assertEquals(
                        expectedMessages.get(i).getSenderNick(),
                        messages.get(i).getSenderNick()
                );
                assertEquals(
                        expectedMessages.get(i).getReceiverNick(),
                        messages.get(i).getReceiverNick()
                );
                assertEquals(
                        expectedMessages.get(i).getText(),
                        messages.get(i).getText()
                );
            }

            for(String player1 : this.nickToPlayer.keySet()) {
                for(String player2 : this.nickToPlayer.keySet()) {
                    if(!player1.equals(player2)) {
                        messages = chats[chatIndex].getMessagesChat(player1, player2);

                        expectedMessages = new ArrayList<>();
                        for(ChatMessage message : expectedChats[chatIndex]) {
                            if(message.getReceiverNick() == null) continue;
                            if(message.getSenderNick().equals(player1) && message.getReceiverNick().equals(player2)) {
                                expectedMessages.add(message);
                            }
                            else if (message.getSenderNick().equals(player2) && message.getReceiverNick().equals(player1)) {
                                expectedMessages.add(message);
                            }
                        }


                        assertEquals(expectedMessages.size(), messages.size());

                        for(int i = 0; i < expectedMessages.size(); i++) {
                            assertEquals(
                                    expectedMessages.get(i).getSenderNick(),
                                    messages.get(i).getSenderNick()
                            );
                            assertEquals(
                                    expectedMessages.get(i).getReceiverNick(),
                                    messages.get(i).getReceiverNick()
                            );
                            assertEquals(
                                    expectedMessages.get(i).getText(),
                                    messages.get(i).getText()
                            );

                            if(i<expectedMessages.size()-1) {
                                assertTrue(expectedMessages.get(i).getTimestamp() <= messages.get(i).getTimestamp());
                            }
                        }
                    }
                }
            }
        }
    }

    void helperSendMessage(List<ChatMessage> expectedChat, Chat chat, Player sender, Player receiver, String text) {
        ChatMessage c = new ChatMessage(text, sender.nickname, (receiver != null ? receiver.nickname : null));
        expectedChat.add(c);
        chat.sendMessage(sender, receiver, text);
    }

    @Test
    void sendMessage() {
        ArrayList<ChatMessage>[] chatsLists = new ArrayList[]{
                new ArrayList<> (Arrays.stream(m1).toList()),
                new ArrayList<> (Arrays.stream(m2).toList()),
                new ArrayList<> (Arrays.stream(m3).toList()),
                new ArrayList<> (Arrays.stream(m4).toList()),
                new ArrayList<> (Arrays.stream(m5).toList())
        };

        helperSendMessage(chatsLists[0], chats[0], p1, p2, "nm1");
        helperSendMessage(chatsLists[1], chats[1], p3, p2, "nm2");
        helperSendMessage(chatsLists[2], chats[2], p4, p2, "nm3");
        helperSendMessage(chatsLists[3], chats[3], p4, p1, "nm4");
        helperSendMessage(chatsLists[4], chats[4], p1, p4, "nm5");
        helperSendMessage(chatsLists[3], chats[3], p2, p4, "nm6");
        helperSendMessage(chatsLists[2], chats[2], p3, p4, "nm7");
        helperSendMessage(chatsLists[1], chats[1], p1, p4, "nm8");
        helperSendMessage(chatsLists[0], chats[0], p4, p2, "nm9");
        helperSendMessage(chatsLists[1], chats[1], p2, p1, "nm10");
        helperSendMessage(chatsLists[2], chats[2], p3, p2, "nm11");
        helperSendMessage(chatsLists[3], chats[3], p1, p3, "nm12");
        helperSendMessage(chatsLists[4], chats[4], p4, p2, "nm13");
        helperSendMessage(chatsLists[0], chats[0], p1, p4, "nm14");
        helperSendMessage(chatsLists[1], chats[1], p4, p1, "nm15");
        helperSendMessage(chatsLists[2], chats[2], p3, p2, "nm16");
        helperSendMessage(chatsLists[3], chats[3], p3, p2, "nm17");
        helperSendMessage(chatsLists[4], chats[4], p2, p1, "nm18");
        helperSendMessage(chatsLists[4], chats[4], p2, p4, "nm19");
        helperSendMessage(chatsLists[3], chats[3], p2, p3, "nm20");
        helperSendMessage(chatsLists[2], chats[2], p3, p1, "nm21");
        helperSendMessage(chatsLists[1], chats[1], p3, p4, "nm22");
        helperSendMessage(chatsLists[0], chats[0], p1, p4, "nm23");
        helperSendMessage(chatsLists[0], chats[0], p1, p2, "nm24");
        helperSendMessage(chatsLists[4], chats[4], p2, p3, "nm25");

        helperSendMessage(chatsLists[0], chats[0], p1, null, "nm26");
        helperSendMessage(chatsLists[1], chats[1], p3, null, "nm27");
        helperSendMessage(chatsLists[2], chats[2], p4, null, "nm28");
        helperSendMessage(chatsLists[3], chats[3], p4, null, "nm29");
        helperSendMessage(chatsLists[4], chats[4], p1, null, "nm30");
        helperSendMessage(chatsLists[3], chats[3], p2, null, "nm31");
        helperSendMessage(chatsLists[2], chats[2], p3, null, "nm32");
        helperSendMessage(chatsLists[1], chats[1], p1, null, "nm33");
        helperSendMessage(chatsLists[0], chats[0], p4, null, "nm34");
        helperSendMessage(chatsLists[1], chats[1], p2, null, "nm35");
        helperSendMessage(chatsLists[2], chats[2], p3, null, "nm36");
        helperSendMessage(chatsLists[3], chats[3], p1, null, "nm37");
        helperSendMessage(chatsLists[4], chats[4], p4, null, "nm38");
        helperSendMessage(chatsLists[0], chats[0], p1, null, "nm39");
        helperSendMessage(chatsLists[1], chats[1], p4, null, "nm40");
        helperSendMessage(chatsLists[2], chats[2], p3, null, "nm41");
        helperSendMessage(chatsLists[3], chats[3], p3, null, "nm42");
        helperSendMessage(chatsLists[4], chats[4], p2, null, "nm43");
        helperSendMessage(chatsLists[4], chats[4], p2, null, "nm44");
        helperSendMessage(chatsLists[3], chats[3], p2, null, "nm45");
        helperSendMessage(chatsLists[2], chats[2], p3, null, "nm46");
        helperSendMessage(chatsLists[1], chats[1], p3, null, "nm47");
        helperSendMessage(chatsLists[0], chats[0], p1, null, "nm48");
        helperSendMessage(chatsLists[0], chats[0], p1, null, "nm49");
        helperSendMessage(chatsLists[4], chats[4], p2, null, "nm50");

        List<ChatMessage> messages;

        for(int chatIndex = 0; chatIndex < chats.length; chatIndex++) {
            messages = chats[chatIndex].getMessagesChat(null, null);

            List<ChatMessage> expectedMessages = new ArrayList<>();
            for(ChatMessage message : chatsLists[chatIndex]) {
                if(message.getReceiverNick() == null) {
                    expectedMessages.add(message);
                }
            }

            assertEquals(expectedMessages.size(), messages.size());

            for(int i = 0; i < expectedMessages.size(); i++) {
                assertEquals(
                        expectedMessages.get(i).getSenderNick(),
                        messages.get(i).getSenderNick()
                );
                assertEquals(
                        expectedMessages.get(i).getReceiverNick(),
                        messages.get(i).getReceiverNick()
                );
                assertEquals(
                        expectedMessages.get(i).getText(),
                        messages.get(i).getText()
                );
            }

            for(String player1 : this.nickToPlayer.keySet()) {
                for(String player2 : this.nickToPlayer.keySet()) {
                    if(!player1.equals(player2)) {
                        messages = chats[chatIndex].getMessagesChat(player1, player2);

                        expectedMessages = new ArrayList<>();
                        for(ChatMessage message : chatsLists[chatIndex]) {
                            if(message.getReceiverNick() == null) continue;
                            if(message.getSenderNick().equals(player1) && message.getReceiverNick().equals(player2)) {
                                expectedMessages.add(message);
                            }
                            else if (message.getSenderNick().equals(player2) && message.getReceiverNick().equals(player1)) {
                                expectedMessages.add(message);
                            }
                        }


                        assertEquals(expectedMessages.size(), messages.size());

                        for(int i = 0; i < expectedMessages.size(); i++) {
                            assertEquals(
                                    expectedMessages.get(i).getSenderNick(),
                                    messages.get(i).getSenderNick()
                            );
                            assertEquals(
                                    expectedMessages.get(i).getReceiverNick(),
                                    messages.get(i).getReceiverNick()
                            );
                            assertEquals(
                                    expectedMessages.get(i).getText(),
                                    messages.get(i).getText()
                            );

                            if(i<expectedMessages.size()-1) {
                                assertTrue(expectedMessages.get(i).getTimestamp() <= messages.get(i).getTimestamp());
                            }
                        }
                    }
                }
            }
        }
    }

    void helperAppendMessage(List<ChatMessage> expectedChat, Chat chat, Player sender, Player receiver, String text) {
        ChatMessage c = new ChatMessage(text, sender.nickname, (receiver != null ? receiver.nickname : null));
        expectedChat.add(c);
        chat.appendMessage(c, sender.nickname, (receiver != null ? receiver.nickname : null));
    }

    @Test
    void appendMessage() {
        ArrayList<ChatMessage>[] chatsLists = new ArrayList[]{
                new ArrayList<> (Arrays.stream(m1).toList()),
                new ArrayList<> (Arrays.stream(m2).toList()),
                new ArrayList<> (Arrays.stream(m3).toList()),
                new ArrayList<> (Arrays.stream(m4).toList()),
                new ArrayList<> (Arrays.stream(m5).toList())
        };

        helperAppendMessage(chatsLists[0], chats[0], p1, p2, "nm1");
        helperAppendMessage(chatsLists[1], chats[1], p3, p2, "nm2");
        helperAppendMessage(chatsLists[2], chats[2], p4, p2, "nm3");
        helperAppendMessage(chatsLists[3], chats[3], p4, p1, "nm4");
        helperAppendMessage(chatsLists[4], chats[4], p1, p4, "nm5");
        helperAppendMessage(chatsLists[3], chats[3], p2, p4, "nm6");
        helperAppendMessage(chatsLists[2], chats[2], p3, p4, "nm7");
        helperAppendMessage(chatsLists[1], chats[1], p1, p4, "nm8");
        helperAppendMessage(chatsLists[0], chats[0], p4, p2, "nm9");
        helperAppendMessage(chatsLists[1], chats[1], p2, p1, "nm10");
        helperAppendMessage(chatsLists[2], chats[2], p3, p2, "nm11");
        helperAppendMessage(chatsLists[3], chats[3], p1, p3, "nm12");
        helperAppendMessage(chatsLists[4], chats[4], p4, p2, "nm13");
        helperAppendMessage(chatsLists[0], chats[0], p1, p4, "nm14");
        helperAppendMessage(chatsLists[1], chats[1], p4, p1, "nm15");
        helperAppendMessage(chatsLists[2], chats[2], p3, p2, "nm16");
        helperAppendMessage(chatsLists[3], chats[3], p3, p2, "nm17");
        helperAppendMessage(chatsLists[4], chats[4], p2, p1, "nm18");
        helperAppendMessage(chatsLists[4], chats[4], p2, p4, "nm19");
        helperAppendMessage(chatsLists[3], chats[3], p2, p3, "nm20");
        helperAppendMessage(chatsLists[2], chats[2], p3, p1, "nm21");
        helperAppendMessage(chatsLists[1], chats[1], p3, p4, "nm22");
        helperAppendMessage(chatsLists[0], chats[0], p1, p4, "nm23");
        helperAppendMessage(chatsLists[0], chats[0], p1, p2, "nm24");
        helperAppendMessage(chatsLists[4], chats[4], p2, p3, "nm25");

        helperAppendMessage(chatsLists[0], chats[0], p1, null, "nm26");
        helperAppendMessage(chatsLists[1], chats[1], p3, null, "nm27");
        helperAppendMessage(chatsLists[2], chats[2], p4, null, "nm28");
        helperAppendMessage(chatsLists[3], chats[3], p4, null, "nm29");
        helperAppendMessage(chatsLists[4], chats[4], p1, null, "nm30");
        helperAppendMessage(chatsLists[3], chats[3], p2, null, "nm31");
        helperAppendMessage(chatsLists[2], chats[2], p3, null, "nm32");
        helperAppendMessage(chatsLists[1], chats[1], p1, null, "nm33");
        helperAppendMessage(chatsLists[0], chats[0], p4, null, "nm34");
        helperAppendMessage(chatsLists[1], chats[1], p2, null, "nm35");
        helperAppendMessage(chatsLists[2], chats[2], p3, null, "nm36");
        helperAppendMessage(chatsLists[3], chats[3], p1, null, "nm37");
        helperAppendMessage(chatsLists[4], chats[4], p4, null, "nm38");
        helperAppendMessage(chatsLists[0], chats[0], p1, null, "nm39");
        helperAppendMessage(chatsLists[1], chats[1], p4, null, "nm40");
        helperAppendMessage(chatsLists[2], chats[2], p3, null, "nm41");
        helperAppendMessage(chatsLists[3], chats[3], p3, null, "nm42");
        helperAppendMessage(chatsLists[4], chats[4], p2, null, "nm43");
        helperAppendMessage(chatsLists[4], chats[4], p2, null, "nm44");
        helperAppendMessage(chatsLists[3], chats[3], p2, null, "nm45");
        helperAppendMessage(chatsLists[2], chats[2], p3, null, "nm46");
        helperAppendMessage(chatsLists[1], chats[1], p3, null, "nm47");
        helperAppendMessage(chatsLists[0], chats[0], p1, null, "nm48");
        helperAppendMessage(chatsLists[0], chats[0], p1, null, "nm49");
        helperAppendMessage(chatsLists[4], chats[4], p2, null, "nm50");

        List<ChatMessage> messages;

        for(int chatIndex = 0; chatIndex < chats.length; chatIndex++) {
            messages = chats[chatIndex].getMessagesChat(null, null);

            List<ChatMessage> expectedMessages = new ArrayList<>();
            for(ChatMessage message : chatsLists[chatIndex]) {
                if(message.getReceiverNick() == null) {
                    expectedMessages.add(message);
                }
            }

            assertEquals(expectedMessages.size(), messages.size());

            for(int i = 0; i < expectedMessages.size(); i++) {
                assertEquals(
                        expectedMessages.get(i).getSenderNick(),
                        messages.get(i).getSenderNick()
                );
                assertEquals(
                        expectedMessages.get(i).getReceiverNick(),
                        messages.get(i).getReceiverNick()
                );
                assertEquals(
                        expectedMessages.get(i).getText(),
                        messages.get(i).getText()
                );
            }

            for(String player1 : this.nickToPlayer.keySet()) {
                for(String player2 : this.nickToPlayer.keySet()) {
                    if(!player1.equals(player2)) {
                        messages = chats[chatIndex].getMessagesChat(player1, player2);

                        expectedMessages = new ArrayList<>();
                        for(ChatMessage message : chatsLists[chatIndex]) {
                            if(message.getReceiverNick() == null) continue;
                            if(message.getSenderNick().equals(player1) && message.getReceiverNick().equals(player2)) {
                                expectedMessages.add(message);
                            }
                            else if (message.getSenderNick().equals(player2) && message.getReceiverNick().equals(player1)) {
                                expectedMessages.add(message);
                            }
                        }


                        assertEquals(expectedMessages.size(), messages.size());

                        for(int i = 0; i < expectedMessages.size(); i++) {
                            assertEquals(
                                    expectedMessages.get(i).getSenderNick(),
                                    messages.get(i).getSenderNick()
                            );
                            assertEquals(
                                    expectedMessages.get(i).getReceiverNick(),
                                    messages.get(i).getReceiverNick()
                            );
                            assertEquals(
                                    expectedMessages.get(i).getText(),
                                    messages.get(i).getText()
                            );

                            if(i<expectedMessages.size()-1) {
                                assertTrue(expectedMessages.get(i).getTimestamp() <= messages.get(i).getTimestamp());
                            }
                        }
                    }
                }
            }
        }
    }
}