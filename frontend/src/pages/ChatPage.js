import React, { useEffect, useState, useRef, useCallback } from 'react';
import { Client } from '@stomp/stompjs';
import api from '../api/axiosConfig';
import AuthService from '../services/AuthService';
import { useNavigate } from 'react-router-dom';

const ChatPage = () => {
    const [chats, setChats] = useState([]);
    const [activeChat, setActiveChat] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [isConnected, setIsConnected] = useState(false);
    
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [isSearching, setIsSearching] = useState(false);
    
    const [currentUserProfile, setCurrentUserProfile] = useState(null);

    // Используем useRef для stompClient, чтобы избежать проблем с замыканиями
    const stompClientRef = useRef(null);
    const activeChatRef = useRef(null);

    const messagesEndRef = useRef(null);
    const navigate = useNavigate();
    const currentUsername = AuthService.getCurrentUser();

    useEffect(() => {
        activeChatRef.current = activeChat;
    }, [activeChat]);

    const loadChats = useCallback(async () => {
        try {
            const response = await api.get('/chats/get-my-chats');
            setChats(response.data);
        } catch (error) {
            console.error("Error loading chats", error);
        }
    }, []);
    
    const loadUserProfile = useCallback(async () => {
        try {
            const response = await api.get(`/users/get-by-username?username=${currentUsername}`);
            if (response.data && response.data.length > 0) {
                setCurrentUserProfile(response.data[0]);
            }
        } catch (error) {
            console.error("Error loading profile", error);
        }
    }, [currentUsername]);

    // Эффект для установки и разрыва WebSocket соединения
    useEffect(() => {
        if (!AuthService.isAuthenticated()) {
            navigate('/login');
            return;
        }

        loadChats();
        loadUserProfile();

        const token = localStorage.getItem('token');
        
        // Создаем экземпляр клиента
        const client = new Client({
            brokerURL: 'ws://192.168.0.107:8080/ws',
            connectHeaders: {
                Authorization: `Bearer ${token}`,
            },
            reconnectDelay: 5000,
            onConnect: () => {
                console.log("✅ Connected to WebSocket");
                setIsConnected(true);
                
                // Подписываемся на личные сообщения
                client.subscribe('/user/queue/messages', (message) => {
                    const receivedMsg = JSON.parse(message.body);
                    console.log("📩 New Message Received:", receivedMsg);
                    
                    // Обновляем список чатов, чтобы поднять чат с новым сообщением
                    loadChats();

                    // Если сообщение для активного чата, обновляем стейт
                    if (activeChatRef.current && receivedMsg.chatId === activeChatRef.current.id) {
                        setMessages((prevMessages) => [...prevMessages, receivedMsg]);
                    }
                });
            },
            onStompError: () => setIsConnected(false),
            onWebSocketClose: () => setIsConnected(false)
        });

        // Сохраняем клиент в ref и активируем
        stompClientRef.current = client;
        client.activate();

        // Функция для разрыва соединения при размонтировании компонента
        return () => {
            if (stompClientRef.current) {
                stompClientRef.current.deactivate();
            }
        };
    }, [navigate, loadChats, loadUserProfile]);

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    useEffect(() => {
        const searchUsers = async () => {
            if (!searchQuery.trim()) {
                setSearchResults([]);
                setIsSearching(false);
                return;
            }
            setIsSearching(true);
            try {
                const response = await api.get(`/users/get-by-username?username=${searchQuery}`);
                const filtered = response.data.filter(u => u.username !== currentUsername);
                setSearchResults(filtered);
            } catch (error) {
                console.error("Search error", error);
            }
        };
        const timeoutId = setTimeout(() => searchUsers(), 300);
        return () => clearTimeout(timeoutId);
    }, [searchQuery, currentUsername]);

    const selectChat = async (chat) => {
        setActiveChat(chat);
        setSearchQuery('');
        setIsSearching(false);
        try {
            const response = await api.get(`/chats/${chat.id}/messages`);
            setMessages(response.data);
        } catch (error) {
            console.error("Error loading messages", error);
        }
    };

    const startChatWithUser = async (user) => {
        try {
            const response = await api.post(`/chats/get-or-create-private-chat?recipientId=${user.id}`);
            await loadChats();
            selectChat(response.data);
        } catch (error) {
            console.error("Error creating chat", error);
        }
    };

    const sendMessage = () => {
        if (!newMessage.trim() || !activeChat || !stompClientRef.current || !isConnected) return;

        const recipient = activeChat.userDtoViews.find(u => u.username !== currentUsername);
        if (!recipient) return;

        const messagePayload = {
            recipientId: recipient.id,
            content: newMessage
        };

        stompClientRef.current.publish({
            destination: '/app/chat',
            body: JSON.stringify(messagePayload),
        });

        const optimisticMsg = {
            id: `temp-${Date.now()}`,
            content: newMessage,
            sender: { 
                username: currentUsername,
                pfpProfileId: currentUserProfile?.pfpProfileId 
            },
            dateSent: new Date().toISOString()
        };
        setMessages((prev) => [...prev, optimisticMsg]);
        setNewMessage('');
    };

    const getChatPartner = (chat) => {
        return chat.userDtoViews.find(u => u.username !== currentUsername);
    };

    const Avatar = ({ user, size = '48px', fontSize = '18px', style = {} }) => {
        const hasAvatar = user && user.pfpProfileId;
        const avatarUrl = hasAvatar ? `http://192.168.0.107:8080/api/profile-pictures/stream?pictureId=${user.pfpProfileId}` : null;
        const initials = user && user.username ? user.username.substring(0, 2).toUpperCase() : '??';

        return (
            <div 
                className="avatar-circle" 
                style={{ 
                    width: size, 
                    height: size, 
                    fontSize: fontSize, 
                    backgroundImage: avatarUrl ? `url(${avatarUrl})` : 'none',
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    ...style
                }}
            >
                {!avatarUrl && initials}
            </div>
        );
    };

    return (
        <div className="app-container">
            <div className="chat-window">
                <div className="sidebar" style={{ display: activeChat && window.innerWidth <= 768 ? 'none' : 'flex' }}>
                    <div style={{ padding: '20px 25px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                        <h4 style={{ margin: 0, fontWeight: 800, background: 'var(--primary-gradient)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>WebChat</h4>
                        <div onClick={() => navigate('/profile')} style={{cursor: 'pointer'}} title="My Profile">
                            <Avatar user={currentUserProfile} size="35px" fontSize="14px" style={{margin: 0, border: '2px solid #667eea'}} />
                        </div>
                    </div>
                    <div className="search-box" style={{paddingTop: '0'}}>
                        <input 
                            type="text" 
                            placeholder="Search users..." 
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                        />
                    </div>
                    <div className="chat-list">
                        {isSearching ? (
                            <>
                                <div style={{padding: '10px 20px', fontSize: '12px', color: '#aaa', fontWeight: 'bold'}}>SEARCH RESULTS</div>
                                {searchResults.length === 0 ? (
                                    <div style={{padding: '20px', textAlign: 'center', color: '#aaa'}}>No users found</div>
                                ) : (
                                    searchResults.map(user => (
                                        <button key={user.id} className="chat-item" onClick={() => startChatWithUser(user)}>
                                            <Avatar user={user} />
                                            <div className="chat-info">
                                                <h6>{user.username}</h6>
                                                <p>Click to start chat</p>
                                            </div>
                                        </button>
                                    ))
                                )}
                            </>
                        ) : (
                            chats.map(chat => {
                                const partner = getChatPartner(chat);
                                return (
                                    <button
                                        key={chat.id}
                                        className={`chat-item ${activeChat?.id === chat.id ? 'active' : ''}`}
                                        onClick={() => selectChat(chat)}
                                    >
                                        <Avatar user={partner} />
                                        <div className="chat-info">
                                            <h6>{partner?.username || 'Unknown'}</h6>
                                            <p>Open chat</p>
                                        </div>
                                    </button>
                                );
                            })
                        )}
                    </div>
                </div>
                <div className={`chat-area ${activeChat ? 'mobile-active' : ''}`}>
                    {activeChat ? (
                        <>
                            <div className="chat-header">
                                <button className="back-button" onClick={() => setActiveChat(null)} style={{display: 'none', background: 'none', border: 'none', marginRight: '15px', fontSize: '24px', color: '#667eea'}}>
                                    ‹
                                </button>
                                <Avatar user={getChatPartner(activeChat)} size="40px" fontSize="14px" />
                                <div style={{ marginLeft: '15px' }}>
                                    <h6 style={{ margin: 0 }}>{getChatPartner(activeChat)?.username}</h6>
                                    <small style={{ color: '#4caf50' }}>Online</small>
                                </div>
                            </div>
                            <div className="messages-container">
                                {messages.map((msg) => (
                                    <div key={msg.id} className={`message-wrapper ${msg.sender.username === currentUsername ? 'sent' : 'received'}`}>
                                        {msg.sender.username !== currentUsername && (
                                            <div style={{marginRight: '10px', alignSelf: 'flex-end'}}>
                                                <Avatar user={msg.sender} size="30px" fontSize="10px" />
                                            </div>
                                        )}
                                        <div className={`message-bubble ${msg.sender.username === currentUsername ? 'message-sent' : 'message-received'}`}>
                                            {msg.content}
                                            <span className="message-time">
                                                {new Date(msg.dateSent).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                            </span>
                                        </div>
                                    </div>
                                ))}
                                <div ref={messagesEndRef} />
                            </div>
                            <div className="input-area">
                                <div className="input-group-custom">
                                    <input
                                        type="text"
                                        placeholder={isConnected ? "Type a message..." : "Connecting..."}
                                        value={newMessage}
                                        onChange={(e) => setNewMessage(e.target.value)}
                                        onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
                                        disabled={!isConnected}
                                    />
                                    <button className="btn-send" onClick={sendMessage} disabled={!isConnected}>
                                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                            <line x1="22" y1="2" x2="11" y2="13"></line>
                                            <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
                                        </svg>
                                    </button>
                                </div>
                            </div>
                        </>
                    ) : (
                        <div className="empty-state">
                            <div style={{ fontSize: '64px', marginBottom: '20px' }}>💬</div>
                            <h3>Select a chat to start messaging</h3>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ChatPage;
