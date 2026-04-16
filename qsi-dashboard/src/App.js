import Dashboard from "./pages/Dashboard";
import QueryDetail from "./pages/QueryDetail";
import { useState } from "react";

function App() {
  const [selectedQuery, setSelectedQuery] = useState(null);

  return (
    <div style={{ display: "flex", height: "100vh" }}>

      {/* Sidebar */}
      <div style={{
        width: 220,
        background: "#020617",
        color: "white",
        padding: 20
      }}>
        <h2>⚡ QSI</h2>
        <p>Dashboard</p>
      </div>

      {/* Main */}
      <div style={{ flex: 1, background: "#0f172a", color: "white", padding: 20 }}>
        {selectedQuery
          ? <QueryDetail query={selectedQuery} onBack={() => setSelectedQuery(null)} />
          : <Dashboard onSelectQuery={setSelectedQuery} />
        }
      </div>

    </div>
  );
}

export default App;