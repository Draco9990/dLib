package dLib.steam;

import com.codedisaster.steamworks.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import dLib.util.events.Event;
import dLib.util.events.localevents.*;

public class SteamCallbacks implements SteamMatchmakingCallback, SteamNetworkingCallback, SteamUtilsCallback, SteamFriendsCallback, SteamRemoteStorageCallback {
    public static QuadConsumerEvent<SteamID, Integer, Integer, Integer> onAvatarImageLoaded = new QuadConsumerEvent<>();

    public static TriConsumerEvent<Boolean, Boolean, SteamResult> onSetPersonaNameResponse = new TriConsumerEvent<>();
    public static BiConsumerEvent<SteamID, SteamFriends.PersonaChange> onPersonaStateChange = new BiConsumerEvent<>();

    public static ConsumerEvent<Boolean> onGameOverlayActivated = new ConsumerEvent<>();

    public static ConsumerEvent<Integer> onLobbyMatchList = new ConsumerEvent<>();
    public static BiConsumerEvent<SteamResult, SteamID> onLobbyCreated = new BiConsumerEvent<>();
    public static QuadConsumerEvent<SteamID, SteamID, Integer, Short> onLobbyGameCreated = new QuadConsumerEvent<>();
    public static BiConsumerEvent<SteamID, SteamID> onGameLobbyJoinRequested = new BiConsumerEvent<>();
    public static BiConsumerEvent<String, String> onGameServerChangeRequested = new BiConsumerEvent<>();
    public static TriConsumerEvent<SteamID, SteamID, Long> onLobbyInvite = new TriConsumerEvent<>();
    public static QuadConsumerEvent<SteamID, Integer, Boolean, SteamMatchmaking.ChatRoomEnterResponse> onLobbyEnter = new QuadConsumerEvent<>();
    public static TriConsumerEvent<SteamID, SteamID, Boolean> onLobbyKicked = new TriConsumerEvent<>();
    public static TriConsumerEvent<SteamID, SteamID, Boolean> onLobbyDataUpdate = new TriConsumerEvent<>();
    public static QuadConsumerEvent<SteamID, SteamID, SteamID, SteamMatchmaking.ChatMemberStateChange> onLobbyChatUpdate = new QuadConsumerEvent<>();
    public static QuadConsumerEvent<SteamID, SteamID, SteamMatchmaking.ChatEntryType, Integer> onLobbyChatMessage = new QuadConsumerEvent<>();

    public static BiConsumerEvent<SteamID, Integer> onFriendRichPresenceUpdate = new BiConsumerEvent<>();
    public static BiConsumerEvent<SteamID, String> onGameRichPresenceJoinRequested = new BiConsumerEvent<>();

    public static HeptConsumerEvent<Integer, Integer, Integer, Integer, Integer, Boolean, Integer> onFavoritesListChanged = new HeptConsumerEvent<>();
    public static ConsumerEvent<SteamResult> onFavoritesListAccountsUpdated = new ConsumerEvent<>();

    public static BiConsumerEvent<SteamID, SteamNetworking.P2PSessionError> onP2PSessionConnectFail = new BiConsumerEvent<>();
    public static ConsumerEvent<SteamID> onP2PSessionRequest = new ConsumerEvent<>();

    public static BiConsumerEvent<SteamUGCHandle, String> onFileShareResult = new BiConsumerEvent<>();
    public static BiConsumerEvent<SteamUGCHandle, SteamResult> onDownloadUGCResult = new BiConsumerEvent<>();
    public static TriConsumerEvent<SteamPublishedFileID, Boolean, SteamResult> onPublishFileResult = new TriConsumerEvent<>();
    public static TriConsumerEvent<SteamPublishedFileID, Boolean, SteamResult> onUpdatePublishedFileResult = new TriConsumerEvent<>();
    public static BiConsumerEvent<SteamPublishedFileID, Integer> onPublishedFileSubscribed = new BiConsumerEvent<>();
    public static BiConsumerEvent<SteamPublishedFileID, Integer> onPublishedFileUnsubscribed = new BiConsumerEvent<>();
    public static BiConsumerEvent<SteamPublishedFileID, Integer> onPublishedFileDeleted = new BiConsumerEvent<>();
    public static ConsumerEvent<SteamResult> onFileWriteAsyncComplete = new ConsumerEvent<>();
    public static QuadConsumerEvent<SteamAPICall, SteamResult, Integer, Integer> onFileReadAsyncComplete = new QuadConsumerEvent<>();

    public static RunnableEvent onSteamShutdown = new RunnableEvent();

    @Override
    public void onSetPersonaNameResponse(boolean b, boolean b1, SteamResult steamResult) {
        forwardToMainThread(() -> onSetPersonaNameResponse.invoke(b, b1, steamResult));
    }

    @Override
    public void onPersonaStateChange(SteamID steamID, SteamFriends.PersonaChange personaChange) {
        forwardToMainThread(() -> onPersonaStateChange.invoke(steamID, personaChange));
    }

    @Override
    public void onGameOverlayActivated(boolean b) {
        forwardToMainThread(() -> onGameOverlayActivated.invoke(b));
    }

    @Override
    public void onGameLobbyJoinRequested(SteamID lobbyID, SteamID inviteeID) {
        forwardToMainThread(() -> onGameLobbyJoinRequested.invoke(lobbyID, inviteeID));
    }

    @Override
    public void onAvatarImageLoaded(SteamID steamID, int i, int i1, int i2) {
        forwardToMainThread(() -> onAvatarImageLoaded.invoke(steamID, i, i1, i2));
    }

    @Override
    public void onFriendRichPresenceUpdate(SteamID steamID, int i) {
        forwardToMainThread(() -> onFriendRichPresenceUpdate.invoke(steamID, i));
    }

