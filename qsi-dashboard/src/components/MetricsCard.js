export default function MetricsCard({ title, value, highlight }) {
  return (
    <div style={{
      padding: 20,
      borderRadius: 12,
      background: highlight ? "#7f1d1d" : "#1e293b",
      transition: "0.3s"
    }}>
      <div style={{ opacity: 0.7 }}>{title}</div>
      <div style={{ fontSize: 24, fontWeight: "bold" }}>
        {value}
      </div>
    </div>
  );
}