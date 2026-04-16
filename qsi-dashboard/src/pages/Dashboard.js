import { useEffect, useState } from "react";
import ChartPanel from "../components/ChartPanel";
import MetricsCard from "../components/MetricsCard";
import AlertPanel from "../components/AlertPanel";
import TopQueryTable from "../components/TopQueryTable";

export default function Dashboard({ onSelectQuery }) {

  const [data, setData] = useState([]);
  const [latest, setLatest] = useState({});

  useEffect(() => {
    const ws = new WebSocket("ws://localhost:8080/ws");

    ws.onmessage = (event) => {
      const msg = JSON.parse(event.data);

      setLatest(msg);

      setData(prev => [
        ...prev.slice(-40),
        {
          time: new Date().toLocaleTimeString(),
          latency: msg.latency,
          qps: msg.qps
        }
      ]);
    };
  }, []);

  return (
    <>
      {/* Top Metrics */}
      <div style={{ display: "flex", gap: 20 }}>
        <MetricsCard title="QPS" value={latest.qps} />
        <MetricsCard title="Latency" value={latest.latency} />
        <MetricsCard
          title="Status"
          value={latest.alert ? "DEGRADED" : "HEALTHY"}
          highlight={!!latest.alert}
        />
      </div>

      {/* Graph */}
      <ChartPanel data={data} />

      {/* Alert */}
      <AlertPanel data={latest} />

      {/* Table */}
      <TopQueryTable
        data={latest.topQueries || []}
        rootCause={latest.rootCause}
        onSelect={onSelectQuery}
      />
    </>
  );
}