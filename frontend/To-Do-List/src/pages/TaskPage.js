import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../TaskPage.css';

export default function TaskPage() {
  const [tasks, setTasks] = useState([]);
  const [newTaskName, setNewTaskName] = useState('');
  const [newTaskDescription, setNewTaskDescription] = useState('');
  const navigate = useNavigate();

  axios.defaults.withCredentials = true;

  useEffect(() => {
    fetchTasks();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const fetchTasks = async () => {
    try {
      const res = await axios.get('http://localhost:8080/tasks');
      setTasks(res.data);
    } catch (err) {
      console.error('Errore nel caricamento dei task:', err);
      if (err.response && err.response.status === 401) {
        navigate('/login');
      }
    }
  };

  const handleAddTask = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/tasks', {
        name: newTaskName,
        description: newTaskDescription,
        completed: false,
        creationDate: new Date().toISOString()
      });
      setNewTaskName('');
      setNewTaskDescription('');
      fetchTasks();
    } catch (err) {
      console.error('Errore durante la creazione del task:', err);
      if (err.response && err.response.status === 401) navigate('/login');
    }
  };

  const toggleCompleted = async (task) => {
    try {
      await axios.put(`http://localhost:8080/tasks/${task.id}`, {
        ...task,
        completed: !task.completed
      });
      fetchTasks();
    } catch (err) {
      console.error('Errore durante l\'aggiornamento del task:', err);
      if (err.response && err.response.status === 401) navigate('/login');
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/tasks/${id}`);
      fetchTasks();
    } catch (err) {
      console.error('Errore durante l\'eliminazione del task:', err);
      if (err.response && err.response.status === 401) navigate('/login');
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toLocaleString();
  };

  return (
    <div className="task-container-light">
      <h2>Gestione task</h2>

      <form onSubmit={handleAddTask} className="task-form-light">
        <input
          type="text"
          placeholder="Titolo"
          value={newTaskName}
          onChange={(e) => setNewTaskName(e.target.value)}
          required
        />
        <textarea
          placeholder="Descrizione"
          value={newTaskDescription}
          onChange={(e) => setNewTaskDescription(e.target.value)}
          required
        />
        <button type="submit">Aggiungi task</button>
      </form>

      <h3>Elenco task</h3>
      <ul className="task-list-light">
        {tasks.map(task => (
          <li key={task.id} className="task-item-light">
            <strong>{task.name}</strong> - {task.description}<br />
            Creato il: {formatDate(task.creationDate)}<br />
            Stato: <span style={{ color: task.completed ? 'green' : 'red' }}>
              {task.completed ? 'Completato' : 'Da completare'}
            </span>
            <br />
            <button
              className={task.completed ? 'btn-yellow' : 'btn-green'}
              onClick={() => toggleCompleted(task)}
            >
              {task.completed ? 'Segna come incompleto' : 'Segna come completato'}
            </button>
            <button className="btn-red" onClick={() => handleDelete(task.id)}>
              Elimina
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
