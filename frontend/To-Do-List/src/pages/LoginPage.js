import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../Form.css';

export default function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [esito, setEsito] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://localhost:8080/login', {
        username,
        password,
      }, {
        headers: { 'Content-Type': 'application/json' },
        withCredentials: true
      });

      console.log('Risposta:', response);

     
      sessionStorage.setItem('isLoggedIn', 'true');
      setEsito('Login effettuato!');
      navigate('/task'); 

    } catch (error) {
      console.error('Errore durante il login:', error);
      setEsito('Login fallito!');
      sessionStorage.removeItem('isLoggedIn');
    }
  };

  return (
    <div className="auth-container">
      <h2>Accedi</h2>
      <form onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Accedi</button>
      </form>
      <p>{esito}</p>
    </div>
  );
}
