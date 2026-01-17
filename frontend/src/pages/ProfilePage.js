import React, { useState, useEffect, useCallback } from 'react';
import api from '../api/axiosConfig';
import AuthService from '../services/AuthService';
import { useNavigate } from 'react-router-dom';

const ProfilePage = () => {
    const [user, setUser] = useState(null);
    const [bio, setBio] = useState('');
    const [isEditingBio, setIsEditingBio] = useState(false);
    const [avatarUrl, setAvatarUrl] = useState(null);
    const navigate = useNavigate();
    const currentUsername = AuthService.getCurrentUser();

    const loadUserProfile = useCallback(async () => {
        try {
            const response = await api.get(`/users/get-by-username?username=${currentUsername}`);
            
            if (response.data && response.data.length > 0) {
                const userId = response.data[0].id;
                const fullProfile = await api.get(`/users/get-by-id/${userId}`);
                setUser(fullProfile.data);
                setBio(fullProfile.data.bio || '');
                
                if (fullProfile.data.pfpPicturesId && fullProfile.data.pfpPicturesId.length > 0) {
                    const lastPicId = fullProfile.data.pfpPicturesId[fullProfile.data.pfpPicturesId.length - 1];
                    setAvatarUrl(`http://localhost:8080/api/profile-pictures/stream?pictureId=${lastPicId}`);
                }
            }
        } catch (error) {
            console.error("Error loading profile", error);
        }
    }, [currentUsername]);

    useEffect(() => {
        loadUserProfile();
    }, [loadUserProfile]);

    const handleAvatarUpload = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append('file', file);

        try {
            await api.post('/profile-pictures/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            loadUserProfile();
        } catch (error) {
            console.error("Error uploading avatar", error);
            alert("Failed to upload avatar");
        }
    };

    const handleSaveBio = async () => {
        try {
            await api.post(`/users/set-bio?bio=${bio}`);
            setIsEditingBio(false);
            setUser(prev => ({ ...prev, bio: bio }));
        } catch (error) {
            console.error("Error saving bio", error);
        }
    };

    const getInitials = (name) => {
        return name ? name.substring(0, 2).toUpperCase() : '??';
    };

    if (!user) return <div className="app-container">Loading...</div>;

    return (
        <div className="app-container">
            <div className="auth-card" style={{ maxWidth: '600px', padding: '50px' }}>
                <div style={{ position: 'relative', display: 'inline-block', marginBottom: '30px' }}>
                    <div className="avatar-circle" style={{ 
                        width: '120px', 
                        height: '120px', 
                        fontSize: '40px', 
                        margin: '0 auto',
                        backgroundImage: avatarUrl ? `url(${avatarUrl})` : 'none',
                        backgroundSize: 'cover',
                        backgroundPosition: 'center'
                    }}>
                        {!avatarUrl && getInitials(user.username)}
                    </div>
                    
                    <label htmlFor="avatar-upload" style={{
                        position: 'absolute',
                        bottom: '0',
                        right: '0',
                        background: '#667eea',
                        width: '40px',
                        height: '40px',
                        borderRadius: '50%',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        cursor: 'pointer',
                        boxShadow: '0 4px 10px rgba(0,0,0,0.2)',
                        color: 'white'
                    }}>
                        📷
                    </label>
                    <input 
                        id="avatar-upload" 
                        type="file" 
                        accept="image/*" 
                        onChange={handleAvatarUpload} 
                        style={{ display: 'none' }} 
                    />
                </div>

                <h2 className="auth-title">{user.username}</h2>
                <p className="auth-subtitle">{user.mail}</p>

                <div style={{ textAlign: 'left', marginTop: '30px' }}>
                    <label style={{ fontWeight: '600', color: '#667eea', marginBottom: '10px', display: 'block' }}>Bio</label>
                    {isEditingBio ? (
                        <div className="input-group-custom" style={{ border: '1px solid #667eea' }}>
                            <input 
                                type="text" 
                                value={bio} 
                                onChange={(e) => setBio(e.target.value)}
                                autoFocus
                            />
                            <button className="btn-send" onClick={handleSaveBio} style={{ width: '30px', height: '30px', marginLeft: '10px' }}>✓</button>
                        </div>
                    ) : (
                        <div 
                            onClick={() => setIsEditingBio(true)}
                            style={{ 
                                padding: '15px', 
                                background: '#f0f2f5', 
                                borderRadius: '16px', 
                                cursor: 'pointer',
                                minHeight: '50px',
                                color: bio ? 'var(--text-primary)' : '#aaa'
                            }}
                        >
                            {bio || "Click to add a bio..."}
                        </div>
                    )}
                </div>

                <div style={{ marginTop: '20px', textAlign: 'left' }}>
                    <label style={{ fontWeight: '600', color: '#667eea', marginBottom: '5px', display: 'block' }}>Phone</label>
                    <p style={{ padding: '10px 0', borderBottom: '1px solid #eee' }}>{user.phoneNumber}</p>
                </div>

                <button 
                    className="auth-btn" 
                    style={{ marginTop: '40px', background: 'transparent', border: '2px solid #667eea', color: '#667eea' }}
                    onClick={() => navigate('/chat')}
                >
                    Back to Chat
                </button>
                
                <button 
                    className="auth-btn" 
                    style={{ marginTop: '10px', background: '#ff4b4b', color: 'white' }}
                    onClick={() => {
                        AuthService.logout();
                        navigate('/login');
                    }}
                >
                    Logout
                </button>
            </div>
        </div>
    );
};

export default ProfilePage;
