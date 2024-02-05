package service;

import com.google.code.chatterbotapi.*;
public class ChatBotService {
    public String getResponse(String message) throws Exception {
        ChatterBotFactory factory = new ChatterBotFactory();
        ChatterBot pandoraBot = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
        ChatterBotSession pandoraBotSession = pandoraBot.createSession();
        return pandoraBotSession.think(message);
    }
}
