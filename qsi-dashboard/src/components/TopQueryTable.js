export default function TopQueryTable({ data }) {
  return (
    <div style={{ marginTop: 30 }}>
      <h3>📋 Top Queries</h3>

      <table border="1" cellPadding="8" style={{ width: "100%" }}>
        <thead>
          <tr>
            <th>Query</th>
            <th>Count</th>
            <th>Avg Latency</th>
          </tr>
        </thead>
        <tbody>
          {data.map((q, i) => (
            <tr key={i}>
              <td>{q.query}</td>
              <td>{q.count}</td>
              <td>{q.avg}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}