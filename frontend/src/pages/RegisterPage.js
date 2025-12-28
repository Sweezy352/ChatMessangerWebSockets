import React, { useState } from 'react';
import AuthService from '../services/AuthService';
import { useNavigate, Link } from 'react-router-dom';

const RegisterPage = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        mail: '',
        phoneNumber: '',
        age: '',
        birthDate: ''
    });
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            // Преобразуем age в число, если нужно
            const dataToSend = {
                ...formData,
                age: parseInt(formData.age)
            };
            
            await AuthService.register(dataToSend);
            // После успешной регистрации сразу логинимся или отправляем на логин
            // Для простоты - на логин
            navigate('/login');
        } catch (err) {
            setError(err.response?.data?.error || 'Registration failed');
            console.error(err);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card" style={{maxWidth: '500px'}}>
                <h2 className="auth-title">Create Account</h2>
                <p className="auth-subtitle">Join our community today</p>
                
                {error && <div style={{color: '#ff4b4b', marginBottom: '20px', fontSize: '14px'}}>{error}</div>}
                
                <form onSubmit={handleRegister}>
                    <div className="row" style={{display: 'flex', gap: '10px'}}>
                        <div className="auth-input-group" style={{flex: 1}}>
                            <input
                                type="text"
                                name="username"
                                className="auth-input"
                                placeholder="Username"
                                value={formData.username}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="auth-input-group" style={{flex: 1}}>
                            <input
                                type="number"
                                name="age"
                                className="auth-input"
                                placeholder="Age"
                                value={formData.age}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>

                    <div className="auth-input-group">
                        <input
                            type="email"
                            name="mail"
                            className="auth-input"
                            placeholder="Email Address"
                            value={formData.mail}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="auth-input-group">
                        <input
                            type="tel"
                            name="phoneNumber"
                            className="auth-input"
                            placeholder="Phone Number"
                            value={formData.phoneNumber}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="auth-input-group">
                        <input
                            type="date"
                            name="birthDate"
                            className="auth-input"
                            placeholder="Birth Date"
                            value={formData.birthDate}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="auth-input-group">
                        <input
                            type="password"
                            name="password"
                            className="auth-input"
                            placeholder="Password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <button type="submit" className="auth-btn">Sign Up</button>
                </form>
                
                <Link to="/login" className="auth-link">
                    Already have an account? <span>Sign In</span>
                </Link>
            </div>
        </div>
    );
};

export default RegisterPage;
