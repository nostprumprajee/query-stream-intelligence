export default function AlertPanel({ data }) {
  if (!data.alert) return null;

  return (
    <div style={{
      marginTop: 20,
      padding: 20,
      borderRadius: 16,
      background: "linear-gradient(135deg, #7f1d1d, #dc2626)",
      position: "sticky",
      top: 10,
      zIndex: 10
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