package dLib.util.helpers;

import basemod.ReflectionHacks;
import com.codedisaster.steamworks.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.integrations.steam.SteamIntegration;
import dLib.steam.SteamCallbacks;

public class SteamHelpers {
    public static SteamCallbacks callbacks;

    public static SteamMatchmaking matchmaking;
    public static SteamFriends friends;
    public static SteamNetworking networking;
    public static SteamUtils utilities;
    public static SteamUser self;

    public static void init(){
        if(!isSteamAvailable()) return;

        callbacks = new SteamCallbacks();

        matchmaking = new SteamMatchmaking(callbacks);
        friends = new SteamFriends(callbacks);
        networking = new SteamNetworking(callbacks);
        utilities = new SteamUtils(callbacks);

        self = ReflectionHacks.getPrivate(CardCrawlGame.publisherIntegration, com.megacrit.cardcrawl.integrations.steam.SteamIntegration.class, "steamUser");
    }

    public static boolean isSteamAvailable(){
        return CardCrawlGame.publisherIntegration instanceof SteamIntegration && SteamAPI.isSteamRunning();
    }
}
