export default function AlertPanel({ data }) {
  if (!data.alert) return null;

  return (
    <div style={{
      marginTop: 20,
      padding: 20,
      background: "#fff1f0",
      border: "1px solid red",
      borderRadius: 10
    }}>
      <h3>🚨 Alert</h3>
      <p>{data.alert}</p>

      <h4>🔥 Root Cause</h4>
      <p>{data.rootCause}</p>

      <h4>🧠 Insight</h4>
      <p>{data.insight}</p>
    </div>
  );
}