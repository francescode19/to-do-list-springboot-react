import './App.css';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';
import TaskPage from './pages/TaskPage';
import ProtectedRoute from './components/ProtectedRoute';

function Home() {
  const navigate = useNavigate();
  const isLoggedIn = sessionStorage.getItem('isLoggedIn') === 'true';

  const handleLogout = () => {
    sessionStorage.removeItem('isLoggedIn');
    navigate('/login');
  };

  return (
    <div className="home-container">
      {isLoggedIn && (
        <div style={{ position: 'absolute', top: '20px', left: '20px' }}>
          <button
            onClick={handleLogout}
            className="btn-primary"
            style={{
              backgroundColor: '#d9534f',
              borderColor: '#d43f3a'
            }}
          >
            Logout
          </button>
        </div>
      )}

      <header className="App-header">
        <h1>Ciao! qui potrai gestire facilmente le tue attività.</h1>
        <p>Scegli un'opzione per iniziare:</p>

        <div className="button-group" style={{ marginBottom: '30px' }}>
          <button onClick={() => navigate('/login')} className="btn-primary">Accedi</button>
          <button onClick={() => navigate('/register')} className="btn-primary">Registrati</button>
        </div>

        <div style={{ display: 'flex', justifyContent: 'center' }}>
          <button onClick={() => navigate('/task')} className="btn-primary">
            Vai ai task
          </button>
        </div>
      </header>
    </div>
  );
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/task" element={<ProtectedRoute><TaskPage /></ProtectedRoute>} />
        <Route path="/" element={<Home />} />
      </Routes>
    </Router>
  );
}

export default App;
