export default function TopQueryTable({ data, rootCause, onSelect }) {
  return (
    <div style={{ marginTop: 30 }}>
      <h3>📋 Top Queries</h3>

      <table style={{ width: "100%" }}>
        <tbody>
          {data.map((q, i) => {
            const isRoot = rootCause?.includes(q.query);

            return (
              <tr
                key={i}
                onClick={() => onSelect(q.query)}
                style={{
                  cursor: "pointer",
                  background: rootCause?.includes(q.query)
                    ? "#7f1d1d"
                    : "transparent"
                }}
              >
                <td>{q.query}</td>
                <td>{q.count}</td>
                <td>{q.avg}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}