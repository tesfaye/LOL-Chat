package com.tesfayeabel.lolchat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jriot.main.JRiot;
import jriot.main.JRiotException;
import jriot.objects.League;
import jriot.objects.LeagueEntry;
import jriot.objects.PlayerStatsSummary;
import jriot.objects.Summoner;

public class ChatService extends Service{
    private LolChat lolChat;
    private final IBinder mBinder = new LocalBinder();
    private ChatListener chatListener;
    private Handler handler = new Handler();
    private Toast toast;
    private NotificationManager notificationManager;
    private ArrayList<Message> missedMessages = new ArrayList<Message>();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate()
    {
        toast = new Toast(getApplicationContext());
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
    public void setChatListener(ChatListener chatListener)
    {
        this.chatListener = chatListener;
    }
    public void connectLOLChat(final String username, final String password, final String server, final LoginCallBack callBack)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                lolChat = new LolChat(ChatServer.getChatServerByName(server), FriendRequestPolicy.REJECT_ALL, "99a0d299-2476-4539-901f-0fdd0598bcf8");
                if(lolChat.login(username, password))
                {
                    Intent mainIntent = new Intent(getApplicationContext(), LOLChatMain.class);
                    startForeground(69, new Notification.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentText(getString(R.string.app_name) + " is running")
                            .setContentTitle(lolChat.getConnectedUsername())
                            .setTicker(getString(R.string.app_name) + " is now running")
                            .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .build());
                    Player connected = new Player(lolChat.getConnectedUsername().replace(" ",""), lolChat.getRiotApi());
                    lolChat.setStatus(new LolStatus()
                            .setLevel(connected.getSummonerLevel())
                            .setProfileIconId(connected.getProfileIcon())
                            .setRankedWins(connected.getRankedWins())
                            .setRankedLeagueTier(LolStatus.Tier.valueOf(connected.getRankedLeagueTier()))
                            .setRankedLeagueDivision(LolStatus.Division.valueOf(connected.getRankedDivision()))
                            .setRankedLeagueName(connected.getRankedLeagueName())
                            .setStatusMessage("USING BETA ABEL CHAT APP"));
                    lolChat.addChatListener(new ChatListener() {
                        @Override
                        public void onMessage(final Friend friend, final String message) {
                            if(chatListener != null && ((ChatActivity)chatListener).getIntent().getStringExtra("friend").equals(friend.getName()))
                            {
                                chatListener.onMessage(friend, message);
                            }else {
                                missedMessages.add(new Message(friend.getName(), message, MessageAdapter.DIRECTION_INCOMING));
                                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                                        0, new Intent(getApplicationContext(), LOLChatMain.class), 0);
                                Notification notification = new Notification.Builder(ChatService.this)
                                        .setContentTitle("New message")
                                        .setContentText("Message from " + friend.getName())
                                        .setSmallIcon(R.drawable.ic_launcher)
                                        .setStyle(getStyle())
                                        .setContentIntent(contentIntent)
                                        .build();
                                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                                notificationManager.notify(79, notification);

                                SharedPreferences sharedPreferences = getSharedPreferences("messageHistory", Context.MODE_PRIVATE);
                                String messageHistory = sharedPreferences.getString(friend.getName() + "History", "");
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                if(!messageHistory.equals(""))
                                {
                                    messageHistory += "\n";
                                }
                                for(String m: message.split("\n"))
                                {
                                    if(!m.isEmpty())
                                        messageHistory += new Message(friend.getName(), m, MessageAdapter.DIRECTION_INCOMING) + "\n";
                                }
                                if(messageHistory.charAt(messageHistory.length()-1) == '\n')//remove extra \n at end of string
                                    messageHistory = messageHistory.substring(0, messageHistory.length()-1);
                                editor.putString(friend.getName() + "History", messageHistory);
                                editor.apply();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showFriendToast(friend.getName(), message, friend.getStatus().getProfileIconId());
                                    }
                                });
                            }
                        }
                    });
                }
                callBack.onLogin(lolChat.isAuthenticated());
            }
        }).start();
    }
    private Notification.InboxStyle getStyle()
    {
        Notification.InboxStyle style =  new Notification.InboxStyle();
        List<Message> list = missedMessages.subList(Math.max(missedMessages.size() - 3, 0), missedMessages.size());//get list of last three messages
        for(Message m: list)
        {
            style.addLine(m.getSender() + ": " + m.getMessage());
        }
        style.setSummaryText(missedMessages.size() + " more");
        return style;
    }
    public void showFriendToast(String friend, String message, int iconId)
    {
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.toast_layout, null, false);
        toast.setView(view);
        TextView friendView = (TextView)view.findViewById(R.id.friend);
        TextView messageView = (TextView)view.findViewById(R.id.message);
        ImageView imageView = (ImageView)view.findViewById(R.id.icon);
        friendView.setText(friend);
        messageView.setText(message);
        Picasso.with(getApplicationContext()).load("http://ddragon.leagueoflegends.com/cdn/4.14.2/img/profileicon/" + iconId + ".png").into(imageView);
        toast.show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(lolChat != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lolChat.disconnect();
                    lolChat = null;
                }
            }).start();
        }
        notificationManager.cancel(79);
        notificationManager = null;
    }
    public class LocalBinder extends Binder {
        ChatService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ChatService.this;
        }
    }
    public LolChat getLolChat()
    {
        return lolChat;
    }

}