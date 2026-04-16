import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid } from "recharts";

export default function ChartPanel({ data }) {
  return (
    <div style={{ marginTop: 30 }}>
      <h3>📈 Realtime Metrics</h3>

      <LineChart width={800} height={300} data={data}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="time" />
        <YAxis />
        <Tooltip
          contentStyle={{ backgroundColor: "#1e293b", border: "none" }}
        />

        <Line
          type="monotone"
          dataKey="latency"
          stroke="#f43f5e"
          strokeWidth={3}
          dot={false}
          isAnimationActive={true}
        />

        <Line
          type="monotone"
          dataKey="qps"
          stroke="#38bdf8"
          strokeWidth={2}
          dot={false}
        />
      </LineChart>
    </div>
  );
}