    @Override
    public void onGameRichPresenceJoinRequested(SteamID steamID, String s) {
        forwardToMainThread(() -> onGameRichPresenceJoinRequested.invoke(steamID, s));
    }

    @Override
    public void onGameServerChangeRequested(String s, String s1) {
        forwardToMainThread(() -> onGameServerChangeRequested.invoke(s, s1));
    }

    @Override
    public void onFavoritesListChanged(int i, int i1, int i2, int i3, int i4, boolean b, int i5) {
        forwardToMainThread(() -> onFavoritesListChanged.invoke(i, i1, i2, i3, i4, b, i5));
    }

    @Override
    public void onLobbyInvite(SteamID steamID, SteamID steamID1, long l) {
        forwardToMainThread(() -> onLobbyInvite.invoke(steamID, steamID1, l));
    }

    @Override
    public void onLobbyEnter(SteamID lobby, int unused, boolean locked, SteamMatchmaking.ChatRoomEnterResponse chatRoomEnterResponse) {
        forwardToMainThread(() -> onLobbyEnter.invoke(lobby, unused, locked, chatRoomEnterResponse));
    }

    @Override
    public void onLobbyDataUpdate(SteamID steamID, SteamID steamID1, boolean b) {
        forwardToMainThread(() -> onLobbyDataUpdate.invoke(steamID, steamID1, b));
    }

    @Override
    public void onLobbyChatUpdate(SteamID lobby, SteamID target, SteamID caller, SteamMatchmaking.ChatMemberStateChange chatMemberStateChange) {
        forwardToMainThread(() -> onLobbyChatUpdate.invoke(lobby, target, caller, chatMemberStateChange));
    }

    @Override
    public void onLobbyChatMessage(SteamID steamID, SteamID steamID1, SteamMatchmaking.ChatEntryType chatEntryType, int i) {
        forwardToMainThread(() -> onLobbyChatMessage.invoke(steamID, steamID1, chatEntryType, i));
    }

    @Override
    public void onLobbyGameCreated(SteamID steamID, SteamID steamID1, int i, short i1) {
        forwardToMainThread(() -> onLobbyGameCreated.invoke(steamID, steamID1, i, i1));
    }

    @Override
    public void onLobbyMatchList(int lobbyCount) {
        forwardToMainThread(() -> onLobbyMatchList.invoke(lobbyCount));
    }

    @Override
    public void onLobbyKicked(SteamID steamID, SteamID steamID1, boolean b) {
        forwardToMainThread(() -> onLobbyKicked.invoke(steamID, steamID1, b));
    }

    @Override
    public void onLobbyCreated(SteamResult result, SteamID lobby) {
        forwardToMainThread(() -> onLobbyCreated.invoke(result, lobby));
    }

    @Override
    public void onFavoritesListAccountsUpdated(SteamResult steamResult) {
        forwardToMainThread(() -> onFavoritesListAccountsUpdated.invoke(steamResult));
    }

    @Override
    public void onP2PSessionConnectFail(SteamID steamID, SteamNetworking.P2PSessionError p2PSessionError) {
        forwardToMainThread(() -> onP2PSessionConnectFail.invoke(steamID, p2PSessionError));
    }

    @Override
    public void onP2PSessionRequest(SteamID steamID) {
        forwardToMainThread(() -> onP2PSessionRequest.invoke(steamID));
    }

    @Override
    public void onSteamShutdown() {
        forwardToMainThread(() -> onSteamShutdown.invoke());
    }

    @Override
    public void onFileShareResult(SteamUGCHandle steamUGCHandle, String s, SteamResult steamResult) {
        forwardToMainThread(() -> onFileShareResult.invoke(steamUGCHandle, s));
    }

    @Override
    public void onDownloadUGCResult(SteamUGCHandle steamUGCHandle, SteamResult steamResult) {
        forwardToMainThread(() -> onDownloadUGCResult.invoke(steamUGCHandle, steamResult));
    }

    @Override
    public void onPublishFileResult(SteamPublishedFileID steamPublishedFileID, boolean b, SteamResult steamResult) {
        forwardToMainThread(() -> onPublishFileResult.invoke(steamPublishedFileID, b, steamResult));
    }

    @Override
    public void onUpdatePublishedFileResult(SteamPublishedFileID steamPublishedFileID, boolean b, SteamResult steamResult) {
        forwardToMainThread(() -> onUpdatePublishedFileResult.invoke(steamPublishedFileID, b, steamResult));
    }

    @Override
    public void onPublishedFileSubscribed(SteamPublishedFileID steamPublishedFileID, int i) {
        forwardToMainThread(() -> onPublishedFileSubscribed.invoke(steamPublishedFileID, i));
    }

    @Override
    public void onPublishedFileUnsubscribed(SteamPublishedFileID steamPublishedFileID, int i) {
        forwardToMainThread(() -> onPublishedFileUnsubscribed.invoke(steamPublishedFileID, i));
    }

    @Override
    public void onPublishedFileDeleted(SteamPublishedFileID steamPublishedFileID, int i) {
        forwardToMainThread(() -> onPublishedFileDeleted.invoke(steamPublishedFileID, i));
    }

    @Override
    public void onFileWriteAsyncComplete(SteamResult steamResult) {
        forwardToMainThread(() -> onFileWriteAsyncComplete.invoke(steamResult));
    }

    @Override
    public void onFileReadAsyncComplete(SteamAPICall steamAPICall, SteamResult steamResult, int i, int i1) {
        forwardToMainThread(() -> onFileReadAsyncComplete.invoke(steamAPICall, steamResult, i, i1));
    }

    public static void forwardToMainThread(Runnable execute){
        synchronized (CardCrawlGame.class){
            execute.run();
        }
    }
}
