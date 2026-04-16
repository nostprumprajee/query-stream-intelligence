export default function QueryDetail({ query, onBack }) {

  return (
    <div>
      <button onClick={onBack}>⬅ Back</button>

      <h2>🔍 Query Detail</h2>
      <p>{query}</p>

      <div style={{
        marginTop: 20,
        padding: 20,
        background: "#1e293b",
        borderRadius: 10
      }}>
        <p>Latency trend (coming soon)</p>
        <p>Execution insight...</p>
      </div>
    </div>
  );
}