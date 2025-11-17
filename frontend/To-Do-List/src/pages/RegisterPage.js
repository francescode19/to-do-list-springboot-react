import { useState } from 'react';
import axios from 'axios';
import '../Form.css';

export default function RegisterPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [esito, setEsito] = useState('');

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      await axios.post('http://localhost:8080/register', {
        username,
        password,
      }, {
        headers: { 'Content-Type': 'application/json' }
      });
      setEsito('Registrazione riuscita!');
    } catch (error) {
      console.error('Errore durante la registrazione:', error);
      setEsito('Registrazione fallita!');
    }
  };

  return (
    <div className="auth-container">
      <h2>Registrati</h2>
      <form onSubmit={handleRegister}>
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
        <button type="submit">Registrati</button>
      </form>
      <p>{esito}</p>
    </div>
  );
}
