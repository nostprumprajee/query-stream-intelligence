import { useEffect, useState } from "react";
import ChartPanel from "./components/ChartPanel";
import MetricsCard from "./components/MetricsCard";
import AlertPanel from "./components/AlertPanel";
import TopQueryTable from "./components/TopQueryTable";

function App() {
  const [data, setData] = useState([]);
  const [latest, setLatest] = useState({});

  useEffect(() => {
    const ws = new WebSocket("ws://localhost:8080/ws");

    ws.onmessage = (event) => {
      const msg = JSON.parse(event.data);

      setLatest(msg);

      setData((prev) => [
        ...prev.slice(-30),
        {
          time: new Date().toLocaleTimeString(),
          latency: msg.latency,
          qps: msg.qps
        }
      ]);
    };
  }, []);

  return (
    <div style={{ padding: 20, fontFamily: "Arial" }}>
      <h2>🔥 Query Insight Dashboard</h2>

      {/* Metrics */}
      <div style={{ display: "flex", gap: 20 }}>
        <MetricsCard title="QPS" value={latest.qps} />
        <MetricsCard title="Latency (ms)" value={latest.latency} />
      </div>

      {/* Chart */}
      <ChartPanel data={data} />

      {/* Alert */}
      <AlertPanel data={latest} />

      {/* Top Queries */}
      <TopQueryTable data={latest.topQueries || []} />
    </div>
  );
}

export default App;