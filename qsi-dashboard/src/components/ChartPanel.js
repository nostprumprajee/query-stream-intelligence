import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid } from "recharts";

export default function ChartPanel({ data }) {
  return (
    <div style={{ marginTop: 30 }}>
      <h3>📈 Realtime Metrics</h3>

      <LineChart width={800} height={300} data={data}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="time" />
        <YAxis />
        <Tooltip />

        <Line type="monotone" dataKey="latency" stroke="#ff4d4f" />
        <Line type="monotone" dataKey="qps" stroke="#1890ff" />
      </LineChart>
    </div>
  );
}