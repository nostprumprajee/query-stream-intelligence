export default function MetricsCard({ title, value }) {
  return (
    <div style={{
      padding: 20,
      borderRadius: 10,
      background: "#f5f5f5",
      width: 150
    }}>
      <h4>{title}</h4>
      <h2>{value || 0}</h2>
    </div>
  );
}